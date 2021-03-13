package com.fgonzalez.categorycalendar.service;

import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.model.Category;

public interface CategoryService {
    Optional<Category> findById(Integer id);
    List<Category> findAll();
    Category save(Category category);
    void deleteById(Integer id);
    void changeActive(Integer id, boolean active);
}
