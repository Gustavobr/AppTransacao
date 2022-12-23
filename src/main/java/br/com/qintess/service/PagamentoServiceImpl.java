package br.com.qintess.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.qintess.DTO.PagamentoDTO;
import br.com.qintess.DTO.TransacaoDTO;
import br.com.qintess.repository.PagamentoRepository;
import br.com.qintess.repository.TransacaoRepository;

@Component
public class PagamentoServiceImpl implements PagamentoService {

	@Autowired(required = false)

	private TransacaoRepository tranRepo;
	private PagamentoRepository pagRepo;

	@Override
	public void createPagamento(@RequestBody TransacaoDTO transacao) {
		if (transacao.getTipo().toString().isEmpty() == true || transacao.getPagamento().toString().isEmpty() == true) {

		} else {

			tranRepo.insert(transacao);

		}

	}

	@Override
	public void deletePagamento(UUID id) {
		if (tranRepo.findById(id).isPresent() == true) {
			TransacaoDTO tran = null;
			tran = tranRepo.findById(id).get();
			tranRepo.delete(tran);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<TransacaoDTO> findAll() {

		return (Collection<TransacaoDTO>) tranRepo.findAll(Sort.by(Direction.ASC, "tipo_pagamento")).stream()
				.collect(Collectors.toList());

	}

	@Override
	public void updateTransacao(UUID id, PagamentoDTO pagamento) {

	}

	@Override
	public void createPagamento(PagamentoDTO transacao) {

	}

}
