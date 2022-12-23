package br.com.qintess.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.stereotype.Repository;

import br.com.qintess.DTO.Login;

@Repository
@EnableMongoRepositories
@EnableReactiveMongoRepositories
public interface LoginRepository extends MongoRepository<Login, Long> {

	
	
}
