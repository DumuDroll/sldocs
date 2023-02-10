package com.dddd.sldocs.core.services;

import com.dddd.sldocs.core.entities.Formulary;
import com.dddd.sldocs.core.repositories.FormularyRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class FormularyService {

    private final FormularyRepository formularyRepository;

    public FormularyService(FormularyRepository formularyRepository) {
        this.formularyRepository = formularyRepository;
    }

    public List<Formulary> listAll(){
        return formularyRepository.findAll();
    }

    public void deleteAll(){
        formularyRepository.deleteAll();
    }

    public void save(Formulary formulary){
        formularyRepository.save(formulary);
    }
}
