package com.fgonzalez.categorycalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.model.CategoryDTO;
import com.fgonzalez.categorycalendar.persistance.entity.Category;
import com.fgonzalez.categorycalendar.persistance.mapper.CategoryMapper;
import com.fgonzalez.categorycalendar.persistance.repository.CategoryRepository;
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

    @Autowired
    private CategoryMapper categoryMapper;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setup() {
        category1 = Category.builder().id(1).name("vacations").color("ff5050").active(true).build();
        category2 = Category.builder().id(2).name("work").color("0066ff").active(true).build();
        doReturn(Optional.of(category1)).when(categoryRepository).findById(1);
        doReturn(Optional.of(category2)).when(categoryRepository).findById(2);
        doReturn(Optional.empty()).when(categoryRepository).findById(3);
        doReturn(category1).when(categoryRepository).findCategoryByNameCategory(category1.getName());
    }

    @Test
    @DisplayName("Test findById")
    public void testFindById() {
        Optional<CategoryDTO> returnedCategory = categoryService.findById(1);
        Assertions.assertTrue(returnedCategory.isPresent(), "Category was not found");
        Assertions.assertEquals(returnedCategory.get(), categoryMapper.toCategoryDTO(category1), "The category returned was not the same as the mock");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        Optional<CategoryDTO> returnedCategory = categoryService.findById(3);
        Assertions.assertFalse(returnedCategory.isPresent(), "Category should not be found");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        doReturn(Arrays.asList(category1, category2)).when(categoryRepository).findAll();
        List<CategoryDTO> categories = categoryService.findAll().get();
        Assertions.assertEquals(2, categories.size(), "findAll should return 2 categories");
    }

    @Test
    @DisplayName("Test save category")
    void testSave() {
        Category category = Category.builder().id(3).name("special").color("00cc00").active(true).build();
        doReturn(category).when(categoryRepository).save(any());

        Optional<CategoryDTO> returnedCategory = categoryService.save(categoryMapper.toCategoryDTO(category1));

        Assertions.assertNotNull(returnedCategory, "The saved category should not be null");
        Assertions.assertEquals(category.getId(), returnedCategory.get().getId(),
                "The return object is not the expected");
    }

    @Test
    @DisplayName("Test save category with no name")
    void testSaveNoName() {
        CategoryDTO category = CategoryDTO.builder().id(1).name("").color("").active(true).build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            categoryService.save(category);
        });
    }

    @Test
    @DisplayName("Test remove Category")
    void testRemoveCategory() {
        categoryService.removeCategory(categoryMapper.toCategoryDTO(category1));

        Optional<CategoryDTO> returnedCategory = categoryService.findById(1);

        Assertions.assertNotNull(returnedCategory, "The saved category should not be null");
        Assertions.assertEquals(false, returnedCategory.get().getActive(), "The return object should not active");
    }
}