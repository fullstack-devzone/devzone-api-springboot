package com.sivalabs.devzone.posts.services;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.posts.models.CreatePostRequest;
import com.sivalabs.devzone.users.services.UserService;
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

        ClassPathResource file = new ClassPathResource(fileName, this.getClass());
        try (InputStreamReader inputStreamReader =
                        new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
                CSVReader csvReader = new CSVReader(inputStreamReader)) {
            csvReader.skip(1);
            CSVIterator iterator = new CSVIterator(csvReader);

            Long userId =
                    userService.getUserByEmail(SYSTEM_USER_EMAIL).orElseThrow().getId();

            while (iterator.hasNext()) {
                String[] nextLine = iterator.next();
                CreatePostRequest createPostRequest = new CreatePostRequest();
                createPostRequest.setUrl(nextLine[0]);
                createPostRequest.setTitle(nextLine[1]);
                createPostRequest.setContent(nextLine[1]);
                createPostRequest.setUserId(userId);

                postService.createPost(createPostRequest);
                count++;
            }
        }
        log.info("Imported {} posts from file {}", count, fileName);
    }
}
