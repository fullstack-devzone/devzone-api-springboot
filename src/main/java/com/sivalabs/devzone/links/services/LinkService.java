package com.sivalabs.devzone.links.services;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.links.entities.Link;
import com.sivalabs.devzone.links.entities.Tag;
import com.sivalabs.devzone.links.mappers.LinkMapper;
import com.sivalabs.devzone.links.models.LinkDTO;
import com.sivalabs.devzone.links.models.LinksDTO;
import com.sivalabs.devzone.links.repositories.LinkRepository;
import com.sivalabs.devzone.links.repositories.TagRepository;
import com.sivalabs.devzone.users.repositories.UserRepository;
import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LinkService {
    private static final int PAGE_SIZE = 10;

    private final LinkRepository linkRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final LinkMapper linkMapper;

    @Transactional(readOnly = true)
    public LinksDTO getAllLinks(int page) {
        Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, PAGE_SIZE, DESC, "createdAt");
        Page<Long> pageOfLinkIds = linkRepository.fetchLinkIds(pageable);
        List<Link> links =
                linkRepository.findLinksWithTags(pageOfLinkIds.getContent(), pageable.getSort());
        Page<Link> pageOfLinks = new PageImpl<>(links, pageable, pageOfLinkIds.getTotalElements());
        return buildLinksResult(pageOfLinks);
    }

    @Transactional(readOnly = true)
    public LinksDTO searchLinks(String query, int page) {
        Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, PAGE_SIZE, DESC, "createdAt");
        Page<Long> pageOfLinkIds =
                linkRepository.fetchLinkIdsByTitleContainingIgnoreCase(query, pageable);
        List<Link> links =
                linkRepository.findLinksWithTags(pageOfLinkIds.getContent(), pageable.getSort());
        Page<Link> pageOfLinks = new PageImpl<>(links, pageable, pageOfLinkIds.getTotalElements());
        return buildLinksResult(pageOfLinks);
    }

    @Transactional(readOnly = true)
    public LinksDTO getLinksByTag(String tag, int page) {
        Pageable pageable = PageRequest.of(page < 1 ? 0 : page - 1, PAGE_SIZE, DESC, "createdAt");
        Optional<Tag> tagOptional = tagRepository.findByName(tag);
        if (tagOptional.isEmpty()) {
            throw new ResourceNotFoundException("Tag " + tag + " not found");
        }
        Page<Long> pageOfLinkIds = linkRepository.fetchLinkIdsByTag(tag, pageable);
        List<Link> links =
                linkRepository.findLinksWithTags(pageOfLinkIds.getContent(), pageable.getSort());
        Page<Link> pageOfLinks = new PageImpl<>(links, pageable, pageOfLinkIds.getTotalElements());
        return buildLinksResult(pageOfLinks);
    }

    @Transactional(readOnly = true)
    public Optional<LinkDTO> getLinkById(Long id) {
        log.debug("process=get_link_by_id, id={}", id);
        return linkRepository.findById(id).map(linkMapper::toDTO);
    }

    public LinkDTO createLink(LinkDTO link) {
        link.setId(null);
        log.debug("process=create_link, url={}", link.getUrl());
        return linkMapper.toDTO(saveLink(link));
    }

    public void deleteLink(Long id) {
        log.debug("process=delete_link_by_id, id={}", id);
        linkRepository.deleteById(id);
    }

    public void deleteAllLinks() {
        log.debug("process=delete_all_links");
        linkRepository.deleteAllInBatch();
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

    private Link saveLink(LinkDTO linkDTO) {
        Link link = new Link();
        if (linkDTO.getId() != null) {
            link = linkRepository.findById(linkDTO.getId()).orElse(link);
        }
        link.setUrl(linkDTO.getUrl());
        link.setTitle(getTitle(linkDTO));
        link.setCreatedBy(userRepository.getReferenceById(linkDTO.getCreatedUserId()));
        link.setCreatedAt(Instant.now());
        Set<Tag> tagsList = new HashSet<>();
        linkDTO.getTags()
                .forEach(
                        tagName -> {
                            if (!tagName.trim().isEmpty()) {
                                Tag tag = createTagIfNotExist(tagName.trim());
                                tagsList.add(tag);
                            }
                        });
        link.setTags(tagsList);
        return linkRepository.save(link);
    }

    private String getTitle(LinkDTO link) {
        if (StringUtils.isNotEmpty(link.getTitle())) {
            return link.getTitle();
        }
        try {
            Document doc = Jsoup.connect(link.getUrl()).get();
            return doc.title();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return link.getUrl();
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
