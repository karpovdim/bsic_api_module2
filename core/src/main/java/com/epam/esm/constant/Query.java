package com.epam.esm.constant;

public final class Query {
    public static final String GET_ALL_TAGS = "SELECT id,name FROM tag";
    public static final String CREATE_TAG = "INSERT INTO tag(name) VALUES (?)";
    public static final String DELETE_TAG_BY_ID = "DELETE FROM tag WHERE id = ?";
    public static final String FIND_TAG_BY_ID = "SELECT id,name FROM tag WHERE id = ?";
    public static final String UPDATE_TAG_BY_ID = "UPDATE tag SET name = ? WHERE id = ?";
    public static final String FIND_TAG_BY_NAME = "SELECT id,name FROM tag WHERE name = ?";
    public static final String GET_TAGS_BY_GIFT_CERTIFICATE_ID = "SELECT tag.id, tag.name FROM gift_certificates.tag JOIN certificates_tags ON tag.id = tag_id JOIN gift_certificate ON certificates_tags.certificate_id = gift_certificate.id WHERE gift_certificate.id =?";
    public static final String CREATE_GIFT_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration) VALUES(?,?,?,?)";
    public static final String GET_ALL_GIFT_CERTIFICATES = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate";
    public static final String DELETE_GIFT_CERTIFICATE_BY_ID = "DELETE FROM gift_certificate WHERE id = ?";
    public static final String GET_GIFT_CERTIFICATE_BY_ID = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate WHERE id = ?";
    public static final String GET_GIFT_CERTIFICATE_BY_NAME = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate WHERE name = ?";
    public static final String GET_GIFT_CERTIFICATE_BY_TAG_NAME = "SELECT gift_certificate.id,gift_certificate.name,description,price,duration,create_date,last_update_date FROM gift_certificate JOIN certificates_tags ON gift_certificate.id = certificate_id JOIN tag ON tag_id = tag.id WHERE tag.name = ?";
    public static final String CREATE_GIFT_CERTIFICATE_TAG_REFERENCE = "INSERT INTO certificates_tags (certificate_id,tag_id)VALUES(?,?)";
    public static final String GET_TAG_IDS_BY_GIFT_CERTIFICATE_ID = "SELECT tag.id FROM tag JOIN certificates_tags ON tag.id = tag_id JOIN gift_certificate ON certificate_id = gift_certificate.id WHERE gift_certificate.id = ?";
}

