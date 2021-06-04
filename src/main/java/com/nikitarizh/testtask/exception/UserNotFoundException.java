package com.nikitarizh.testtask.exception;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(Integer id) {
        super("User", id);
    }

    public UserNotFoundException(String nickname) {
        super("User", "nickname", nickname);
    }
}
