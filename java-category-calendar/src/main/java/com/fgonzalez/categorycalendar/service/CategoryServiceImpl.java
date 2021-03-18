package com.fgonzalez.categorycalendar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.exception.RecordNotFoundException;
import com.fgonzalez.categorycalendar.model.CategoryDTO;
import com.fgonzalez.categorycalendar.persistance.entity.Category;
import com.fgonzalez.categorycalendar.persistance.mapper.CategoryMapper;
import com.fgonzalez.categorycalendar.persistance.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Optional<CategoryDTO> findById(Integer id) {
        return categoryRepository.findById(id).map(category -> categoryMapper.toCategoryDTO(category));
    }

    @Override
    public Optional<List<CategoryDTO>> findAll() {
        List<Category> actualList = new ArrayList<Category>();
        categoryRepository.findAll().forEach(actualList::add);
        return Optional.of(categoryMapper.toCategoriesDTO(actualList));
    }

    @Override
    public Optional<CategoryDTO> addNew(CategoryDTO categoryDTO) throws IllegalArgumentException {
        if (categoryDTO.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("The new Category require a name");
        }
        // search by category name on lowercase
        categoryDTO.setCategoryName(categoryDTO.getCategoryName().toLowerCase());
        Category responseCategory = categoryRepository.findCategoryByNameCategory(categoryDTO.getCategoryName());
        if (responseCategory != null) {
            responseCategory.setActive(true);
            return Optional.of(categoryMapper.toCategoryDTO(categoryRepository.save(responseCategory)));
        }
        return save(categoryDTO);
    }

    @Override
    public Optional<CategoryDTO> save(CategoryDTO categoryDTO) {
        if (categoryDTO.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("The category must be a name");
        }
        categoryDTO.setCategoryName(categoryDTO.getCategoryName().toLowerCase());
        Category cat = categoryMapper.toCategory(categoryDTO);
        return Optional
                .of(categoryMapper.toCategoryDTO(categoryRepository.save(cat)));
    }

    // No category must be deleted
    @Override
    public void removeCategory(CategoryDTO categoryDTO) throws IllegalArgumentException {
        Optional<Category> searchCategory = categoryRepository.findById(categoryDTO.getId());
        if (searchCategory.isPresent()) {
            searchCategory.get().setActive(false);
            categoryRepository.save(searchCategory.get());
        } else {
            throw new RecordNotFoundException("Category with id '" + categoryDTO.getId() + "' does no exist");
        }
    }
}
