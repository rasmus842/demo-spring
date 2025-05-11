package com.example.demo.exceptions;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final Class<?> entityType;
    private final Long id;

    public EntityNotFoundException(Class<?> entityType, Long id) {
        super();
        this.entityType = entityType;
        this.id = id;
    }
}
