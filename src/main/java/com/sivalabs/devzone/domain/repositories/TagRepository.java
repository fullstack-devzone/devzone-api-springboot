package com.sivalabs.devzone.domain.repositories;

import com.sivalabs.devzone.domain.entities.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;

public interface TagRepository {
    Optional<Tag> findByName(String tag);

    List<Tag> findAll(Sort sort);

    Tag save(Tag tag);
}
