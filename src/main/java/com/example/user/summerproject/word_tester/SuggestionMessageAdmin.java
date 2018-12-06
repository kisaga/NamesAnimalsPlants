package com.example.user.summerproject.word_tester;


import com.example.user.summerproject.myTools.SuggestionMessage;

public class SuggestionMessageAdmin extends SuggestionMessage {

    public String playerSuggestion,categorySuggestion,ourMessage;
    public int points;
    public String ID;

    public SuggestionMessageAdmin(){

    }

    public SuggestionMessageAdmin(String playerSuggestion, String categorySuggestion, String ourMessage, int points, String ID){

        this.playerSuggestion = playerSuggestion;
        this.categorySuggestion = categorySuggestion;
        this.ourMessage = ourMessage;
        this.points = points;
        this.ID = ID;
    }




}
