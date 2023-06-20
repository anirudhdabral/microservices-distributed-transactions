package com.nagarro.serviceb.repository;

import com.nagarro.serviceb.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
}
