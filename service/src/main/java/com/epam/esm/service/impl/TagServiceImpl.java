package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.impl.TagValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.constant.ExceptionMessageConstant.*;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final TagValidatorImpl tagValidator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, TagValidatorImpl tagValidator) {
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
                () -> new NoSuchEntityException(CANT_FIND_TAG_BY_ID_MSG));
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
                () -> new NoSuchEntityException(CANT_FIND_TAG_BY_NAME_MSG));
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
        tagValidator.validate(tag);
        tagValidator.checkPresenceTagByName(tag, tagDao);
        tagDao.create(tag);
    }

    @Transactional
    @Override
    public void updateNameById(Long id, Tag tag) {
        tagValidator.tagIsEmpty(tag);
        tagValidator.checkPresenceTagById(id,tagDao);
        tagDao.updateNameById(id, tag.getName());
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        tagValidator.checkPresenceTagById(id, tagDao);
        tagDao.deleteById(id);
    }

}
