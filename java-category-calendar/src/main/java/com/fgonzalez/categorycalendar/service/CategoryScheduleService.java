package com.fgonzalez.categorycalendar.service;

import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.model.CategoryScheduleDTO;

public interface CategoryScheduleService extends BaseService<CategoryScheduleDTO> {
    Optional<List<CategoryScheduleDTO>> findByYear(Integer year) throws IllegalArgumentException;
}
