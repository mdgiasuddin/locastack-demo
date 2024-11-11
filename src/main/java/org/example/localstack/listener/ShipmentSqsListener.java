package org.example.localstack.listener;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipmentSqsListener {

    @SqsListener(value = "shipment-sqs-queue.fifo")
    public void receiveMessage(String message) {
        log.info("Successfully received message: {}", message);
        log.info("Received message {}", message);
        log.info("Thanks");
    }
}
