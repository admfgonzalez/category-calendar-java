package com.fgonzalez.categorycalendar.repository;

import com.fgonzalez.categorycalendar.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE TBL_CATEGORIES cat SET cat.active =:active WHERE cat.id =:id", nativeQuery = true)
    public void updateCategoryActive(@Param("id") Integer id, @Param("active") boolean active);
}
