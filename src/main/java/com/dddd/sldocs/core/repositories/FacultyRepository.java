package com.dddd.sldocs.core.repositories;

import com.dddd.sldocs.core.entities.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FacultyRepository extends JpaRepository<Faculty,Long> {

    @Modifying
    @Query("delete from Faculty f")
    void deleteAll();

    @Query("SELECT u FROM Faculty u WHERE u.name = :name")
    Faculty getFacultyByName(@Param("name") String name);
}
