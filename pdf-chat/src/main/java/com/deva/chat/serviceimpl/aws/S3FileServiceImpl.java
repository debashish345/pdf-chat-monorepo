package com.deva.chat.serviceimpl.aws;

import com.deva.chat.enums.AccessType;
import com.deva.chat.model.SQSPDFMessage;
import com.deva.chat.service.aws.S3FileService;
import com.deva.chat.service.aws.SQSPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.exception.SdkServiceException;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.text.Normalizer;
import java.time.Duration;

@Service
public class S3FileServiceImpl implements S3FileService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final SQSPublisher sqsPublisher;
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3FileServiceImpl(S3Client s3Client, S3Presigner s3Presigner, SQSPublisher sqsPublisher) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.sqsPublisher = sqsPublisher;
    }

    public static String buildFilename(String filename) {
        return String.format("%s_%s", System.currentTimeMillis(), filename);
    }

    private static String sanitizeFileName(String fileName) {
        String normalizedFileName = Normalizer.normalize(fileName, Normalizer.Form.NFKD);
        return normalizedFileName.replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9.\\-_]", "");
    }

    @Override
    public void uploadFileToS3(String fileName, String fileUrl) {

    }

    @Override
    public void deleteFileFromS3(String fileName) {

    }

    @Override
    public String generatePreSignedUrl(String filePath, SdkHttpMethod method, AccessType accessType) {
        if (method == SdkHttpMethod.GET) {
            return generateGetPresignedUrl(filePath);
        } else if (method == SdkHttpMethod.PUT) {
            return generatePutPresignedUrl(filePath, accessType);
        } else {
            throw new UnsupportedOperationException("Unsupported HTTP method: " + method);
        }
    }

    @Override
    public void notifySQSonS3FileUpload(String fileName) {
        sqsPublisher.publishPdfMessageToSQS(SQSPDFMessage.builder()
                .fileName(fileName)
                .build());
    }

    private String generateGetPresignedUrl(String filePath) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath)
                .build();

        // you can change expiration time here
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

    private String generatePutPresignedUrl(String filePath, AccessType accessType) {
        PutObjectRequest.Builder putObjectRequestBuilder = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(filePath);

        if (accessType == AccessType.PUBLIC) {
            putObjectRequestBuilder.acl(ObjectCannedACL.PUBLIC_READ);
        }

        PutObjectRequest putObjectRequest = putObjectRequestBuilder.build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        return presignedRequest.url().toString();
    }

    private boolean doesFileExist(String fileName) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (SdkServiceException | SdkClientException e) {
            System.err.println("Error checking file existence: " + e.getMessage());
            return false;
        }
    }
}
