package br.com.qintess.RabbitMQ;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.qintess.Util.QueueUtils;

//@Component
public class MQconfig implements QueueUtils {
	/*
	 * Binding
	 * DirectExchange
	 * Queue
	 * Message
	 * RoutingKey
	 */

	@Autowired
	private AmqpAdmin amqpAdmin;

	public org.springframework.amqp.core.Queue queue(String queueName) {
		return new org.springframework.amqp.core.Queue(QueueUtils.exchangeName, true, false, false);

	}

	private DirectExchange createDirectExchange() {
		return new DirectExchange(QueueUtils.exchangeName);
	}

	private org.springframework.amqp.core.Binding relate(org.springframework.amqp.core.Queue queue,
			DirectExchange exchange) {
		return new org.springframework.amqp.core.Binding(queue.getName(), DestinationType.QUEUE, exchange.getName(),
				queue.getName(), null);
	}

	@PostConstruct
	private void create() {
		org.springframework.amqp.core.Queue queue = queue(QueueUtils.queueName);

		DirectExchange directExchange = createDirectExchange();

		org.springframework.amqp.core.Binding relate = relate(queue, directExchange);
		
		
		amqpAdmin.declareQueue(queue);
		amqpAdmin.declareExchange(directExchange);
		amqpAdmin.declareBinding(relate);

	}
}
