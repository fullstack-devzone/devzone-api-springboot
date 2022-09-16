package com.sivalabs.devzone.links.web.controllers;

import com.sivalabs.devzone.links.entities.Tag;
import com.sivalabs.devzone.links.services.LinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Slf4j
public class TagController {
    private final LinkService linkService;

    @GetMapping
    public List<Tag> allTags() {
        return linkService.findAllTags();
    }
}