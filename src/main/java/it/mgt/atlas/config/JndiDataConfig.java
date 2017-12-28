package it.mgt.atlas.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

@Configuration
public class JndiDataConfig {
	
	@Bean
	public DataSource jndiDatasource() {
		JndiDataSourceLookup dsl = new JndiDataSourceLookup();
		return dsl.getDataSource("jdbc/atlas");
	}

}
