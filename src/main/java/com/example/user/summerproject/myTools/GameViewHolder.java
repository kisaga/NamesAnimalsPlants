package com.example.user.summerproject.myTools;


import android.view.View;
import android.widget.TextView;

import com.example.user.summerproject.R;

public class GameViewHolder {
    public GameGridTextView letterTextView;
    public GameGridTextView nameTextView;
    public GameGridTextView levelTextView;
    public GameViewHolder(View view){
        this.letterTextView= (GameGridTextView) view.findViewById(R.id.letter_text);
        this.nameTextView=(GameGridTextView)view.findViewById(R.id.name_text);
        this.levelTextView=(GameGridTextView)view.findViewById(R.id.level_text);
    }
}
