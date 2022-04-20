package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.impl.GiftCertificateValidatorImpl;
import com.epam.esm.validator.impl.TagValidatorImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.epam.esm.constant.ColumnName.*;
import static com.epam.esm.constant.ExceptionMessageConstant.*;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final TagDao tagDao;
    private final TagValidatorImpl tagValidator;
    private final GiftCertificateDao giftCertificateDao;
    private final GiftCertificateValidatorImpl giftCertificateValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao,
                                      GiftCertificateValidatorImpl giftCertificateValidator,
                                      TagValidatorImpl tagValidator) {
        this.giftCertificateValidator = giftCertificateValidator;
        this.giftCertificateDao = giftCertificateDao;
        this.tagValidator = tagValidator;
        this.tagDao = tagDao;
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
        GiftCertificate giftCertificate = giftCertificateDao.getById(id).orElseThrow(() -> new NoSuchEntityException(NOT_FOUND_MSG));
        List<GiftCertificateDto> listOfGiftCertificate = new ArrayList<>();
        listOfGiftCertificate.add(new GiftCertificateDto(giftCertificate));
        return listOfGiftCertificate;
    }

    @Override
    @Transactional
    public void create(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateDto.getGiftCertificate();
        giftCertificateValidator.validateGiftCertificate(giftCertificate);
        tagValidator.validateTags(giftCertificateDto.getCertificateTags());
        String giftCertificateName = getGiftCertificateName(giftCertificate);
        giftCertificateDao.create(giftCertificate);
        Long giftCertificateId = giftCertificateDao.getByName(giftCertificateName)
                .map(GiftCertificate::getId).orElseThrow(() -> new NoSuchEntityException(NOT_FOUND_MSG));
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
        giftCertificateValidator.presenceCheckGiftCertificateById(id, giftCertificateDao);
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
        List<String> sortColumnsProcessing = getEmptyListIfListIsNull(sortColumns);
        List<String> orderTypeProcessing = getEmptyListIfListIsNull(orderType);
        List<String> filterByProcessing = getEmptyListIfListIsNull(filterBy);
        List<GiftCertificate> giftCertificateList = giftCertificateDao
                .getAllWithSortingAndFiltering(sortColumnsProcessing, orderTypeProcessing, filterByProcessing);
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
        giftCertificateValidator.checkParamsNotNull(giftCertificate, tagList);
        giftCertificateValidator.presenceCheckGiftCertificateById(giftCertificateId, giftCertificateDao);
        giftCertificateDao.updateById(giftCertificateId, updateInfo(giftCertificate));
        updateCertificateTags(tagList, giftCertificateId);
        return buildGiftCertificateDto(giftCertificateDao.getById(giftCertificateId).orElseThrow(
                () -> new NoSuchEntityException(NOT_FOUND_MSG)));
    }


    private Map<String, Object> updateInfo(GiftCertificate giftCertificate) {
        Map<String, Object> updateInfo = new HashMap<>();
        if (giftCertificateValidator.isValidNameForUpdate(giftCertificate)) {
            updateInfo.put(GIFT_CERTIFICATE_NAME.getValue(), giftCertificate.getName());
        }
        if (giftCertificateValidator.isValidDescriptionForUpdate(giftCertificate)) {
            updateInfo.put(GIFT_CERTIFICATE_DESCRIPTION.getValue(), giftCertificate.getDescription());
        }
        if (giftCertificateValidator.isPriceValidForUpdate(giftCertificate)) {
            updateInfo.put(GIFT_CERTIFICATE_PRICE.getValue(), giftCertificate.getPrice());
        }
        if (giftCertificateValidator.isDurationValidForUpdate(giftCertificate)) {
            updateInfo.put(GIFT_CERTIFICATE_DURATION.getValue(), giftCertificate.getDuration());
        }
        return updateInfo;
    }

    private String getGiftCertificateName(GiftCertificate giftCertificate) {
        String giftCertificateName = giftCertificate.getName();
        giftCertificateDao.getByName(giftCertificateName).orElseThrow(
                () -> new DuplicateEntityException(GIFT_CERTIFICATE_EXISTS_MSG));
        return giftCertificateName;
    }


    private void updateCertificateTags(List<Tag> tagList, Long giftCertificateId) {
        for (Tag tag : tagList) {
            String tagName = tag.getName();
            if (StringUtils.isNotEmpty(tagName)) {
                Optional<Tag> tagOptional = tagDao.getByName(tagName);
                Long tagId = createTagIfNotExists(tag, tagOptional);
                createReferenceIfCertificateNotContainsTag(giftCertificateId, tagId);
            }
        }
    }

    private Long createTagIfNotExists(Tag tag, Optional<Tag> tagOptional) {
        Tag fullTag = tagOptional.orElseGet(() -> createGiftCertificateTag(tag));
        return fullTag.getId();
    }

    private void createReferenceIfCertificateNotContainsTag(Long giftCertificateId, Long tagId) {
        if (!giftCertificateDao.getTagIdsByGiftCertificateId(giftCertificateId).contains(tagId)) {
            giftCertificateDao.createGiftCertificateTagReference(giftCertificateId, tagId);
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
        return tagDao.getByName(tag.getName()).orElseThrow(
                () -> new NoSuchEntityException(NOT_FOUND_MSG));
    }

    private List<String> getEmptyListIfListIsNull(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

}
