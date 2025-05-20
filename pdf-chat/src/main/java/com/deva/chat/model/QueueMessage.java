package com.deva.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
public class QueueMessage {

    private String id;
    private Timestamp createdAt;

}
