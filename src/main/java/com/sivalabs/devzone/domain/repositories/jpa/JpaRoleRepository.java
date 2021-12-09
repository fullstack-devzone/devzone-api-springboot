package com.sivalabs.devzone.domain.repositories.jpa;

import com.sivalabs.devzone.domain.entities.Role;
import com.sivalabs.devzone.domain.repositories.RoleRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleRepository extends RoleRepository, JpaRepository<Role, Long> {}
