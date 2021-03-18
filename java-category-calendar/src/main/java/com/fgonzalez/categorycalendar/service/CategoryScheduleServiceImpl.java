package com.fgonzalez.categorycalendar.service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fgonzalez.categorycalendar.exception.RecordNotFoundException;
import com.fgonzalez.categorycalendar.model.CategoryScheduleDTO;
import com.fgonzalez.categorycalendar.persistance.entity.Category;
import com.fgonzalez.categorycalendar.persistance.entity.CategorySchedule;
import com.fgonzalez.categorycalendar.persistance.mapper.CategoryMapper;
import com.fgonzalez.categorycalendar.persistance.mapper.CategoryScheduleMapper;
import com.fgonzalez.categorycalendar.persistance.repository.CategoryRepository;
import com.fgonzalez.categorycalendar.persistance.repository.CategoryScheduleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryScheduleServiceImpl implements CategoryScheduleService {
    @Autowired
    private CategoryScheduleRepository categoryScheduleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryScheduleMapper categoryScheduleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Optional<CategoryScheduleDTO> findById(Integer id) {
        return categoryScheduleRepository.findById(id).map(categorySchedule -> categoryScheduleMapper.toCategoryScheduleDTO(categorySchedule));
    }

    @Override
    public Optional<List<CategoryScheduleDTO>> findAll() {
        Iterable<CategorySchedule> result = categoryScheduleRepository.findAll();
        return Optional.of(categoryScheduleMapper.toCategorySchedulesDTO(
                StreamSupport.stream(result.spliterator(), false).collect(Collectors.toList())));
    }

    @Override
    public Optional<List<CategoryScheduleDTO>> findByYear(Integer year) throws IllegalArgumentException {
        if (year < 1) {
            throw new IllegalArgumentException("The year mus be positive");
        }
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (year > currentYear) {
            throw new IllegalArgumentException("The Category Schedule date must be past");
        }

        return Optional.of(categoryScheduleMapper
                .toCategorySchedulesDTO(categoryScheduleRepository.findCategorySchedulesByYear(year)));
    }

    @Override
    public Optional<CategoryScheduleDTO> addNew(CategoryScheduleDTO categoryScheduleDTO)
            throws IllegalArgumentException {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (categoryScheduleDTO.getScheduleDate() > currentYear) {
            throw new IllegalArgumentException("The Category Schedule date must be past");
        }

        Optional<Category> searchCategory = categoryRepository.findById(categoryScheduleDTO.getCategory().getId());
        if (!searchCategory.isPresent()) {
            throw new RecordNotFoundException("Category not found");
        }

        CategorySchedule categoryByScheduleAndCategoryId = categoryScheduleRepository
                .findCategoryByScheduleAndCategoryId(categoryScheduleDTO.getScheduleDate(), searchCategory.get());

        if (categoryByScheduleAndCategoryId != null) {
            categoryByScheduleAndCategoryId.setActive(true);
            return Optional.of(categoryScheduleMapper
                    .toCategoryScheduleDTO(categoryScheduleRepository.save(categoryByScheduleAndCategoryId)));
        }

        categoryScheduleDTO.setCategory(categoryMapper.toCategoryDTO(searchCategory.get()));
        return Optional.of(categoryScheduleMapper.toCategoryScheduleDTO(
                categoryScheduleRepository.save(categoryScheduleMapper.toCategorySchedule(categoryScheduleDTO))));
    }

    @Override
    public Optional<CategoryScheduleDTO> save(CategoryScheduleDTO categoryScheduleDTO) throws IllegalArgumentException {
        if (categoryScheduleDTO == null) {
            throw new IllegalArgumentException("The Category Schedule must be not null");
        }
        return Optional.of(categoryScheduleMapper.toCategoryScheduleDTO(
                categoryScheduleRepository.save(categoryScheduleMapper.toCategorySchedule(categoryScheduleDTO))));
    }

    // No category schedule must be deleted
    @Override
    public void removeCategorySchedule(CategoryScheduleDTO categoryScheduleDTO) {
        Optional<CategorySchedule> searchCategory = categoryScheduleRepository.findById(categoryScheduleDTO.getId());
        if (searchCategory.isPresent()) {
            searchCategory.get().setActive(false);
            categoryScheduleRepository.save(searchCategory.get());
        } else {
            throw new RecordNotFoundException(
                    "Category Schedule with id '" + categoryScheduleDTO.getId() + "' does no exist");
        }
    }
}