package com.sivalabs.devzone.common;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(
        properties = {
            "spring.test.database.replace=none",
            "spring.datasource.url=jdbc:tc:postgresql:15.3-alpine:///testdb"
        })
public abstract class AbstractRepositoryTest {}
