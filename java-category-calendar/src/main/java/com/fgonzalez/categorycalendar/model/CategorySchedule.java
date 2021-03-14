package com.fgonzalez.categorycalendar.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "TBL_CATEGORY_SCHEDULES")
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public @Data class CategorySchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "schedule_date")
    @NonNull
    private Date scheduleDate;

    @JoinColumn(name = "fk_categories_id")
    @OneToOne(optional = true)
    @NonNull
    private Category category;

    @Column
    private boolean active;
}
