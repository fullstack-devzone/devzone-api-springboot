package com.sivalabs.devzone.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.sivalabs.devzone.common.AbstractRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

class PostRepositoryTests extends AbstractRepositoryTest {

    @Autowired
    private JpaPostRepository postRepository;

    @Test
    void shouldSearchPosts() {
        Page<PostEntity> results = postRepository.searchByTitle("a", Pageable.ofSize(1));
        assertThat(results).isNotNull();
    }
}
