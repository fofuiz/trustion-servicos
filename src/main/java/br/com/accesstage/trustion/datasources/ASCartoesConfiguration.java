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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static br.com.accesstage.trustion.datasources.ASCartoesConfiguration.MODEL;
import static br.com.accesstage.trustion.datasources.ASCartoesConfiguration.REPOSITORY;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "cartoesEntityManagerFactory",
        basePackages = {MODEL, REPOSITORY},
        transactionManagerRef = "cartoesTransactionManager")
public class ASCartoesConfiguration {

    public static final String MODEL = "br.com.accesstage.trustion.ascartoes.model";
    public static final String REPOSITORY = "br.com.accesstage.trustion.repository.ascartoes.interfaces";

    @Bean(name = "cartoesDataSource")
    public DataSource cartoesDataSource(
            @Qualifier("cartoesDataSourceProperties") DataSourceProperties cartoesDataSourceProperties) {
        return cartoesDataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "cartoesDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.ascartoes")
    public DataSourceProperties cartoesDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "cartoesEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean cartoesEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("cartoesDataSource") DataSource cartoesDataSource) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        properties.put("hibernate.connection.driver_class", "oracle.jdbc.OracleDriver");

        return builder
                .dataSource(cartoesDataSource)
                .packages(MODEL)
                .persistenceUnit("ascartoes")
                .properties(properties)
                .build();
    }

    @Bean(name = "cartoesTransactionManager")
    public PlatformTransactionManager cartoesTransactionManager(
            @Qualifier("cartoesEntityManagerFactory") EntityManagerFactory cartoesEntityManagerFactory) {
        return new JpaTransactionManager(cartoesEntityManagerFactory);
    }
}
