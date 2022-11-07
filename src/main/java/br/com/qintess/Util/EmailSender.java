package br.com.qintess.Util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

public class EmailSender {

	private static String[] list_address = { "sac@qintess.com", "contasareceber@qintess.com" };

	public static void main(String[] args) throws AddressException, MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.mailtrap.io");
		props.put("mail.smtp.port", "25");
		props.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(System.getenv("user_email"), System.getenv("user_password"));

			}

		});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("qintess@qintess.com"));
		message.setRecipients(RecipientType.TO, InternetAddress.parse(list_address.toString()));
		message.setSubject("---==== Contas a Receber--======");

		String msg = "";
		MimeBodyPart mime = new MimeBodyPart();

		mime.setContent(msg, "text/html; charset=utf-8");
		Multipart part = new MimeMultipart();

		part.addBodyPart(mime);

		message.setContent(part);

		Transport.send(message);

	};

}
