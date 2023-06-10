package com.sivalabs.devzone.application.service;

import com.sivalabs.devzone.application.port.in.GetCurrentUserUseCase;
import com.sivalabs.devzone.application.port.out.GetCurrentUserPort;
import com.sivalabs.devzone.domain.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class GetCurrentUserService implements GetCurrentUserUseCase {
    private final GetCurrentUserPort getCurrentUserPort;

    @Override
    public Optional<User> getCurrentUser() {
        return getCurrentUserPort.getCurrentUser();
    }
}
