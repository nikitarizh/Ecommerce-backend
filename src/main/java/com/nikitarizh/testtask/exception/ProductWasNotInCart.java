package com.nikitarizh.testtask.exception;

import com.nikitarizh.testtask.entity.Product;
import com.nikitarizh.testtask.entity.User;

public class ProductWasNotInCart extends RuntimeException {
    public ProductWasNotInCart(Product product, User user) {
        super("Product " + product + " is not in cart of user " + user + " so it can't be deleted");
    }
}
