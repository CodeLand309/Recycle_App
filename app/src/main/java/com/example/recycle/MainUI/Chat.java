package com.example.recycle.MainUI;

public class Chat {
    private String Sender;
    private String Receiver;
    private String Message;
    private boolean Seen_Status;

    public Chat(String sender, String receiver, String message, boolean seenStatus){
        Sender = sender;
        Receiver = receiver;
        Message = message;
        Seen_Status = seenStatus;
    }

    public Chat() {
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        this.Sender = sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String receiver) {
        this.Receiver = receiver;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public boolean isSeen_Status() {
        return Seen_Status;
    }

    public void setSeen_Status(boolean seen_Status) {
        Seen_Status = seen_Status;
    }
}
