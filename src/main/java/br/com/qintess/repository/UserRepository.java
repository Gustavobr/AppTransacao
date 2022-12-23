package br.com.qintess.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import br.com.qintess.DTO.Login;

@Repository
@Component
@Qualifier
public interface UserRepository extends MongoRepository<Login, String> {

	@Query("{'username': ?0, 'password': ?1}")
	UserDetails findByUser(String username, String password);

}
