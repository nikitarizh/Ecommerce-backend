package com.nikitarizh.testtask.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entity, Integer id) {
        super("Entity " + entity + " is not found by id " + id);
    }

    public EntityNotFoundException(String entity, String field, String value) {
        super("Entity " + entity + " is not found by field " + field + " with value " + value);
    }
}
