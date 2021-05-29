package com.sivalabs.devzone.domain.repositories;

import com.sivalabs.devzone.domain.entities.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
