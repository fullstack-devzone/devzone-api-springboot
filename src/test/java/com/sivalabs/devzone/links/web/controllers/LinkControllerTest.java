package com.sivalabs.devzone.links.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sivalabs.devzone.common.AbstractWebMvcTest;
import com.sivalabs.devzone.links.models.LinksDTO;
import com.sivalabs.devzone.links.services.LinkService;
import com.sivalabs.devzone.users.services.SecurityService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

@WebMvcTest(controllers = LinkController.class)
class LinkControllerTest extends AbstractWebMvcTest {
    @MockBean protected LinkService linkService;

    @MockBean protected SecurityService securityService;

    @Test
    void shouldFetchLinksFirstPage() throws Exception {
        LinksDTO linksDTO = new LinksDTO();
        given(linkService.getAllLinks(any(Pageable.class))).willReturn(linksDTO);

        this.mockMvc.perform(get("/api/links")).andExpect(status().isOk());
    }
}
