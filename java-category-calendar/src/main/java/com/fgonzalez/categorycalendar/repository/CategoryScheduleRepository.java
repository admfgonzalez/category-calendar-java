package com.fgonzalez.categorycalendar.repository;

import java.util.List;

import com.fgonzalez.categorycalendar.model.Category;
import com.fgonzalez.categorycalendar.model.CategorySchedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryScheduleRepository extends JpaRepository<CategorySchedule, Integer> {
    @Query(value = "SELECT c FROM CategorySchedule c WHERE c.scheduleDate = :scheduleDate AND c.category = :category")
    public CategorySchedule findCategoryByScheduleAndCategoryId(@Param("scheduleDate") Integer scheduleDate, @Param("category") Category category);


    @Query(value = "FROM CategorySchedule WHERE scheduleDate >= (:year * 10000) AND scheduleDate < ((:year + 1) * 10000)")
    public List<CategorySchedule> findCategorySchedulesByYear(@Param("year") Integer year);
}