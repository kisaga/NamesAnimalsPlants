package com.example.user.summerproject.word_tester;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerAdapterTester extends FragmentPagerAdapter   {

        private FragmentOneTester mFragment1;
        private FragmentTwoTester mFragment2;
        private FragmentThreeTester mFragment3;
        private FragmentFourTester mFragment4;
        private FragmentFiveTester mFragment5;
        private FragmentSixTester mFragment6;

    public PagerAdapterTester(FragmentManager fm,boolean sounds,String mID) {

        super(fm);

        Bundle bundle=new Bundle();
        bundle.putBoolean("sounds",sounds);
        bundle.putString("mID",mID);


        mFragment1 = new FragmentOneTester();
        mFragment1.setArguments(bundle);
        mFragment2 = new FragmentTwoTester();
        mFragment2.setArguments(bundle);
        mFragment3 = new FragmentThreeTester();
        mFragment3.setArguments(bundle);
        mFragment4 = new FragmentFourTester();
        mFragment4.setArguments(bundle);
        mFragment5 = new FragmentFiveTester();
        mFragment5.setArguments(bundle);
        mFragment6 = new FragmentSixTester();
        mFragment6.setArguments(bundle);
    }

    @Override
    public Fragment getItem(int arg0){
        switch (arg0){
            case 0:
                return  mFragment1;
            case 1:
                return mFragment2;
            case 2:
                return mFragment3;
            case 3:
                return mFragment4;
            case 4:
                return mFragment5;
            case 5:
                return mFragment6;
        }
            return null;
    }
    @Override
    public int getCount(){
        return 6;
    }

    public Fragment[] getFragments(){
        Fragment[] fragments={mFragment1,mFragment2,mFragment3,mFragment4,mFragment5,mFragment6};
        return fragments;
    }
}
