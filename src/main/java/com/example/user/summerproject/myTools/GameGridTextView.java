package com.example.user.summerproject.myTools;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by User on 27/10/2017.
 */

public class GameGridTextView extends TextView {
    public GameGridTextView(Context context) {
        super(context);
        Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
        this.setTypeface(pencilTypeface);
    }

    public GameGridTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
        this.setTypeface(pencilTypeface);
    }

    public GameGridTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
        this.setTypeface(pencilTypeface);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameGridTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
        this.setTypeface(pencilTypeface);
    }
}
