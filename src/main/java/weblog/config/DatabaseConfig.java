package weblog.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class DatabaseConfig {
	private final String CREATE_VIEWS_SQL = "classpath:create-views.sql";
	private final String DATA_DELAYED_SQL = "classpath:data-delayed.sql";
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private DataSource datasource;
	
    @Autowired
    private WebApplicationContext applicationContext;
	
	@PostConstruct
	public void loadViews() throws Exception {
		logger.info("Creating SQL Views");
		Resource resource = applicationContext.getResource(CREATE_VIEWS_SQL);
		ScriptUtils.executeSqlScript(datasource.getConnection(), resource);
	}

	@PostConstruct
	public void loadData() throws Exception {
		logger.info("Loading delayed data");
		Resource resource = applicationContext.getResource(DATA_DELAYED_SQL);
		ScriptUtils.executeSqlScript(datasource.getConnection(), resource);
	}
}
