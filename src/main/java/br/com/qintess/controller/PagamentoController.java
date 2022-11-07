package br.com.qintess.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.azure.core.exception.HttpResponseException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import br.com.qintess.DTO.PagamentoDTO;
import br.com.qintess.DTO.TransacaoDTO;
import br.com.qintess.repository.TransacaoRepository;
import br.com.qintess.service.TransacaoServiceImpl;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api")
@EnableMongoRepositories(basePackageClasses = br.com.qintess.repository.TransacaoRepository.class)
@PropertySource("classpath:application.properties")
//@EnableCaching(mode = AdviceMode.ASPECTJ)
@ComponentScan(basePackageClasses = br.com.qintess.service.TransacaoServiceImpl.class)
public class PagamentoController {

	private static MongoClient mongo = MongoClients.create("mongodb://localhost:27017/api");
	private static MongoDatabase DB = mongo.getDatabase("api");
	private static MongoCollection collection = mongo.getDatabase("api").getCollection("Transacao");
	@Qualifier("tranRepo")
	@Autowired
	private TransacaoRepository tranRepo;

	// @Autowired(required = true)
	@Autowired(required = true)
	private TransacaoServiceImpl service;

	@ResponseBody
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@CacheEvict(allEntries = true)
	public Collection<Document> pagamentoEndpoint() {
		try {
			@SuppressWarnings("unchecked")
			Collection<Document> list = new ArrayList<>();
			FindIterable<Document> count = DB.getCollection("Transacao").find();
			if (count != null) {
				count.forEach(document -> {
					list.add(document);
				});
			}
			return list;
		} catch (RuntimeException e) {
			System.out.println(e.getCause());
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@ResponseBody
	public ResponseEntity<TransacaoDTO> addTransacao(@RequestBody(description = "transacao") TransacaoDTO transacao)
			throws RequestRejectedException, Exception {
		try {
			if (transacao != null && !transacao.toString().isEmpty() == true) {
				service.createTransacao(transacao);
				return ResponseEntity.status(HttpStatus.CREATED).build();
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (HttpResponseException ex) {
			throw new RuntimeException(ex.getCause());
		}

	}

	@DeleteMapping(value = "/api/transacao/delete/{id}", produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public ResponseEntity<TransacaoDTO> deleteTransacao(@PathVariable(required = true, value = "id") UUID id)
			throws Exception {
		try {
			TransacaoDTO transacao = tranRepo.findById(id).orElseThrow();
			if (transacao != null) {
				tranRepo.delete(transacao);
				return ResponseEntity.status(HttpStatus.OK).build();
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {

		}
		return null;
	}

	@Transactional
	@PostMapping(value = "/update/{id}")
	public ResponseEntity<TransacaoDTO> updateTransacao(@PathVariable(required = true, value = "id") UUID id)
			throws Exception {
		if (id != null && !tranRepo.findById(id).get().toString().isEmpty() == true) {
			TransacaoDTO transacao = tranRepo.findById(id).get();
			service.deleteTransacao(transacao);
			new ResponseEntity<>(HttpStatus.ACCEPTED);
			return ResponseEntity.accepted().build();
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/api/transacao/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@CacheEvict(allEntries = true)
	public Document getDocumentTransacao(@PathVariable(name = "ID", required = true) UUID id) throws Exception {
		try {
			if (id != null) {
				Document doc = new Document();
				TransacaoDTO transacao = tranRepo.findById(id).get();
				if (transacao != null) {
					doc.append("id", transacao.getId().toString());
					doc.append("tipo_transacao", transacao.getTipo());
					if (doc.containsKey("pagamento") == false) {
						doc.append("pagamento", new PagamentoDTO("Casas Bahia", 1500.00, false));
					} else {
						return doc;
					}

				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getCause());
		}
		return null;

	}

}
