package com.epam.esm.validator.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.constant.ExceptionMessageConstant.*;

@PropertySource({"classpath:validator.properties"})
@Component
public class TagValidatorImpl implements Validator<Tag> {
    @Value("${tag.maxNameLength}")
    private int maxNameLength;
    @Value("${tag.minNameLength}")
    private int minNameLength;

    @Override
    public boolean isValid(Tag tag) {
            return StringUtils.isNotEmpty(tag.getName())
                    && tag.getName().length() >= minNameLength
                    && tag.getName().length() <= maxNameLength;
    }

    @Override
    public boolean isNull(Tag tag) {
        return tag != null;
    }

    public void validateTags(List<Tag> tags) {
        boolean isCorrectTags = tags.stream().allMatch(this::isValid);
        if (!isCorrectTags) {
            throw new InvalidEntityException(TAG_INVALID_MSG);
        }
    }

    public void checkPresenceTagByName(Tag tag, TagDao tagDao) {
        tagDao.getByName(tag.getName()).orElseThrow(() -> new DuplicateEntityException(TAG_EXISTS));
    }

    public void checkPresenceTagById(Long id, TagDao tagDao) {
        tagDao.getById(id).orElseThrow(()-> new NoSuchEntityException(TAG_DOES_NOT_EXISTS));
    }

    public void validate(Tag tag) {
        if (!isValid(tag)) {
            throw new InvalidEntityException(TAG_INVALID_MSG);
        }
    }
    public void tagIsEmpty(Tag tag) {
        if (StringUtils.isEmpty(tag.getName())) {
            throw new InvalidEntityException(TAG_INVALID_MSG);
        }
    }

    public boolean allParamsIsNull(Long tagId, String tagName, Long giftCertificateId) {
        return tagId == null && giftCertificateId == null && StringUtils.isEmpty(tagName);
    }
}
