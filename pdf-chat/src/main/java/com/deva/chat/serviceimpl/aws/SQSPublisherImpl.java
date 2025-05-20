package com.deva.chat.serviceimpl.aws;

import com.deva.chat.model.QueueMessage;
import com.deva.chat.model.SQSPDFMessage;
import com.deva.chat.service.aws.SQSPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

@Service
@Slf4j
public class SQSPublisherImpl implements SQSPublisher {

    @Value("${aws.sqs.queue-name}")
    private String awsSqsPdfQueueName;

    private final SqsClient amazonSQSClient;
    private final ObjectMapper objectMapper;

    public SQSPublisherImpl(SqsClient amazonSQSClient, ObjectMapper objectMapper) {
        this.amazonSQSClient = amazonSQSClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishPdfMessageToSQS(SQSPDFMessage message) {
        publishToSQS(message);
    }

    private void publishToSQS(QueueMessage message){
        try {
            GetQueueUrlResponse queueUrl = amazonSQSClient.getQueueUrl((builder -> builder.queueName(awsSqsPdfQueueName).build()));
            SendMessageResponse result = amazonSQSClient.sendMessage(SendMessageRequest.builder()
                            .queueUrl(queueUrl.queueUrl())
                            .messageBody(objectMapper.writeValueAsString(message))
                    .build());
            log.info("Message ID: {}", result.messageId());
        } catch (Exception e) {
            log.error("Queue Exception Message: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
