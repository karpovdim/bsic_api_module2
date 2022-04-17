package com.epam.esm.validator.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.validator.Validator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class GiftCertificateValidatorImpl implements Validator<GiftCertificate> {

    private static final int MAX_NAME_LENGTH = 300;
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_DESCRIPTION_LENGTH = 300;
    private static final int MIN_DESCRIPTION_LENGTH = 1;
    private static final int MIN_DURATION_VALUE = 1;
    private static final int MAX_DURATION_VALUE = Integer.MAX_VALUE;
    private static final BigDecimal MIN_PRICE_VALUE = BigDecimal.ONE;
    private static final BigDecimal MAX_PRICE_VALUE = new BigDecimal(Integer.MAX_VALUE);

    @Override
    public boolean isValid(GiftCertificate giftCertificate) {
        return isNameValid(giftCertificate.getName()) &&
                isDescriptionValid(giftCertificate.getDescription()) &&
                isPriceValid(giftCertificate.getPrice()) &&
                isDurationValid(giftCertificate.getDuration());
    }

    public boolean isNameValid(String name) {
        return name != null &&
                name.length() >= MIN_NAME_LENGTH &&
                name.length() <= MAX_NAME_LENGTH;
    }

    public boolean isDescriptionValid(String description) {
        return description != null &&
                description.length() >= MIN_DESCRIPTION_LENGTH &&
                description.length() <= MAX_DESCRIPTION_LENGTH;
    }

    public boolean isPriceValid(BigDecimal price) {
        return price != null &&
                price.compareTo(MIN_PRICE_VALUE) >= 0 &&
                price.compareTo(MAX_PRICE_VALUE) <= 0;
    }

    public boolean isDurationValid(Integer duration) {
        return duration != null && duration >= MIN_DURATION_VALUE;
    }

}
