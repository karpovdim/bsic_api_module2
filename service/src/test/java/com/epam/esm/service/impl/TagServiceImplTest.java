package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NoSuchEntityException;
import com.epam.esm.validator.impl.TagValidatorImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class TagServiceImplTest {
    private Long id;
    private Tag tag;
    private TagDao tagDao;
    private TagValidatorImpl tagValidator;
    private TagServiceImpl tagService;

    @BeforeAll
    public void setUp() {
        id = 1L;
        tag = new Tag(id, "tag");
        tagDao = Mockito.mock(TagDaoImpl.class);
        tagValidator = Mockito.mock(TagValidatorImpl.class);
        tagService = new TagServiceImpl(tagDao, tagValidator);
    }


    @Test
    public void testCreateShouldCreateWhenValidAndNotExist() {
        when(tagValidator.isValid(any())).thenReturn(true);
        when(tagDao.getByName(anyString())).thenReturn(Optional.empty());
        tagService.create(tag);
        verify(tagDao).create(tag);
    }

    @Test
    public void testGetAllShouldGetAll() {
        tagService.getAll();
        verify(tagDao).getAll();
    }

    @Test
    public void testGetByIdShouldGetWhenFound() {
        when(tagDao.getById(anyLong())).thenReturn(Optional.of(tag));
        tagService.getById(id);
        verify(tagDao).getById(id);
    }


    @Test
    public void testGetByIdShouldThrowServiceExceptionWhenNotFound() {
        when(tagDao.getById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> tagService.getById(id));
    }

    @Test
    public void testDeleteByIdShouldDeleteWhenFound() {
        when(tagDao.getById(anyLong())).thenReturn(Optional.of(tag));
        tagService.deleteById(id);
        verify(tagDao).deleteById(id);
    }

    @AfterAll
    public void tierDown() {
        id = null;
        tag = null;
        tagDao = null;
        tagValidator = null;
        tagService = null;
    }
}