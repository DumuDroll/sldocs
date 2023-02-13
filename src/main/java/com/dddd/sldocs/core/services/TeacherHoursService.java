package com.dddd.sldocs.core.services;

import com.dddd.sldocs.core.entities.TeacherHours;
import com.dddd.sldocs.core.repositories.TeacherHoursRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TeacherHoursService {

    private final TeacherHoursRepository teacherHoursRepository;

    public TeacherHoursService(TeacherHoursRepository teacherHoursRepository) {
        this.teacherHoursRepository = teacherHoursRepository;
    }

    public void save(TeacherHours teacherHours) {
        teacherHoursRepository.save(teacherHours);
    }
}
