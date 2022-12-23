package br.com.qintess.unitTests;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.bson.Document;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.web.servlet.MockMvc;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.com.qintess.DTO.Login;

@EnableAutoConfiguration
@AutoConfigureMockMvc(webClientEnabled = true, webDriverEnabled = true)
@EnableMongoRepositories
class LoginTest {

	@Autowired
	private MockMvc mockMvc;

	private static MongoClient mongo = MongoClients.create("mongodb://localhost:27017/api");
	private static MongoDatabase DB = mongo.getDatabase("api");
	private static MongoCollection<Document> userCollection = mongo.getDatabase("api").getCollection("user");
	private static final String URL_LOGIN = "http://localhost:8080/login";

	@Test

	void TestConnection() {
		MongoClient connection = MongoClients.create("mongodb://localhost:27017/api");
		org.junit.Assert.assertEquals(mongo, connection);
		assertNotNull(connection);
	}

	@Test

	void testValidateLogin(Login login) {
		try {
			mockMvc.perform(post(URL_LOGIN).contentType("application/json")).andExpect(result -> status().isOk());
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
