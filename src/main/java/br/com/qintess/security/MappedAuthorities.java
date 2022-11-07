package br.com.qintess.security;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.servlet.AuthorizeRequestsDsl;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@EnableWebSecurity
@Profile("UAT")
public class MappedAuthorities extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests(authorizeRequests -> authorizeRequests.mvcMatchers("/oidc-principal").permitAll()
				.anyRequest().authenticated()).oauth2Login(oauthLogin -> oauthLogin.permitAll())
				.logout((logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler())));

	}

	@Autowired(required = true)
	private ClientRegistrationRepository clientRegistrationRepository;

	private LogoutSuccessHandler oidcLogoutSuccessHandler() {
		
		OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(
				this.clientRegistrationRepository);
		oidcLogoutSuccessHandler.setPostLogoutRedirectUri(URI.create("http://localhost:8081/home")); // Oauth2ServerAuthrorization

		return oidcLogoutSuccessHandler;
		
	}
}
