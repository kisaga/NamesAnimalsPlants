package com.example.user.summerproject.myTools;

/**
 * Created by Tommys on 13-Sep-17.
 */

public class LevelMessage extends Message {

    public String levelMessage,TAGmessage;
    public String hash;

    public LevelMessage(){

    }
    public LevelMessage(String levelMessage,String TAGmessage){

        this.levelMessage=levelMessage;
        this.TAGmessage=TAGmessage;


    }
    public LevelMessage(String levelMessage,String TAGmessage,String hash){

        this.levelMessage=levelMessage;
        this.TAGmessage=TAGmessage;
        this.hash=hash;


    }


}
