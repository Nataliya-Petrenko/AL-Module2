package com.petrenko.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class Product {
    private final String id;
    private TypeProduct typeProduct;
    public Product(TypeProduct typeProduct) {
        this.id = UUID.randomUUID().toString();
        this.typeProduct = typeProduct;
    }
    public abstract int getPrice();
}
