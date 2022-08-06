package com.dddd.sldocs.core.repositories;

import com.dddd.sldocs.core.entities.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpecialtyRepository extends JpaRepository<Specialty,Long> {

    @Modifying
    @Query("delete from Specialty s")
    void deleteAll();

    @Query("SELECT u FROM Specialty u WHERE u.name = :name")
    Specialty getSpecialtyByName(@Param("name") String name);
}
