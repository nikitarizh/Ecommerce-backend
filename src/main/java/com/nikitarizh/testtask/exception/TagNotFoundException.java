package com.nikitarizh.testtask.exception;

public class TagNotFoundException extends EntityNotFoundException {
    public TagNotFoundException(Integer id) {
        super("Tag", id);
    }
}
