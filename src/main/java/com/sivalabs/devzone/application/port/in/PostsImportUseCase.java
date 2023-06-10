package com.sivalabs.devzone.application.port.in;

import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;

public interface PostsImportUseCase {
    void importPosts(String fileName) throws IOException, CsvValidationException;
}
