package weblog.config;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import weblog.service.WeblogUserDetailsService;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private WebApplicationContext applicationContext;
	
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private WeblogUserDetailsService userDetailsService;
    
    @PostConstruct
    public void completeSetup() {
        userDetailsService = applicationContext.getBean(WeblogUserDetailsService.class);
    }
    
   @Override
   protected void configure(HttpSecurity http) throws Exception {
/*      http
      	 .csrf().disable()
         .authorizeRequests()
            .antMatchers("/", "/webjars/**", "/css/**", "/js/**", "/images/**", "/location/**", "/rigctl/**", "/map").permitAll()
            .anyRequest().authenticated()
            .and()
         .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .permitAll();
*/
	      http
	      	 .csrf().disable()
	         .authorizeRequests()
	            .antMatchers("/", "/webjars/**", "/css/**", "/js/**", "/images/**", "/location/**", "/map", "/search/**").permitAll()
	         .and()
	            .httpBasic()
	            .realmName("Weblog API")
	         .and().authorizeRequests()
	            .anyRequest().authenticated()
	            .and()
	         .formLogin()
	            .loginPage("/login")
	            .permitAll()
	            .and()
	            .logout()
	            .permitAll();

   }
   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

      auth.userDetailsService(userDetailsService)
      .passwordEncoder(encoder())
      .and()
      .authenticationProvider(authenticationProvider())
      .jdbcAuthentication()
      .dataSource(dataSource);
/*      
         .inMemoryAuthentication()
         .withUser("user").password("{noop}password").roles("USER");
*/
   }
   
   @Bean
   public DaoAuthenticationProvider authenticationProvider() {
       final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       authProvider.setUserDetailsService(userDetailsService);
       authProvider.setPasswordEncoder(encoder());
       return authProvider;
   }
   
   @Bean
   public PasswordEncoder encoder() {
       return new BCryptPasswordEncoder(11);
   }
}