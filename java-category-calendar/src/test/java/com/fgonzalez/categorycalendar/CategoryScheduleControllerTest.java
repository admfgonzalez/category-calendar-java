package com.fgonzalez.categorycalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fgonzalez.categorycalendar.model.Category;
import com.fgonzalez.categorycalendar.model.CategorySchedule;
import com.fgonzalez.categorycalendar.repository.CategoryScheduleRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CategoryScheduleControllerTest extends AbstractControllerTest {
    @MockBean
    private CategoryScheduleRepository categoryScheduleRepository;

    private Category defaultCategory;
    private CategorySchedule categorySchedule1;
    private CategorySchedule categorySchedule2;
    private CategorySchedule newCategorySchedule;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        defaultCategory = new Category(1, "work", true);

        categorySchedule1 = new CategorySchedule(1, 20210313, defaultCategory, true);
        categorySchedule2 = new CategorySchedule(2, 20210313, defaultCategory, true);
        newCategorySchedule = new CategorySchedule(3, 20210313, defaultCategory, true);

        doReturn(Arrays.asList(categorySchedule1, categorySchedule2)).when(categoryScheduleRepository).findAll();
        when(categoryScheduleRepository.findById(1)).thenReturn(Optional.of(categorySchedule1));
        when(categoryScheduleRepository.findById(2)).thenReturn(Optional.of(categorySchedule2));
        when(categoryScheduleRepository.save(newCategorySchedule)).thenReturn(newCategorySchedule);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(categoryScheduleRepository).save(any());
    }

    @Test
    @DisplayName("Test get all schedules")
    public void testGetCategories() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/categoryschedule/getcategoryschedules/")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertArrayEquals(new CategorySchedule[] { categorySchedule1, categorySchedule2 },
                mapFromJson(content, CategorySchedule[].class), "The retuns must be all the categories");
    }

    @Test
    @DisplayName("Remove category controller test")
    public void testRemoveCategorySchedule() throws Exception {
        when(categoryScheduleRepository.findOne(any())).thenReturn(Optional.of(categorySchedule1));

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post("/categoryschedule/removecategoryschedule")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(categorySchedule1)))
                .andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(),
                "Expected operation was correct");
    }

    @Test
    @DisplayName("Add new Category Schedule test")
    public void testAddcategorySchedule() throws Exception {

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post("/categoryschedule/addcategoryschedule")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(newCategorySchedule)))
                .andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(),
                "Expected operation was correct");

        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("scheduleDate", null);
        payload.put("active", true);
        mvcResult = mvc.perform(MockMvcRequestBuilders.post("/categoryschedule/addcategoryschedule")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(payload))).andReturn();
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), mvcResult.getResponse().getStatus(),
                "Expected error on server side");
    }

    @Test
    @DisplayName("Test getcategoryschedulesbyyear")
    void testGetCategorySchedulesByYear() throws Exception {
        List<CategorySchedule> expected = Arrays.asList(categorySchedule1, categorySchedule2);
        when(categoryScheduleRepository.findCategorySchedulesByYear(2021)).thenReturn(expected);

        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.get("/categoryschedule/getcategoryschedulesbyyear")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("year", "2021"))
                .andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(),  "Expected operation was correct");
    }
}
