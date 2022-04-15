package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {

    void create(GiftCertificateDto giftCertificateDto);

    List<GiftCertificateDto> getAll();

    List<GiftCertificateDto> getRoute(String tagName, List<String> sortColumns,
                                      List<String> orderType, List<String> filterBy,
                                      Long giftCertificateId, String allWithTags);

    void deleteById(Long id);

    List<GiftCertificateDto> getById(Long id);

    GiftCertificateDto updateById(Long giftCertificateId, GiftCertificateDto giftCertificateDto);

    List<GiftCertificateDto> getAllWithTags();

    List<GiftCertificateDto> getAllWithSortingAndFiltering(List<String> sortColumns, List<String> orderType, List<String> filterBy);

    List<GiftCertificateDto> getAllByTagName(String tagName);

}
