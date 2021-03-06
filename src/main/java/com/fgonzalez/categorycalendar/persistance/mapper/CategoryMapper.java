package com.fgonzalez.categorycalendar.persistance.mapper;

import java.util.List;

import com.fgonzalez.categorycalendar.domain.CategoryDTO;
import com.fgonzalez.categorycalendar.persistance.entity.Category;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "color", target = "color"),
        @Mapping(source = "active", target = "active")
    })
    CategoryDTO toCategoryDTO(Category category);
    List<CategoryDTO> toCategoriesDTO(List<Category> categories);

    @InheritInverseConfiguration
    Category toCategory(CategoryDTO categoryDTO);
    List<Category> toCategories(List<CategoryDTO> categoriesDTO);
}
