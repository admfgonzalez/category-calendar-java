package com.fgonzalez.categorycalendar.service;

import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.model.CategorySchedule;

public interface CategoryScheduleService {
    Optional<CategorySchedule> findById(Integer id);
    List<CategorySchedule> findAll();
    CategorySchedule save(CategorySchedule category);
    void deleteById(Integer id);
}
