package com.sivalabs.devzone.posts.repositories;

import com.sivalabs.devzone.posts.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where upper(p.title) like upper(:query)")
    Page<Post> searchByTitle(@Param("query") String query, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCase(String query, Pageable pageable);
}
