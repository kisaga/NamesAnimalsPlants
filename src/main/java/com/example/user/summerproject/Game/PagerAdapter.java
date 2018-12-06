package com.example.user.summerproject.Game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PagerAdapter extends FragmentPagerAdapter {
    public FragmentOne fragmentOne;
    public FragmentTwo fragmentTwo;
    public FragmentThree fragmentThree;
    public FragmentFour fragmentFour;
    public FragmentFive fragmentFive;
    public FragmentSix fragmentSix;

    public PagerAdapter(FragmentManager fm,boolean gameSounds) {
        super(fm);

        Bundle bundle=new Bundle();
        bundle.putBoolean("gameSounds",gameSounds);

        fragmentOne=new FragmentOne();
        fragmentOne.setArguments(bundle);
        fragmentTwo=new FragmentTwo();
        fragmentTwo.setArguments(bundle);
        fragmentThree=new FragmentThree();
        fragmentThree.setArguments(bundle);
        fragmentFour=new FragmentFour();
        fragmentFour.setArguments(bundle);
        fragmentFive=new FragmentFive();
        fragmentFive.setArguments(bundle);
        fragmentSix=new FragmentSix();
        fragmentSix.setArguments(bundle);
    }

    @Override
    public Fragment getItem(int arg0){



        switch (arg0){
            //case 0:
                //return firstVirtualFragment;
            case 0:
                return fragmentOne;
            case 1:
                return fragmentTwo;
            case 2:
                return fragmentThree;
            case 3:
                return fragmentFour;
            case 4:
                return fragmentFive;
            case 5:
                return fragmentSix;
            //case 7:
                //return lastVirtualFragment;


        }
            return null;

    }

    @Override
    public int getCount(){
        return 6;
    }

    public Fragment[] getFragments(){
        Fragment[] fragments={fragmentOne,fragmentTwo,fragmentThree,fragmentFour,fragmentFive,fragmentSix};
        return fragments;
    }


}
