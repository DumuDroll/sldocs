package com.dddd.sldocs.core.repositories;

import com.dddd.sldocs.core.entities.views.EdAsStView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EdAsStViewRepository extends JpaRepository<EdAsStView,Long> {
    @Query(value = "select e from EdAsStView e where e.csem=:semester and e.ccor<:course")
    List<EdAsStView> getEAS_VM13(@Param("semester") String semester, @Param("course") String course);
    @Query(value = "select e from EdAsStView e where e.csem=:semester and e.ccor=:course")
    List<EdAsStView> getEAS_VM(@Param("semester") String semester, @Param("course") String course);

}
