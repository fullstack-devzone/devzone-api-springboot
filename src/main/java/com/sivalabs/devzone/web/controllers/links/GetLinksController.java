package com.sivalabs.devzone.web.controllers.links;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.sivalabs.devzone.domain.models.LinkDTO;
import com.sivalabs.devzone.domain.models.LinksDTO;
import com.sivalabs.devzone.domain.services.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
@Slf4j
public class GetLinksController {
    private final LinkService linkService;

    @GetMapping
    public LinksDTO getLinks(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "tag", required = false) String tag,
            @PageableDefault(size = 15)
                    @SortDefault.SortDefaults({@SortDefault(sort = "createdAt", direction = DESC)})
                    Pageable pageable) {
        LinksDTO data;
        if (StringUtils.isNotEmpty(tag)) {
            log.info("Fetching links for tag {} with page: {}", tag, pageable.getPageNumber());
            data = linkService.getLinksByTag(tag, pageable);
        } else if (StringUtils.isNotEmpty(query)) {
            log.info("Searching links for {} with page: {}", query, pageable.getPageNumber());
            data = linkService.searchLinks(query, pageable);
        } else {
            log.info("Fetching links with page: {}", pageable.getPageNumber());
            data = linkService.getAllLinks(pageable);
        }
        return data;
    }

    @GetMapping("/{id}")
    public LinkDTO getLink(@PathVariable Long id) {
        return linkService.getLinkById(id).orElse(null);
    }
}
