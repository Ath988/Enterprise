package com.bilgeadam.enterprise.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	public static final String MESSAGE_QUEUE = "chatMessageQueue";
	public static final String MESSAGE_EXCHANGE = "chatExchange";
	public static final String ROUTING_KEY = "chat.routingKey";
	
	@Bean
	public Queue queue() {
		return new Queue(MESSAGE_QUEUE, true);
	}
	
	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(MESSAGE_EXCHANGE);
	}
	
	@Bean
	public Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
	}
}