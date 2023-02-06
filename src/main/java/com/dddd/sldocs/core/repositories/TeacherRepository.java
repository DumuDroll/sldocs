package com.dddd.sldocs.core.repositories;

import com.dddd.sldocs.core.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    @Query("SELECT u FROM Teacher u order by u.name")
    List<Teacher> listAllOrderName();

    @Modifying
    @Query("delete from Teacher p")
    void deleteAll();

    @Query("SELECT u FROM Teacher u WHERE u.name = :name")
    Teacher getTechearByName(@Param("name") String name);

    @Query("SELECT u FROM Teacher u WHERE u.id = :id")
    Teacher getById(@Param("id") long id);

    @Query("SELECT u.ipFilename FROM Teacher u")
    List<String> listIpFilenames();

    @Query(value="SELECT * FROM Teacher as u WHERE u.posada IS NULL OR u.posada='' OR u.nauk_stupin IS NULL OR u.nauk_stupin='' " +
            "OR u.vch_zvana IS NULL OR u.vch_zvana='' OR u.stavka IS NULL OR  u.stavka='' OR u.email_address IS NULL " +
            "OR u.email_address='' AND u.name NOT LIKE 'курсов%' AND NOT u.name='' OR u.name IS NULL ORDER BY u.name", nativeQuery = true)
    List<Teacher> listUnedited();

    @Query(value="SELECT u FROM Teacher u WHERE u.emailAddress IS NOT NULL OR NOT u.emailAddress=''")
    List<Teacher> listWithEmails();
}
