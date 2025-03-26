package com.ageinghippy.repository;

import com.ageinghippy.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    public Optional<Role> findByAuthority(String Authority);

}
