package com.deva.chat.controller;

import com.deva.chat.enums.AccessType;
import com.deva.chat.service.aws.S3FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.http.SdkHttpMethod;

import java.util.Map;

import static com.deva.chat.serviceimpl.aws.S3FileServiceImpl.buildFilename;

@RestController
@RequestMapping("/file/s3")
public class S3FileController {

    private final S3FileService s3FileService;

    public S3FileController(S3FileService s3FileService) {
        this.s3FileService = s3FileService;
    }

    @PostMapping("/pre-signed-url")
    public ResponseEntity<Map<String, Object>> generateUrl(
            @RequestParam(name = "filename", required = false, defaultValue = "") String filename) {
        filename = buildFilename(filename);
        System.out.println("filename: " + filename);
        String url = s3FileService.generatePreSignedUrl(filename, SdkHttpMethod.PUT, AccessType.PRIVATE);
        return ResponseEntity.ok(Map.of("url", url, "file", filename));
    }

    @GetMapping("/upload-complete")
    public ResponseEntity<Void> notifyUploadComplete(@RequestParam(name = "filename", required = true) String filename) {
        s3FileService.notifySQSonS3FileUpload(filename);
        return ResponseEntity.ok().build();
    }
}
