package com.fgonzalez.categorycalendar.persistance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@Table(name = "TBL_Categories")
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public @Data class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "category_name")
    @NonNull
    private String name;

    @Column
    @NonNull
    private String color;

    @Column
    private Boolean active;
}
