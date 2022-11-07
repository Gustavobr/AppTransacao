package br.com.qintess.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Profile(value= {"prod", "test"})
@EnableWebSecurity(debug = false)
@EnableResourceServer
@EnableAuthorizationServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class Security extends WebSecurityConfigurerAdapter {

	@Bean

	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	/*
	 * 
	 * private AuthenticattionManager authenticationManager() throws Exception{
	 * 	return super.authenticationManager();
	 * 
	 * 
	 * protected void configure(AuthenticationManagerBuilder auth)throws Exception, SecurityException{
	 * a	auth.inMemoryAuthentication().withUser("user").password("123").roles("ADMIN");
	 */

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("123").roles("ADMIN");
		
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}
