package com.example.user.summerproject.myTools;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsTextView extends TextView {


    public FriendsTextView(Context context) {
        super(context);
        Typeface chalkTypeface=Typeface.createFromAsset(context.getAssets(),"fonts/romanscript.ttf");
        this.setTypeface(chalkTypeface);
    }

    public FriendsTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Typeface chalkTypeface=Typeface.createFromAsset(context.getAssets(),"fonts/romanscript.ttf");
        this.setTypeface(chalkTypeface);
    }

    public FriendsTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface chalkTypeface=Typeface.createFromAsset(context.getAssets(),"fonts/romanscript.ttf");
        this.setTypeface(chalkTypeface);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FriendsTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Typeface chalkTypeface=Typeface.createFromAsset(context.getAssets(),"fonts/romanscript.ttf");
        this.setTypeface(chalkTypeface);
    }



    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            return performClick();
        }
        return true;
    }


}
