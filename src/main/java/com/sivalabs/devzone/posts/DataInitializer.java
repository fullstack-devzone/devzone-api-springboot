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
public class DataInitializer implements CommandLineRunner {
    private final ApplicationProperties applicationProperties;
    private final PostService postService;
    private final PostsImportService postsImportService;

    @Override
    public void run(String... args) throws Exception {
        if (applicationProperties.isImportDataEnabled()) {
            postService.deleteAllPosts();
            String fileName = applicationProperties.getImportFilePath();
            postsImportService.importPosts(fileName);
        } else {
            log.info("Data importing is disabled");
        }
    }
}
