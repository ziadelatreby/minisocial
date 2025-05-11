package com.minisocial.minisocialapi.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class NotificationDTO implements Serializable {
    private Long id;
    private Long recipientId;
    private String type;
    private String message;
    private boolean isRead;
    private LocalDateTime timestamp;
    private  Map<String, Serializable> notiSpecificData;

    // Constructor
    public NotificationDTO() {
        this.notiSpecificData = new HashMap<>();
    }

    // Getters and setters
    // ... standard getters/setters ...


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getRecipientId() {
        return recipientId;
    }
    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isRead() {
        return isRead;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    public boolean getIsRead() {
        return this.isRead;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public Map<String, Serializable> getNotiSpecificData() {
        return notiSpecificData;
    }

    public void addData(String key, Serializable value) {
        this.notiSpecificData.put(key, value);
    }

    public Object getData(String key) {
        return this.notiSpecificData.get(key);
    }
}