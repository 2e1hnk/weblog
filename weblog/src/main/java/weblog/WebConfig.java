package weblog;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	   @Override
	   public void addViewControllers(ViewControllerRegistry registry) {
	      registry.addViewController("/home").setViewName("home");
	      //registry.addViewController("/").setViewName("index");
	      registry.addViewController("/test").setViewName("test");
	      registry.addViewController("/login").setViewName("login");
	      System.out.println(registry.toString());
	   }
	   
	   @Override
	   public void addResourceHandlers(ResourceHandlerRegistry registry) {
		   //registry.addResourceHandler("/**").addResourceLocations("/static/");
	   }
}
