package com.bilgeadam.enterprise.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
	public static final String PRIVATE_QUEUE = "private.queue";
	public static final String GROUP_QUEUE = "group.queue";
	public static final String MESSAGE_EXCHANGE = "chatExchange";
	public static final String ROUTING_KEY = "chat.routingKey";
	
	@Bean
	public TopicExchange chatExchange() {
		return new TopicExchange(MESSAGE_EXCHANGE);
	}
	
	@Bean
	public Queue privateQueue() {
		return new Queue(PRIVATE_QUEUE, true);
	}
	
	@Bean
	public Queue groupQueue() {
		return new Queue(GROUP_QUEUE, true);
	}
	
	@Bean
	public Binding privateBinding(Queue privateQueue, TopicExchange chatExchange) {
		return BindingBuilder.bind(privateQueue).to(chatExchange).with("private.message");
	}
	
	@Bean
	public Binding groupBinding(Queue groupQueue, TopicExchange chatExchange) {
		return BindingBuilder.bind(groupQueue).to(chatExchange).with("group.message");
	}
}