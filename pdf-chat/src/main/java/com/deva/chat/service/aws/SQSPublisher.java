package com.deva.chat.service.aws;

import com.deva.chat.model.SQSPDFMessage;

public interface SQSPublisher {

    void publishPdfMessageToSQS(SQSPDFMessage message);
}
