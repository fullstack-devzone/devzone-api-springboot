package com.sivalabs.devzone.web.controllers.links;

import com.sivalabs.devzone.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.domain.models.LinkDTO;
import com.sivalabs.devzone.domain.services.LinkService;
import com.sivalabs.devzone.domain.services.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
@Slf4j
public class DeleteLinkController {
    private final LinkService linkService;
    private final SecurityService securityService;

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
