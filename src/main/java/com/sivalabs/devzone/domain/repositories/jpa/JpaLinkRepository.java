package com.sivalabs.devzone.domain.repositories.jpa;

import com.sivalabs.devzone.domain.entities.Link;
import com.sivalabs.devzone.domain.repositories.LinkRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaLinkRepository extends LinkRepository, JpaRepository<Link, Long> {

    @Query("select distinct l from Link l join l.tags t where t.name=?1")
    @Override
    Page<Link> findByTag(String tagName, Pageable pageable);
}
