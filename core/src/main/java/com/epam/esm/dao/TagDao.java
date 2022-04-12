package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {

    void create(Tag tag);

    Optional<Tag> getByName(String name);

    void updateNameById(Long id, String name);

    void deleteById(Long id);

    List<Tag> getTagsByGiftCertificateId(Long giftCertificateId);

    Optional<Tag> getById(Long id);

    List<Tag> getAll();
}
