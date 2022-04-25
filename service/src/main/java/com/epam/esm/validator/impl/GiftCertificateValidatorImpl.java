package com.epam.esm.validator.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.validator.Validator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.constant.ExceptionMessageConstant.*;

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
    private int minDescriptionLength;
    @Value("${certificate.minDurationValue}")
    private int minDurationValue;
    @Value("${certificate.maxDurationValue}")
    private int maxDurationValue;

    @Override
    public boolean isValid(GiftCertificate giftCertificate) {
        return isNameValid(giftCertificate.getName()) &&
                isDescriptionValid(giftCertificate.getDescription()) &&
                isPriceValid(giftCertificate.getPrice()) &&
                isDurationValid(giftCertificate.getDuration());
    }

    @Override
    public boolean isNull(GiftCertificate giftCertificate) {
        return giftCertificate == null;
    }

    public boolean isNameValid(String name) {
        return name != null &&
                name.length() >= minNameLength &&
                name.length() <= maxNameLength;
    }

    public void validateGiftCertificate(GiftCertificate giftCertificate) {
        if (!isValid(giftCertificate)) {
            throw new InvalidEntityException(GIFT_CERTIFICATE_INVALID_MSG);
        }
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

    public void presenceCheckGiftCertificateById(Long id, GiftCertificateDao giftCertificateDao) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.getById(id);
        if (giftCertificateOptional.isEmpty()) {
            throw new NoSuchEntityException(NOT_FOUND_MSG);
        }
    }

    public void checkParamsNotNull(GiftCertificate giftCertificate, List<Tag> tagList) {
        if (isListNull(tagList) || isNull(giftCertificate)) {
            throw new InvalidEntityException(CANT_UPDATE_GIFT_CERTIFICATE);
        }
    }

    public boolean isDurationValidForUpdate(GiftCertificate giftCertificate) {
        Integer duration = giftCertificate.getDuration();
        if (!isDurationValid(duration)) {
            throw new InvalidEntityException(CANT_UPDATE_INVALID_DURATION_MSG);
        }
        return true;
    }

    public boolean isPriceValidForUpdate(GiftCertificate giftCertificate) {
        BigDecimal price = giftCertificate.getPrice();
        if (price != null) {
            if (!isPriceValid(price)) {
                throw new InvalidEntityException(CANT_UPDATE_INVALID_PRICE_MSG);
            }
        }
        return true;
    }

    public boolean isValidDescriptionForUpdate(GiftCertificate giftCertificate) {
        String description = giftCertificate.getDescription();
        if (StringUtils.isNotBlank(description)) {
            if (!isDescriptionValid(description)) {
                throw new InvalidEntityException(CANT_UPDATE_INVALID_DESCRIPTION_MSG);
            }
        }
        return true;
    }

    public boolean isValidNameForUpdate(GiftCertificate giftCertificate) {
        String name = giftCertificate.getName();
        if (StringUtils.isNotBlank(name)) {
            if (!isNameValid(name)) {
                throw new InvalidEntityException(CANT_UPDATE_INVALID_NAME_MSG);
            }
        }
        return true;
    }

    public boolean idIsNull(Long id) {
        return id == null;
    }

    public boolean sortColumnsAndFilterByIsNull(List<String> sortColumns, List<String> filterBy) {
        return sortColumns == null && filterBy == null;
    }

    private boolean isListNull(List<Tag> list) {
        return list == null;
    }
}
