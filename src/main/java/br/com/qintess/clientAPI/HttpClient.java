package br.com.qintess.clientAPI;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.test.BeforeOAuth2Context;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.JsonObject;

public class HttpClient {
	private static java.net.http.HttpClient cliente;

	final static Logger log = LogManager.getLogger(HttpClient.class);

	public HttpClient() {

	}

	@Autowired
	private static Environment env;

	private static String CLIENT_PROPERTY_KEY = "security.oauth2.client.client-id";
	private static String PASSWORD_PROPERTY_KEY = "security.oauth2.client.client-secret";
	// asyncronous operations no blocking reactive programming WebClient Spring -
	// 5.0
	/* Flux streams of elements */
	/* Mono a specific, but very common type */

	@Value("${security.oauth2.client.client-id}")
	static String clientId;
	@Value("${security.oauth2.client.client-secret}")
	static String clientSecret;

	public static String getAccessToken(String clientId, String clientSecret) throws Exception {
		log.info("retrieving tokens");
		clientId = System.getProperty(clientSecret);
		clientSecret = System.getProperty(clientSecret);
		HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8080/oauth/token"))
				.header("Authorization", " Basic").GET().build();
		HttpResponse<String> response = cliente.send(request, BodyHandlers.ofString());

		String accessToken = response.body();
		com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
		JsonObject obj = parser.parse(accessToken).getAsJsonObject();
		accessToken = obj.get("access_token").getAsString();
		return accessToken;

	}

	public static void main(String[] args) throws Exception {
		try {
			String clientId = env.getProperty(CLIENT_PROPERTY_KEY);
			String clientSecret = env.getProperty(PASSWORD_PROPERTY_KEY);
			WebClient client = WebClient.builder().baseUrl("http://localhost:8080").build();
			SecurityContext security = SecurityContextHolder.createEmptyContext();

			Authentication authentication = new TestingAuthenticationToken(clientId, clientSecret, "ROLE_USER");

			security.setAuthentication(authentication);
			SecurityContextHolder.setContext(security);

			WebClient.ResponseSpec responseSpec = client.get().uri("http://localhost:8080/oauth/token")
					.attribute("Authorization", "Basic ").attribute(clientId, "").attribute(clientSecret, "")
					.retrieve();
			String responseBody = responseSpec.bodyToMono(String.class).block(Duration.ofMinutes(1));
		} catch (Exception e) {

		}
	}

}
