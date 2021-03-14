package com.fgonzalez.categorycalendar.repository;

import com.fgonzalez.categorycalendar.model.Category;
import com.fgonzalez.categorycalendar.model.CategorySchedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryScheduleRepository extends JpaRepository<CategorySchedule, Integer> {
    @Query(value = "SELECT c FROM CategorySchedule c WHERE c.scheduleDate = :scheduleDate AND c.category = :category")
    public CategorySchedule getCategoryByScheduleAndCategoryId(@Param("scheduleDate") Integer scheduleDate, @Param("category") Category category);
}