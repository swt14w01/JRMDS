package jrmds.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebMvcSecurity
@ComponentScan(basePackageClasses=RegistredUserDetailsService.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	 @Autowired
	 private UserDetailsService userDetailsService;
	
	public SecurityConfiguration() {
		
	}	
	
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	            .authorizeRequests()
	                .antMatchers("/**").permitAll()
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
	    public void configureGlobal(/*UserDetailsService userDetailsService,*/ AuthenticationManagerBuilder auth) throws Exception {
	        //auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
	    	
	    	auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
	    }
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}	

