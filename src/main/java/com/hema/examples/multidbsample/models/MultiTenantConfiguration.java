package com.hema.examples.multidbsample.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by hemabhatia on 2/8/17.
 */
@Configuration
public class MultiTenantConfiguration {

    @Autowired
    private DataSourceProperties properties;

    @Bean
    @ConfigurationProperties(
            prefix = "spring.datasource"
    )

    public DataSource dataSource() throws URISyntaxException {
        File[] files = Paths.get(ClassLoader.getSystemResource("tenants").toURI()).toFile().listFiles();
        Map<Object,Object> resolvedDataSources = new HashMap<>();
        for(File propertyFile : files) {
            Properties tenantProperties = new Properties();
            DataSourceBuilder dataSourceBuilder = new DataSourceBuilder(this.getClass().getClassLoader());
            try {
                tenantProperties.load(new FileInputStream(propertyFile));
                String tenantId = tenantProperties.getProperty("name");
                // Assumption: The tenant database uses the same driver class
                // as the default database that you configure.
                dataSourceBuilder.driverClassName(properties.getDriverClassName())
                        .url(tenantProperties.getProperty("datasource.url"))
                        .username(tenantProperties.getProperty("datasource.username"))
                        .password(tenantProperties.getProperty("datasource.password"));
                if(properties.getType() != null) {
                    dataSourceBuilder.type(properties.getType());
                }
                resolvedDataSources.put(tenantId, dataSourceBuilder.build());
            } catch (IOException e) {
                // Ooops, tenant could not be loaded. This is bad.
                // Stop the application!
                e.printStackTrace();
                return null;
            }
        }
        // Create the final multi-tenant source.
        // It needs a default database to connect to.
        // Make sure that the default database is actually an empty tenant database.
        // Don't use that for a regular tenant if you want things to be safe!
        MultiTenantDataSource dataSource = new MultiTenantDataSource();
        dataSource.setDefaultTargetDataSource(defaultDataSource());
        dataSource.setTargetDataSources(resolvedDataSources);
        // Call this to finalize the initialization of the data source.
        dataSource.afterPropertiesSet();
        return dataSource;
    }

    /**
     * Creates the default data source for the application
     * @return
     */
    private DataSource defaultDataSource() {
        DataSourceBuilder dataSourceBuilder = new DataSourceBuilder(this.getClass().getClassLoader())
                .driverClassName(properties.getDriverClassName())
                .url(properties.getUrl())
                .username(properties.getUsername())
                .password(properties.getPassword());

        if(properties.getType() != null) {
            dataSourceBuilder.type(properties.getType());
        }

        return dataSourceBuilder.build();
    }

}
