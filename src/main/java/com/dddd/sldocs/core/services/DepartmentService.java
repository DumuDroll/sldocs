package com.dddd.sldocs.core.services;

import com.dddd.sldocs.core.entities.Department;
import com.dddd.sldocs.core.repositories.DepartmentRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public void save(Department department) {
        departmentRepository.save(department);
    }

    public Department findByName(String name) {
        return departmentRepository.getDepartmentByName(name);
    }

    public void deleteAll() {
        departmentRepository.deleteAll();
    }
}
