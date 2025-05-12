package com.example.demo.account.controller;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.generated.model.ErrorMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice(basePackageClasses = AccountController.class)
@Slf4j
public class AccountControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(EntityNotFoundException e) {
        val errorMessage = new ErrorMessage();
        errorMessage.setTitle("Resource not found");
        errorMessage.setDetail(String.format("Resource of type %s and id %d not found!",
                e.getEntityType().getName(), e.getId()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorMessage);
    }

    // @RestController param validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleInvalidRequestParams(
            MethodArgumentNotValidException e
    ) {
        val errorMessage = new ErrorMessage();
        errorMessage.setTitle("Bad Request");
        errorMessage.setDetail("Invalid input: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }

    // @Service layer validation errors
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handle(ConstraintViolationException e) {
        val errorMessage = new ErrorMessage();
        errorMessage.setTitle("Bad Request: invalid input");
        val message = e.getConstraintViolations().stream()
                .map(this::mapViolationToMessage)
                .collect(Collectors.joining("; "));
        errorMessage.setDetail(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGenericError(Exception e) {
        log.error("Server error: {}", e.getMessage(), e);
        val errorMessage = new ErrorMessage();
        errorMessage.setTitle("Internal Server Error");
        errorMessage.setDetail("Unexpected internal server error.");
        return ResponseEntity.internalServerError()
                .body(errorMessage);
    }

    private String mapViolationToMessage(ConstraintViolation<?> violation) {
        return String.format("%s value %s is invalid: %s",
                violation.getPropertyPath(), violation.getInvalidValue(), violation.getMessage());
    }
}
