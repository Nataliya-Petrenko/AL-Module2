package com.petrenko.repository;

import com.petrenko.model.Product;

import java.util.*;

public class ProductRepository implements Crud<Product> {
    private static ProductRepository instance;
    private static final Map<String, Product> PRODUCTS = new HashMap<>();

    private ProductRepository() {
    }

    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    @Override
    public void save(Product value) {
        if (value == null || PRODUCTS.get(value.getId()) != null) {
            return;
        }
        if (!productAlreadyExist(value)) {
            PRODUCTS.put(value.getId(), value);
        }
    }

    private boolean productAlreadyExist(Product value) {
        return PRODUCTS.values().stream()
                .anyMatch(product -> value.hashCode() == product.hashCode());
    }

    @Override
    public Set<Product> getAll() {
        return new HashSet<>(PRODUCTS.values());
    }

}
