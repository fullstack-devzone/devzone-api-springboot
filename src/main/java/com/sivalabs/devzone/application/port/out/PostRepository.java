package com.sivalabs.devzone.application.port.out;

import com.sivalabs.devzone.domain.Post;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PostRepository {
    Page<Post> findAll(int page);

    Page<Post> searchByTitle(String query, int page);

    Optional<Post> findById(Long id);

    Post save(Post post);

    void deleteById(Long id);

    void deleteAll();
}
