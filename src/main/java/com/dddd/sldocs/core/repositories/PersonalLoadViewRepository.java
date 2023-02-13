package com.dddd.sldocs.core.repositories;

import com.dddd.sldocs.core.entities.views.PersonalLoadView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonalLoadViewRepository extends JpaRepository<PersonalLoadView, Long> {
    @Query(value = "select p from PersonalLoadView p where p.csem=:semester and p.teacherName=:pname")
    List<PersonalLoadView> getPSLVM(@Param("semester") String semester, @Param("pname") String pname);

}
