package com.example.ask_me;

public class message {
     public static String me = "me";
     public  static String bot = "bot";
   String message;
    String sender;

    public message(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
