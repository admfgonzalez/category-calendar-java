package com.fgonzalez.categorycalendar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.exception.RecordNotFoundException;
import com.fgonzalez.categorycalendar.model.Category;
import com.fgonzalez.categorycalendar.model.CategorySchedule;
import com.fgonzalez.categorycalendar.repository.CategoryRepository;
import com.fgonzalez.categorycalendar.repository.CategoryScheduleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryScheduleServiceImpl implements CategoryScheduleService {
    @Autowired
    private CategoryScheduleRepository categoryScheduleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Optional<CategorySchedule> findById(Integer id) {
        return categoryScheduleRepository.findById(id);
    }

    @Override
    public List<CategorySchedule> findAll() {
        List<CategorySchedule> actualList = new ArrayList<CategorySchedule>();
        categoryScheduleRepository.findAll().forEach(actualList::add);
        return actualList;
    }

    @Override
    public CategorySchedule addNew(CategorySchedule categorySchedule) throws IllegalArgumentException {
        if (categorySchedule.getScheduleDate() == null) {
            throw new IllegalArgumentException("The new Category Schedule require a date");
        }
        if (categorySchedule.getCategory() == null) {
            throw new IllegalArgumentException("The new Category Schedule require a Category");
        } else {
            Optional<Category> response = categoryRepository.findById(categorySchedule.getCategory().getId());
            if (!response.isPresent()) {
                throw new RecordNotFoundException("Category not found");
            } else {
                categorySchedule.setCategory(response.get());
            }
        }
        return categoryScheduleRepository.save(categorySchedule);
    }

    @Override
    public CategorySchedule save(CategorySchedule categorySchedule) throws IllegalArgumentException {
        if (categorySchedule == null) {
            throw new IllegalArgumentException("The Category Schedule must be not null");
        }
        return categoryScheduleRepository.save(categorySchedule);
    }

    // No category schedule must be deleted
    @Override
    public void removeCategorySchedule(CategorySchedule categorySchedule) {
        Optional<CategorySchedule> searchCategory = categoryScheduleRepository.findById(categorySchedule.getId());
        if (searchCategory.isPresent()) {
            searchCategory.get().setActive(false);
            categoryScheduleRepository.save(searchCategory.get());
        } else {
            throw new RecordNotFoundException("Category Schedule with id '" + categorySchedule.getId() + "' does no exist");
        }
    }
}