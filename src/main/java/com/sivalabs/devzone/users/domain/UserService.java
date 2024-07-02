package com.sivalabs.devzone.users.domain;

import com.sivalabs.devzone.common.exceptions.BadRequestException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email " + request.email() + " is already in use");
        }
        User user = new User(
                null, request.name(), request.email(), passwordEncoder.encode(request.password()), Role.ROLE_USER);
        return userRepository.save(user);
    }
}
