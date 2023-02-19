package com.dddd.sldocs.core.services;

import com.dddd.sldocs.core.entities.views.EdAsStView;
import com.dddd.sldocs.core.repositories.EdAsStViewRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EdAsStViewService {

    private final EdAsStViewRepository edAsStViewRepository;

    public EdAsStViewService(EdAsStViewRepository edAsStViewRepository) {
        this.edAsStViewRepository = edAsStViewRepository;
    }

    public List<EdAsStView> getEASVM13Data(String semester, String course) {
        return edAsStViewRepository.getEASVM13(semester, course);
    }

    public List<EdAsStView> getEASVMData(String semester, String course) {
        return edAsStViewRepository.getEASVM(semester, course);
    }

}
