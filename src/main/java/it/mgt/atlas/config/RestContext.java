package it.mgt.atlas.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WebContext.class)
@ComponentScan(basePackages = {"it.mgt.atlas.rest.controller"})
public class RestContext {

}
