package org.example.localstack.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.localstack.entity.Shipment;
import org.example.localstack.service.ShipmentService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RestController
@RequestMapping("api/shipment")
@RequiredArgsConstructor
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping
    public List<Shipment> getAllShipments() {
        return shipmentService.getAllShipments();
    }

    @GetMapping(path = "{shipmentId}/image/download", produces = IMAGE_JPEG_VALUE)
    public byte[] downloadShipmentImage(@PathVariable("shipmentId") String shipmentId) {
        return shipmentService.downloadShipmentImage(shipmentId);
    }

    @DeleteMapping("/{shipmentId}")
    public String deleteShipment(@PathVariable("shipmentId") String shipmentId) {
        return shipmentService.deleteShipment(shipmentId);
    }

    @PostMapping(
            path = "{shipmentId}/image/upload",
            consumes = MULTIPART_FORM_DATA_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public void uploadShipmentImage(@PathVariable("shipmentId") String shipmentId,
                                    @RequestParam("file") MultipartFile file) {
        shipmentService.uploadShipmentImage(shipmentId, file);
    }

    @PostMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public Shipment saveUpdateShipment(@RequestBody Shipment shipment) {
        return shipmentService.saveShipment(shipment);
    }

    @GetMapping("/sqs-message")
    public String sendSqsMessage() {
        log.info("sqs-message");
        shipmentService.sendSqsMessage();
        return "Message sent successfully!";
    }

}