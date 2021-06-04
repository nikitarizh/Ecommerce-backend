package com.nikitarizh.testtask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class Handler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public void handle(EntityNotFoundException e) {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductAlreadyInCartException.class)
    public void handle(ProductAlreadyInCartException e) {}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductIsInCartException.class)
    public void handle(ProductIsInCartException e) {}

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CartIsEmptyException.class)
    public void handle(CartIsEmptyException e) {}
}
