package com.sivalabs.devzone.domain.repositories;

import com.sivalabs.devzone.domain.entities.Link;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LinkRepository {

    Page<Link> findByTag(String tagName, Pageable pageable);

    Page<Link> findByTitleContainingIgnoreCase(String query, Pageable pageable);

    List<Link> findAll();

    Page<Link> findAll(Pageable pageable);

    Optional<Link> findById(Long id);

    Link save(Link link);

    void deleteById(Long id);

    void deleteAll();
}
