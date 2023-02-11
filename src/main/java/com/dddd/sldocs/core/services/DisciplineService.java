package com.dddd.sldocs.core.services;

import com.dddd.sldocs.core.entities.Discipline;
import com.dddd.sldocs.core.repositories.DisciplineRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public DisciplineService(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    public List<Discipline> listAll(){
        return disciplineRepository.findAll();
    }

    public void save(Discipline discipline) {
        disciplineRepository.save(discipline);
    }

    public Discipline findByName(String name) {
        return disciplineRepository.getDisciplineByName(name);
    }

    public void deleteAll(){
        disciplineRepository.deleteAll();
    }
}
