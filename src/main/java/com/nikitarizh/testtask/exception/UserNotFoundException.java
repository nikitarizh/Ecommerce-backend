package com.nikitarizh.testtask.exception;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(Integer id) {
        super("User", id);
    }
}
