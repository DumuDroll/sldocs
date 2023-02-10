package com.dddd.sldocs.core.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String fullName;
    private String posada;
    private String naukStupin;
    private String vchZvana;
    private String stavka;
    private String note;
    private String emailAddress;
    private String emailedDate;

    @OneToOne
    private TeacherHours teacherHours = new TeacherHours();

    @OneToMany(mappedBy = "teacher")
    private Set<StudyloadRow> studyloadRows = new HashSet<>();

}
