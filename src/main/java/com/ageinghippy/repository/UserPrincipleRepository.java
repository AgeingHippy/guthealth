package com.ageinghippy.repository;

import com.ageinghippy.model.entity.UserPrinciple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPrincipleRepository extends JpaRepository<UserPrinciple, Long> {

    Optional<UserPrinciple> findByUsername(String username);
}
