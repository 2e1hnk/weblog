package weblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableScheduling
public class Application {
 
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    // Provide multi-tenancy support
    // https://bytefish.de/blog/spring_boot_multitenancy/
}