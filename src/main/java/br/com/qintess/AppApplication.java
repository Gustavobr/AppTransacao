package br.com.qintess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@EnableMongoRepositories("br.com.qintess.repository")
//@ComponentScan({ "br.com.qintess.service" })
//@EntityScan("br.com.qintess.DTO")
@RestController
@RequestMapping("/")
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Api").version("1.1.0")
				.description("API de transação de pagamentos.").termsOfService("http://swagger.io/terms/")
				.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}
	
	
	
	/*
	 * public OpenAPI customOpenAPI(){
	 * 		return new OpenAPi().info(new Info().title("API").version(1.1.0")
	 * .description("Api teste").termsOfService("http...")
	 * .license(*new License().name("....").utl("...")));
	 * }
	 * 
	 */

}