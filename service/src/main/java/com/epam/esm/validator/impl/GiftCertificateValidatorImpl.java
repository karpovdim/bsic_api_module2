package com.epam.esm.validator.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.validator.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@PropertySource({"classpath:validator.properties"})
@Component
public class GiftCertificateValidatorImpl implements Validator<GiftCertificate> {
    private static final BigDecimal MIN_PRICE_VALUE = BigDecimal.ONE;
    private static final BigDecimal MAX_PRICE_VALUE = new BigDecimal(Integer.MAX_VALUE);
    @Value("${certificate.maxNameLength}")
    private int maxNameLength;
    @Value("${certificate.minNameLength}")
    private int minNameLength;
    @Value("${certificate.maxDescriptionLength}")
    private int maxDescriptionLength;
    @Value("${certificate.minDescriptionLength}")
    private  int minDescriptionLength ;
    @Value("${certificate.minDurationValue}")
    private  int minDurationValue ;
    @Value("${certificate.maxDurationValue}")
    private  int maxDurationValue;

    @Override
    public boolean isValid(GiftCertificate giftCertificate) {
        return isNameValid(giftCertificate.getName()) &&
                isDescriptionValid(giftCertificate.getDescription()) &&
                isPriceValid(giftCertificate.getPrice()) &&
                isDurationValid(giftCertificate.getDuration());
    }

    public boolean isNameValid(String name) {
        return name != null &&
                name.length() >= minNameLength &&
                name.length() <= maxNameLength;
    }

    public boolean isDescriptionValid(String description) {
        return description != null &&
                description.length() >= minDescriptionLength &&
                description.length() <= maxDescriptionLength;
    }

    public boolean isPriceValid(BigDecimal price) {
        return price != null &&
                price.compareTo(MIN_PRICE_VALUE) >= 0 &&
                price.compareTo(MAX_PRICE_VALUE) <= 0;
    }

    public boolean isDurationValid(Integer duration) {
        return duration != null && duration >= minDurationValue && duration <= maxDurationValue;
    }

}
