CREATE SCHEMA IF NOT EXISTS `gift_certificates` DEFAULT CHARACTER SET utf8;
USE `gift_certificates`;

CREATE TABLE IF NOT EXISTS `gift_certificates`.`gift_certificate`
(
    `id`               INT            NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(90)    NOT NULL,
    `description`      VARCHAR(250)   NOT NULL,
    `price`            DECIMAL(10, 0) NOT NULL,
    `duration`         INT            NOT NULL,
    `create_date`      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `last_update_date` TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `gift_certificates`.`tag`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(90) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_tag_UNIQUE` (`name` ASC) VISIBLE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `gift_certificates`.`certificates_tags`
(
    `certificate_id` INT NOT NULL,
    `tag_id`         INT NOT NULL,
    PRIMARY KEY (`certificate_id`, `tag_id`),
    INDEX `fk_gift_certificate_has_tag_tag1_idx` (`tag_id` ASC) VISIBLE,
    INDEX `fk_gift_certificate_has_tag_gift_certificate_idx` (`certificate_id` ASC) VISIBLE,
    CONSTRAINT `fk_gift_certificate_has_tag_gift_certificate`
        FOREIGN KEY (`certificate_id`)
            REFERENCES `gift_certificates`.`gift_certificate` (`id`)
            ON DELETE CASCADE,
    CONSTRAINT `fk_gift_certificate_has_tag_tag`
        FOREIGN KEY (`tag_id`)
            REFERENCES `gift_certificates`.`tag` (`id`)
            ON DELETE CASCADE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;