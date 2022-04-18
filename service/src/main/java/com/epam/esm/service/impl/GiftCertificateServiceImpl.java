package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.Validator;
import com.epam.esm.validator.impl.GiftCertificateValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;
    private final GiftCertificateValidatorImpl giftCertificateValidator;
    private final Validator<Tag> tagValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao,
                                      GiftCertificateValidatorImpl giftCertificateValidator,
                                      Validator<Tag> tagValidator) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.giftCertificateValidator = giftCertificateValidator;
        this.tagValidator = tagValidator;

    }

    @Override
    public List<GiftCertificateDto> getRoute(String tagName, List<String> sortColumns,
                                             List<String> orderType, List<String> filterBy,
                                             Long giftCertificateId, String allWithTags) {
        if (giftCertificateId != null) {
            return getById(giftCertificateId);
        } else if (sortColumns != null || filterBy != null) {
            return getAllWithSortingAndFiltering(sortColumns, orderType, filterBy);
        } else if (tagName != null) {
            return getAllByTagName(tagName);
        } else if (allWithTags != null) {
            return getAllWithTags();
        } else {
            return getAll();
        }
    }

    @Override
    public List<GiftCertificateDto> getAll() {
        List<GiftCertificate> giftCertificateList = giftCertificateDao.getAll();
        return giftCertificateList.stream()
                .map(GiftCertificateDto::new)
                .collect(Collectors.toList());

    }

    @Override
    public List<GiftCertificateDto> getById(Long id) {
        isPresentGiftCertificate(id);
        GiftCertificate giftCertificate = giftCertificateDao.getById(id).orElseThrow(() -> new NoSuchEntityException("message.notFound"));
        List<GiftCertificateDto> listOfGiftCertificate = new ArrayList<>();
        listOfGiftCertificate.add(new GiftCertificateDto(giftCertificate));
        return listOfGiftCertificate;
    }

    @Override
    @Transactional
    public void create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateDto.getGiftCertificate();
        validateGiftCertificate(giftCertificate);
        validateTags(giftCertificateDto.getCertificateTags());
        String giftCertificateName = getGiftCertificateName(giftCertificate);
        giftCertificateDao.create(giftCertificate);
        Long giftCertificateId = giftCertificateDao.getByName(giftCertificateName)
                .map(GiftCertificate::getId).orElseThrow(() -> new NoSuchEntityException("message.notFound"));
        createGiftCertificateTagReference(giftCertificateDto.getCertificateTags(), giftCertificateId);
    }

    @Override
    public List<GiftCertificateDto> getAllWithTags() {
        List<GiftCertificateDto> listOfDto = new ArrayList<>();
        List<GiftCertificate> listOfCertificates = giftCertificateDao.getAll();
        for (GiftCertificate giftCertificate : listOfCertificates) {
            List<Tag> listOfTags = tagDao.getTagsByGiftCertificateId(giftCertificate.getId());
            GiftCertificateDto giftCertificateDto = new GiftCertificateDto(giftCertificate);
            giftCertificateDto.setCertificateTags(listOfTags);
            listOfDto.add(giftCertificateDto);
        }
        return listOfDto;
    }


    @Transactional
    @Override
    public void deleteById(Long id) {
        isPresentGiftCertificate(id);
        giftCertificateDao.deleteById(id);
    }

    @Override
    public List<GiftCertificateDto> getAllByTagName(String tagName) {
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
        List<GiftCertificate> giftCertificateList = giftCertificateDao.getGiftCertificateByTagName(tagName);
        for (GiftCertificate giftCertificate : giftCertificateList) {
            giftCertificateDtoList.add(buildGiftCertificateDto(giftCertificate));
        }
        return giftCertificateDtoList;
    }

    @Override
    public List<GiftCertificateDto> getAllWithSortingAndFiltering(List<String> sortColumns,
                                                                  List<String> orderType,
                                                                  List<String> filterBy) {
        List<GiftCertificateDto> giftCertificateDtoList = new ArrayList<>();
        if (sortColumns == null) {
            sortColumns = Collections.emptyList();
        }
        if (orderType == null) {
            orderType = Collections.emptyList();
        }
        if (filterBy == null) {
            filterBy = Collections.emptyList();
        }
        List<GiftCertificate> giftCertificateList = giftCertificateDao.getAllWithSortingAndFiltering(sortColumns, orderType, filterBy);
        for (GiftCertificate giftCertificate : giftCertificateList) {
            giftCertificateDtoList.add(buildGiftCertificateDto(giftCertificate));
        }
        return giftCertificateDtoList;
    }

    @Transactional
    @Override
    public GiftCertificateDto updateById(Long giftCertificateId, GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateDto.getGiftCertificate();
        List<Tag> tagList = giftCertificateDto.getCertificateTags();
        if (tagList == null || giftCertificate == null) {
            throw new InvalidEntityException("message.cantUpdateGiftCertificateById");
        }
        isPresentGiftCertificate(giftCertificateId);
        giftCertificateDao.updateById(giftCertificateId, updateInfo(giftCertificate));
        updateCertificateTags(tagList, giftCertificateId);
        return buildGiftCertificateDto(giftCertificateDao.getById(giftCertificateId).orElseThrow(() -> new NoSuchEntityException("message.notFound")));
    }

    private Map<String, Object> updateInfo(GiftCertificate giftCertificate) {
        Map<String, Object> updateInfo = new HashMap<>();
        isValidNameForUpdate(giftCertificate, updateInfo, giftCertificateValidator);
        isValidDescriptionForUpdate(giftCertificate, updateInfo, giftCertificateValidator);
        isPriceValidForUpdate(giftCertificate, updateInfo, giftCertificateValidator);
        isDurationValidForUpdate(giftCertificate, updateInfo, giftCertificateValidator);
        return updateInfo;
    }

    private String getGiftCertificateName(GiftCertificate giftCertificate) {
        String giftCertificateName = giftCertificate.getName();
        boolean isCertificateExist = giftCertificateDao.getByName(giftCertificateName).isPresent();
        if (isCertificateExist) {
            throw new DuplicateEntityException("message.giftCertificateExists");
        }
        return giftCertificateName;
    }

    private void isDurationValidForUpdate(GiftCertificate giftCertificate, Map<String, Object> updateInfo, GiftCertificateValidatorImpl giftCertificateValidator) {
        Integer duration = giftCertificate.getDuration();
        if (!giftCertificateValidator.isDurationValid(duration)) {
            throw new InvalidEntityException("message.cantUpdateInvalidDuration");
        }
        updateInfo.put("duration", duration);
    }

    private void isPriceValidForUpdate(GiftCertificate giftCertificate, Map<String, Object> updateInfo, GiftCertificateValidatorImpl giftCertificateValidator) {
        BigDecimal price = giftCertificate.getPrice();
        if (price != null) {
            if (!giftCertificateValidator.isPriceValid(price)) {
                throw new InvalidEntityException("message.cantUpdateInvalidPrice");
            }
            updateInfo.put("price", price);
        }
    }

    private void isValidDescriptionForUpdate(GiftCertificate giftCertificate, Map<String, Object> updateInfo, GiftCertificateValidatorImpl giftCertificateValidator) {
        String description = giftCertificate.getDescription();
        if (description != null) {
            if (!giftCertificateValidator.isDescriptionValid(description)) {
                throw new InvalidEntityException("message.cantUpdateInvalidDescription");
            }
            updateInfo.put("description", description);
        }
    }

    private void isValidNameForUpdate(GiftCertificate giftCertificate, Map<String, Object> updateInfo, GiftCertificateValidatorImpl giftCertificateValidator) {
        String name = giftCertificate.getName();
        if (name != null) {
            if (!giftCertificateValidator.isNameValid(name)) {
                throw new InvalidEntityException("message.cantUpdateInvalidName");
            }
            updateInfo.put("name", name);
        }
    }

    private void isPresentGiftCertificate(Long id) {
        Optional<GiftCertificate> giftCertificateOptional = giftCertificateDao.getById(id);
        if (giftCertificateOptional.isEmpty()) {
            throw new NoSuchEntityException("message.notFound");
        }
    }

    private void updateCertificateTags(List<Tag> tagList, Long giftCertificateId) {
        for (Tag tag : tagList) {
            String tagName = tag.getName();
            if (tagName != null) {
                Optional<Tag> tagOptional = tagDao.getByName(tagName);
                Tag fullTag = tagOptional.orElseGet(() -> createGiftCertificateTag(tag));
                Long tagId = fullTag.getId();
                if (!giftCertificateDao.getTagIdsByGiftCertificateId(giftCertificateId).contains(tagId)) {
                    giftCertificateDao.createGiftCertificateTagReference(giftCertificateId, tagId);
                }
            }
        }
    }

    private GiftCertificateDto buildGiftCertificateDto(GiftCertificate giftCertificate) {
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto(giftCertificate);
        List<Optional<Tag>> optionalTagList = new ArrayList<>();
        List<Long> tagIds = giftCertificateDao.getTagIdsByGiftCertificateId(giftCertificate.getId());
        tagIds.forEach(id -> optionalTagList.add(tagDao.getById(id)));
        optionalTagList.stream()
                .filter(Optional::isPresent)
                .forEach(tag -> giftCertificateDto.addTag(tag.get()));
        return giftCertificateDto;
    }


    private void createGiftCertificateTagReference(List<Tag> tags, Long giftCertificateId) {
        for (Tag tag : tags) {
            String tagName = tag.getName();
            Optional<Tag> tagOptional = tagDao.getByName(tagName);
            Tag fullTag = tagOptional.orElseGet(() -> createGiftCertificateTag(tag));
            Long tagId = fullTag.getId();
            giftCertificateDao.createGiftCertificateTagReference(giftCertificateId, tagId);
        }
    }

    private Tag createGiftCertificateTag(Tag tag) {
        tagDao.create(tag);
        return tagDao.getByName(tag.getName()).orElseThrow(() -> new NoSuchEntityException("message.notFound"));
    }

    private void validateGiftCertificate(GiftCertificate giftCertificate) {
        if (!giftCertificateValidator.isValid(giftCertificate)) {
            throw new InvalidEntityException("message.giftCertificateInvalid");
        }
    }

    private void validateTags(List<Tag> tags) {
        boolean isCorrectTags = tags.stream().allMatch(tagValidator::isValid);
        if (!isCorrectTags) {
            throw new InvalidEntityException("message.tagInvalid");
        }
    }
}
