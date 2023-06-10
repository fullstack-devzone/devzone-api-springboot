package com.sivalabs.devzone.config;

import com.sivalabs.devzone.application.port.in.PostUseCase;
import com.sivalabs.devzone.application.port.in.PostsImportUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final ApplicationProperties applicationProperties;
    private final PostUseCase postUseCase;
    private final PostsImportUseCase postsImportUseCase;

    @Override
    public void run(String... args) throws Exception {
        if (applicationProperties.isImportDataEnabled()) {
            postUseCase.deleteAllPosts();
            String fileName = applicationProperties.getImportFilePath();
            postsImportUseCase.importPosts(fileName);
        } else {
            log.info("Data importing is disabled");
        }
    }
}
