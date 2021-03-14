package com.fgonzalez.categorycalendar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.exception.RecordNotFoundException;
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
    public Category addNew(Category category) throws IllegalArgumentException {
        if (category.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("The new Category require a name");
        }
        // search by category name on lowercase
        category.setCategoryName(category.getCategoryName().toLowerCase());
        Category responseCategory = categoryRepository.findCategoryByNameCategory(category.getCategoryName());
        if (responseCategory != null) {
            responseCategory.setActive(true);
            return categoryRepository.save(responseCategory);
        }
        return save(category);
    }

    @Override
    public Category save(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("The category must be not null");
        }
        if (category.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("The category must be a name");
        }
        category.setCategoryName(category.getCategoryName().toLowerCase());
        return categoryRepository.save(category);
    }

    // No category must be deleted
    @Override
    public void removeCategory(Category category) throws IllegalArgumentException {
        Optional<Category> searchCategory = categoryRepository.findById(category.getId());
        if (searchCategory.isPresent()) {
            searchCategory.get().setActive(false);
            categoryRepository.save(searchCategory.get());
        } else {
            throw new RecordNotFoundException("Category with id '" + category.getId() + "' does no exist");
        }
    }
}
