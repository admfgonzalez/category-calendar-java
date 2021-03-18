package com.fgonzalez.categorycalendar.service;

import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.model.CategoryDTO;

public interface CategoryService {
    Optional<CategoryDTO> findById(Integer id);

    Optional<List<CategoryDTO>> findAll();

    Optional<CategoryDTO> addNew(CategoryDTO category) throws IllegalArgumentException;

    void removeCategory(CategoryDTO category) throws IllegalArgumentException;

    Optional<CategoryDTO> save(CategoryDTO category) throws IllegalArgumentException;
}
