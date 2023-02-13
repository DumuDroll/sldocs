package com.dddd.sldocs.core.entities.views;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "psl_vm")
public class PersonalLoadView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String year;
    private String course;
    private String csem;
    private String numberOfSubgroups;
    private String studentsNumber;
    private String groupNames;
    private String lecHours;
    private String consultHours;
    private String labHours;
    private String practHours;
    private String indTaskHours;
    private String cpHours;
    private String zalikHours;
    private String examHours;
    private String diplomaHours;
    private String decCell;
    private String ndrs;
    private String aspirantHours;
    private String practice;
    private String otherFormsHours;
    private String teacherName;
    private String disciplineName;
    private String depNameGc;
    private String depNameNc;
    private String institute;
    private String naukStupin;
    private String posada;
    private String vchZvana;
    private String stavka;
}
