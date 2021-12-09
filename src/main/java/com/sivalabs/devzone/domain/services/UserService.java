package com.sivalabs.devzone.domain.services;

import static com.sivalabs.devzone.domain.utils.AppConstants.ROLE_USER;

import com.sivalabs.devzone.domain.entities.Role;
import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.exceptions.DevZoneException;
import com.sivalabs.devzone.domain.models.UserDTO;
import com.sivalabs.devzone.domain.repositories.RoleRepository;
import com.sivalabs.devzone.domain.repositories.UserRepository;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(UserDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDTO createUser(UserDTO user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DevZoneException("Email " + user.getEmail() + " is already in use");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userEntity = user.toEntity();
        Optional<Role> roleUser = roleRepository.findByName(ROLE_USER);
        userEntity.setRoles(Collections.singleton(roleUser.orElse(null)));
        return UserDTO.fromEntity(userRepository.save(userEntity));
    }
}
