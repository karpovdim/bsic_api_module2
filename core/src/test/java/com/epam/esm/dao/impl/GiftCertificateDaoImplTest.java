package com.epam.esm.dao.impl;

import com.epam.esm.config.DeveloperConfig;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DeveloperConfig.class)
@ActiveProfiles("dev")
@DirtiesContext
class GiftCertificateDaoImplTest {
    @Autowired
    private GiftCertificateDaoImpl giftCertificateDao;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        GiftCertificate certificate = new GiftCertificate(0L,
                "Photography Gift Certificate",
                "It is perfect for photography session gift certificates",
                BigDecimal.valueOf(50),
                60,
                ZonedDateTime.now(),
                ZonedDateTime.now());
        giftCertificateDao.create(certificate);
        final var photography_gift_certificate = giftCertificateDao.getByName("Photography Gift Certificate");
        final var certificate1 = photography_gift_certificate.get();
        final var id = certificate1.getId();
        Assertions.assertNotEquals(0L, id);
    }

    @Test
    void getAll() {
        final var all = giftCertificateDao.getAll();
        Assertions.assertEquals(3L, all.size());
    }

    @Test
    void deleteById() {
        giftCertificateDao.deleteById(1L);
        final var all = giftCertificateDao.getAll();
        Assertions.assertEquals(2L, all.size());

    }

    @Test
    void getById() {
        final var certificate = giftCertificateDao.getById(2L).get();
        Assertions.assertEquals(2L, certificate.getId());
    }

    @Test
    void getByName() {
        final var certificate = giftCertificateDao.getByName("First tandem skydive TEST").get();
        Assertions.assertEquals("First tandem skydive TEST", certificate.getName());

    }

    @Test
    void updateById() {
        Map<String, Object> updateValues = new HashMap<>();
        updateValues.put("price",BigDecimal.TEN);
        giftCertificateDao.updateById(2L,updateValues);
        final var actual = giftCertificateDao.getById(2L).get().getPrice();
        Assertions.assertEquals(BigDecimal.TEN,actual);
    }

    @Test
    void getGiftCertificateByTagName() {
        final var byTagName = giftCertificateDao.getGiftCertificateByTagName("speed");
        Assertions.assertNotEquals(0,byTagName.size());
    }

    @Test
    void getTagIdsByGiftCertificateId() {
    }

    @Test
    void getAllWithSortingAndFiltering() {
    }

    @Test
    void createGiftCertificateTagReference() {
    }
}