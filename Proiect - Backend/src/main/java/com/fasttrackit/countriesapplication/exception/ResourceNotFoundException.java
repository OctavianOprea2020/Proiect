package com.fasttrackit.countriesapplication.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private int entityId;

    public ResourceNotFoundException(String message, int entityId) {
        super(message);
        this.entityId = entityId;
    }
}
