package com.sivalabs.devzone.domain.repositories;

import com.sivalabs.devzone.domain.entities.Role;
import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(String name);
}
