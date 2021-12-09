package com.sivalabs.devzone.domain.repositories.jpa;

import com.sivalabs.devzone.domain.entities.User;
import com.sivalabs.devzone.domain.repositories.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {}
