package com.epam.esm.service;

import com.epam.esm.entity.Tag;

import java.util.List;


public interface TagService {

    void create(Tag tag);

    List<Tag> getByName(String name);

    void updateNameById(Long id, Tag tag);

    void deleteById(Long id);

    List<Tag> getTagsByGiftCertificateId(Long giftCertificateId);

    List<Tag> getById(Long id);

    List<Tag> getAll();

    List<Tag> getRoute(Long tagId, String tagName, Long giftCertificateId);
}
