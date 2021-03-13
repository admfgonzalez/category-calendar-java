package com.fgonzalez.categorycalendar.repository;

import com.fgonzalez.categorycalendar.model.CategorySchedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryScheduleRepository extends JpaRepository<CategorySchedule, Integer> { 

}