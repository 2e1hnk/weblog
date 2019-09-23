package weblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import weblog.config.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@ComponentScan(basePackages= {"weblog", "QRZClient2", "DXCluster"})
@EnableScheduling
public class Application {
 
    public static final String DEFAULT_TENANT_ID = "default";

	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    // Provide multi-tenancy support
    // https://bytefish.de/blog/spring_boot_multitenancy/

/*
    @Bean
    public DataSource dataSource() {

        AbstractRoutingDataSource dataSource = new TenantAwareRoutingSource();

        Map<Object,Object> targetDataSources = new HashMap<>();

        targetDataSources.put(null, defaultTenant());
        targetDataSources.put("2E1HNK", tenantOne());
        targetDataSources.put("GB1WSG", tenantTwo());

        dataSource.setTargetDataSources(targetDataSources);

        dataSource.afterPropertiesSet();

        return dataSource;
    }

    public DataSource defaultTenant() {

        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setInitializationFailTimeout(0);
        dataSource.setMaximumPoolSize(5);
        //dataSource.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        dataSource.addDataSourceProperty("url", "jdbc:mysql://localhost:3306/weblog.2e1hnk?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/London");
        //dataSource.addDataSourceProperty("user", "root");
        //dataSource.addDataSourceProperty("password", "test_pwd");

        return dataSource;
    }

    public DataSource tenantOne() {

        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setInitializationFailTimeout(0);
        dataSource.setMaximumPoolSize(5);
        //dataSource.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        dataSource.addDataSourceProperty("url", "jdbc:mysql://localhost:3306/weblog.2e1hnk?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/London");
        //dataSource.addDataSourceProperty("user", "root");
        //dataSource.addDataSourceProperty("password", "test_pwd");

        return dataSource;
    }

    public DataSource tenantTwo() {

        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setInitializationFailTimeout(0);
        dataSource.setMaximumPoolSize(5);
        //dataSource.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        dataSource.addDataSourceProperty("url", "jdbc:mysql://localhost:3306/weblog.gb1wsg?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/London");
        //dataSource.addDataSourceProperty("user", "philipp");
        //dataSource.addDataSourceProperty("password", "test_pwd");

        return dataSource;
    }
*/
}