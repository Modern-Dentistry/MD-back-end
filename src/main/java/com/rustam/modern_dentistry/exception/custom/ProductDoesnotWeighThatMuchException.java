package com.rustam.modern_dentistry.exception.custom;

import lombok.Getter;

@Getter
public class ProductDoesnotWeighThatMuchException extends RuntimeException {

    private final String message;
    public ProductDoesnotWeighThatMuchException(String message) {
        super(message);
        this.message = message;
    }
}
