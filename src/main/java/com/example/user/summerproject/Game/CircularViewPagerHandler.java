package com.example.user.summerproject.Game;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

public class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
    private ViewPager   mViewPager;
    private int         mCurrentPosition;
    private int         mScrollState;
    private Typeface mTypeface;
    //=================new fields======================================
    private TextView[]  myIndicators;
    private int         mPreviousPosition;
    private Context context;
    //=================================================================

    public CircularViewPagerHandler(final ViewPager viewPager, TextView[] indicators,Typeface typeface,Context context) {
        mViewPager = viewPager;

        //new code just save the text view====
        myIndicators=indicators;
        mTypeface=typeface;
        this.context=context;
        //====================================
    }

    @Override
    public void onPageSelected(final int position) {
        mCurrentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
        handleScrollState(state);
        mScrollState = state;

    }


    private void handleScrollState(final int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {

            setNextItemIfNeeded();
        }
        myIndicators[mCurrentPosition].setTypeface(mTypeface,Typeface.BOLD_ITALIC);
        myIndicators[mPreviousPosition].setTypeface(mTypeface,Typeface.NORMAL);

        mPreviousPosition=mCurrentPosition;
    }

    private void setNextItemIfNeeded() {
        if (!isScrollStateSettling()) {
            handleSetNextItem();
        }
        //==============================================================
        //========================NEW CODE==============================
        //==============================================================

        //==============================================================
        //==============================================================
    }

    private boolean isScrollStateSettling() {
        return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
    }

    private void handleSetNextItem() {
        final int lastPosition = mViewPager.getAdapter().getCount() - 1;
        if(mCurrentPosition == 0) {
            mViewPager.setCurrentItem(lastPosition, true);
        } else if(mCurrentPosition == lastPosition) {
            mViewPager.setCurrentItem(0, true);
        }
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
    }
}
