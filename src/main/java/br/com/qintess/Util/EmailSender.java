package br.com.qintess.Util;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.nio.channels.UnresolvedAddressException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.springframework.messaging.MessagingException;

public class EmailSender {

	public static String[] list_address = { "gustavo.rodrigues@qintess.com" };

	public static void sendEmail(String user_email, String user_password, String[] recipients)
			throws Exception, IOException {
		if (user_email != null && user_password != null && recipients.length >= 1) {
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "outlook.office365.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getDefaultInstance(props, new Authenticator() {
				protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
					return new javax.mail.PasswordAuthentication(user_email, user_password);
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user_email));
			message.setRecipients(RecipientType.TO, InternetAddress.parse(user_email.toString()));
			message.setSubject("---========Email test----==============");

			String msg = "<h3> Test Email </h3>";

			MimeBodyPart mime = new MimeBodyPart();
			mime.setContent(msg, "text/html; charset=utf-8");

			Multipart part = new MimeMultipart();

			part.addBodyPart(mime);

			message.setContent(part);

			Transport.send(message);

			System.out.println("Email enviado!");
		}
	}

	public static void main(String[] args)
			throws UnresolvedAddressException, MessagingException, AddressException, javax.mail.MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "outlook.office365.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(System.getenv("user_email"),
						System.getenv("user_password"));

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
