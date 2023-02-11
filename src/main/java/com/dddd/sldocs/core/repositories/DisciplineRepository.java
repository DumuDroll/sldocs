package com.dddd.sldocs.core.repositories;

import com.dddd.sldocs.core.entities.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DisciplineRepository extends JpaRepository<Discipline,Long> {

    @Modifying
    @Query("delete from Discipline d")
    void deleteAll();

    @Query("SELECT u FROM Discipline u WHERE u.name = :name")
    Discipline getDisciplineByName(@Param("name") String name);
}
