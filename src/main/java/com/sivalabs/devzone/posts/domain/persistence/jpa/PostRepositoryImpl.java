package com.sivalabs.devzone.posts.domain.persistence.jpa;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.sivalabs.devzone.common.models.PagedResult;
import com.sivalabs.devzone.posts.domain.Post;
import com.sivalabs.devzone.posts.domain.PostDTO;
import com.sivalabs.devzone.posts.domain.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class PostRepositoryImpl implements PostRepository {
    private final JpaPostRepository jpaPostRepository;

    public PagedResult<PostDTO> getPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, size, DESC, "createdAt");
        Page<PostDTO> pageOfPosts = jpaPostRepository.findAllBy(pageable);
        return new PagedResult<>(
                pageOfPosts.getContent(),
                pageOfPosts.getTotalElements(),
                pageOfPosts.getNumber() + 1,
                pageOfPosts.getTotalPages());
    }

    public PagedResult<PostDTO> searchPosts(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, size, DESC, "createdAt");
        Page<PostDTO> pageOfPosts = jpaPostRepository.searchByTitle("%" + query + "%", pageable);
        return new PagedResult<>(
                pageOfPosts.getContent(),
                pageOfPosts.getTotalElements(),
                pageOfPosts.getNumber() + 1,
                pageOfPosts.getTotalPages());
    }

    @Override
    public Optional<PostDTO> findById(Long id) {
        return jpaPostRepository.findPostById(id);
    }

    @Override
    public PostDTO save(Post post) {
        PostEntity entity = new PostEntity();
        entity.setUrl(post.url());
        entity.setTitle(post.title());
        entity.setContent(post.content());
        entity.setCreatedAt(post.createdAt());
        entity.setUpdatedAt(post.updatedAt());
        entity.setCreatedBy(post.createdBy());
        var savedPost = jpaPostRepository.save(entity);
        return findById(savedPost.getId()).orElseThrow();
    }

    @Override
    public void deleteById(Long id) {
        jpaPostRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        jpaPostRepository.deleteAllInBatch();
    }
}
