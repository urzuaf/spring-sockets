package com.example.chatapp.model;
import java.time.Instant;

public class Mensaje {
    private String sender;
    private String content;
    private String destinatary;
    private Instant timestamp;
    private boolean urgent;

    // Getters y Setters

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDestinatary() {
        return destinatary;
    }

    public void setDestinatary(String destinatary) {
        this.destinatary = destinatary;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }
}
