package com.deva.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SQSPDFMessage extends QueueMessage {

    private String fileName;
    private String fileUrl;

}
