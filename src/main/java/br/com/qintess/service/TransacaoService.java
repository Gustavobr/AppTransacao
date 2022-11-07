package br.com.qintess.service;

import java.util.Collection;
import java.util.UUID;

import org.springframework.stereotype.Component;

import br.com.qintess.DTO.PagamentoDTO;
import br.com.qintess.DTO.TransacaoDTO;
@Component
//@ComponentScan(basePackageClasses = br.com.qintess.repository.TransacaoRepository.class)
public interface TransacaoService {
	public abstract void createTransacao(TransacaoDTO transacao);

	public abstract void deleteTransacao(TransacaoDTO transacao);

	public abstract Collection<TransacaoDTO> findAll();

	//public abstract void updateTransacao(UUID id, TransacaoDTO transacao);

	public  void updateTransacao(UUID id, PagamentoDTO pagamento);

	void deleteTransacao(UUID id);

	

}
