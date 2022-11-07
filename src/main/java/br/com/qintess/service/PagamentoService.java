package br.com.qintess.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.qintess.DTO.PagamentoDTO;
import br.com.qintess.DTO.TransacaoDTO;

@Component
public interface PagamentoService {

	public abstract void createPagamento(TransacaoDTO transacao);

	public abstract void deletePagamento(UUID id);

	public abstract Collection<TransacaoDTO> findAll();

	// public abstract void updateTransacao(UUID id, TransacaoDTO transacao);

	public void updateTransacao(UUID id, PagamentoDTO pagamento);

	void createPagamento(PagamentoDTO transacao);
}
