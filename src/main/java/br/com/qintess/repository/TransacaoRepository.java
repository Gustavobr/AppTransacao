package br.com.qintess.repository;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.stereotype.Repository;

import br.com.qintess.DTO.TransacaoDTO;

@Repository
@Qualifier(value = "tranRepo")
@EnableReactiveMongoRepositories
@EnableMongoAuditing(modifyOnCreate = true)
public interface TransacaoRepository extends MongoRepository<TransacaoDTO, UUID> {

	
	//@Query(nativeQuery = true, value = "db.Tra")
	//void deleteDoc(String tipo, String estabelecimento);
	
}
