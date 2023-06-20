package com.nagarro.serviceb.service;

import com.nagarro.serviceb.model.Order;
import com.nagarro.serviceb.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder() {
        Order order = new Order();
        order.setOrderStatus("PENDING");
        return orderRepository.save(order);
//    return null;
    }

    public void cancelOrder(int orderId) {
        orderRepository.deleteById(orderId);
    }

    public void setOrderToConfirm(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(NoSuchElementException::new);
        order.setOrderStatus("CONFIRMED");
        orderRepository.save(order);
    }


}
