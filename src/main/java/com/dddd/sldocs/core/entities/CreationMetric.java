package com.dddd.sldocs.core.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "creation_metric")
public class CreationMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int teacherNumber;
    private double timeToForm;
}
