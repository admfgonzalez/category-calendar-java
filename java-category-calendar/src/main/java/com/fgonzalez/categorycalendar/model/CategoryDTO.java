package com.fgonzalez.categorycalendar.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private String color;
    @NotNull
    private Boolean active;
}
