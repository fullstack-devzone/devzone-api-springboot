package com.sivalabs.devzone.posts.repositories;

import com.sivalabs.devzone.posts.entities.Post;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select l.id from Post l")
    Page<Long> fetchPostIds(Pageable pageable);

    @Query("select l.id from Post l where lower(l.title) like lower(concat('%', :query,'%'))")
    Page<Long> fetchPostIdsByTitleContainingIgnoreCase(@Param("query") String query, Pageable pageable);

    @Query("select DISTINCT l from Post l join fetch l.createdBy where l.id in ?1")
    List<Post> findPosts(List<Long> postIds, Sort sort);
}
