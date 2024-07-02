package com.sivalabs.devzone.posts.domain;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.users.domain.UserService;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostsImportService {
    public static final String SYSTEM_USER_EMAIL = "admin@gmail.com";
    private final PostService postService;
    private final UserService userService;

    public void importPosts(String fileName) throws IOException, CsvValidationException {
        log.info("Importing posts from file: {}", fileName);
        long count = 0L;

        var file = new ClassPathResource(fileName, this.getClass());
        try (var inputStreamReader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
                var csvReader = new CSVReader(inputStreamReader)) {
            csvReader.skip(1);
            var iterator = new CSVIterator(csvReader);

            Long userId =
                    userService.getUserByEmail(SYSTEM_USER_EMAIL).orElseThrow().id();

            while (iterator.hasNext()) {
                String[] nextLine = iterator.next();
                var createPostCmd = new CreatePostCmd(nextLine[0], nextLine[1], nextLine[1], userId);
                postService.createPost(createPostCmd);
                count++;
            }
        }
        log.info("Imported {} posts from file {}", count, fileName);
    }
}
