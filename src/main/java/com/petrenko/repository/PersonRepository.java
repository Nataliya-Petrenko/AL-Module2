package com.petrenko.repository;

import com.petrenko.model.Customer;

import java.util.*;

public class PersonRepository implements Crud<Customer> {
    private static PersonRepository instance;
    private static final Map<String, Customer> CUSTOMERS = new HashMap<>();

    private PersonRepository() {
    }

    public static PersonRepository getInstance() {
        if (instance == null) {
            instance = new PersonRepository();
        }
        return instance;
    }

    @Override
    public void save(Customer value) {
        if (value == null || CUSTOMERS.get(value.getId()) != null) {
            return;
        }
        CUSTOMERS.put(value.getId(), value);
    }

    @Override
    public Set<Customer> getAll() {
        return new HashSet<>(CUSTOMERS.values());
    }

}
