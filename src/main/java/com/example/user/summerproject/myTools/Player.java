package com.example.user.summerproject.myTools;




public class Player{

    protected String name;
    private boolean soundOn,musicOn;
    private int numberOfOldGames;
    private OldGame[] oldGames;
    private Game[] myGames=new Game[12];


    public Player(String name){
        this.name=name;
    }



    public String getName(){
        return name;
    }
    public boolean getSoundState(){
        return soundOn;
    }
    public void setSound(boolean state){
        soundOn=state;
    }
    public boolean getMusicState(){
        return musicOn;
    }
    public void setMusic(boolean state){
        musicOn=state;
    }



    public void setOldGames(OldGame[] oldGames){
        this.oldGames=oldGames;
    }




}
