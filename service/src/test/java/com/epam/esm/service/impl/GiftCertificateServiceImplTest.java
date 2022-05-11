package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.InvalidEntityException;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.impl.GiftCertificateValidatorImpl;
import com.epam.esm.validator.impl.TagValidatorImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class GiftCertificateServiceImplTest {
    private long id;
    private GiftCertificateValidatorImpl giftCertificateValidator;
    private TagValidatorImpl tagValidator;
    private GiftCertificateDao giftCertificateDao;
    private TagDao tagDao;
    private GiftCertificateService giftCertificateService;
    private GiftCertificate giftCertificate;
    private GiftCertificateDto giftCertificateDto;

    @BeforeAll
    public void setUp() {
        id = 1L;
        giftCertificateValidator = mock(GiftCertificateValidatorImpl.class);
        tagValidator = mock(TagValidatorImpl.class);
        giftCertificateDao = mock(GiftCertificateDaoImpl.class);
        tagDao = mock(TagDaoImpl.class);
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao, tagDao
                , giftCertificateValidator, tagValidator);
        giftCertificate = new GiftCertificate(id, "name",
                "description", BigDecimal.TEN, 5, ZonedDateTime.now(), ZonedDateTime.now());
        giftCertificateDto = new GiftCertificateDto(giftCertificate);
    }

    @Test
    public void testCreateShouldThrowsExceptionWhenNotValid() {
        when(giftCertificateValidator.isValid(any())).thenReturn(false);
        assertThrows(InvalidEntityException.class, () -> giftCertificateService.create(giftCertificateDto));
    }

    @Test
    public void testCreateShouldThrowsExceptionWhenExist() {
        when(giftCertificateValidator.isValid(any())).thenReturn(true);
        when(giftCertificateDao.getByName(anyString())).thenReturn(Optional.of(giftCertificate));
        assertThrows(DuplicateEntityException.class, () -> giftCertificateService.create(giftCertificateDto));
    }

    @Test
    public void testGetAllShouldGetAll() {
        giftCertificateService.getAll();
        verify(giftCertificateDao, atLeast(2)).getAll();
    }

    @Test
    public void testGetByIdShouldGetWhenFound() {
        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.of(giftCertificate));
        giftCertificateService.getById(id);
        verify(giftCertificateDao, atLeast(2)).getById(id);
    }

    @Test
    public void getAllWithTagsShouldGetAllWithTags() {
        giftCertificateService.getAllWithTags();
        verify(giftCertificateDao).getAll();
    }

    @Test
    public void testUpdateByIdShouldUpdateWhenFound() {
        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.of(giftCertificate));
        when((giftCertificateValidator.isNameValid(anyString()))).thenReturn(true);
        when((giftCertificateValidator.isDescriptionValid(anyString()))).thenReturn(true);
        when((giftCertificateValidator.isDurationValid(anyInt()))).thenReturn(true);
        when((giftCertificateValidator.isPriceValid(any()))).thenReturn(true);
        giftCertificateService.updateById(id, giftCertificateDto);
        verify(giftCertificateDao).updateById(anyLong(), any());
    }

    @Test
    public void testUpdateByIdShouldThrowsExceptionWhenNotFound() {
        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.updateById(id, giftCertificateDto));
    }

    @Test
    public void testDeleteByIdShouldDeleteWhenFound() {
        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.of(giftCertificate));
        giftCertificateService.deleteById(id);
        verify(giftCertificateDao).deleteById(id);
    }

    @Test
    public void testDeleteByIdShouldThrowsExceptionWhenNotFound() {
        when(giftCertificateDao.getById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> giftCertificateService.deleteById(id));
    }

    @AfterAll
    public void tierDown() {
        giftCertificateValidator = null;
        tagValidator = null;
        giftCertificateDao = null;
        tagDao = null;
        giftCertificateService = null;
        giftCertificate = null;
        giftCertificateDto = null;
    }
}