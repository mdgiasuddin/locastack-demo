package org.example.localstack.repository;

import lombok.RequiredArgsConstructor;
import org.example.localstack.entity.Shipment;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DynamoDBService {

    private final DynamoDbTable<Shipment> shipmentTable;

    public Shipment upsert(Shipment shipment) {
        if (Objects.isNull(shipment.getId())) {
            shipmentTable.putItem(shipment);
        } else {
            shipmentTable.updateItem(shipment);
        }
        return shipment;
    }

    public Optional<Shipment> getShipment(String shipmentId) {
        return Optional.ofNullable(
                shipmentTable.getItem(Key.builder().partitionValue(shipmentId).build()));
    }

    public String delete(String shipmentId) {
        shipmentTable.deleteItem(Key.builder().partitionValue(shipmentId).build());

        return "Shipment has been deleted";
    }

    public List<Shipment> getAllShipments() {
        ScanEnhancedRequest request = ScanEnhancedRequest.builder().build();
        SdkIterable<Shipment> shipments = shipmentTable.scan(request).items();
        return shipments.stream().toList();
    }

    public void removeImageLink(String shipmentId) {
        Optional.ofNullable(shipmentTable.getItem(Key.builder().partitionValue(shipmentId).build()))
                .ifPresent(shipment -> shipment.setImageLink(null));
    }

    public void updateImageLink(String shipmentId, String message) {
        Optional.ofNullable(shipmentTable.getItem(Key.builder().partitionValue(shipmentId).build()))
                .ifPresent(shipment -> {
                    shipment.setImageLink(message);
                    shipmentTable.updateItem(shipment);
                });

    }
}
