package com.example.user.summerproject.myTools;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;



public class PaperTextView extends TextView {
    public PaperTextView(Context context) {
        super(context);
        Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
        this.setTypeface(pencilTypeface);
        setClickable(true);
    }

    public PaperTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
        this.setTypeface(pencilTypeface);
        setClickable(true);
    }

    public PaperTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
        this.setTypeface(pencilTypeface);
        setClickable(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PaperTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Typeface pencilTypeface= Typeface.createFromAsset(context.getAssets(),"fonts/ghoststory.otf");
        this.setTypeface(pencilTypeface);
        setClickable(true);
    }
    /*
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            return performClick();
        }
        return true;
    }
    */
}
