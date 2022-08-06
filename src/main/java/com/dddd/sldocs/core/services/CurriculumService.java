package com.dddd.sldocs.core.services;

import com.dddd.sldocs.core.entities.Curriculum;
import com.dddd.sldocs.core.repositories.CurriculumRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CurriculumService {

    private final CurriculumRepository curriculumRepository;

    public CurriculumService(CurriculumRepository curriculumRepository) {
        this.curriculumRepository = curriculumRepository;
    }

    public List<Curriculum> listAllistAll() {
        return curriculumRepository.findAll();
    }

    public void save(Curriculum curriculum) {
        curriculumRepository.save(curriculum);
    }

    public void deleteAll() {
        curriculumRepository.deleteAll();
    }

}
