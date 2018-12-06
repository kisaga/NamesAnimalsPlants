package com.example.user.summerproject.myTools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.user.summerproject.R;

import java.util.ArrayList;


public class GameAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Game> games;
    private String playerName;
    public GameAdapter(Context context, ArrayList<Game> games,String playerName){
        this.games=games;
        this.context=context;
        this.playerName=playerName;
    }
    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        GameViewHolder gameViewHolder=null;

        if(row==null){
            LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.game_relative,parent,false);
            gameViewHolder=new GameViewHolder(row);
            if(games.get(position) instanceof CompletedGame){
                gameViewHolder.letterTextView.setText(((CompletedGame) games.get(position)).letter);
                gameViewHolder.nameTextView.setText(((CompletedGame) games.get(position)).player2);
                gameViewHolder.levelTextView.setText(((CompletedGame) games.get(position)).level2);

                CompletedGame completedGame=(CompletedGame)games.get(position);

                Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
                if(((CompletedGame) games.get(position)).level2>=20){
                    gameViewHolder.nameTextView.setTypeface(pencilTypeface,Typeface.BOLD);
                }
                completedGame.calculateTotalPoints();
                if (completedGame.sumPoints1 > completedGame.sumPoints2) {
                    int color=context.getResources().getColor(R.color.rightGreen);
                    gameViewHolder.letterTextView.setTextColor(color);
                } else if (completedGame.sumPoints1 < completedGame.sumPoints2) {
                    gameViewHolder.letterTextView.setTextColor(Color.RED);
                } else {
                    if (completedGame.time1 > completedGame.time2) {
                        int color=context.getResources().getColor(R.color.rightGreen);
                        gameViewHolder.letterTextView.setTextColor(color);
                    } else if (completedGame.time1 < completedGame.time2) {
                        gameViewHolder.letterTextView.setTextColor(Color.RED);
                    } else {

                    }
                }
            }else if(games.get(position) instanceof HalfGame){
                HalfGame halfGame=(HalfGame)games.get(position);

                if(playerName.equals(halfGame.player1)){
                    gameViewHolder.letterTextView.setText(halfGame.letter);
                    gameViewHolder.nameTextView.setText(halfGame.player2);
                    gameViewHolder.levelTextView.setText("Lvl "+halfGame.level2);
                    if(halfGame.level2>=20){
                        Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
                        gameViewHolder.nameTextView.setTypeface(pencilTypeface,Typeface.BOLD);
                    }
                }else{
                    gameViewHolder.letterTextView.setText("?");
                    gameViewHolder.nameTextView.setText(halfGame.player1);
                    gameViewHolder.levelTextView.setText("Lvl "+halfGame.level1);
                    if(halfGame.level1>=20){
                        Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
                        gameViewHolder.nameTextView.setTypeface(pencilTypeface,Typeface.BOLD);
                    }
                }
            }






            row.setTag(gameViewHolder);

        }else{
            gameViewHolder=(GameViewHolder) row.getTag();

            if(games.get(position) instanceof CompletedGame){
                gameViewHolder.letterTextView.setText(((CompletedGame) games.get(position)).letter);
                gameViewHolder.nameTextView.setText(((CompletedGame) games.get(position)).player2);
                gameViewHolder.levelTextView.setText(((CompletedGame) games.get(position)).level2);

                CompletedGame completedGame=(CompletedGame)games.get(position);

                Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
                if(((CompletedGame) games.get(position)).level2>=20){
                    gameViewHolder.nameTextView.setTypeface(pencilTypeface,Typeface.BOLD);
                }
                completedGame.calculateTotalPoints();
                if (completedGame.sumPoints1 > completedGame.sumPoints2) {
                    int color=context.getResources().getColor(R.color.rightGreen);
                    gameViewHolder.letterTextView.setTextColor(color);
                } else if (completedGame.sumPoints1 < completedGame.sumPoints2) {
                    gameViewHolder.letterTextView.setTextColor(Color.RED);
                } else {
                    if (completedGame.time1 > completedGame.time2) {
                        int color=context.getResources().getColor(R.color.rightGreen);
                        gameViewHolder.letterTextView.setTextColor(color);
                    } else if (completedGame.time1 < completedGame.time2) {
                        gameViewHolder.letterTextView.setTextColor(Color.RED);
                    } else {

                    }
                }
            }else if(games.get(position) instanceof HalfGame){
                HalfGame halfGame=(HalfGame)games.get(position);

                if(playerName.equals(halfGame.player1)){
                    gameViewHolder.letterTextView.setText(halfGame.letter);
                    gameViewHolder.nameTextView.setText(halfGame.player2);
                    gameViewHolder.levelTextView.setText("Lvl "+halfGame.level2);
                    if(halfGame.level2>=20){
                        Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
                        gameViewHolder.nameTextView.setTypeface(pencilTypeface,Typeface.BOLD);
                    }
                }else{
                    gameViewHolder.letterTextView.setText("?");
                    gameViewHolder.nameTextView.setText(halfGame.player1);
                    gameViewHolder.levelTextView.setText("Lvl "+halfGame.level1);
                    if(halfGame.level1>=20){
                        Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
                        gameViewHolder.nameTextView.setTypeface(pencilTypeface,Typeface.BOLD);
                    }
                }
            }
        }

        return row;
    }
}
