
package com.leysoft.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DatabaseConfiguration {

    @Value(
            value = "${spring.datasource.username}")
    private String username;

    @Value(
            value = "${spring.datasource.password}")
    private String password;

    @Value(
            value = "${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value(
            value = "${spring.datasource.url}")
    private String url;

    @Bean(
            name = {
                "dataSourceH2"
            })
    public DataSource dataSource() {
        return DataSourceBuilder.create().username(username).password(password).url(url)
                .driverClassName(driverClassName).build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JdbcOperations jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcOperations namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
