
package com.epam.esm.config;


import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.TagMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
@Configuration
@PropertySource({"classpath:application.properties"})
@ComponentScan(
        basePackages = {"com.epam.esm"}
)
public class SpringJdbcConfig {
    @Value("${mysqlDriver}")
    private String driveClass;
    @Value("${db.url}")
    private String databaseUrl;
    @Value("${db.username}")
    private String userName;
    @Value("${db.password}")
    private String password;
    @Value("${db.poll.size}")
    private int maxPoolSize;

    public SpringJdbcConfig() {
    }

    @Bean
    @Profile("!dev")
    public HikariConfig getHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(this.driveClass);
        config.setJdbcUrl(this.databaseUrl);
        config.setUsername(this.userName);
        config.setPassword(this.password);
        config.setMaximumPoolSize(this.maxPoolSize);
        return config;
    }

    @Lazy
    @Bean(name = {"source"})
    public DataSource dataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = {"jdbcTemplate"})
    public JdbcTemplate getJdbcTemplate(DataSource source) {
        return new JdbcTemplate(source);
    }

    @Bean(name = {"certificateMapper"})
    public GiftCertificateMapper getCertificateMapper() {
        return new GiftCertificateMapper();
    }

    @Bean(name = {"tagMapper"})
    public TagMapper getTagMapper() {
        return new TagMapper();
    }

    @Bean
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
        return new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Bean(name = {"tagDao"})
    public TagDao getTagDao(JdbcTemplate jdbcTemplate, TagMapper tagMapper) {
        return new TagDaoImpl(jdbcTemplate, tagMapper);
    }
}
