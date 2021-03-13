package com.fgonzalez.categorycalendar.controller;

import java.util.List;

import com.fgonzalez.categorycalendar.model.Category;
import com.fgonzalez.categorycalendar.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping(value="/getcategories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategories() {
        return categoryService.findAll();
    }
}
