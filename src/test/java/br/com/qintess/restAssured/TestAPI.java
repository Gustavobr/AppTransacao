package br.com.qintess.restAssured;

import java.net.URI;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;


@AutoConfigureDataMongo
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TestAPI {
	
	
	@Autowired
	private MockMvc mvcMock;

	private static final String ENDPOINT = "http://localhost:8080/api/all";
	private static final String ENDPOINT_FIRST_DOCUMENT = "http://localhost:8080/api/first";

	@Test

	public void getAllDocuments() {
		RestAssured.given(null).when().get(ENDPOINT).then().statusCode(200).and().contentType(ContentType.JSON);
	}

	@Test

	public void getFirstDocument() {
		try {
			RestAssured.given().when().get(URI.create(ENDPOINT_FIRST_DOCUMENT)).then().statusCode(200);
		} catch (Exception ex) {
		}
	}
}
