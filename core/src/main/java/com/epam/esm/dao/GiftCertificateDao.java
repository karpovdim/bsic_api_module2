package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificateDao {

    void create(GiftCertificate giftCertificate);

    void createGiftCertificateTagReference(Long giftCertificateId, Long tagId);

    List<GiftCertificate> getAll();

    void deleteById(Long id);

    Optional<GiftCertificate> getById(Long id);

    Optional<GiftCertificate> getByName(String name);

    void updateById(Long giftCertificateId, Map<String, Object> giftCertificateInfoForUpdate);

    List<GiftCertificate> getGiftCertificateByTagName(String tagName);

    List<Long> getTagIdsByGiftCertificateId(Long giftCertificateId);

    List<GiftCertificate> getAllWithSortingAndFiltering(List<String> sortColumns,
                                                        List<String> orderType,
                                                        List<String> filterBy);


}
