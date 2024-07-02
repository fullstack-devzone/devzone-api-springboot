package com.sivalabs.devzone.users.domain;

public record User(Long id, String name, String email, String password, Role role) {}
