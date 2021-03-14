package com.fgonzalez.categorycalendar.controller;

import java.util.List;

import com.fgonzalez.categorycalendar.model.CategorySchedule;
import com.fgonzalez.categorycalendar.service.CategoryScheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categoryschedule")
public class CategoryScheduleController {
    @Autowired
    private CategoryScheduleService categoryScheduleService;

    @RequestMapping(value = "/getcategoryschedules", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CategorySchedule> getCategorySchedules() {
        return categoryScheduleService.findAll();
    }

    @RequestMapping(value = "/getcategoryschedulesbyyear", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CategorySchedule> getCategorySchedulesByYear(@RequestParam("year") Integer year) {
        return categoryScheduleService.findByYear(year);
    }

    @RequestMapping(value = "/removecategoryschedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void removeCategorySchedule(@RequestBody CategorySchedule oldCategorySchedule) {
        categoryScheduleService.removeCategorySchedule(oldCategorySchedule);
    }

    @RequestMapping(value= "/addcategoryschedule", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public CategorySchedule addCategorySchedule(@RequestBody CategorySchedule newCategorySchedule) {
        return categoryScheduleService.addNew(newCategorySchedule);
    }
}
