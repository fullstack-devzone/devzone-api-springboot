package com.sivalabs.devzone.web.controllers;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.sivalabs.devzone.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.annotations.CurrentUser;
import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.domain.models.CreateLinkRequest;
import com.sivalabs.devzone.domain.models.LinkDTO;
import com.sivalabs.devzone.domain.models.LinksDTO;
import com.sivalabs.devzone.domain.services.LinkService;
import com.sivalabs.devzone.domain.services.SecurityService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
@Slf4j
public class LinkRestController {
    private final LinkService linkService;
    private final SecurityService securityService;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AnyAuthenticatedUser
    public LinkDTO createLink(
            @Valid @RequestBody CreateLinkRequest createLinkRequest,
            @CurrentUser SecurityUser loginUser) {
        createLinkRequest.setCreatedUserId(loginUser.getUser().getId());
        return linkService.createLink(createLinkRequest);
    }

    @DeleteMapping("/{id}")
    @AnyAuthenticatedUser
    public ResponseEntity<Void> deleteLink(@PathVariable Long id) {
        LinkDTO link =
                linkService
                        .getLinkById(id)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Link with id=" + id + " not found"));
        this.checkPrivilege(id, link);
        linkService.deleteLink(id);
        return ResponseEntity.ok().build();
    }

    private void checkPrivilege(Long linkId, LinkDTO link) {
        final boolean canEditLink =
                securityService.canCurrentUserEditLink(link.getCreatedBy().getId());
        if (!canEditLink) {
            throw new ResourceNotFoundException("Link not found with id=" + linkId);
        }
    }
}
