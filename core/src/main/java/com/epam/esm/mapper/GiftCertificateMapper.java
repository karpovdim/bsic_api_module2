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
                .id(rs.getLong(GIFT_CERTIFICATE_ID.getValue()))
                .name(rs.getString(GIFT_CERTIFICATE_NAME.getValue()))
                .duration(rs.getInt(GIFT_CERTIFICATE_DURATION.getValue()))
                .price(rs.getBigDecimal(GIFT_CERTIFICATE_PRICE.getValue()))
                .description(rs.getString(GIFT_CERTIFICATE_DESCRIPTION.getValue()))
                .createDate(rs.getTimestamp(GIFT_CERTIFICATE_CREATE_TIME.getValue()).toLocalDateTime().atZone(ZoneId.of("GMT+3")))
                .lastUpdateDate(rs.getTimestamp(GIFT_CERTIFICATE_LAST_UPDATE_DATE.getValue()).toLocalDateTime().atZone(ZoneId.of("GMT+3")))
                .build();
    }
}
