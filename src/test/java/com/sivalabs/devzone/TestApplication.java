package com.sivalabs.devzone;

import com.sivalabs.devzone.common.TestcontainersConfig;
import org.springframework.boot.SpringApplication;

public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.from(Application::main)
                .with(TestcontainersConfig.class)
                .run(args);
    }
}
