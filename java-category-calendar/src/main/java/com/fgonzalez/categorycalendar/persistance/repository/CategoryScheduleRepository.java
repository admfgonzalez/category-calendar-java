package com.fgonzalez.categorycalendar.persistance.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.fgonzalez.categorycalendar.persistance.entity.Category;
import com.fgonzalez.categorycalendar.persistance.entity.CategorySchedule;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryScheduleRepository extends CrudRepository<CategorySchedule, Integer> {
    @Query(value = "SELECT c FROM CategorySchedule c WHERE c.scheduleDate = :scheduleDate AND c.category = :category")
    CategorySchedule findCategoryByScheduleAndCategoryId(@Param("scheduleDate") Integer scheduleDate, @Param("category") Category category);

    @Query(value = "FROM CategorySchedule WHERE scheduleDate >= (:year * 10000) AND scheduleDate < ((:year + 1) * 10000)")
    List<CategorySchedule> findCategorySchedulesByYear(@Param("year") Integer year);

    @Transactional
    @Modifying
    @Query(value = "UPDATE CategorySchedule c SET c.active = false WHERE c.category = :category")
    void deactivateByCategoryId(@Param("category") Category category);
}