package com.fgonzalez.categorycalendar.service;

import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.model.CategorySchedule;

public interface CategoryScheduleService {
    Optional<CategorySchedule> findById(Integer id);
    List<CategorySchedule> findAll();
    CategorySchedule addNew(CategorySchedule category) throws IllegalArgumentException;
    void removeCategorySchedule(CategorySchedule category) throws IllegalArgumentException;
    CategorySchedule save(CategorySchedule category) throws IllegalArgumentException;
}
