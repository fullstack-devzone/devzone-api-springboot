package com.sivalabs.devzone.posts.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.devzone.common.AbstractRepositoryTest;
import com.sivalabs.devzone.posts.entities.Post;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PostRepositoryTests extends AbstractRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void shouldReturnAllPosts() {
        List<Post> allPosts = postRepository.findAll();
        assertThat(allPosts).isNotNull();
    }
}
