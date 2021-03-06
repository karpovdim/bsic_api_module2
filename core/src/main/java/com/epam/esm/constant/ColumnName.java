package com.epam.esm.constant;

import lombok.val;

public enum ColumnName {
    TAG_ID("id"),
    TAG_NAME("name"),
    GIFT_CERTIFICATE_ID("id"),
    GIFT_CERTIFICATE_NAME("name"),
    GIFT_CERTIFICATE_PRICE("price"),
    GIFT_CERTIFICATE_DURATION("duration"),
    GIFT_CERTIFICATE_DESCRIPTION("description"),
    GIFT_CERTIFICATE_CREATE_TIME("create_date"),
    GIFT_CERTIFICATE_LAST_UPDATE_DATE("last_update_date");
    private String value;

    ColumnName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
