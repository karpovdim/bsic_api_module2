package com.epam.esm.validator.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
@PropertySource({"classpath:validator.properties"})
@Component
public class TagValidatorImpl implements Validator<Tag> {
    @Value("${tag.maxNameLength}")
    private  int maxNameLength;
    @Value("${tag.minNameLength}")
    private  int minNameLength;

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
}
