package com.nikitarizh.testtask.exception;

import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.User;

public class ProductAlreadyInCartException extends RuntimeException {
    public ProductAlreadyInCartException(Product product, User user) {
        super("Product " + product + " is already in cart of user " + user);
    }
}
