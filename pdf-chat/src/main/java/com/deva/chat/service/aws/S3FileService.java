package com.deva.chat.service.aws;

import com.deva.chat.enums.AccessType;
import software.amazon.awssdk.http.SdkHttpMethod;

public interface S3FileService {

    void uploadFileToS3(String fileName, String fileUrl);

    void deleteFileFromS3(String fileName);

    String generatePreSignedUrl(String filePath, SdkHttpMethod method, AccessType accessType);

    void notifySQSonS3FileUpload(String fileName);
}
