package com.example.chatapp;

public class Message {
    private String sender;
    private String text;

    // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    public Message() {
    }

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
