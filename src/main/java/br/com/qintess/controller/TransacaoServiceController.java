package br.com.qintess.controller;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.qintess.DTO.PagamentoDTO;
import br.com.qintess.repository.PagamentoRepository;
import br.com.qintess.service.PagamentoService;

@RestController
@RequestMapping("/api/transacao")
@EnableMongoRepositories(basePackageClasses = br.com.qintess.repository.PagamentoRepository.class)
@EnableReactiveMongoRepositories
public class TransacaoServiceController {

	@Autowired(required = false)

	private PagamentoService service;

	@RequestMapping(value = "/api/transacao/pagamentos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@Transactional
	@CacheEvict(allEntries = true)
	public Collection<PagamentoDTO> allPagamentos() throws Exception, IOException {

		return null;
	}

}