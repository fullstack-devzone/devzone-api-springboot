package com.sivalabs.devzone.users.domain.persistence.jpa;

import com.sivalabs.devzone.users.domain.User;
import com.sivalabs.devzone.users.domain.UserDTO;
import com.sivalabs.devzone.users.domain.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(UserEntity::toUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public Optional<UserDTO> findById(Long id) {
        return jpaUserRepository.findById(id).map(UserEntity::toUserDTO);
    }

    @Override
    public UserDTO save(User user) {
        UserEntity entity = new UserEntity();
        entity.setName(user.name());
        entity.setEmail(user.email());
        entity.setPassword(user.password());
        entity.setRole(user.role());
        var savedUser = jpaUserRepository.save(entity);
        return new UserDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole());
    }
}
