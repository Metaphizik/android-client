package com.example.metaphizik.design.chat;

import java.util.Date;

public class Message {
    private String author;
    private String textMessage;
    private long timeMessage;

    public Message(String author, String texMessage) {
        this.author = author;
        this.textMessage = texMessage;

        timeMessage = new Date().getTime();
    }

    public Message() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTexMessage(String texMessage) {
        this.textMessage = texMessage;
    }

    public long getTimeMessage() {
        return timeMessage;
    }

    public void setTimeMessage(long timeMessage) {
        this.timeMessage = timeMessage;
    }
}
