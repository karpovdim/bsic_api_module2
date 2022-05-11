package com.epam.esm.dao.impl;

import com.epam.esm.config.DeveloperConfig;
import com.epam.esm.entity.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DeveloperConfig.class)
@ActiveProfiles("dev")
class TagDaoImplTest {

    @Autowired
    private TagDaoImpl tagDao;

    @Test
    void create() {
        final var testTag = new Tag(0L, "testTag");
        tagDao.create(testTag);
        final var actual = tagDao.getByName("testTag").get().getName();
        Assertions.assertEquals("testTag", actual);
    }

    @Test
    void getByName() {
        final var actual = tagDao.getByName("speed").get().getName();
        Assertions.assertEquals("speed", actual);
    }

    @Test
    void getById() {
        final var actual = tagDao.getById(1L).get().getId();
        Assertions.assertEquals(1L,actual);
    }

    @Test
    void getAll() {
        final var actual = tagDao.getAll().size();
        Long expected = 4L;
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void deleteById() {
        tagDao.deleteById(1L);
        final var actual = tagDao.getAll().size();
        Long expected = 3L;
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void updateNameById() {
        tagDao.updateNameById(2L,"test");
        final var actual = tagDao.getById(2L).get().getName();
        String expected = "test";
        Assertions.assertEquals(expected,actual);
    }
}