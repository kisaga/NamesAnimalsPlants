package com.example.user.summerproject.Game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.summerproject.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class FragmentSix extends Fragment {
    private EditText editText;
    private TextView mTextView;
    private HomeListener mCallback;
    private String aChar;

    //=====================================================
    //=====================================================
    //=====================================================
    //=====================================================
    //=====================================================
    private int previousLength;

    private boolean gameSounds;

    //=====================================================
    //=====================================================
    //=====================================================
    //=====================================================
    //=====================================================

    public FragmentSix(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View view=inflater.inflate(R.layout.fragment_landing_6,container,false);
        editText=(EditText)view.findViewById(R.id.answer6);

        gameSounds=getArguments().getBoolean("gameSounds");

        mTextView=(TextView)view.findViewById(R.id.textView6);

        String font=getResources().getString(R.string.font_name);
        Typeface mTypeface= Typeface.createFromAsset(getContext().getAssets(),font);


        editText.setTypeface(mTypeface);
        mTextView.setTypeface(mTypeface);

        SharedPreferences settings=getContext().getSharedPreferences("myPreferences",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=settings.edit();
        aChar=settings.getString("aChar","not found");

        editText.setHint(aChar+"...");
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});

        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        final MediaPlayer mpWriting = MediaPlayer.create(this.getContext(), R.raw.pencil8);
        final MediaPlayer mpErasing = MediaPlayer.create(this.getContext(), R.raw.erasing5);
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //=====================================================
                //=====================================================
                //=====================================================
                //=====================================================
                //=====================================================
                int currentLength=s.length();
                if (gameSounds) {
                    if (currentLength > previousLength) {
                        mpWriting.start();
                    } else if (currentLength < previousLength) {
                        mpErasing.start();
                    }
                }
                previousLength=s.length();
                //=====================================================
                //=====================================================
                //=====================================================
                //=====================================================
                //=====================================================
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if( search( s.toString().trim() ) && !hasEnglish(s.toString().trim() ) && s.length()>0 ){
                        mCallback.onHomeClick(s.toString().trim(),5,true,true);
                    }else{
                        if(s.toString().equals("")){
                            mCallback.onHomeClick(s.toString().trim(),5,false,false);
                        }else{
                            mCallback.onHomeClick(s.toString().trim(),5,false,true);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context con) {
        super.onAttach(con);
        try {
            mCallback = (HomeListener) con;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }


    public boolean search(String name) throws IOException {
        boolean x=false;
        BufferedReader reader=null;
        try{
            //reader=new BufferedReader(new InputStreamReader(getContext().getAssets().open("CitiesCountriesUp.txt"),"UTF-8"));
            FileInputStream fin = getContext().openFileInput("cities.txt");
            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            //do reading usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {


                if( x=mLine.equals(name) &&  (aChar.charAt(0)==mLine.charAt(0) )) {
                    break;
                }

            }
            return x;

        }catch(IOException e){
            return x;
        }finally {
            if(reader!=null){
                try{
                    reader.close();
                }catch (IOException e){
                    //log the exception
                }
            }
        }


    }




    public boolean hasEnglish(String name){
        boolean has=false;
        char[] letters=name.toCharArray();

        for(int i=0;i<name.length();i++){
            String letter=Character.toString(letters[i]);
            if(letter.matches("[a-zA-Z0-9]+")){
                has=true;
            }
        }


        return has;
    }
    public void keyboardClick(String letter){

        editText.append(letter);
    }
    public void backspaceClicked(){
        String name=editText.getText().toString();
        if(name.length()>=1) {
            editText.setText(name.substring(0, name.length() - 1));
        }
    }
}
