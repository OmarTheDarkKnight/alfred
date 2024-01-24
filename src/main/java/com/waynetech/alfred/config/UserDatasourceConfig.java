package com.waynetech.alfred.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "userEntityManagerFactory",
        transactionManagerRef = "userTransactionManager",
        basePackages = { "com.waynetech.alfred.user" })
public class UserDatasourceConfig {
    // Step 1: Get the datasource properties
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.user")
    public DataSourceProperties userDatasourceProperties() {
        return new DataSourceProperties();
    }

    // Step 2: Build the datasource from the properties
    @Bean
    @Primary
    public DataSource userDataSource(
            @Qualifier("userDatasourceProperties") DataSourceProperties userDataProps) {

        return userDataProps.initializeDataSourceBuilder().build();
    }

    // Step 3: Initialize the datasource with custom script
    @Bean
    public DataSourceInitializer userDataSourceInitializer(
            @Qualifier("userDataSource") DataSource userDatasource) {

        ResourceDatabasePopulator userResourceDatabasePopulator = new ResourceDatabasePopulator();
        // adding only the schema file
        // the data.sql can be added to the obj as well
        userResourceDatabasePopulator.addScript(new ClassPathResource("user-schema.sql"));

        DataSourceInitializer userDataSourceInitializer = new DataSourceInitializer();
        userDataSourceInitializer.setDataSource(userDatasource);
        userDataSourceInitializer.setDatabasePopulator(userResourceDatabasePopulator);
        return userDataSourceInitializer;
    }

    /**
     * Step 4: Configure Entity Manager
     * since we are using Spring Data JPA to access data through repository interface,
     * we need to set up the Entity Manager for both the Datasource.
     * We will provide the data source and base package that holds the entity
     * to the EntityManagerFactory and build Entity Manager for that datasource.
     */
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("userDataSource") DataSource userDataSource
    ) {

        return builder
                .dataSource(userDataSource)
                .packages("com.waynetech.alfred.user")
                .persistenceUnit("user")
                .build();
    }

    /**
     * Step 5: Configure Transaction Manager
     * Use the EntityManagerFactory again to create the Transaction manager
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.jpa")
    public PlatformTransactionManager userTransactionManager (
            @Qualifier("userEntityManagerFactory") EntityManagerFactory userEntityMangerFactory) {

        return new JpaTransactionManager(userEntityMangerFactory);
    }

}