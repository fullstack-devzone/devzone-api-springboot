package com.sivalabs.devzone.application.port.out;

import com.sivalabs.devzone.domain.User;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User save(User user);
}
