
package br.com.qintess.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.com.qintess.DTO.PagamentoDTO;
import br.com.qintess.DTO.Tipo_Transacao;
import br.com.qintess.DTO.TransacaoDTO;
import br.com.qintess.repository.TransacaoRepository;
import br.com.qintess.service.TransacaoService;

@SpringBootTest
//@RunWith(SpringRunner.class)
@ActiveProfiles({ "test", "prod" })
@EnableMongoRepositories
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureMockMvc
public class PagamentoControllerTest {

	@Mock
	private TransacaoService service;

	private static MongoClient mongo = MongoClients.create("mongodb://localhost:27017/api");
	private static MongoDatabase DB = mongo.getDatabase("api");
	private static MongoCollection collection = mongo.getDatabase("api").getCollection("Transacao");
	private static final String ENDPOINT_ADD = "http://localhost:8080/add";
	@Qualifier("tranRepo")

	@Autowired
	private MockMvc mockMvc;

	private static Gson gson;
	@Autowired

	private TransacaoRepository tranRepo;

	@Test
	public void retrievelAllPagamentosTest() {
		Collection<TransacaoDTO> list = tranRepo.findAll();
		assertNotNull(list);
		assertEquals(tranRepo.findAll(), list);

	}

	@Test
	public void generateTransacaoTest() throws Exception, IOException {
		TransacaoDTO transacao = new TransacaoDTO(Tipo_Transacao.TRANSFERENCIA,
				new PagamentoDTO("EXTRA", 600.00, false));
		String jsonBody = gson.toJson(transacao);
		mockMvc.perform(MockMvcRequestBuilders.post(URI.create(ENDPOINT_ADD)).content(jsonBody.toString())
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.status().is(400));
		/*
		 * Mockmvc.perform(MockMvcRequestBuilders.post(URI.create(url).content(jsonBody.
		 * toString())
		 * .contentType(MediaTYpe.application/json).andExpect(MockMvcResultMatchers.
		 * status(0.is(400));
		 */
	}

}
