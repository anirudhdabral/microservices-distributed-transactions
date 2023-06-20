package com.nagarro.serviceb.repository;

import com.nagarro.serviceb.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    void deleteByOrderId(int orderId);
}
