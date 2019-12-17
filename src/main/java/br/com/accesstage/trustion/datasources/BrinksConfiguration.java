package br.com.accesstage.trustion.datasources;

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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static br.com.accesstage.trustion.datasources.BrinksConfiguration.MODEL;
import static br.com.accesstage.trustion.datasources.BrinksConfiguration.REPOSITORY;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "brinksEntityManagerFactory",
        basePackages = {MODEL, REPOSITORY},
        transactionManagerRef = "brinksTransactionManager")
public class BrinksConfiguration {

    public static final String MODEL = "br.com.accesstage.trustion.model";
    public static final String REPOSITORY = "br.com.accesstage.trustion.repository.interfaces";

    @Primary
    @Bean(name = "brinksDataSource")
    public DataSource brinksDataSource(
            @Qualifier("brinksDataSourceProperties") DataSourceProperties brinksDataSourceProperties) {
        return brinksDataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "brinksDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.brinks")
    public DataSourceProperties brinksDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "brinksEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean brinksEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("brinksDataSource") DataSource brinksDataSource) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");

        return builder.dataSource(brinksDataSource)
                .packages(MODEL)
                .persistenceUnit("brinks")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "brinksTransactionManager")
    public PlatformTransactionManager brinksTransactionManager(
            @Qualifier("brinksEntityManagerFactory") EntityManagerFactory brinksEntityManagerFactory) {
        return new JpaTransactionManager(brinksEntityManagerFactory);
    }
}
