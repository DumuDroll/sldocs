package com.dddd.sldocs.core.entities.views;

import lombok.Getter;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import javax.persistence.*;

@Getter
@Entity
@Immutable
@Subselect("select eas_vm.* from eas_vm")
@Table(name = "eas_vm")
public class EdAsStView {

    @Id
    private Long id;
    private String csem;
    private String ccor;
    private String disciplineName;
    private String groupNames;
    private String lecHours;
    private String labHours;
    private String teacherName;
    private String practHours;
    private String note;
    private String numberOfSubgroups;
    private String year;
}
