package com.fgonzalez.categorycalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fgonzalez.categorycalendar.persistance.entity.Category;
import com.fgonzalez.categorycalendar.persistance.repository.CategoryRepository;

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
public class CategoryControllerTest extends AbstractControllerTest {

    @MockBean
    private CategoryRepository categoryRepository;

    private Category category1;
    private Category category2;
    private Category newCategory;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        // mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        category1 = Category.builder().id(1).categoryName("Category Name").active(true).build();
        category2 = Category.builder().id(2).categoryName("Category Name 2").active(true).build();
        newCategory = Category.builder().id(3).categoryName("New Category Name").active(true).build();

        doReturn(Arrays.asList(category1, category2)).when(categoryRepository).findAll();
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(2)).thenReturn(Optional.of(category2));
        when(categoryRepository.save(newCategory)).thenReturn(newCategory);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(categoryRepository).save(any());
    }

    @Test
    @DisplayName("Test get all categories")
    public void testGetCategories() throws Exception {
        MvcResult mvcResult = mvc
                .perform(
                        MockMvcRequestBuilders.get("/category/getcategories/").accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertArrayEquals(new Category[] { category1, category2 }, mapFromJson(content, Category[].class),
                "The retuns must be all the categories");
    }

    @Test
    @DisplayName("Remove category controller test")
    public void testRemoveCategory() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/category/removecategory")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(category1))).andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(),
                "Expected operation was correct");
    }

    @Test
    @DisplayName("Add new Category test")
    public void testAddcategory() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/category/addcategory")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(newCategory))).andReturn();

        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(),
                "Expected operation was correct");

        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("categoryName", null);
        payload.put("active", true);
        mvcResult = mvc.perform(MockMvcRequestBuilders.post("/category/addcategory")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(payload))).andReturn();
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), mvcResult.getResponse().getStatus(),
                "Expected error on server side");
    }
}
