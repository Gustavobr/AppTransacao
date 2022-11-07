package br.com.qintess.security;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Profile("dev")
@EnableWebSecurity
public class DevSecurity extends WebSecurityConfigurerAdapter {

	protected void configure(HttpSecurity http) throws Exception {
		try {
			http.authorizeHttpRequests().antMatchers(HttpMethod.POST, "http://localhost:8080/api/**").permitAll();
			http.authorizeHttpRequests().antMatchers(HttpMethod.GET, "http://localhost:8080/api/").permitAll();
			http.authorizeHttpRequests().antMatchers(HttpMethod.DELETE, "http://localhost:8080/api/**").permitAll();
			http.authorizeHttpRequests().antMatchers(HttpMethod.POST, "http://localhost:8080/api/add").permitAll();
			// http.authorizeHttpRequests().anyRequest().hasRole("ADMIN").and()
			// .antMatcher("http://localhost:8080/api/add");
			/*
			 * http.authorizeHttpRequests()
			 * .antMatchers(org.springframework.http.HttpMethod.GET, "http://localhost:8080/
			 */// **").permitAll();
				// http.authorizeHttpRequests()
				// .antMatchers(org.springframework.http.HttpMethod.POST,
				// "http://localhost:8080/api/add").permitAll();
				// .hasAnyRole("USER", "ADMIN").and().build();
				// http.authorizeRequests().anyRequest().authenticated();
		} catch (SecurityException ex) {
			throw new SecurityException(ex.getCause());
		}

	}

}
