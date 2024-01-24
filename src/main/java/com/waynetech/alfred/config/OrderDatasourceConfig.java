package com.waynetech.alfred.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        entityManagerFactoryRef = "orderEntityManagerFactory",
        transactionManagerRef = "orderTransactionManager",
        basePackages = { "com.waynetech.alfred.order" })
public class OrderDatasourceConfig {
    // Step 1: Get the datasource properties
    @Bean
    @ConfigurationProperties("spring.datasource.order")
    public DataSourceProperties orderDatasourceProperties() {
        return new DataSourceProperties();
    }

    // Step 2: Build the datasource from the properties
    @Bean
    public DataSource orderDataSource(
            @Qualifier("orderDatasourceProperties") DataSourceProperties orderDataProps) {

        return orderDataProps.initializeDataSourceBuilder().build();
    }

    // Step 3: Initialize the db with script
    @Bean
    public DataSourceInitializer orderDataSourceInitializer(
            @Qualifier("orderDataSource") DataSource orderDatasource) {

        ResourceDatabasePopulator orderResourceDatabasePopulator = new ResourceDatabasePopulator();
        // adding only the schema file
        // the data.sql can be added to the obj as well
        orderResourceDatabasePopulator.addScript(new ClassPathResource("order-schema.sql"));

        DataSourceInitializer orderDataSourceInitializer = new DataSourceInitializer();
        orderDataSourceInitializer.setDataSource(orderDatasource);
        orderDataSourceInitializer.setDatabasePopulator(orderResourceDatabasePopulator);
        return  orderDataSourceInitializer;
    }

    /**
     * Step 4: Configure Entity Manager
     * since we are using Spring Data JPA to access data through repository interface,
     * we need to set up the Entity Manager for both the Datasource.
     * We will provide the data source and base package that holds the entity
     * to the EntityManagerFactory and build Entity Manager for that datasource.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean orderEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("orderDataSource") DataSource orderDataSource
    ) {

        return builder
                .dataSource(orderDataSource)
                .packages("com.waynetech.alfred.order")
                .persistenceUnit("order")
                .build();
    }


    /**
     * Step 5: Configure Transaction Manager
     * Use the EntityManagerFactory again to create the Transaction manager
     */
    @Bean
    @ConfigurationProperties("spring.jpa")
    public PlatformTransactionManager orderTransactionManager (
            @Qualifier("orderEntityManagerFactory") EntityManagerFactory orderEntityMangerFactory) {

        return new JpaTransactionManager(orderEntityMangerFactory);
    }

}