package jrmds.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security Configuration.
 * @author Leroy Buchholz
 *
 */
@Configuration
@EnableWebMvcSecurity
@ComponentScan(basePackageClasses=RegistredUserDetailsService.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private RegistredUserDetailsService RegistredUserDetailsService;
	
	public SecurityConfiguration() {
		
	}	
	
	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure
	 * (org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
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
	 
	
	/**
	 * Cofiguration for getting Registred Users with the UserDetailsService from the repository to login them.
	 */
	@Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(this.RegistredUserDetailsService).passwordEncoder(passwordEncoder());
	    }
	
	/**
	 * A Bean witch creates an password encoder.
	 * @return encoder to encode passwords.
	 */
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}	

