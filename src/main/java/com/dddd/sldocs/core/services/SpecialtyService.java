package com.dddd.sldocs.core.services;

import com.dddd.sldocs.core.entities.Specialty;
import com.dddd.sldocs.core.repositories.SpecialtyRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    public void save(Specialty specialty) {
        specialtyRepository.save(specialty);
    }

    public void deleteAll() {
        specialtyRepository.deleteAll();
    }
}
