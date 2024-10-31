package org.example.localstack.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.localstack.entity.Address;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsService {
    private final SqsAsyncClient sqsAsyncClient;
    private final ObjectMapper objectMapper;

    @Value("${shipment.sqs.queue.name}")
    private String shipmentQueueName;

    public void sendSqsMessage(Address address) {
        try {
//            String queueUrl = getQueueUrl(shipmentQueueName);
//            log.info("QueueUrl: {}", queueUrl);
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl("http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/shipment-sqs-queue")
                    .messageGroupId("Shipment")
                    .messageBody(objectMapper.writeValueAsString(address))
                    .messageDeduplicationId(UUID.randomUUID().toString())
                    .build();
            sqsAsyncClient.sendMessage(request);
            log.info("Sent message to queue {}", shipmentQueueName);
        } catch (JsonProcessingException e) {
            log.error("Error while sending message to queue {}", shipmentQueueName, e);
        }
    }

    private String getQueueUrl(String queueName) {
        return sqsAsyncClient.getQueueUrl(GetQueueUrlRequest
                        .builder()
                        .queueName(queueName)
                        .build())
                .thenApply(GetQueueUrlResponse::queueUrl)
                .join();
    }
}
