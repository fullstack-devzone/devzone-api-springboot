package com.sivalabs.devzone.posts;

import com.sivalabs.devzone.ApplicationProperties;
import com.sivalabs.devzone.posts.domain.PostService;
import com.sivalabs.devzone.posts.domain.PostsImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
class DataInitializer implements CommandLineRunner {
    private final PostService postService;
    private final PostsImportService postsImportService;
    private final ApplicationProperties properties;

    @Override
    public void run(String... args) throws Exception {
        if (!properties.isImportDataEnabled()) {
            log.info("Data importing is disabled");
        }
        postService.deleteAllPosts();
        String fileName = properties.getImportFilePath();
        postsImportService.importPosts(fileName);
    }
}
