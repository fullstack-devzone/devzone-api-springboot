package com.sivalabs.devzone.links.web.controllers;

import com.sivalabs.devzone.common.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.common.annotations.CurrentUser;
import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.links.models.LinkDTO;
import com.sivalabs.devzone.links.models.LinksDTO;
import com.sivalabs.devzone.links.services.LinkService;
import com.sivalabs.devzone.users.entities.User;
import com.sivalabs.devzone.users.services.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LinkController {
    private final LinkService linkService;
    private final SecurityService securityService;

    @GetMapping("/links")
    public LinksDTO getLinks(
            @RequestParam(name = "tag", defaultValue = "") String tag,
            @RequestParam(name = "query", defaultValue = "") String query,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        if (StringUtils.isNotEmpty(query)) {
            log.info("Searching links for {} with page: {}", query, page);
            return linkService.searchLinks(query, page);
        } else if (StringUtils.isNotEmpty(tag)) {
            log.info("Fetching links for tag {} with page: {}", tag, page);
            return linkService.getLinksByTag(tag, page);
        } else {
            log.info("Fetching links with page: {}", page);
            return linkService.getAllLinks(page);
        }
    }

    @GetMapping("/links/{id}")
    public LinkDTO getLink(@PathVariable Long id) {
        return linkService
                .getLinkById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Link with id: " + id + " not found"));
    }

    @PostMapping("/links")
    @ResponseStatus(HttpStatus.CREATED)
    @AnyAuthenticatedUser
    @Operation(summary = "Create Link", security = @SecurityRequirement(name = "bearerAuth"))
    public LinkDTO createLink(@Valid @RequestBody LinkDTO link, @CurrentUser User loginUser) {
        link.setCreatedUserId(loginUser.getId());
        return linkService.createLink(link);
    }

    @DeleteMapping("/links/{id}")
    @AnyAuthenticatedUser
    @Operation(summary = "Delete Link", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteLink(@PathVariable Long id, @CurrentUser User loginUser) {
        LinkDTO link = linkService.getLinkById(id).orElse(null);
        this.checkPrivilege(id, link, loginUser);
        linkService.deleteLink(id);
        return ResponseEntity.ok().build();
    }

    private void checkPrivilege(Long linkId, LinkDTO link, User loginUser) {
        if (link == null
                || !(link.getCreatedUserId().equals(loginUser.getId())
                        || securityService.isCurrentUserAdmin())) {
            throw new ResourceNotFoundException("Link not found with id=" + linkId);
        }
    }
}
