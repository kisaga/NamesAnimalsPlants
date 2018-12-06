package com.example.user.summerproject.myTools;



public class UserMessage extends Message {
    public String sender;
    public String message;
    public String hash;
    public UserMessage(){}
    public UserMessage(String sender,String message){
        this.sender=sender;
        this.message=message;
    }
    public UserMessage(String sender,String message,String hash){
        this.sender=sender;
        this.message=message;
        this.hash=hash;
    }
}
