package org.example.localstack.config;

import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

public class AWSClientConfig {

    @Value("${aws.credentials.access-key}")
    protected String awsAccessKey;

    @Value("${aws.credentials.secret-key}")
    protected String awsSecretKey;

    @Value("${aws.region}")
    protected String awsRegion;

    protected AwsCredentialsProvider amazonAWSCredentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKey, awsSecretKey));
    }
}
