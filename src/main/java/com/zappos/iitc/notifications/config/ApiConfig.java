package com.zappos.iitc.notifications.config;

import com.zappos.iitc.commons.auth.filter.TokenBasedAuthenticationFilter;
import com.zappos.iitc.notifications.interceptor.NotificationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

/**
 * @author RaviGupta
 * Spring  Bean Configuration Class.
 */
@Configuration
@EnableWebSecurity
public class ApiConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    //Field to determine if stub responses are to be sent.
    @Value("#{systemProperties[isStub] ?: 'false'}")
    boolean isStub;

    @Autowired
    NotificationInterceptor notificationInterceptor;

    //TODO:: /productRequests/** can be removed from anyMatchers, if /productRequests api will have x-auth-token as parameter
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/swagger-resources/**","/configuration/security", "/swagger-ui.html", "/webjars/**","/actuator/**");
    }

    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(etagFilter());
        registration.addUrlPatterns("/*");
        registration.setName("etagFilter");
        registration.setOrder(1);
        return registration;
    }
    @Bean(name = "etagFilter")
    public Filter etagFilter() {
        return new ShallowEtagHeaderFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterAfter(new TokenBasedAuthenticationFilter("/**"), BasicAuthenticationFilter.class)
                .csrf().disable()
                .cors();
        /*.headers().xssProtection().block(false)
        .and().frameOptions().disable()
        .and().headers().contentTypeOptions().disable();*/

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(notificationInterceptor).addPathPatterns("/**");//.excludePathPatterns("/notifications/**");
    }

    /*@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();

    }*/
}
