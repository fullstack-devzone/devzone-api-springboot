package com.sivalabs.devzone.adapter.out.persistence;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.sivalabs.devzone.application.port.out.PostRepository;
import com.sivalabs.devzone.domain.Post;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class PostRepositoryImpl implements PostRepository {
    private static final int PAGE_SIZE = 10;

    private final JpaPostRepository jpaPostRepository;
    private final JpaUserRepository jpaUserRepository;

    @Override
    public Page<Post> findAll(int page) {
        Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, PAGE_SIZE, DESC, "createdAt");
        return jpaPostRepository.findAll(pageable).map(PostMapper::fromEntity);
    }

    @Override
    public Page<Post> searchByTitle(String query, int page) {
        Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, PAGE_SIZE, DESC, "createdAt");
        return jpaPostRepository.searchByTitle("%" + query + "%", pageable).map(PostMapper::fromEntity);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return jpaPostRepository.findById(id).map(PostMapper::fromEntity);
    }

    @Override
    public Post save(Post post) {
        PostEntity postEntity = PostMapper.toEntity(post);
        Long userId = post.getCreatedBy().getId();
        UserEntity userEntity = jpaUserRepository.findById(userId).orElseThrow();
        postEntity.setCreatedBy(userEntity);
        PostEntity savedPost = jpaPostRepository.save(postEntity);
        return PostMapper.fromEntity(savedPost);
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
