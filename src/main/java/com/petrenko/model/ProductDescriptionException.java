package com.petrenko.model;

public class ProductDescriptionException extends IllegalArgumentException{
    public  ProductDescriptionException () {

    };
    public  ProductDescriptionException (final String message) {
        super(message);
    }
}
