package com.sivalabs.devzone.domain.repositories.jpa;

import com.sivalabs.devzone.domain.entities.Tag;
import com.sivalabs.devzone.domain.repositories.TagRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTagRepository extends TagRepository, JpaRepository<Tag, Long> {}
