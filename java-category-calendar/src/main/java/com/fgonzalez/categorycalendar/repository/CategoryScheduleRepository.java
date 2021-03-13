package com.fgonzalez.categorycalendar.repository;

import com.fgonzalez.categorycalendar.model.CategorySchedule;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryScheduleRepository extends CrudRepository<CategorySchedule, Integer> { 

}