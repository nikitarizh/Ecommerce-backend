package com.nikitarizh.testtask.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Handler {

    @ExceptionHandler(ProductAlreadyInCartException.class)
    public ResponseEntity<Object> handle(ProductAlreadyInCartException e) {
        return new ResponseEntity<>("Product is already in cart", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductIsInCartException.class)
    public ResponseEntity<Object> handle(ProductIsInCartException e) {
        return new ResponseEntity<>("Product is in cart of at least one user", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handle(DataIntegrityViolationException e) {
        return new ResponseEntity<>("Database exception. Possible duplicate", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TagIsUsedException.class)
    public ResponseEntity<Object> handle(TagIsUsedException e) {
        return new ResponseEntity<>("Tag is used", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handle(EntityNotFoundException e) {
        return new ResponseEntity<>("Entity not found", new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartIsEmptyException.class)
    public ResponseEntity<Object> handle(CartIsEmptyException e) {
        return new ResponseEntity<>("Cart is empty", new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductWasNotInCart.class)
    public ResponseEntity<Object> handle(ProductWasNotInCart e) {
        return new ResponseEntity<>("Product was not in cart", new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handle(InvalidCredentialsException e) {
        return new ResponseEntity<>("Invalid credentials", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }
}
