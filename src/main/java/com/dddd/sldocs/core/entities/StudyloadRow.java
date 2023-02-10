package com.dddd.sldocs.core.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "studyload_row")
public class StudyloadRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String year;
    private String lecHours;
    private String labHours;
    private String note;
    private String numberOfSubgroups;
    private String examHours;
    private String zalikHours;
    private String cpHours;
    private String consultsHours;
    private String diplomaHours;
    private String decCell;
    private String ndrs;
    private String aspirantHours;
    private String practice;
    private String otherFormsHours;
    private String practHours;
    private String indTaskHours;
    private String semester;
    private String groupNames;
    private String studentsNumber;
    private String course;

    @ManyToOne
    @JoinColumn(name = "formulary_id", referencedColumnName = "id")
    private Formulary formulary;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "discipline_id", referencedColumnName = "id")
    private Discipline discipline;
}
