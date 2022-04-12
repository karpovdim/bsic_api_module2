package com.epam.esm.util;

import java.util.List;

public class QueryBuildHelper {

    public static final String BLANK_FOR_UPDATE_GIFT_CERTIFICATE_BY_ID = "UPDATE gift_certificate SET ";
    private static final String BLANK_FOR_GIFT_CERTIFICATE = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate";
    private static final String NAME = "name";
    private static final String DURATION = "duration";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";


    public String buildSortingQuery(List<String> sortColumns, List<String> orderTypes, List<String> filterBy) {
        StringBuilder stringBuilder = new StringBuilder(BLANK_FOR_GIFT_CERTIFICATE);
        buildFilteringQuery(stringBuilder, filterBy);
        if (!sortColumns.isEmpty()) {
            stringBuilder.append(" ORDER BY ");
            for (int i = 0; i < sortColumns.size(); i++) {
                if (i != 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append(sortColumns.get(i)).append(" ");
                stringBuilder.append(i < orderTypes.size() ? orderTypes.get(i) : "ASC");
            }
        }
        return stringBuilder.toString();
    }

    private String buildFilteringQuery(StringBuilder stringBuilder, List<String> filterBy) {
        if (!filterBy.isEmpty()) {
            stringBuilder.append(" WHERE ");
        }
        for (int i = 0; i < filterBy.size(); i++) {
            if (i != 0) {
                stringBuilder.append(" AND ");
            }
            if (i == 0) {
                stringBuilder.append(NAME);
            } else if (i == 1) {
                stringBuilder.append(DESCRIPTION);
            }
            stringBuilder.append(" LIKE '%").append(filterBy.get(i)).append("%'");
        }
        return stringBuilder.toString();
    }

    public String buildQueryForUpdate(List<String> columns) {
        StringBuilder stringBuilder = new StringBuilder(BLANK_FOR_UPDATE_GIFT_CERTIFICATE_BY_ID);
        if (columns != null) {
            for (String column : columns) {
                switch (column) {
                    case NAME, PRICE, DESCRIPTION, DURATION -> {
                        stringBuilder.append(column);
                        stringBuilder.append("=?,");
                    }
                }
            }
        }
        return stringBuilder.append("last_update_date = NOW() WHERE id = ?").toString();
    }
}
