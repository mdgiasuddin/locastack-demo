package org.example.localstack.service;

import lombok.RequiredArgsConstructor;
import org.example.localstack.entity.Shipment;
import org.example.localstack.repository.DynamoDBService;
import org.example.localstack.repository.S3StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShipmentService {


    private final DynamoDBService dynamoDBService;
    private final S3StorageService s3StorageService;

    public List<Shipment> getAllShipments() {
        return dynamoDBService.getAllShipments();
    }

    public String deleteShipment(String shipmentId) {
        s3StorageService.delete(shipmentId);
        return dynamoDBService.delete(shipmentId);
    }

    public Shipment saveShipment(Shipment shipment) {
        return dynamoDBService.upsert(shipment);
    }

    public void removeImageLink(String shipmentId) {
        dynamoDBService.removeImageLink(shipmentId);
    }

    public void uploadShipmentImage(String shipmentId, MultipartFile file) {

        checkIfFileIsEmpty(file);

        Shipment shipment = getShipment(shipmentId);

        String path = shipment.getId();

        String fileName = String.format("%s-%s", UUID.randomUUID().toString().replace("-", ""), file.getOriginalFilename());
        try {
            s3StorageService.save(path, fileName, file);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        shipment.setImageLink(String.format("%s/%s", path, fileName));
        dynamoDBService.upsert(shipment);
    }


    public byte[] downloadShipmentImage(String shipmentId) throws IllegalStateException {
        Shipment shipment = dynamoDBService.getShipment(shipmentId).stream()
                .findFirst()
                .orElseThrow(
                        () -> new IllegalStateException(String.format("Shipment %s was not found.", shipmentId)));

        try {
            return Optional.ofNullable(shipment.getImageLink())
                    .map(link -> {
                        try {
                            return s3StorageService.download(link);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .orElse(Files.readAllBytes(new File("src/main/resources/placeholder.jpg").toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Shipment getShipment(String shipmentId) {
        return dynamoDBService.getShipment(shipmentId).stream()
                .findFirst()
                .orElseThrow(
                        () -> new IllegalStateException(String.format("Shipment %s was not found.", shipmentId)));
    }

    private void checkIfFileIsEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot save empty file to S3. File size: [" + file.getSize() + "]");
        }
    }

    public void updateImageLink(String shipmentId, String imageLink) {
        dynamoDBService.updateImageLink(shipmentId, imageLink);
    }
}
