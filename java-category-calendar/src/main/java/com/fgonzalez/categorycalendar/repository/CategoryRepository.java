package com.fgonzalez.categorycalendar.repository;

import com.fgonzalez.categorycalendar.model.Category;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> { 

}
