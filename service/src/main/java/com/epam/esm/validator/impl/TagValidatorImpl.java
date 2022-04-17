package com.epam.esm.validator.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.validator.Validator;
import org.springframework.stereotype.Component;

@Component
public class TagValidatorImpl implements Validator<Tag> {

    private static final int MAX_NAME_LENGTH = 300;
    private static final int MIN_NAME_LENGTH = 1;

    @Override
    public boolean isValid(Tag tag) {
        String tagName = tag.getName();
        if (tagName != null) {
            int nameLength = tagName.length();
            return nameLength >= MIN_NAME_LENGTH &&
                    nameLength <= MAX_NAME_LENGTH;
        }
        return false;
    }
}
