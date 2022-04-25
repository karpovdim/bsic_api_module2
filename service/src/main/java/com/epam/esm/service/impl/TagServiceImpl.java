package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.impl.TagValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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
        List<Tag> tags = new ArrayList<>();
        tags.addAll(getTagsByParams(tagId));
        tags.addAll(getTagsByParams(tagName));
        tags.addAll(getTagsByParams(giftCertificateId));
        tags.addAll(getTagsByParams(tagId, tagName, giftCertificateId));
        return tags;
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

    private List<Tag> getTagsByParams(Long tagId, String tagName, Long giftCertificateId) {
        if(tagValidator.allParamsIsNull(tagId,tagName,giftCertificateId)){
            return getAll();
        }
        return Collections.emptyList();
    }
    private List<Tag> getTagsByParams(Long id) {
        if(id == null){
            return Collections.emptyList();
        }
        return getById(id);
    }
    private List<Tag> getTagsByParams(String tagName) {
        if(StringUtils.isEmpty(tagName)){
            return Collections.emptyList();
        }
        return getByName(tagName);
    }

}
