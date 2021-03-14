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
import org.junit.jupiter.api.BeforeEach;
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

    private Category category1;
    private Category category2;
    @BeforeEach
    void setup() {
        category1 = new Category(1, "work", true);
        category2 = new Category(2, "vacations", true);
        doReturn(Optional.of(category1)).when(categoryRepository).findById(1);
        doReturn(Optional.of(category2)).when(categoryRepository).findById(2);
        doReturn(Optional.empty()).when(categoryRepository).findById(3);
        doReturn(category1).when(categoryRepository).getCategoryByNameCategory(category1.getCategoryName());
    }
    
    @Test
    @DisplayName("Test findById")
    public void testFindById() {
        Optional<Category> returnedCategory = categoryService.findById(1);

        Assertions.assertTrue(returnedCategory.isPresent(), "Category was not found");
        Assertions.assertSame(returnedCategory.get(), category1, "The category returned was not the same as the mock");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        Optional<Category> returnedCategory = categoryService.findById(3);
        Assertions.assertFalse(returnedCategory.isPresent(), "Category should not be found");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        doReturn(Arrays.asList(category1, category2)).when(categoryRepository).findAll();
        List<Category> categories = categoryService.findAll();
        Assertions.assertEquals(2, categories.size(), "findAll should return 2 categories");
    }

    @Test
    @DisplayName("Test save category")
    void testSave() {
        Category category = new Category(3, "work", true);
        doReturn(category).when(categoryRepository).save(any());

        Category returnedCategory = categoryService.save(category);

        Assertions.assertNotNull(returnedCategory, "The saved category should not be null");
        Assertions.assertEquals(category.getId(), returnedCategory.getId(), "The return object is not the expected");
    }

    @Test
    @DisplayName("Test save category with no name")
    void testSaveNoName() {
        Category category = new Category(1, "", true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            categoryService.save(category);
        });
    }

    @Test
    @DisplayName("Test remove Category")
    void testRemoveCategory() { 
         categoryService.removeCategory(category1);

         Optional<Category> returnedCategory = categoryService.findById(1);
         
         Assertions.assertNotNull(returnedCategory, "The saved category should not be null");
         Assertions.assertEquals(false, returnedCategory.get().isActive(), "The return object should not active");
    }
}