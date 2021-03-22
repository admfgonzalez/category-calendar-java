package com.fgonzalez.categorycalendar.model;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryScheduleDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    @NotNull
    private Integer scheduleDate;
    @NotNull
    @Valid
    private CategoryDTO category;
    @NotNull
    private Boolean active;
}