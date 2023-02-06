package com.dddd.sldocs.core.entities.views;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "eas_vm")
public class EdAsStView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String csem;
    private String ccor;
    private String dname;
    private String groupNames;
    private String lecHours;
    private String labHours;
    private String pname;
    private String practHours;
    private String note;
    private String numberOfSubgroups;
    private String year;
}
