package com.deva.chat.embedding.controller;

import com.deva.chat.embedding.service.EmbeddingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/embedding")
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    public EmbeddingController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @PostMapping
    public ResponseEntity<Void> storeEmbeddings(@RequestParam("file") MultipartFile file) {
        try {
            embeddingService.storeEmbeddings(file);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<String> chat(@RequestParam("query") String query) {
        String results = embeddingService.chat(query);
        return ResponseEntity.ok(results);
    }
}
