package com.mycompany.repository;

import com.mycompany.entity.RoleEntity;
import com.mycompany.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(ERole eRole);
}
