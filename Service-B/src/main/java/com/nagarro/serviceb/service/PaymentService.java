package com.nagarro.serviceb.service;

import com.nagarro.serviceb.model.Payment;
import com.nagarro.serviceb.model.Message;
import com.nagarro.serviceb.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(Message message) {
        Payment payment = new Payment();
        payment.setOrderId(message.getOrderId());
        payment.setTransactionCode("J7BGX5@PAV#I&2NK");
        return paymentRepository.save(payment);
//        return null;
    }

    @Transactional
    public void cancelPayment(int orderId) {
        paymentRepository.deleteByOrderId(orderId);
    }
}
