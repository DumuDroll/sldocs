package com.dddd.sldocs.core.repositories;

import com.dddd.sldocs.core.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<Department,Long> {

    @Modifying
    @Query("delete from Department d")
    void deleteAll();

    @Query("SELECT u FROM Department u WHERE u.name = :name")
    Department getDepartmentByName(@Param("name") String name);
}
