package com.petrenko.exceptions;

public class ProductDescriptionException extends IllegalArgumentException{
    public  ProductDescriptionException () {

    };
    public  ProductDescriptionException (final String message) {
        super(message);
    }
}
