package com.dddd.sldocs.core.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="faculty")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String easFilename;
    private String pslFilename;
    private String indPlanZipFilename;

    @OneToMany(mappedBy = "faculty", cascade = {CascadeType.ALL})
    private Set<Department> departments = new HashSet<>();

}