package com.sivalabs.devzone.posts.repositories;

import static com.sivalabs.devzone.utils.TestConstants.PROFILE_IT;
import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.devzone.posts.entities.Post;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest(
        properties = {
            "spring.test.database.replace=none",
            "spring.datasource.url=jdbc:tc:postgresql:15.3-alpine:///testdb"
        })
@ActiveProfiles(PROFILE_IT)
class PostRepositoryTests {

    @Autowired
    private PostRepository postRepository;

    @Test
    void shouldReturnAllPosts() {
        List<Post> allPosts = postRepository.findAll();
        assertThat(allPosts).isNotNull();
    }
}
