package it.mgt.atlas.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import it.mgt.uti.jpajsonsearch.JpaJsonSearchFactory;
import it.mgt.util.json2jpa.Json2JpaFactory;
import it.mgt.util.spring.config.EntityPackage;
import it.mgt.util.spring.config.EntityPackageAwareConfiguration;
import it.mgt.util.spring.log4j.Log4JRefresh;
import it.mgt.util.spring.meta.ProjectMetaSvcImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.http.converter.json.SpringHandlerInstantiator;
import it.mgt.util.spring.meta.ProjectMetaSvc;

@Configuration
@ComponentScan(basePackages = {"it.mgt.atlas.repository.impl", "it.mgt.atlas.service.impl", "it.mgt.atlas.task"})
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@Import({JndiDataConfig.class})
@PropertySources({
    @PropertySource("classpath:jpa.properties"),
    @PropertySource("classpath:atlas.properties")})
@EntityPackage("it.mgt.atlas.entity")
public class BaseContext extends EntityPackageAwareConfiguration {
    
    @Autowired
    private DataSource dataSource;

    @Autowired
    private Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.setThreadNamePrefix("Spring ThreadPoolTaskScheduler-");

        return taskScheduler;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setThreadNamePrefix("Spring ThreadPoolTaskExecutor-");

        return taskExecutor;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(getEntityPackages());
        em.setJpaVendorAdapter(jpaVendorAdapter());
        em.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", env.getProperty("it.mgt.atlas.hibernate.hbm2ddl.auto"));
        em.getJpaPropertyMap().put("hibernate.physical_naming_strategy", env.getProperty("it.mgt.atlas.hibernate.physical_naming_strategy"));
        em.getJpaPropertyMap().put("hibernate.id.new_generator_mappings", env.getProperty("it.mgt.atlas.hibernate.id.new_generator_mappings"));

        String dialect = env.getProperty("it.mgt.atlas.hibernate.dialect");
        if (dialect != null)
            em.getJpaPropertyMap().put("hibernate.dialect", dialect);

        String storageEngine = env.getProperty("it.mgt.atlas.hibernate.dialect.storage_engine");
        if (storageEngine != null)
            em.getJpaPropertyMap().put("hibernate.dialect.storage_engine", storageEngine);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        AnnotationIntrospector primary = new JacksonAnnotationIntrospector();
        AnnotationIntrospector secondary = new JaxbAnnotationIntrospector(mapper.getTypeFactory());
        AnnotationIntrospector pair = new AnnotationIntrospectorPair(primary, secondary);

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setAnnotationIntrospector(pair);
        mapper.setHandlerInstantiator(new SpringHandlerInstantiator(applicationContext.getAutowireCapableBeanFactory()));

        return mapper;
    }

    @Bean
    public MailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("it.mgt.atlas.mail.host"));
        mailSender.setUsername(env.getProperty("it.mgt.atlas.mail.username"));
        mailSender.setPassword(env.getProperty("it.mgt.atlas.mail.password"));

        return mailSender;
    }

    @Bean
    public SimpleMailMessage mailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(env.getProperty("it.mgt.atlas.mail.from"));

        return simpleMailMessage;
    }

    @Bean
    public ProjectMetaSvc versionService() {
        return new ProjectMetaSvcImpl("meta.properties");
    }

    @Bean
    public Json2JpaFactory json2JpaFactory() {
        return new Json2JpaFactory();
    }

    @Bean
    public JpaJsonSearchFactory jpaJsonSearchFactory() {
        return new JpaJsonSearchFactory();
    }

}
