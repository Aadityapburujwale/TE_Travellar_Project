package com.android.travelapp;

public class ChatBotModel {

    String message;
    String messageSender;

    ChatBotModel(String message, String messageSender){
        this.message = message;
        this.messageSender = messageSender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    public String getMessageSender() {
        return messageSender;
    }

}
