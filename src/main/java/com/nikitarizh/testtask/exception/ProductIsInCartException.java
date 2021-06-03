package com.nikitarizh.testtask.exception;

import com.nikitarizh.testtask.entity.Product;

public class ProductIsInCartException extends RuntimeException {
    public ProductIsInCartException(Product product) {
        super("Product " + product + " cannot be modified: it is in cart of at least one user");
    }
}
