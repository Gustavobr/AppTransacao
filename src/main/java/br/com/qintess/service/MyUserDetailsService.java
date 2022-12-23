package br.com.qintess.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.qintess.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	//@Qualifier("UserRepository")
	private UserRepository repo;

	public UserDetails loadUserByUsername(String username, String password)
			throws UsernameNotFoundException, UserPrincipalNotFoundException {

		try {
			UserDetails login = (UserDetails) repo.findByUser(username, password);
			return login;
		} catch (Exception ex) {
			throw new UserPrincipalNotFoundException(username);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return null;
	}

}
