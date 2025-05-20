package com.deva.chat.embedding.serviceimpl;

import com.deva.chat.embedding.service.EmbeddingService;
import com.deva.chat.embedding.util.PdfUtil;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OllamaEmbeddingServiceImpl implements EmbeddingService {

    private final PdfUtil pdfUtil;

    private final VectorStore vectorStore;

    private final EmbeddingModel embeddingModel;

    private final ChatModel chatModel;

    public OllamaEmbeddingServiceImpl(PdfUtil pdfUtil, VectorStore vectorStore, EmbeddingModel embeddingModel,
                                      @Qualifier("vertexAiGeminiChat") ChatModel chatModel) {
        this.pdfUtil = pdfUtil;
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
        this.chatModel = chatModel;
    }

    @Override
    public void storeEmbeddings(MultipartFile multipartFile) throws Exception {
        List<Document> documentList = pdfUtil.extractPdf(multipartFile);
        vectorStore.add(documentList);
    }

    @Override
    public List<String> similaritySearch(String text) {
        List<Document> results = this.vectorStore.similaritySearch(SearchRequest.builder().query(text).topK(5).build());
        return results.stream()
                .map(Document::getText)
                .toList();
    }

    @Override
    public String chat(String query) {
        String context = similaritySearch(query).stream().collect(Collectors.joining("\n---\n"));
        String prompt = "Answer the question " + query + " using this context " + context;
        return chatModel.call(prompt);
    }
}
