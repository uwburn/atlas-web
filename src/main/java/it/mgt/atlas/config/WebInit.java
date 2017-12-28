package it.mgt.atlas.config;

import it.mgt.util.spring.web.auth.AuthRequestWrapperFilter;
import it.mgt.util.spring.web.cors.CorsFilter;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class WebInit implements WebApplicationInitializer {

    private final static Logger LOGGER = LoggerFactory.getLogger(WebInit.class);

    private static final String DISPATCHER_SERVLET_NAME = "dispatcher";
    private static final String CORS_FILTER_NAME = "cors";
    private static final String AUTH_REQUEST_WRAPPER_FILTER_NAME = "authRequestWrapper";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        registerListener(servletContext);
        List<Dynamic> filters = registerFilters(servletContext);
        registerDispatcherServlet(RestContext.class, servletContext, "/rest/*", filters);
        registerDispatcherServlet(RpcContext.class, servletContext, "/rpc/*", filters);
    }

    private void registerListener(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext rootContext;
        rootContext = createContext(BaseContext.class);
        rootContext.getEnvironment().setActiveProfiles("prod");
        servletContext.addListener(new ContextLoaderListener(rootContext));
    }
    
    private List<Dynamic> registerFilters(ServletContext servletContext) {
        List<Dynamic> filters = new ArrayList<>();
        CorsFilter corsFilter = corsFilter();
        filters.add(servletContext.addFilter(CORS_FILTER_NAME, corsFilter));
        AuthRequestWrapperFilter authRequestWrapperFilter = new AuthRequestWrapperFilter();
        filters.add(servletContext.addFilter(AUTH_REQUEST_WRAPPER_FILTER_NAME, authRequestWrapperFilter));
        return filters;
    }

    private void registerDispatcherServlet(Class<?> webContext, ServletContext servletContext, String mapping, List<Dynamic> filters) {
        AnnotationConfigWebApplicationContext dispatcherContext = createContext(webContext);

        ServletRegistration.Dynamic dispatcher;
        dispatcher = servletContext.addServlet(webContext.getSimpleName(), new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(mapping);
        
        filters.forEach((f) -> {
            f.addMappingForServletNames(null, true, webContext.getSimpleName());
        });
    }

    private AnnotationConfigWebApplicationContext createContext(final Class<?>... annotatedClasses) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(annotatedClasses);
        return context;
    }

    private CorsFilter corsFilter() {
        CorsFilter corsFilter = new CorsFilter();

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("atlas.properties");
        Properties props = new Properties();
        try {
            props.load(is);
        } catch (IOException e) {
            LOGGER.warn("Error properties file CORS filter, loading defaults", e);
        }

        String awoStr = props.getProperty("it.mgt.atlas.cors.allowWithoutOrigin", "false");
        String oaoStr = props.getProperty("it.mgt.atlas.cors.onlyAllowedOrigins", "true");
        String aoStr = props.getProperty("it.mgt.atlas.cors.allowedOrigins", "http://localhost:8080");

        boolean awo = Boolean.valueOf(awoStr);
        boolean oao = Boolean.valueOf(oaoStr);
        Set<String> origins = new HashSet<>();
        for (String origin : aoStr.split(","))
            origins.add(origin.trim());

        corsFilter.setAllowWithoutOrigin(awo);
        corsFilter.setOnlyAllowedOrigins(oao);
        corsFilter.setAllowedOrigins(origins);

        return corsFilter;
    }

}
