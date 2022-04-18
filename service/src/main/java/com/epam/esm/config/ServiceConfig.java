package com.epam.esm.config;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.validator.Validator;
import com.epam.esm.validator.impl.GiftCertificateValidatorImpl;
import com.epam.esm.validator.impl.TagValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.epam.esm"})
public class ServiceConfig {

    private final TagDao tagDao;
    private final GiftCertificateDao certificateDao;

    @Autowired
    public ServiceConfig(TagDao tagDao, GiftCertificateDao certificateDao) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
    }

    @Bean(name = "certificateValidator")
    public Validator<GiftCertificate> getCertificateValidator() {
        return new GiftCertificateValidatorImpl();
    }

    @Bean(name = {"tagValidator"})
    public Validator<Tag> getTagValidator() {
        return new TagValidatorImpl();
    }

    @Bean(name = "certificateService")
    public GiftCertificateService getCertificateService(GiftCertificateValidatorImpl certificateValidator,
                                                        TagValidatorImpl tagValidator) {
        return new GiftCertificateServiceImpl(certificateDao, tagDao, certificateValidator, tagValidator);
    }

    @Bean(name = {"tagService"})
    public TagService getTagService(TagValidatorImpl tagValidator) {
        return new TagServiceImpl(tagDao, tagValidator);
    }
}
