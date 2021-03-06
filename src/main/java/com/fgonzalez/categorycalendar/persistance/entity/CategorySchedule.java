package com.fgonzalez.categorycalendar.persistance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "TBL_CATEGORY_SCHEDULES")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public @Data class CategorySchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "schedule_date")
    @NonNull
    private Integer scheduleDate;

    @JoinColumn(name = "fk_categories_id", updatable = false)
    @ManyToOne
    @NonNull
    private Category category;

    @Column
    private Boolean active;
}
