package com.nagarro.serviceb.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "microservicesAB-exchange";

    // Queues

    @Bean
    public Queue queueAddShipment() {
        return new Queue("add-shipment-queue");
    }

    @Bean
    public Queue queueAddShipmentResponse() {
        return new Queue("add-shipment-response-queue");
    }

    @Bean
    public Queue queueCancelOrder() {
        return new Queue("cancel-order-queue");
    }

    @Bean
    public Queue queueCancelOrderResponse() {
        return new Queue("cancel-order-response-queue");
    }

    @Bean
    public Queue queuePaymentReversal() {
        return new Queue("payment-reversal-queue");
    }

    @Bean
    public Queue queuePaymentReversalResponse() {
        return new Queue("payment-reversal-response-queue");
    }

    @Bean
    public Queue queuePlaceOrder() {
        return new Queue("place-order-queue");
    }

    @Bean
    public Queue queuePlaceOrderResponse() {
        return new Queue("place-order-response-queue");
    }

    @Bean
    public Queue queueProcessPayment() {
        return new Queue("process-payment-queue");
    }

    @Bean
    public Queue queueProcessPaymentResponse() {
        return new Queue("process-payment-response-queue");
    }


    // Binding queues with respective routing
    @Bean
    public Binding bindingAddShipment(TopicExchange exchange) {
        return BindingBuilder.bind(queueAddShipment()).to(exchange)
                .with("add-shipment-routing-key");
    }

    @Bean
    public Binding bindingAddShipmentResponse(TopicExchange exchange) {
        return BindingBuilder.bind(queueAddShipmentResponse()).to(exchange)
                .with("add-shipment-response-routing-key");
    }

    @Bean
    public Binding bindingCancelOrder(TopicExchange exchange) {
        return BindingBuilder.bind(queueCancelOrder()).to(exchange)
                .with("cancel-order-routing-key");
    }

    @Bean
    public Binding bindingCancelOrderResponse(TopicExchange exchange) {
        return BindingBuilder.bind(queueCancelOrderResponse()).to(exchange)
                .with("cancel-order-response-routing-key");
    }

    @Bean
    public Binding bindingPaymentReversal(TopicExchange exchange) {
        return BindingBuilder.bind(queuePaymentReversal()).to(exchange)
                .with("payment-reversal-routing-key");
    }

    @Bean
    public Binding bindingPaymentReversalResponse(TopicExchange exchange) {
        return BindingBuilder.bind(queuePaymentReversalResponse()).to(exchange)
                .with("payment-reversal-response-routing-key");
    }

    @Bean
    public Binding bindingPlaceOrder(TopicExchange exchange) {
        return BindingBuilder.bind(queuePlaceOrder()).to(exchange)
                .with("place-order-routing-key");
    }

    @Bean
    public Binding bindingPlaceOrderResponse(TopicExchange exchange) {
        return BindingBuilder.bind(queuePlaceOrderResponse()).to(exchange)
                .with("place-order-response-routing-key");
    }

    @Bean
    public Binding bindingProcessPayment(TopicExchange exchange) {
        return BindingBuilder.bind(queueProcessPayment()).to(exchange)
                .with("process-payment-routing-key");
    }

    @Bean
    public Binding bindingProcessPaymentResponse(TopicExchange exchange) {
        return BindingBuilder.bind(queueProcessPaymentResponse()).to(exchange)
                .with("process-payment-response-routing-key");
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
