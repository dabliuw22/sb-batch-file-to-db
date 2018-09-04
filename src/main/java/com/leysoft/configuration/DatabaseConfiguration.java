package com.leysoft.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

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
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
    	return new NamedParameterJdbcTemplate(dataSource);
    }
}
