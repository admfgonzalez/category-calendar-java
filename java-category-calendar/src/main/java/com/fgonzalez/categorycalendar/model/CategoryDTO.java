package com.fgonzalez.categorycalendar.model;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDTO {
    private Integer id;
    @NotNull
    private String categoryName;
    @NotNull
    private Boolean active;
}
