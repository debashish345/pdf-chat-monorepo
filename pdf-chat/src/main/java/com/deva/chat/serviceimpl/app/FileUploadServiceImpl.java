package com.deva.chat.serviceimpl.app;

import com.deva.chat.model.SQSPDFMessage;
import com.deva.chat.service.app.FileUploadService;
import com.deva.chat.service.aws.SQSPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload-dir}")
    private String UPLOAD_DIRECTORY;

    private final SQSPublisher sqsPublisher;

    public FileUploadServiceImpl(SQSPublisher sqsPublisher) {
        this.sqsPublisher = sqsPublisher;
    }

    @Override
    public void uploadFile(MultipartFile file) {
        try {
            // Create the upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Build the file path
            Path filePath = uploadPath.resolve(file.getOriginalFilename());

            // Transfer the file to the server
            Files.copy(file.getInputStream(), filePath);

            // Publish to SQS
            SQSPDFMessage message = SQSPDFMessage.builder()
                    .fileName(file.getOriginalFilename())
                    .fileUrl(filePath.toString())
                    .build();
            sqsPublisher.publishPdfMessageToSQS(message);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
