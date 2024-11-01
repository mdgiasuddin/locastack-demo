package org.example.localstack.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.localstack.buckets.BucketName;
import org.example.localstack.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Error;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3StorageService {

    private final S3Client s3Client;
    private final BucketName bucketName;

    public void save(String path, String fileName,
                     MultipartFile multipartFile)
            throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName.getShipmentPictureBucket())
                .key(path + "/" + fileName)
                .contentType(multipartFile.getContentType())
                .contentLength(multipartFile.getSize())
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromFile(FileUtil.convertMultipartFileToFile(multipartFile)));

    }

    public byte[] download(String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName.getShipmentPictureBucket())
                .key(key)
                .build();
        byte[] object = new byte[0];
        try {
            object = s3Client.getObject(getObjectRequest).readAllBytes();
        } catch (NoSuchKeyException noSuchKeyException) {
            log.warn("Could not find object: {}", noSuchKeyException.getMessage());
        }
        return object;
    }

    public void delete(String folderPrefix) {
        List<ObjectIdentifier> keysToDelete = new ArrayList<>();
        s3Client.listObjectsV2Paginator(
                        builder -> builder.bucket(bucketName.getShipmentPictureBucket())
                                .prefix(folderPrefix + "/"))
                .contents().stream()
                .map(S3Object::key)
                .forEach(key -> keysToDelete.add(ObjectIdentifier.builder().key(key).build()));

        DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName.getShipmentPictureBucket())
                .delete(builder -> builder.objects(keysToDelete).build())
                .build();

        try {
            DeleteObjectsResponse response = s3Client.deleteObjects(deleteRequest);
            List<S3Error> errors = response.errors();
            if (!errors.isEmpty()) {
                log.error("Errors occurred while deleting objects:");
                errors.forEach(error -> log.error("Object: {}, Error Code: {}, Error Message: {}", error.key(), error.code(), error.message()));
            } else {
                log.info("Objects deleted successfully.");
            }
        } catch (SdkException e) {
            log.error("Error occurred during object deletion: {}", e.getMessage());
        }
    }

}
