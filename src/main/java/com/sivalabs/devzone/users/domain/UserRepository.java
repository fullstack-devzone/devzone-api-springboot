package com.sivalabs.devzone.users.domain;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserDTO> findById(Long id);

    UserDTO save(User user);
}
