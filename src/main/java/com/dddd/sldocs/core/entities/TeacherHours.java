package com.dddd.sldocs.core.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "teacher_hours")
public class TeacherHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String ipFilename;
    private String pslFilename;
    private String bachNum;
    private String fifthCourseNum;
    private String masterProfNum;
    private String masterScNum;
    private String autumnSumFact;
    private String autumnSumPlan;
    private String springSumFact;
    private String springSumPlan;
}
