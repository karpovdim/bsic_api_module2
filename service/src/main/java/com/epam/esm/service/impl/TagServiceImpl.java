package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final Validator<Tag> tagValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, Validator<Tag> tagValidator) {
        this.tagDao = tagDao;
        this.tagValidator = tagValidator;
    }

    @Override
    public List<Tag> getRoute(Long tagId, String tagName, Long giftCertificateId) {
        if (tagId != null) {
            return getById(tagId);
        } else if (tagName != null) {
            return getByName(tagName);
        } else if (giftCertificateId != null) {
            return getTagsByGiftCertificateId(giftCertificateId);
        } else {
            return getAll();
        }
    }

    @Override
    public List<Tag> getByName(String name) {
        Tag tag = tagDao.getByName(name).orElseThrow(
                () -> new NoSuchEntityException("message.cantFindTagByName"));
        List<Tag> listOfTags = new ArrayList<>();
        listOfTags.add(tag);
        return listOfTags;
    }


    @Override
    public List<Tag> getTagsByGiftCertificateId(Long giftCertificateId) {
        return tagDao.getTagsByGiftCertificateId(giftCertificateId);
    }

    @Override
    public List<Tag> getById(Long id) {
        Tag tag = tagDao.getById(id).orElseThrow(
                () -> new NoSuchEntityException("message.cantFindTagById"));
        List<Tag> listOfTags = new ArrayList<>();
        listOfTags.add(tag);
        return listOfTags;
    }

    @Override
    public List<Tag> getAll() {
        return tagDao.getAll();
    }

    @Transactional
    @Override
    public void create(Tag tag) {
        if (!tagValidator.isValid(tag)) {
            throw new InvalidEntityException("message.tagInvalid");
        }
        isExists(tag);
        tagDao.create(tag);
    }

    @Transactional
    @Override
    public void updateNameById(Long id, Tag tag) {
        isPresent(id);
        if (tag.getName() != null) {
            tagDao.updateNameById(id, tag.getName());
        } else {
            throw new InvalidEntityException("message.tagInvalid");
        }
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        isPresent(id);
        tagDao.deleteById(id);
    }

    private void isPresent(Long id) {
        if (tagDao.getById(id).isEmpty()) {
            throw new NoSuchEntityException("message.tagDoesntExist");
        }
    }

    private void isExists(Tag tag) {
        String tagName = tag.getName();
        boolean isTagExist = tagDao.getByName(tagName).isPresent();
        if (isTagExist) {
            throw new DuplicateEntityException("message.tagExists");
        }
    }
}
