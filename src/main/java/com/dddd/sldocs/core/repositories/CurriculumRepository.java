package com.dddd.sldocs.core.repositories;

import com.dddd.sldocs.core.entities.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CurriculumRepository extends JpaRepository<Curriculum,Long> {
    @Modifying
    @Query("delete from Curriculum c")
    void deleteAll();
}
