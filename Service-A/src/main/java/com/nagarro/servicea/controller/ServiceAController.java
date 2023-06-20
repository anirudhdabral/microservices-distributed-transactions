package com.nagarro.servicea.controller;

import com.nagarro.servicea.config.RabbitMQConfig;
import com.nagarro.servicea.model.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    API 1 = placing order
    API 2 = processing payment
    API 3 = adding shipment
*/

@RestController
@Component
public class ServiceAController {
    @GetMapping("/start")
    public String start() {
        invokeAPIs();
        return "APIs called!";
    }

    private final String SUCCESS = "SUCCESS";
    private final String FAILED = "FAILED";

    @Autowired
    RabbitTemplate template;

    private void publishMessage(String routingKey, int orderId, String status) {
        Message message = new Message(orderId, status);
        template.convertAndSend(RabbitMQConfig.EXCHANGE, routingKey, message);
    }


    // invoking API 1
    public void invokeAPIs() {
        publishMessage("place-order-routing-key", -1, "");
    }

    // API 1 response, if SUCCESS invoking API 2
    @RabbitListener(queues = "place-order-response-queue")
    public void handlePlaceOrderResponse(Message message) {
        if (message.getStatus().equals(SUCCESS)) {
            publishMessage("process-payment-routing-key", message.getOrderId(), SUCCESS);
            System.out.println("Order created! Starting payment processing.");
        } else {
            System.out.println("Unable to place order!");
        }
    }

    // API 2 response, if SUCCESS invoking API 3 else reversing API 1
    @RabbitListener(queues = "process-payment-response-queue")
    public void handleProcessPaymentResponse(Message message) {
        String routingKey = "";
        if (message.getStatus().equals(SUCCESS)) {
            routingKey = "add-shipment-routing-key";
            System.out.println("Payment done! Adding to shipment.");
        } else {
            routingKey = "cancel-order-routing-key";
            System.out.println("Payment failed! Cancelling order.");
        }
        publishMessage(routingKey, message.getOrderId(), "");
    }

    // API 3 response, if FAILED reversing API 2
    @RabbitListener(queues = "add-shipment-response-queue")
    public void handleAddShipmentResponse(Message message) {
        if (message.getStatus().equals(SUCCESS)) {
            System.out.println("Shipment added!");
            System.out.println("Order placed successfully!");
        } else {
            System.out.println("Adding shipment failed! Initiating payment reversal.");
            publishMessage("payment-reversal-routing-key", message.getOrderId(), "");
        }
    }

    // confirming API 1 reversal
    @RabbitListener(queues = "cancel-order-response-queue")
    public void handleCancelOrderResponse(Message message) {
        System.out.println("Order cancelled!");
    }

    // reversal of API 1 after API 2 reversal
    @RabbitListener(queues = "payment-reversal-response-queue")
    public void handlePaymentReversalResponse(Message message) {
        System.out.println("Payment reversed! Cancelling order!");
        publishMessage("cancel-order-routing-key", message.getOrderId(), "");
    }
}
