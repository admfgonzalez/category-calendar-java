package com.fgonzalez.categorycalendar;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fgonzalez.categorycalendar.domain.CategoryScheduleDTO;
import com.fgonzalez.categorycalendar.persistance.entity.Category;
import com.fgonzalez.categorycalendar.persistance.entity.CategorySchedule;
import com.fgonzalez.categorycalendar.persistance.mapper.CategoryScheduleMapper;
import com.fgonzalez.categorycalendar.persistance.repository.CategoryScheduleRepository;
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

    @Autowired
    private CategoryScheduleMapper categoryScheduleMapper;

    private Category defaultCategory;
    private CategorySchedule categorySchedule1;
    private CategorySchedule categorySchedule2;
    private CategorySchedule newCategorySchedule;

    @BeforeEach
    void setup() {
        defaultCategory = Category.builder().id(1).name("vacations").color("ff5050").active(true).build();
        categorySchedule1 = new CategorySchedule(1, 20210313, defaultCategory, true);
        categorySchedule2 = new CategorySchedule(2, 20210313, defaultCategory, true);
        newCategorySchedule = new CategorySchedule(3, 20210313, defaultCategory, true);

        doReturn(Optional.of(categorySchedule1)).when(categoryScheduleRepository).findById(1);
        doReturn(Optional.of(categorySchedule2)).when(categoryScheduleRepository).findById(2);
        doReturn(Optional.empty()).when(categoryScheduleRepository).findById(4);
        doReturn(Arrays.asList(categorySchedule1, categorySchedule2)).when(categoryScheduleRepository).findAll();
        doReturn(newCategorySchedule).when(categoryScheduleRepository).save(newCategorySchedule);
    }

    @Test
    @DisplayName("Test findById")
    public void testFindById() {
        Optional<CategoryScheduleDTO> returnedCategorySchedule = categoryScheduleService.findById(1);
        Assertions.assertTrue(returnedCategorySchedule.isPresent(), "Category Schedule was not found");
        Assertions.assertEquals(returnedCategorySchedule.get(),
                categoryScheduleMapper.toCategoryScheduleDTO(categorySchedule1),
                "The category schedule returned was not the same as the mock");
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        Optional<CategoryScheduleDTO> returnedCategorySchedule = categoryScheduleService.findById(4);
        Assertions.assertFalse(returnedCategorySchedule.isPresent(), "Category schedule should not be found");
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        Optional<List<CategoryScheduleDTO>> categories = categoryScheduleService.findAll();
        Assertions.assertEquals(2, categories.get().size(), "findAll should return 2 registers");
    }

    @Test
    @DisplayName("Test save category")
    void testSave() {
        Optional<CategoryScheduleDTO> returnedCategory = categoryScheduleService
                .save(categoryScheduleMapper.toCategoryScheduleDTO(newCategorySchedule));

        Assertions.assertNotNull(returnedCategory, "The saved category schedule should not be null");
        Assertions.assertEquals(newCategorySchedule.getId(), returnedCategory.get().getId(),
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
        categoryScheduleService.remove(categoryScheduleMapper.toCategoryScheduleDTO(categorySchedule1));
        Optional<CategorySchedule> returnedCategorySchedule = categoryScheduleRepository
                .findById(categorySchedule1.getId());

        Assertions.assertNotNull(returnedCategorySchedule, "The saved category should not be null");
        Assertions.assertEquals(false, returnedCategorySchedule.get().getActive(),
                "The return object should not active");
    }

    @Test
    @DisplayName("Test getCategorySchedulesByYear")
    public void testGetCategorySchedulesByYear() {
        List<CategorySchedule> expected = Arrays.asList(categorySchedule1, categorySchedule2);
        when(categoryScheduleRepository.findCategorySchedulesByYear(2021)).thenReturn(expected);

        Optional<List<CategoryScheduleDTO>> categoriSchedulesReturn = categoryScheduleService.findByYear(2021);
        Assertions.assertEquals(categoryScheduleMapper.toCategorySchedulesDTO(expected), categoriSchedulesReturn.get(),
                "CategorySchedules By year have not expected result");
    }
}
