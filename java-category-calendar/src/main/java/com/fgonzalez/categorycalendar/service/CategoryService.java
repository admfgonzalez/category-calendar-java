package com.fgonzalez.categorycalendar.service;

import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.model.Category;

public interface CategoryService {
    Optional<Category> findById(Integer id);
    List<Category> findAll();
    Category addNew(Category category) throws IllegalArgumentException;
    void removeCategory(Category category) throws IllegalArgumentException;
    Category save(Category category) throws IllegalArgumentException;
}
