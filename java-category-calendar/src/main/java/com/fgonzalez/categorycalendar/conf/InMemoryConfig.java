package com.fgonzalez.categorycalendar.conf;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class InMemoryConfig {
    private final String SAMPLE_DATA = "classpath:data.sql";
	
	@Autowired
	private DataSource datasource;

    @Autowired
    private WebApplicationContext webApplicationContext;
	
	@PostConstruct
	public void loadIfInMemory() throws Exception {
		Resource resource = webApplicationContext.getResource(SAMPLE_DATA);
		ScriptUtils.executeSqlScript(datasource.getConnection(), resource);
	}
}