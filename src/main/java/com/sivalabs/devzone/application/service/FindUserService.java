package com.sivalabs.devzone.application.service;

import com.sivalabs.devzone.application.port.in.FindUserUseCase;
import com.sivalabs.devzone.application.port.out.UserRepository;
import com.sivalabs.devzone.domain.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class FindUserService implements FindUserUseCase {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
