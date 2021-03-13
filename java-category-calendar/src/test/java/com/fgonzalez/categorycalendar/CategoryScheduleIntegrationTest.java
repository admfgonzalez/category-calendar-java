package com.fgonzalez.categorycalendar;

import static org.mockito.ArgumentMatchers.any;
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
import org.junit.jupiter.api.BeforeAll;
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

    private static Category defaultCategory;

    @BeforeAll
    static void loadCategory() {
        defaultCategory = new Category(1, "work", true);
    }

    @Test
    @DisplayName("Test findById")
    public void testFindById() {
        // Setup our mock repository
        CategorySchedule categorySchedule;
        doReturn(Optional.of(categorySchedule = new CategorySchedule(1, new Date(), defaultCategory, true))).when(categoryScheduleRepository).findById(1);
        doReturn(Optional.of(new CategorySchedule(2, new Date(), defaultCategory, true))).when(categoryScheduleRepository).findById(2);

        // Execute the service call
        Optional<CategorySchedule> returnedCategorySchedule = categoryScheduleService.findById(1);

        // Assert the response
        Assertions.assertTrue(returnedCategorySchedule.isPresent(), "Category Schedule was not found");
        Assertions.assertSame(returnedCategorySchedule.get(), categorySchedule, "The category schedule returned was not the same as the mock");
    }


    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        // Setup our mock repository
        doReturn(Optional.empty()).when(categoryScheduleRepository).findById(1);

        // Execute the service call
        Optional<CategorySchedule> returnedCategorySchedule = categoryScheduleService.findById(1);

        // Assert the response
        Assertions.assertFalse(returnedCategorySchedule.isPresent(), "Category schedule should not be found");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        // Setup our mock repository
        CategorySchedule categorySchedule1 = new CategorySchedule(1, new Date(), defaultCategory, true);
        CategorySchedule categorySchedule2 = new CategorySchedule(2, new Date(), defaultCategory, true);
        doReturn(Arrays.asList(categorySchedule1, categorySchedule2)).when(categoryScheduleRepository).findAll();

        // Execute the service call
        List<CategorySchedule> categories = categoryScheduleService.findAll();

        // Assert the response
        Assertions.assertEquals(2, categories.size(), "findAll should return 2 registers");
    }

    @Test
    @DisplayName("Test save category")
    void testSave() {
        // Setup our mock repository
        CategorySchedule categorySchedule = new CategorySchedule(1, new Date(), defaultCategory, true);;
        doReturn(categorySchedule).when(categoryScheduleRepository).save(any());

        // Execute the service call
        CategorySchedule returnedCategory = categoryScheduleService.save(categorySchedule);

        // Assert the response
        Assertions.assertNotNull(returnedCategory, "The saved category schedule should not be null");
        Assertions.assertEquals(categorySchedule.getId(), returnedCategory.getId(), "The return object is not the expected");
    }
}
