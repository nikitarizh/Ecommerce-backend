package com.nikitarizh.testtask.exception;

import java.util.List;

public class ProductNotFoundException extends EntityNotFoundException {

    public ProductNotFoundException(Integer id) {
        super("Product", id);
    }

    public ProductNotFoundException(String description, List<Integer> tagIds) {
        super("Product", "{ description, tagIds }", description + ";" + tagIds);
    }
}
