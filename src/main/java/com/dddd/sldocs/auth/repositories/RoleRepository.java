package com.dddd.sldocs.auth.repositories;

import com.dddd.sldocs.auth.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query(value = "SELECT r FROM Role r WHERE r.name=:name")
    Role getRoleByName(String name);
}
