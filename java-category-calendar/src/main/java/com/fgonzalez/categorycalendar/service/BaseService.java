package com.fgonzalez.categorycalendar.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {
    Boolean thereAreChanges(Long timeInMillis);

    Long getLastChangeTime();

    Optional<T> findById(Integer id);

    Optional<List<T>> findAll();

    Optional<T> addNew(T category) throws IllegalArgumentException;

    void remove(T category) throws IllegalArgumentException;

    Optional<T> save(T category) throws IllegalArgumentException;
}
