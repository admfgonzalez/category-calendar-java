package com.fgonzalez.categorycalendar.persistance.repository;

import com.fgonzalez.categorycalendar.persistance.entity.Category;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    @Query(value = "SELECT c FROM Category c WHERE c.name = :name")
    Category findCategoryByNameCategory(@Param("name") String name);
}
