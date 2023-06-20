package com.nagarro.serviceb.controller;

import com.nagarro.serviceb.config.RabbitMQConfig;
import com.nagarro.serviceb.model.Message;
import com.nagarro.serviceb.model.Order;
import com.nagarro.serviceb.model.Payment;
import com.nagarro.serviceb.model.Shipment;
import com.nagarro.serviceb.service.OrderService;
import com.nagarro.serviceb.service.PaymentService;
import com.nagarro.serviceb.service.ShipmentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
    API 1 = placing order
    API 2 = processing payment
    API 3 = adding shipment
*/

@Component
public class ServiceBController {

    private final String SUCCESS = "SUCCESS";
    private final String FAILED = "FAILED";

    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private RabbitTemplate template;

    private void publishMessage(String routingKey, int orderId, String status) {
        Message message = new Message(orderId, status);
        template.convertAndSend(RabbitMQConfig.EXCHANGE, routingKey, message);
    }

    // API 1 request
    @RabbitListener(queues = "place-order-queue")
    public void handlePlaceOrderRequest(Message message) {
        int orderId = -1;
        String status = FAILED;
        try {
            Order order = orderService.createOrder();
            orderId = order.getOrderId();
            status = SUCCESS;
        } catch (Exception e) {
            System.out.println("ERROR creating order! Details: " + e.getMessage());
        } finally {
            publishMessage("place-order-response-routing-key", orderId, status);
        }
    }

    // API 2 request
    @RabbitListener(queues = "process-payment-queue")
    public void handleProcessPaymentRequest(Message message) {
        String status = FAILED;
        try {
            Payment payment = paymentService.createPayment(message);
            payment.getPaymentId();
            status = SUCCESS;
        } catch (Exception e) {
            System.out.println("ERROR processing payment! Details: " + e.getMessage());
        } finally {
            publishMessage("process-payment-response-routing-key", message.getOrderId(), status);
        }
    }

    // API 3 request
    @RabbitListener(queues = "add-shipment-queue")
    public void handleAddShipmentRequest(Message message) {
        String status = FAILED;
        try {
            Shipment shipment = shipmentService.addShipment(message);
            shipment.getShipmentStatus();
            status = SUCCESS;
        } catch (Exception e) {
            System.out.println("ERROR adding shipment! Details  : " + e.getMessage());
        } finally {
            publishMessage("add-shipment-response-routing-key", message.getOrderId(), status);
            orderService.setOrderToConfirm(message.getOrderId());
        }
    }

    // API 1 reversal request
    @RabbitListener(queues = "cancel-order-queue")
    public void handleCancelOrderRequest(Message message) {
        orderService.cancelOrder(message.getOrderId());
        publishMessage("cancel-order-response-routing-key", message.getOrderId(), SUCCESS);
    }

    // API 2 reversal request
    @RabbitListener(queues = "payment-reversal-queue")
    public void handlePaymentReversalRequest(Message message) {
        paymentService.cancelPayment(message.getOrderId());
        publishMessage("payment-reversal-response-routing-key", message.getOrderId(), SUCCESS);
    }
}
