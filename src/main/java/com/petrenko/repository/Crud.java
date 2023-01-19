package com.petrenko.repository;

import java.util.HashSet;
import java.util.Optional;

public interface Crud<T> {
    void save(final T value);
    HashSet<T> getAll();
    Optional<T> getById(final String id);
    void deleteById(final String id);
}
