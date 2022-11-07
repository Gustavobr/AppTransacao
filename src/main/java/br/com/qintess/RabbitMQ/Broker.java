package br.com.qintess.RabbitMQ;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.AbstractConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;

import com.rabbitmq.client.AMQP.Channel;
import com.rabbitmq.client.AMQP.Connection;

public class Broker {

	@Bean
	public static ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		// ConnectionFactory factory = new ConnectionFactory();
		connectionFactory.setAddresses(System.getenv("address"));

		connectionFactory.setUsername("user");
		connectionFactory.setPassword("qintess");
		return connectionFactory;
	}

	public static ConnectionFactory connection() {
		ConnectionFactory factory = (ConnectionFactory) new com.rabbitmq.client.ConnectionFactory();
		((AbstractConnectionFactory) factory).setHost("localhost");
		((AbstractConnectionFactory) factory).setHost("localhost");
		Connection connection = (Connection) factory.createConnection();
		com.rabbitmq.client.Channel channel = (com.rabbitmq.client.Channel) new Channel();
		return factory;
	}

	@Bean
	public org.springframework.amqp.core.Queue myQueue() {
		com.rabbitmq.client.ConnectionFactory client = (com.rabbitmq.client.ConnectionFactory) Broker
				.connectionFactory();
		return new org.springframework.amqp.core.Queue("MyQueue", false);
	}
	/* Consume our Message */

	@RabbitListener(queues = "myQueue")
	public void listen(String in) {
		System.out.println("Mensagem lida de myQueue :" + in);
	}

	/* Producer */

	public static void sendNotification() throws IOException, TimeoutException {
		try {
			ConnectionFactory factory = Broker.connection();

			com.rabbitmq.client.Channel channel = (com.rabbitmq.client.Channel) new Channel();
			channel.queueDeclare("queue_products", false, false, false, null);
			String message = "product added ";
			channel.basicPublish("", "queue_products", null, message.getBytes());
			System.out.println("Notification was sent.");
			if (factory.isPublisherConfirms() == true) {
				channel.close();
				factory.clearConnectionListeners();
			}

		} catch (IOException ex) {
			throw new IOException(ex.getCause());

		}
	}

	public static void main(String[] args) throws IOException, TimeoutException {
		System.out.println(Broker.connectionFactory().getUsername());
		RabbitTemplate rm = (RabbitTemplate) new RabbitTemplate().convertSendAndReceive("myQueue", "Minha execucao!");
		Broker.sendNotification();
	}
}
