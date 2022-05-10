
package com.epam.esm.config;


import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.mapper.TagMapper;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:database.properties"})
@ComponentScan(basePackages = {"com.epam.esm"})
@Profile("dev")
public class DeveloperConfig {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("testdb/init_db.sql")
                .addScript("testdb/date_init.sql")
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
    @Bean(name = {"certificateMapper"})
    public GiftCertificateMapper getCertificateMapper() {
        return new GiftCertificateMapper();
    }
    @Bean(name = {"tagMapper"})
    public TagMapper getTagMapper() {
        return new TagMapper();
    }
}
