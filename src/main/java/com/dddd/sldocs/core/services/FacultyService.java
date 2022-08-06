package com.dddd.sldocs.core.services;

import com.dddd.sldocs.core.entities.Faculty;
import com.dddd.sldocs.core.repositories.FacultyRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public List<Faculty> listAll() {
        return facultyRepository.findAll();
    }

    public void save(Faculty faculty) {
        facultyRepository.save(faculty);
    }

    public Faculty findByName(String name) {
        return facultyRepository.getFacultyByName(name);
    }

    public void deleteAll() {
        facultyRepository.deleteAll();
    }
}
