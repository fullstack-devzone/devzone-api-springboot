package com.sivalabs.devzone.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPostRepository extends JpaRepository<PostEntity, Long> {

    @Query("select p from PostEntity p where upper(p.title) like upper(:query)")
    Page<PostEntity> searchByTitle(@Param("query") String query, Pageable pageable);
}
