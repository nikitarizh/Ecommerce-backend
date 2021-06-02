package com.nikitarizh.testtask.exception;

public class ProductNotFoundException extends EntityNotFoundException {
    public ProductNotFoundException(Integer id) {
        super("Product", id);
    }
}
