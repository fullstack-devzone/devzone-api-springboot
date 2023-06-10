package com.sivalabs.devzone.application.port.in;

import com.sivalabs.devzone.domain.User;
import java.util.Optional;

public interface FindUserUseCase {
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
}
