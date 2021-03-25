package com.fgonzalez.categorycalendar.controller;

import java.util.List;

import javax.validation.Valid;

import com.fgonzalez.categorycalendar.domain.CategoryDTO;
import com.fgonzalez.categorycalendar.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/getcategories", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Get all categories active and no active")
    public List<CategoryDTO> getCategories() {
        return categoryService.findAll().get();
    }

    @PostMapping(value = "/removecategory", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Remove the send category")
    public void removeCategory(
            @ApiParam(value = "The category to remove", required = true) @Valid @RequestBody CategoryDTO oldCategoryDTO) {
        categoryService.remove(oldCategoryDTO);
    }

    @ApiOperation("Add the send category")
    @PostMapping(value = "/addcategory", produces = MediaType.APPLICATION_JSON_VALUE)
    public CategoryDTO addCategory(
            @ApiParam(value = "The category to add", required = true) @RequestBody CategoryDTO newCategoryDTO) {
        return categoryService.addNew(newCategoryDTO).get();
    }

    @ApiOperation("Get the last time of changes in millis")
    @GetMapping(value = "/getlastchangetime", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getLastChangeTime() {
        return categoryService.getLastChangeTime();
    }
}
