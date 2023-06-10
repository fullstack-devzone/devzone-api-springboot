package com.sivalabs.devzone.application.service;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.sivalabs.devzone.application.port.in.CreatePostRequest;
import com.sivalabs.devzone.application.port.in.FindUserUseCase;
import com.sivalabs.devzone.application.port.in.PostUseCase;
import com.sivalabs.devzone.application.port.in.PostsImportUseCase;
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
public class PostsImportService implements PostsImportUseCase {
    public static final String SYSTEM_USER_EMAIL = "admin@gmail.com";
    private final PostUseCase postUseCase;
    private final FindUserUseCase findUserUseCase;

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
                    findUserUseCase.findByEmail(SYSTEM_USER_EMAIL).orElseThrow().getId();

            while (iterator.hasNext()) {
                String[] nextLine = iterator.next();
                CreatePostRequest createPostRequest =
                        new CreatePostRequest(nextLine[1], nextLine[0], nextLine[1], userId);
                postUseCase.createPost(createPostRequest);
                count++;
            }
        }
        log.info("Imported {} posts from file {}", count, fileName);
    }
}
