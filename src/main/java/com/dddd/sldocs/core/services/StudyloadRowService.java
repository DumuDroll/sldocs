package com.dddd.sldocs.core.services;

import com.dddd.sldocs.core.entities.StudyloadRow;
import com.dddd.sldocs.core.repositories.StudyloadRowRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class StudyloadRowService {

    private final StudyloadRowRepository studyloadRowRepository;

    public StudyloadRowService(StudyloadRowRepository studyloadRowRepository) {
        this.studyloadRowRepository = studyloadRowRepository;
    }

    public void save(StudyloadRow studyloadRow) {
        studyloadRowRepository.save(studyloadRow);
    }

    public void deleteAll() {
        studyloadRowRepository.deleteAll();
    }

}
