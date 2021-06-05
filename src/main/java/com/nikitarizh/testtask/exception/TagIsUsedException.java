package com.nikitarizh.testtask.exception;

import com.nikitarizh.testtask.entity.Tag;

public class TagIsUsedException extends RuntimeException {
    public TagIsUsedException(Tag tag) {
        super("Tag " + tag + " can't be deleted because it is used in one or more products");
    }
}
