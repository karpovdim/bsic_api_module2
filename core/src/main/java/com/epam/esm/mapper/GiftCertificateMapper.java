package com.epam.esm.mapper;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

import static com.epam.esm.constant.ColumnName.*;


public class GiftCertificateMapper implements RowMapper<GiftCertificate> {
    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GiftCertificate.builder()
                .id(rs.getLong(GIFT_CERTIFICATE_ID.name()))
                .name(rs.getString(GIFT_CERTIFICATE_NAME.name()))
                .duration(rs.getInt(GIFT_CERTIFICATE_DURATION.name()))
                .price(rs.getBigDecimal(GIFT_CERTIFICATE_PRICE.name()))
                .description(rs.getString(GIFT_CERTIFICATE_DESCRIPTION.name()))
                .createDate(rs.getTimestamp(GIFT_CERTIFICATE_CREATE_TIME.name()).toLocalDateTime().atZone(ZoneId.of("GMT+3")))
                .lastUpdateDate(rs.getTimestamp(GIFT_CERTIFICATE_LAST_UPDATE_DATE.name()).toLocalDateTime().atZone(ZoneId.of("GMT+3")))
                .build();
    }
}
