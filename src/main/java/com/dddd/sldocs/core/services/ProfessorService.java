package com.dddd.sldocs.core.services;

import com.dddd.sldocs.core.entities.Professor;
import com.dddd.sldocs.core.repositories.ProfessorRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public List<Professor> listAll() {
        return professorRepository.findAll();
    }

    public List<Professor> listAllOrderName() {
        return professorRepository.listAllOrderName();
    }

    public void save(Professor professor) {
        professorRepository.save(professor);
    }

    public Professor findByName(String name) {
        return professorRepository.getProfessorByName(name);
    }

    public Professor getByID(long id) {
        return professorRepository.getById(id);
    }

    public List<String> listIpFilenames(){
        return professorRepository.listIpFilenames();
    }

    public void deleteAll() {
        professorRepository.deleteAll();
    }

    public List<Professor> listWithEmails() {
        return professorRepository.listWithEmails();
    }

}
