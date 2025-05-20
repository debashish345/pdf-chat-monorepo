package com.deva.chat.embedding.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EmbeddingService {
    public void storeEmbeddings(MultipartFile multipartFile) throws Exception;

    public List<String> similaritySearch(String text);

    public String chat(String query);
}
