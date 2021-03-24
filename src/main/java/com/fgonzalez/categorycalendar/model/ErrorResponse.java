package com.fgonzalez.categorycalendar.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper=false)
public @Data class ErrorResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @NonNull private String message;
    private List<String> details;
}