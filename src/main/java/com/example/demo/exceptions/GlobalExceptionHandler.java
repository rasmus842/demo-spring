package com.example.demo.exceptions;

import com.example.demo.generated.model.ErrorMessage;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFound(EntityNotFoundException e) {
        val errorMessage = new ErrorMessage();
        errorMessage.setTitle("Resource not found");
        errorMessage.setDetail(String.format("Resource of type %s and id %d not found!",
                e.getEntityType().getName(), e.getId()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorMessage);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorMessage> handleInvalidInput(InvalidInputException e) {
        val errorMessage = new ErrorMessage();
        errorMessage.setTitle("Bad Request");
        errorMessage.setDetail("Invalid input: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGenericError(Exception e) {
        val errorMessage = new ErrorMessage();
        errorMessage.setTitle("Internal Server Error");
        errorMessage.setDetail("Unexpected internal server error.");
        return ResponseEntity.internalServerError()
                .body(errorMessage);
    }
}
