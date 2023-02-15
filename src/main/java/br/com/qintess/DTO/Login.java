package br.com.qintess.DTO;

import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.nimbusds.jose.util.Base64;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Setter
@Getter

/* Pojo class Login */
public class Login {

	public Login() {

	}

	private String user;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	private String _id;

	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Login(String _id, String username, String password) {
		super();
		this._id = _id;
		this.username = username;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String username;

	private String password;

	@Bean
	public static PasswordEncoder encoder(String pass) {
		return (PasswordEncoder) Base64.encode(pass);
	}

}
