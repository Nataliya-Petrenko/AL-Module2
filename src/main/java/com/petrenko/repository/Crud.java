package com.petrenko.repository;

import java.util.Set;

public interface Crud<T> {
    void save(final T value);
    Set<T> getAll();
}
