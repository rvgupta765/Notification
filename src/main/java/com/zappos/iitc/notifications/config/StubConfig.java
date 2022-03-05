package com.zappos.iitc.notifications.config;

import org.h2.jdbcx.JdbcDataSource;
import org.h2.server.web.WebServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @author RaviGupta
 * Spring  Stub Configuration Class.
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "com.zappos.iitc.notifications.stub.repository",
        entityManagerFactoryRef = "stubEntityManager",
        transactionManagerRef = "stubTransactionManager"
)
public class StubConfig {
    @Autowired
    private Environment env;
    private static Logger logger = LoggerFactory.getLogger(StubConfig.class);

    @Bean
    public LocalContainerEntityManagerFactoryBean stubEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        DataSource dataSource = stubDataSource();
        Resource initSchema = new ClassPathResource("schema.sql");
        Resource initData = new ClassPathResource("data.sql");
        DatabasePopulator databasePopulator = new ResourceDatabasePopulator(initSchema, initData);
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);

        em.setDataSource(dataSource);
        em.setPackagesToScan(
                new String[]{"com.zappos.iitc.notifications.stub.domain"});
        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                "none");
        properties.put("hibernate.dialect",
                env.getProperty("stub.jpa.properties.hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public DataSource stubDataSource() {

        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl(env.getProperty("stub.datasource.url"));
        dataSource.setUser(env.getProperty("stub.datasource.username"));
        dataSource.setPassword(env.getProperty("stub.datasource.password"));

        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(("schema.sql"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String s = null;
        try {
            s = bufferedReader.readLine();
        } catch (IOException e) {
            logger.error("", e);
        }

        String s1 = s;


        return dataSource;
    }

    @Bean
    public PlatformTransactionManager stubTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                stubEntityManager().getObject());
        return transactionManager;
    }


    @Bean
    public ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
        registration.addUrlMappings("/h2-console/*");
        return registration;
    }
}