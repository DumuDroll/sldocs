package com.dddd.sldocs.core.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyLoad {

    private StudyloadRow studyloadRow;
    private Department department;
    private Discipline discipline;
    private Faculty faculty;
    private Teacher teacher;
    private Specialty specialty;


    public StudyLoad() {
        studyloadRow = new StudyloadRow();
        department = new Department();
        discipline = new Discipline();
        faculty = new Faculty();
        teacher = new Teacher();
        specialty = new Specialty();
    }

}
