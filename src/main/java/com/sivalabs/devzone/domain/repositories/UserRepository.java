package com.sivalabs.devzone.domain.repositories;

import com.sivalabs.devzone.domain.entities.User;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User save(User user);

    Optional<User> findById(Long userId);
}
