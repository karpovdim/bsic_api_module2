package com.epam.esm.dao.impl;

import com.epam.esm.constant.Query;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.constant.Query.*;


@Repository
public class TagDaoImpl implements TagDao {

    private final RowMapper<Tag> rowMapper;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDaoImpl( JdbcTemplate jdbcTemplate,RowMapper<Tag> rowMapper) {
        this.rowMapper = rowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Tag tag) {
        jdbcTemplate.update(Query.CREATE_TAG, tag.getName());
    }

    @Override
    public Optional<Tag> getByName(String value) {
        final var tag = jdbcTemplate.queryForObject(FIND_TAG_BY_NAME, rowMapper, value);
        return Optional.ofNullable(tag);
    }

    @Override
    public Optional<Tag> getById(Long id) {
        final var tag = jdbcTemplate.queryForObject(FIND_TAG_BY_ID, rowMapper, id);
        return Optional.ofNullable(tag);
    }

    @Override
    public List<Tag> getAll() {
        final var list = jdbcTemplate.query(GET_ALL_TAGS, rowMapper);
        return resultCheck(list);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_TAG_BY_ID, id);
    }

    @Override
    public List<Tag> getTagsByGiftCertificateId(Long giftCertificateId) {
        final var list = jdbcTemplate.query(GET_TAGS_BY_GIFT_CERTIFICATE_ID, rowMapper, giftCertificateId);
        return resultCheck(list);
    }

    @Override
    public void updateNameById(Long id, String name) {
        jdbcTemplate.update(UPDATE_TAG_BY_ID, name, id);
    }

    private  <T> T resultCheck(@Nullable T result) {
        Assert.state(result != null, "No result");
        return result;
    }
}
