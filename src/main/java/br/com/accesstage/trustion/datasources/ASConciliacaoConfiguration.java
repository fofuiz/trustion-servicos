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

import static br.com.accesstage.trustion.datasources.ASConciliacaoConfiguration.MODEL;
import static br.com.accesstage.trustion.datasources.ASConciliacaoConfiguration.REPOSITORY;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "conciliacaoEntityManagerFactory",
        basePackages = {MODEL, REPOSITORY},
        transactionManagerRef = "conciliacaoTransactionManager")
public class ASConciliacaoConfiguration {

    public static final String MODEL = "br.com.accesstage.trustion.asconciliacao.model";
    public static final String REPOSITORY = "br.com.accesstage.trustion.repository.asconciliacao.interfaces";

    @Bean(name = "conciliacaoDataSource")
    public DataSource conciliacaoDataSource(
            @Qualifier("conciliacaoDataSourceProperties") DataSourceProperties conciliacaoDataSourceProperties) {
        return conciliacaoDataSourceProperties
                .initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "conciliacaoDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.asconciliacao")
    public DataSourceProperties conciliacaoDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "conciliacaoEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean conciliacaoEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("conciliacaoDataSource") DataSource conciliacaoDataSource) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        properties.put("hibernate.connection.driver_class", "oracle.jdbc.OracleDriver");

        return builder
                .dataSource(conciliacaoDataSource)
                .packages(MODEL)
                .persistenceUnit("asconciliacao")
                .properties(properties)
                .build();
    }

    @Bean(name = "conciliacaoTransactionManager")
    public PlatformTransactionManager conciliacaoTransactionManager(
            @Qualifier("conciliacaoEntityManagerFactory") EntityManagerFactory conciliacaoEntityManagerFactory) {
        return new JpaTransactionManager(conciliacaoEntityManagerFactory);
    }
}
