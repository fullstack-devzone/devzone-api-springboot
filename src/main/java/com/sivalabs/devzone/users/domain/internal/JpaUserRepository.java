package com.sivalabs.devzone.users.domain.internal;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
