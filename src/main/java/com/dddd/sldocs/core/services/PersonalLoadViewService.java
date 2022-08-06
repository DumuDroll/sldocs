package com.dddd.sldocs.core.services;

import com.dddd.sldocs.core.entities.views.PersonalLoadView;
import com.dddd.sldocs.core.repositories.PersonalLoadViewRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PersonalLoadViewService {

    private final PersonalLoadViewRepository personalLoadViewRepository;

    public PersonalLoadViewService(PersonalLoadViewRepository personalLoadViewRepository) {
        this.personalLoadViewRepository = personalLoadViewRepository;
    }

    public List<PersonalLoadView> getPSL_VMData(String semester, String pname) {
        return personalLoadViewRepository.getPSL_VM(semester, pname);
    }
}
