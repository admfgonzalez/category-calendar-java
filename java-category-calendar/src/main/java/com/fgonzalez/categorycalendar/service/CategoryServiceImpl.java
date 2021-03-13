package com.fgonzalez.categorycalendar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.model.Category;
import com.fgonzalez.categorycalendar.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> findAll() {
        List<Category> actualList = new ArrayList<Category>();
        categoryRepository.findAll().forEach(actualList::add);
        return actualList;
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void changeActive(Integer id, boolean active) {
        categoryRepository.updateCategoryActive(id, active);
    }
}
