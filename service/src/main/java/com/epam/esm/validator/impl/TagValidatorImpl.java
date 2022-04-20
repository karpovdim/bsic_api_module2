package com.epam.esm.validator.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.validator.Validator;
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
        String tagName = tag.getName();
        if (tagName != null) {
            int nameLength = tagName.length();
            return nameLength >= minNameLength &&
                    nameLength <= maxNameLength;
        }
        return false;
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
        String tagName = tag.getName();
        tagDao.getByName(tagName).orElseThrow(() -> new DuplicateEntityException(TAG_EXISTS));
    }

    public void checkPresenceTagById(Long id, TagDao tagDao) {
        if (tagDao.getById(id).isEmpty()) {
            throw new NoSuchEntityException(TAG_DOES_NOT_EXISTS);
        }
    }

    public void validate(Tag tag) {
        if (!isValid(tag)) {
            throw new InvalidEntityException(TAG_INVALID_MSG);
        }
    }

}
