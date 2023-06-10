package com.sivalabs.devzone.application.port.out;

import com.sivalabs.devzone.domain.User;
import java.util.Optional;

public interface GetCurrentUserPort {
    Optional<User> getCurrentUser();
}
