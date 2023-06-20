package com.nagarro.serviceb.service;

import com.nagarro.serviceb.model.Message;
import com.nagarro.serviceb.model.Shipment;
import com.nagarro.serviceb.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {
    @Autowired
    private ShipmentRepository shipmentRepository;

    public Shipment addShipment(Message message) {
        Shipment shipment = new Shipment();
        shipment.setCity("Kanpur");
        shipment.setOrderId(message.getOrderId());
        shipment.setShipmentStatus("in_transit");
        return shipmentRepository.save(shipment);
//        return null;
    }
}
