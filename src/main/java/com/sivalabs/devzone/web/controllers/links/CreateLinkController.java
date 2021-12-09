package com.sivalabs.devzone.web.controllers.links;

import com.sivalabs.devzone.annotations.AnyAuthenticatedUser;
import com.sivalabs.devzone.annotations.CurrentUser;
import com.sivalabs.devzone.config.security.SecurityUser;
import com.sivalabs.devzone.domain.models.CreateLinkRequest;
import com.sivalabs.devzone.domain.models.LinkDTO;
import com.sivalabs.devzone.domain.services.LinkService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/links")
@RequiredArgsConstructor
@Slf4j
public class CreateLinkController {
    private final LinkService linkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AnyAuthenticatedUser
    public LinkDTO createLink(
            @Valid @RequestBody CreateLinkRequest createLinkRequest,
            @CurrentUser SecurityUser loginUser) {
        createLinkRequest.setCreatedUserId(loginUser.getUser().getId());
        return linkService.createLink(createLinkRequest);
    }
}
