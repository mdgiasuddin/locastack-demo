package org.example.localstack.config;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@Configuration
public class AmazonSQSConfig extends AWSClientConfig {
    @Value("${spring.cloud.aws.sqs.endpoint}")
    private String awsSqsEndPoint;

    @Bean
    public SqsAsyncClient sqsClient() {
        return SqsAsyncClient.builder()
                .endpointOverride(URI.create(awsSqsEndPoint))
                .credentialsProvider(amazonAWSCredentialsProvider())
                .region(Region.of(awsRegion))
                .build();
    }

    @Bean
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsClient) {
        return SqsTemplate.builder().sqsAsyncClient(sqsClient).build();
    }
}
