package com.example.user.summerproject.myTools;

/**
 * Created by Tommys on 13-Sep-17.
 */

public class SuggestionMessage extends Message {

    public String playerSuggestion,categorySuggestion,ourMessage;
    public int points;
    public String hash;

    public SuggestionMessage(){

    }

    public SuggestionMessage(String playerSuggestion,String categorySuggestion, String ourMessage,int points,String hash){

        this.playerSuggestion=playerSuggestion;
        this.categorySuggestion=categorySuggestion;
        this.ourMessage = ourMessage;
        this.points = points;
        this.hash=hash;
    }

    public SuggestionMessage(String playerSuggestion,String categorySuggestion, String ourMessage,int points){

        this.playerSuggestion=playerSuggestion;
        this.categorySuggestion=categorySuggestion;
        this.ourMessage = ourMessage;
        this.points = points;
    }
}
