package com.dddd.sldocs.core.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyLoad {

    private StudyloadRow studyloadRow;
    private Discipline discipline;
    private Teacher teacher;


    public StudyLoad() {
        studyloadRow = new StudyloadRow();
        discipline = new Discipline();
        teacher = new Teacher();
    }

}
