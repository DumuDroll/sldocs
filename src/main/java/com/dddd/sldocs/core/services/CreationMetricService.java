package com.dddd.sldocs.core.services;

import com.dddd.sldocs.core.entities.CreationMetric;
import com.dddd.sldocs.core.repositories.CreationMetricRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CreationMetricService {

    private final CreationMetricRepository metricRepository;

    public CreationMetricService(CreationMetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    @Transactional
    public void save(CreationMetric creationMetric) {
        metricRepository.save(creationMetric);
    }
}
