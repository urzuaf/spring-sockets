package com.example.chatapp.model;

import java.sql.Time;

public class Message {
    private String sender;
    private String content;
    private String destinatary;
    private String timestamp;

    public Message(String sender, String content, String destinatary, String timestamp) {
        this.sender = sender;
        this.content = content;
        this.destinatary = destinatary;
        this.timestamp = Time.valueOf("00:00:00").toString();
    }

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

    public String getTimestamp() {
        return timestamp;
    }   

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
