package com.fgonzalez.categorycalendar.repository;

import com.fgonzalez.categorycalendar.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "SELECT c FROM Category c WHERE c.categoryName = :categoryName")
    public Category getCategoryByNameCategory(@Param("categoryName") String categoryName);
}
