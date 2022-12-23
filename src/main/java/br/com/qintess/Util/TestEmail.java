package br.com.qintess.Util;

import java.io.IOException;

import javax.mail.MessagingException;

import org.bouncycastle.openssl.PasswordException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestEmail {

	public TestEmail() {
		super();
	}

	private String[] list = EmailSender.list_address;
	// private static final UpdatableBCrypt bcrypt = new UpdatableBCrypt(11);

	public static String encoder(String password) throws PasswordException {
		String passCrypt = new BCryptPasswordEncoder().encode(password);
		return passCrypt;
	}

	public static void main(String[] args) throws IOException, Exception, MessagingException {
		EmailSender sender = new EmailSender();
		try {
			EmailSender.sendEmail(System.getenv("user_email"), TestEmail.encoder("user_password"),
					EmailSender.list_address);
			System.out.println("email enviado!");
		} catch (Exception e) {
			throw new javax.mail.AuthenticationFailedException();
		}
	}

}
