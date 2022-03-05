package com.zappos.iitc.notifications.config;


import com.zappos.iitc.commons.account.config.spring.BeanConfig;
import com.zappos.iitc.notification.config.spring.NotificationBeanConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author RaviGupta
 * Database Configuration Class.
 */
@Configuration
@Import({BeanConfig.class, NotificationBeanConfig.class})
public class DBConfig { }
