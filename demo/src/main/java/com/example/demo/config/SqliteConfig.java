package com.example.demo.config;

import java.nio.file.Path;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@PropertySource("classpath:persistence.properties")
class SqliteConfig {
    @Autowired
    Environment env;

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Path dbPath = Path.of(env.getProperty("absoluteDBPath"));
        dataSource.setDriverClassName(env.getProperty("driverClassName"));
        System.out.println(dbPath.toAbsolutePath());
        dataSource.setUrl("jdbc:sqlite:/" + dbPath.toAbsolutePath());

        dataSource.setUsername(env.getProperty("user"));
        dataSource.setPassword(env.getProperty("password"));
        return dataSource;
    }
}