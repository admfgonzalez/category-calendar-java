package com.fgonzalez.categorycalendar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.model.CategorySchedule;
import com.fgonzalez.categorycalendar.repository.CategoryScheduleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryScheduleServiceImpl implements CategoryScheduleService {

    @Autowired
    private CategoryScheduleRepository categoryScheduleRepository;

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
    public CategorySchedule save(CategorySchedule categorySchedule) {
        return categoryScheduleRepository.save(categorySchedule);
    }

    @Override
    public void deleteById(Integer id) {
        categoryScheduleRepository.deleteById(id);
    }
}
