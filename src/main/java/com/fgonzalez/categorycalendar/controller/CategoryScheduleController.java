package com.fgonzalez.categorycalendar.controller;

import java.util.List;

import com.fgonzalez.categorycalendar.model.CategoryScheduleDTO;
import com.fgonzalez.categorycalendar.service.CategoryScheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/categoryschedule")
public class CategoryScheduleController {
    @Autowired
    private CategoryScheduleService categoryScheduleService;

    @ApiOperation("Get all category schedules active and no active")
    @GetMapping(value = "/getcategoryschedules", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CategoryScheduleDTO> getCategorySchedules() {
        return categoryScheduleService.findAll().get();
    }

    @ApiOperation("Get all category schedule active and no active from the send year")
    @GetMapping(value = "/getcategoryschedulesbyyear", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CategoryScheduleDTO> getCategorySchedulesByYear(
            @ApiParam(value = "The year to search schedules", required = true, example = "2021") @RequestParam("year") Integer year) {
        return categoryScheduleService.findByYear(year).get();
    }

    @ApiOperation("Remove the send Category Schedule")
    @PostMapping(value = "/removecategoryschedule", produces = MediaType.APPLICATION_JSON_VALUE)
    public void removeCategorySchedule(
            @ApiParam(value = "The category schedule to remove", required = true) @RequestBody CategoryScheduleDTO oldCategorySchedule) {
        categoryScheduleService.remove(oldCategorySchedule);
    }

    @ApiOperation("Add the send Category Schedule")
    @PostMapping(value = "/addcategoryschedule", produces = MediaType.APPLICATION_JSON_VALUE)
    public CategoryScheduleDTO addCategorySchedule(
            @ApiParam(value = "The category schedule to add", required = true) @RequestBody CategoryScheduleDTO newCategoryScheduleDTO) {
        return categoryScheduleService.addNew(newCategoryScheduleDTO).get();
    }

    @ApiOperation("Get the last time of changes in millis")
    @GetMapping(value = "/getlastchangetime", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getLastChangeTime() {
        return categoryScheduleService.getLastChangeTime();
    }

    @ApiOperation("Returns true if there are changes since the send time")
    @GetMapping(value = "/therearechanges", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean thereAreChanges(Long timeInMillis) {
        return categoryScheduleService.thereAreChanges(timeInMillis);
    }
}
