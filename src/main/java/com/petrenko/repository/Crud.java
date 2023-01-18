package com.petrenko.repository;

import com.petrenko.model.Invoice;

import java.util.LinkedList;
import java.util.Optional;

public interface Crud<T> {
    void save(final T value);
    LinkedList<T> getAll();
    Optional<T> getById(final String id);
    void deleteById(final String id);
}
