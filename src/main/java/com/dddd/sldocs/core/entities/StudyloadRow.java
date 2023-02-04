package com.dddd.sldocs.core.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "studyload_row_discipline",
            joinColumns = @JoinColumn(name = "studyload_row_id"),
            inverseJoinColumns = @JoinColumn(name = "discipline_id")
    )
    private Set<Discipline> disciplines = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "studyload_row_professor",
            joinColumns = @JoinColumn(name = "studyload_row_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id")
    )
    private Set<Professor> professors = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "specialty_id", referencedColumnName = "id")
    private Specialty specialty;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

}
