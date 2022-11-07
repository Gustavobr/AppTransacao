package br.com.qintess.repository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.qintess.DTO.PagamentoDTO;
@Repository
@Qualifier(value = "pagRepo")
public interface PagamentoRepository extends MongoRepository<PagamentoDTO, UUID> {

}
