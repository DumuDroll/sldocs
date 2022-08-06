package com.dddd.sldocs.core.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyLoad {

    private Curriculum curriculum;
    private Department department;
    private Discipline discipline;
    private Faculty faculty;
    private Professor professor;
    private Specialty specialty;


    public StudyLoad() {
        curriculum = new Curriculum();
        department = new Department();
        discipline = new Discipline();
        faculty = new Faculty();
        professor = new Professor();
        specialty = new Specialty();
    }

}
