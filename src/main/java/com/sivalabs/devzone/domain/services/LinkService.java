package com.sivalabs.devzone.domain.services;

import com.sivalabs.devzone.domain.entities.Link;
import com.sivalabs.devzone.domain.entities.Tag;
import com.sivalabs.devzone.domain.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.domain.mappers.LinkMapper;
import com.sivalabs.devzone.domain.models.CreateLinkRequest;
import com.sivalabs.devzone.domain.models.LinkDTO;
import com.sivalabs.devzone.domain.models.LinksDTO;
import com.sivalabs.devzone.domain.models.UpdateLinkRequest;
import com.sivalabs.devzone.domain.repositories.LinkRepository;
import com.sivalabs.devzone.domain.repositories.TagRepository;
import com.sivalabs.devzone.domain.repositories.UserRepository;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LinkService {
    private final LinkRepository linkRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final LinkMapper linkMapper;

    @Transactional(readOnly = true)
    public LinksDTO getAllLinks(Pageable pageable) {
        return buildLinksResult(linkRepository.findAll(pageable));
    }

    @Transactional(readOnly = true)
    public LinksDTO searchLinks(String query, Pageable pageable) {
        return buildLinksResult(linkRepository.findByTitleContainingIgnoreCase(query, pageable));
    }

    @Transactional(readOnly = true)
    public LinksDTO getLinksByTag(String tag, Pageable pageable) {
        Optional<Tag> tagOptional = tagRepository.findByName(tag);
        if (tagOptional.isEmpty()) {
            throw new ResourceNotFoundException("Tag " + tag + " not found");
        }
        return buildLinksResult(linkRepository.findByTag(tag, pageable));
    }

    @Transactional(readOnly = true)
    public Optional<LinkDTO> getLinkById(Long id) {
        log.debug("process=get_link_by_id, id={}", id);
        return linkRepository.findById(id).map(linkMapper::toDTO);
    }

    public LinkDTO createLink(CreateLinkRequest createLinkRequest) {
        Link link = new Link();
        link.setUrl(createLinkRequest.getUrl());
        link.setTitle(getTitle(createLinkRequest.getUrl(), createLinkRequest.getTitle()));
        link.setTags(getOrCreateTags(createLinkRequest.getTags()));
        link.setCreatedBy(userRepository.getOne(createLinkRequest.getCreatedUserId()));

        log.debug("process=create_link, url={}", link.getUrl());
        return linkMapper.toDTO(linkRepository.save(link));
    }

    public LinkDTO updateLink(UpdateLinkRequest updateLinkRequest) {
        Link link = linkRepository.findById(updateLinkRequest.getId()).get();
        link.setUrl(updateLinkRequest.getUrl());
        link.setTitle(getTitle(updateLinkRequest.getUrl(), updateLinkRequest.getTitle()));
        link.setTags(getOrCreateTags(updateLinkRequest.getTags()));
        log.debug("process=update_link, url={}", link.getUrl());
        return linkMapper.toDTO(linkRepository.save(link));
    }

    public void deleteLink(Long id) {
        log.debug("process=delete_link_by_id, id={}", id);
        linkRepository.deleteById(id);
    }

    public void deleteAllLinks() {
        log.debug("process=delete_all_links");
        linkRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<Tag> findAllTags() {
        Sort sort = Sort.by("name");
        return tagRepository.findAll(sort);
    }

    private LinksDTO buildLinksResult(Page<Link> links) {
        log.trace("Found {} links in page", links.getNumberOfElements());
        return new LinksDTO(links.map(linkMapper::toDTO));
    }

    private Set<Tag> getOrCreateTags(List<String> tagNames) {
        Set<Tag> tagsList = new HashSet<>();
        tagNames.forEach(
                tagName -> {
                    if (!tagName.trim().isEmpty()) {
                        Tag tag = createTagIfNotExist(tagName.trim());
                        tagsList.add(tag);
                    }
                });
        return tagsList;
    }

    private String getTitle(String url, String title) {
        if (StringUtils.isNotEmpty(title)) {
            return title;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.title();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return url;
    }

    private Tag createTagIfNotExist(String tagName) {
        Optional<Tag> tagOptional = tagRepository.findByName(tagName);
        if (tagOptional.isPresent()) {
            return tagOptional.get();
        }
        Tag tag = new Tag();
        tag.setName(tagName);
        return tagRepository.save(tag);
    }
}
