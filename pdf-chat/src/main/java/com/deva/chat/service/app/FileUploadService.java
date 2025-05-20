package com.deva.chat.service.app;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    void uploadFile(MultipartFile file);
}
