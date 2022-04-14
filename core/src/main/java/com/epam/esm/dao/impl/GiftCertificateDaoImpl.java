package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.util.QueryBuildHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.*;

import static com.epam.esm.constant.Query.*;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> rowMapper;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<GiftCertificate> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public void create(GiftCertificate giftCertificate) {
        jdbcTemplate.update(CREATE_GIFT_CERTIFICATE, giftCertificate.getName()
                , giftCertificate.getDescription(), giftCertificate.getPrice()
                , giftCertificate.getDuration());
    }

    @Override
    public List<GiftCertificate> getAll() {
        final var list = jdbcTemplate.query(GET_ALL_GIFT_CERTIFICATES, rowMapper);
        return resultCheck(list);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_GIFT_CERTIFICATE_BY_ID, id);
    }

    @Override
    public Optional<GiftCertificate> getById(Long id) {
        try {
            final var giftCertificate = jdbcTemplate.queryForObject(GET_GIFT_CERTIFICATE_BY_ID, rowMapper, id);
            return Optional.ofNullable(giftCertificate);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<GiftCertificate> getByName(String name) {
        try {
            final var giftCertificate = jdbcTemplate.queryForObject(GET_GIFT_CERTIFICATE_BY_NAME, rowMapper, name);
            return Optional.ofNullable(giftCertificate);
        } catch(EmptyResultDataAccessException e ){
            return Optional.empty();
        }
    }

    @Override
    public void updateById(Long giftCertificateId, Map<String, Object> giftCertificateInfoForUpdate) {
        List<Object> values = new ArrayList<>(giftCertificateInfoForUpdate.values());
        values.add(giftCertificateId);
        String query = new QueryBuildHelper()
                .buildQueryForUpdate(new ArrayList<>(giftCertificateInfoForUpdate.keySet()));
        jdbcTemplate.update(query, values.toArray());
    }

    @Override
    public List<GiftCertificate> getGiftCertificateByTagName(String tagName) {
        final var list = jdbcTemplate.query(GET_GIFT_CERTIFICATE_BY_TAG_NAME, rowMapper, tagName);
        return resultCheck(list);
    }

    @Override
    public List<Long> getTagIdsByGiftCertificateId(Long certificateId) {
        final var list = jdbcTemplate.query(GET_TAG_IDS_BY_GIFT_CERTIFICATE_ID,
                (resultSet, i) -> resultSet.getLong("tag.id"), certificateId);
        return resultCheck(list);
    }

    public List<GiftCertificate> getAllWithSortingAndFiltering(List<String> sortColumns,
                                                               List<String> orderType,
                                                               List<String> filterBy) {
        String query = new QueryBuildHelper().buildSortingQuery(sortColumns, orderType, filterBy);
        final var list = jdbcTemplate.query(query, rowMapper);
        return resultCheck(list);
    }

    @Override
    public void createGiftCertificateTagReference(Long giftCertificateId, Long tagId) {
        jdbcTemplate.update(CREATE_GIFT_CERTIFICATE_TAG_REFERENCE, giftCertificateId, tagId);
    }

    private <T> T resultCheck(@Nullable T result) {
        Assert.state(result != null, "No result");
        return result;
    }
}
