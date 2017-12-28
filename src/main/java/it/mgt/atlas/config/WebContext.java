package it.mgt.atlas.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mgt.util.spring.web.auth.AuthorizationInterceptor;
import it.mgt.util.spring.web.auth.BasicAuthInterceptor;
import it.mgt.util.spring.web.auth.RequiredOperationInterceptor;
import it.mgt.util.spring.web.auth.SessionTokenInterceptor;
import it.mgt.util.spring.web.config.EnhancedWebMvcConfigurerAdapter;
import it.mgt.util.spring.web.config.JsonViewConfiguration;
import it.mgt.util.spring.web.jpa.JpaResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.util.List;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = {"it.mgt.atlas.advice", "it.mgt.atlas.resolver"})
@JsonViewConfiguration(defaultView = "default", packages = "it.mgt.atlas.view")
public class WebContext extends EnhancedWebMvcConfigurerAdapter {

    @Autowired
    ObjectMapper objectMapper;
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        
        argumentResolvers.add(jpaResolver());
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor());

        registry.addInterceptor(basicAuthInterceptor());
        registry.addInterceptor(sessionTokenInterceptor());
        registry.addInterceptor(authorizationInterceptor());
        registry.addInterceptor(webContentInterceptor());
        registry.addInterceptor(requiredOperationInterceptor());
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jackson2Converter());
        converters.add(stringHttpMessageConverter());
        converters.add(resourceHttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter jackson2Converter() {
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter();
    }

    @Bean
    public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
        return new OpenEntityManagerInViewInterceptor();
    }

    @Bean
    public BasicAuthInterceptor basicAuthInterceptor() {
        return new BasicAuthInterceptor();
    }

    @Bean
    public SessionTokenInterceptor sessionTokenInterceptor() {
        SessionTokenInterceptor sessionTokenInterceptor = new SessionTokenInterceptor();
        sessionTokenInterceptor.setCookieName("it.mgt.atlas");

        return sessionTokenInterceptor;
    }

    @Bean
    public AuthorizationInterceptor authorizationInterceptor() {
        return new AuthorizationInterceptor();
    }

    @Bean
    public WebContentInterceptor webContentInterceptor() {
        WebContentInterceptor interceptor = new WebContentInterceptor();
        interceptor.setCacheSeconds(0);

        return interceptor;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000000);

        return  multipartResolver;
    }

    @Bean
    public ResourceHttpMessageConverter resourceHttpMessageConverter() {
        return new ResourceHttpMessageConverter();
    }

    @Bean
    RequiredOperationInterceptor requiredOperationInterceptor() {
        return new RequiredOperationInterceptor();
    }
    
    @Bean
    JpaResolver jpaResolver() {
        return new JpaResolver();
    }

}
