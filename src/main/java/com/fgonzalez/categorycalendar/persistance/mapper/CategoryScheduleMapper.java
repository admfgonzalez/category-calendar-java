package com.fgonzalez.categorycalendar.persistance.mapper;

import java.util.List;

import com.fgonzalez.categorycalendar.domain.CategoryScheduleDTO;
import com.fgonzalez.categorycalendar.persistance.entity.CategorySchedule;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface CategoryScheduleMapper {
    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "scheduleDate", target = "scheduleDate"),
        @Mapping(source = "category", target = "category"),
        @Mapping(source = "active", target = "active")
    })
    CategoryScheduleDTO toCategoryScheduleDTO(CategorySchedule categorySchedule);
    List<CategoryScheduleDTO> toCategorySchedulesDTO(List<CategorySchedule> categorySchedules);

    @InheritInverseConfiguration
    CategorySchedule toCategorySchedule(CategoryScheduleDTO categoryScheduleDTO);
    List<CategorySchedule> toCategorySchedules(List<CategoryScheduleDTO> categorySchedulesDTO);
}
