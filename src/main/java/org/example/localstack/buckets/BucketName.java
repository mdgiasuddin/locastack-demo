package org.example.localstack.buckets;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@PropertySource(value = "classpath:buckets.properties")
@Getter
public class BucketName {

    @Value("${shipment-picture-bucket}")
    private String shipmentPictureBucket;
    @Value("${shipment-picture-bucket-validator}")
    private String shipmentPictureValidatorBucket;
}
