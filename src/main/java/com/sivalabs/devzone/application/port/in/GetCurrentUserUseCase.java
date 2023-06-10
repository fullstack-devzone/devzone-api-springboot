package com.sivalabs.devzone.application.port.in;

import com.sivalabs.devzone.domain.User;
import java.util.Optional;

public interface GetCurrentUserUseCase {
    Optional<User> getCurrentUser();
}
