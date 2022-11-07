package br.com.qintess;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.com.qintess.DTO.PagamentoDTO;
import br.com.qintess.DTO.Tipo_Transacao;
import br.com.qintess.DTO.TransacaoDTO;
import br.com.qintess.pojo.Pagamento;
import br.com.qintess.repository.TransacaoRepository;
import br.com.qintess.service.PagamentoService;
import br.com.qintess.service.TransacaoService;

@SpringBootTest
@EnableMongoRepositories(basePackageClasses = br.com.qintess.repository.TransacaoRepository.class)
@ComponentScan(basePackages = { "br.com.qintess.repository" })
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
//@DataJpaTest
//@DataMongoTest
class AppApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired(required = true)
	private TransacaoRepository tranRepo;

	// @Autowired(required = false)

	// private TestEntityManager em;

	private TransacaoService tranService;
	private PagamentoService pagService;
	private static MongoClient mongo = MongoClients.create("mongodb://localhost:27017/api");
	private static MongoDatabase DB = mongo.getDatabase("api");
	private static MongoCollection collection = mongo.getDatabase("api").getCollection("Transacao");

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Test
	public void getAllTransacao() {
		Collection<?> list = tranService.findAll();
		ArrayList<Document> documents = new ArrayList<>();
		documents = (ArrayList<Document>) collection.find();
		Assert.isTrue(!documents.isEmpty() == true);
		ArrayList<Document> docs = new ArrayList<>();
		list.forEach(doc -> {
			TransacaoDTO transacao = new TransacaoDTO(Tipo_Transacao.BOLETO, null);
			if (transacao.getPagamento() != null) {
				PagamentoDTO pagamento = new PagamentoDTO("TESTE", 0, false);

				docs.add(new Document(transacao.getId().toString(), transacao.getTipo())
						.append("valor", pagamento.getValor())
						.append("estabelecimento", pagamento.getEstabelecimento()));
				Assert.isTrue(pagamento.getValor() >= 100.00, "Adimite apenas pagamentos acima de R$100.00");
			} else {
				docs.add(new Document(transacao.getId().toString(), transacao.getTipo()));
				assertNotNull(doc);

			}
		});

	}
}
