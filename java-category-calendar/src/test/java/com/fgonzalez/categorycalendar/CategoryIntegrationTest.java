package com.fgonzalez.categorycalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.model.Category;
import com.fgonzalez.categorycalendar.repository.CategoryRepository;
import com.fgonzalez.categorycalendar.service.CategoryService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CategoryIntegrationTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("Test findById")
    public void testFindById() {
        // Setup our mock repository
        Category category;
        doReturn(Optional.of(category = new Category(1, "work", true))).when(categoryRepository).findById(1);
        doReturn(Optional.of(new Category(2, "vacations", true))).when(categoryRepository).findById(2);

        // Execute the service call
        Optional<Category> returnedCategory = categoryService.findById(1);

        // Assert the response
        Assertions.assertTrue(returnedCategory.isPresent(), "Category was not found");
        Assertions.assertSame(returnedCategory.get(), category, "The category returned was not the same as the mock");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        // Setup our mock repository
        doReturn(Optional.empty()).when(categoryRepository).findById(1);

        // Execute the service call
        Optional<Category> returnedCategory = categoryService.findById(1);

        // Assert the response
        Assertions.assertFalse(returnedCategory.isPresent(), "Category should not be found");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        // Setup our mock repository
        Category category1 = new Category(1, "Category Name", true);
        Category category2 = new Category(2, "Category 2 Name", true);
        doReturn(Arrays.asList(category1, category2)).when(categoryRepository).findAll();

        // Execute the service call
        List<Category> categories = categoryService.findAll();

        // Assert the response
        Assertions.assertEquals(2, categories.size(), "findAll should return 2 categories");
    }

    @Test
    @DisplayName("Test save category")
    void testSave() {
        // Setup our mock repository
        Category category = new Category(1, "work", true);
        doReturn(category).when(categoryRepository).save(any());

        // Execute the service call
        Category returnedCategory = categoryService.save(category);

        // Assert the response
        Assertions.assertNotNull(returnedCategory, "The saved category should not be null");
        Assertions.assertEquals(category.getId(), returnedCategory.getId(), "The return object is not the expected");
    }
}