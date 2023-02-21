package com.dddd.sldocs.core.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="formulary")
public class Formulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String easFilename;
    private String pslFilename;
    private String indPlanZipFilename;
    private String departmentShortName;
    private String departmentFullNameGenitiveCase;
    private String departmentFullNameNominativeCase;
    private String academicYear;
    private String departmentHeadTittle;
    private String departmentHeadPositionName;
    private String departmentHeadFullName;
    private String institute;
    private String protocolNumber;
    private String protocolDate;
    private String approvedByTittle;
    private String approvedByPosition;
    private String approvedByFullName;

}
