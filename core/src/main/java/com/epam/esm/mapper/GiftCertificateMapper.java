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
                .id(rs.getLong(GIFT_CERTIFICATE_ID))
                .name(rs.getString(GIFT_CERTIFICATE_NAME))
                .description(rs.getString(GIFT_CERTIFICATE_DESCRIPTION))
                .price(rs.getBigDecimal(GIFT_CERTIFICATE_PRICE))
                .duration(rs.getInt(GIFT_CERTIFICATE_DURATION))
                .createDate(rs.getTimestamp(GIFT_CERTIFICATE_CREATE_TIME).toLocalDateTime().atZone(ZoneId.of("GMT+3")))
                .lastUpdateDate(rs.getTimestamp(GIFT_CERTIFICATE_LAST_UPDATE_DATE).toLocalDateTime().atZone(ZoneId.of("GMT+3")))
                .build();
    }
}
