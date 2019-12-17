/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.accesstage.trustion.datasources;

import static br.com.accesstage.trustion.datasources.FornaxConfiguration.MODEL;
import static br.com.accesstage.trustion.datasources.FornaxConfiguration.REPOSITORY;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author caiomoraes
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "fornaxEntityManagerFactory",
        basePackages = {MODEL, REPOSITORY},
        transactionManagerRef = "fornaxTransactionManager")
public class FornaxConfiguration {

    public static final String MODEL = "br.com.accesstage.trustion.fornax.model";
    public static final String REPOSITORY = "br.com.accesstage.trustion.fornax.repository.interfaces";

    
    @Bean(name = "fornaxDataSource")
    public DataSource fornaxDataSource(
            @Qualifier("fornaxDataSourceProperties") DataSourceProperties fornaxDataSourceProperties) {
        return fornaxDataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    
    @Bean(name = "fornaxDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.fornax")
    public DataSourceProperties fornaxDataSourceProperties() {
        return new DataSourceProperties();
    }

    
    @Bean(name = "fornaxEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean fornaxEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("fornaxDataSource") DataSource fornaxDataSource) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");

        return builder.dataSource(fornaxDataSource)
                .packages(MODEL)
                .persistenceUnit("fornax")
                .properties(properties)
                .build();
    }

    
    @Bean(name = "fornaxTransactionManager")
    public PlatformTransactionManager fornaxTransactionManager(
            @Qualifier("fornaxEntityManagerFactory") EntityManagerFactory fornaxEntityManagerFactory) {
        return new JpaTransactionManager(fornaxEntityManagerFactory);
    }
}
