package com.fgonzalez.categorycalendar;

import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.model.Category;
import com.fgonzalez.categorycalendar.model.CategorySchedule;
import com.fgonzalez.categorycalendar.repository.CategoryScheduleRepository;
import com.fgonzalez.categorycalendar.service.CategoryScheduleService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CategoryScheduleIntegrationTest {
    @MockBean
    private CategoryScheduleRepository categoryScheduleRepository;

    @Autowired
    private CategoryScheduleService categoryScheduleService;

    private Category defaultCategory;
    private CategorySchedule categorySchedule1;
    private CategorySchedule categorySchedule2;
    private CategorySchedule newCategorySchedule;

    @BeforeEach
    void setup() {
        defaultCategory = new Category(1, "work", true);
        categorySchedule1 = new CategorySchedule(1, new Date(), defaultCategory, true);
        categorySchedule2 = new CategorySchedule(2, new Date(), defaultCategory, true);
        newCategorySchedule = new CategorySchedule(3, new Date(), defaultCategory, true);
 
        doReturn(Optional.of(categorySchedule1)).when(categoryScheduleRepository).findById(1);
        doReturn(Optional.of(categorySchedule2)).when(categoryScheduleRepository).findById(2);
        doReturn(Optional.empty()).when(categoryScheduleRepository).findById(4);
        doReturn(Arrays.asList(categorySchedule1, categorySchedule2)).when(categoryScheduleRepository).findAll();
        doReturn(newCategorySchedule).when(categoryScheduleRepository).save(newCategorySchedule);
    }

    @Test
    @DisplayName("Test findById")
    public void testFindById() {
        Optional<CategorySchedule> returnedCategorySchedule = categoryScheduleService.findById(1);
        Assertions.assertTrue(returnedCategorySchedule.isPresent(), "Category Schedule was not found");
        Assertions.assertSame(returnedCategorySchedule.get(), categorySchedule1,
                "The category schedule returned was not the same as the mock");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        Optional<CategorySchedule> returnedCategorySchedule = categoryScheduleService.findById(4);
        Assertions.assertFalse(returnedCategorySchedule.isPresent(), "Category schedule should not be found");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        List<CategorySchedule> categories = categoryScheduleService.findAll();
        Assertions.assertEquals(2, categories.size(), "findAll should return 2 registers");
    }

    @Test
    @DisplayName("Test save category")
    void testSave() {
        CategorySchedule returnedCategory = categoryScheduleService.save(newCategorySchedule);

        Assertions.assertNotNull(returnedCategory, "The saved category schedule should not be null");
        Assertions.assertEquals(newCategorySchedule.getId(), returnedCategory.getId(),
                "The return object is not the expected");
    }

    @Test
    @DisplayName("Test save null category schedule")
    void testSaveNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            categoryScheduleService.save(null);
        });
    }

    @Test
    @DisplayName("Test remove Category Schedule")
    void testRemoveCategorySchedule() {
        categoryScheduleService.removeCategorySchedule(categorySchedule1);
        Optional<CategorySchedule> returnedCategorySchedule = categoryScheduleRepository.findById(categorySchedule1.getId());

        Assertions.assertNotNull(returnedCategorySchedule, "The saved category should not be null");
        Assertions.assertEquals(false, returnedCategorySchedule.get().isActive(), "The return object should not active");
    }
}
