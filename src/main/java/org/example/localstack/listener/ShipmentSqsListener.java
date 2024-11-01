package org.example.localstack.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipmentSqsListener {

//    @SqsListener(value = "shipment-sqs-queue")
//    public void receiveMessage(String message) {
//        log.info("Received message {}", message);
//    }
}
