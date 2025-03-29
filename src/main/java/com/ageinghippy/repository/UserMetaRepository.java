package com.ageinghippy.repository;

import com.ageinghippy.model.entity.UserMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMetaRepository extends JpaRepository<UserMeta,Long> {
}
