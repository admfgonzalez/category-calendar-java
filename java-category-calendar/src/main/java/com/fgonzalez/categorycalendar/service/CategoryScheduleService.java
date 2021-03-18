package com.fgonzalez.categorycalendar.service;

import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.model.CategoryScheduleDTO;

public interface CategoryScheduleService {
    Optional<CategoryScheduleDTO> findById(Integer id);

    Optional<List<CategoryScheduleDTO>> findAll();

    Optional<List<CategoryScheduleDTO>> findByYear(Integer year) throws IllegalArgumentException;

    Optional<CategoryScheduleDTO> addNew(CategoryScheduleDTO category) throws IllegalArgumentException;

    void removeCategorySchedule(CategoryScheduleDTO category) throws IllegalArgumentException;

    Optional<CategoryScheduleDTO> save(CategoryScheduleDTO category) throws IllegalArgumentException;
}
