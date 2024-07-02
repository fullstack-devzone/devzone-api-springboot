package com.sivalabs.devzone.posts.domain.internal;

import com.sivalabs.devzone.posts.domain.PostDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface JpaPostRepository extends JpaRepository<PostEntity, Long> {

    @Query(
            """
        select new com.sivalabs.devzone.posts.domain.PostDTO(
        p.id, p.url, p.title,p.content, u.id, u.name, p.createdAt, p.updatedAt)
      from PostEntity p left join UserEntity u on u.id = p.createdBy
    """)
    Page<PostDTO> findAllBy(Pageable pageable);

    @Query(
            """
        select new com.sivalabs.devzone.posts.domain.PostDTO(
        p.id, p.url, p.title,p.content, u.id, u.name, p.createdAt, p.updatedAt)
        from PostEntity p left join UserEntity u on u.id = p.createdBy
        where upper(p.title) like upper(:query)
        """)
    Page<PostDTO> searchByTitle(@Param("query") String query, Pageable pageable);

    @Query(
            """
        select new com.sivalabs.devzone.posts.domain.PostDTO(
        p.id, p.url, p.title,p.content, u.id, u.name, p.createdAt, p.updatedAt)
        from PostEntity p left join UserEntity u on u.id = p.createdBy
        where p.id=?1
    """)
    Optional<PostDTO> findPostById(Long id);
}
