package com.dddd.sldocs.core.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "professor")
public class Professor {

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
    private String ipFilename;
    private String pslFilename;
    private String emailAddress;
    private String emailedDate;
    private String bachNum;
    private String fifthCourseNum;
    private String masterProfNum;
    private String masterScNum;

    @ManyToMany(mappedBy = "professors", fetch = FetchType.LAZY)
    private Set<Curriculum> curriculums = new HashSet<>();

}
