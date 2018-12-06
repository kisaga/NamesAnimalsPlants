package com.example.user.summerproject.word_tester;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.summerproject.MainActivity;
import com.example.user.summerproject.R;
import com.example.user.summerproject.categoryTools.CategoryTools;
import com.example.user.summerproject.categoryTools.Plant;
import com.example.user.summerproject.myTools.LisgarTools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public  class FragmentThreeTester extends Fragment {

    private EditText mEditText;
    private TextView mTextView;
    private Button mButton;
    private String mID;

    //=====================================================
    //=====================================================
    //=====================================================
    //=====================================================
    //=====================================================
    private int previousLength;
    private ProgressDialog progressDialog;

    private boolean sounds;
    //=====================================================
    //=====================================================
    //=====================================================
    //=====================================================
    //=====================================================


    public FragmentThreeTester(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v =  inflater.inflate(R.layout.fragment_landing_3_tester,container,false);

        sounds=getArguments().getBoolean("sounds");
        mID = getArguments().getString("mID");
        String font=getResources().getString(R.string.font_name);
        Typeface mTypeface= Typeface.createFromAsset(getContext().getAssets(),font);
        mTextView=(TextView)v.findViewById(R.id.textView3tester);
        mTextView.setTypeface(mTypeface);
        mEditText = (EditText) v.findViewById(R.id.answer3tester);
        mEditText.setTypeface(mTypeface);
        mButton = (Button) v.findViewById(R.id.check3tester);
        mButton.setTypeface(mTypeface);
        mButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(MainActivity.hasInternet) {
                        try {
                            String name = mEditText.getText().toString().trim().toUpperCase();
                            if ( name.equals("$@V0UR@D3R!") ) {
                                Intent newAdmin = new Intent(getActivity(), PasswordActivity.class);
                                startActivity(newAdmin);
                            } else {
                                search(name, mButton);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast toast=Toast.makeText(getContext(),"Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                        toast.show();
                    }
                }
            });


        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        final MediaPlayer mpWriting = MediaPlayer.create(this.getContext(), R.raw.pencil8);
        final MediaPlayer mpErasing = MediaPlayer.create(this.getContext(), R.raw.erasing5);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currentLength=s.length();
                if(sounds) {
                    if (currentLength > previousLength) {
                        mpWriting.start();
                    } else if (currentLength < previousLength) {
                        mpErasing.start();
                    }
                }
                previousLength=s.length();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================



        return v;
    }
    public void search(final String name, Button b1) throws IOException {
        boolean x=false;
        BufferedReader reader=null;
        final String name1=name;
        try{
            //reader=new BufferedReader(new InputStreamReader(getContext().getAssets().open("PlantsUp.txt"),"UTF-8"));
            FileInputStream fin = getContext().openFileInput("plants.txt");
            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            //do reading usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                b1.setEnabled(false);
                if (x = mLine.equals(name)){
                    Toast toast=Toast.makeText(getContext(),"Η λέξη υπάρχει",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                    break;
                }
            }
            if (!x && name.length()>0){
                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(getContext());
                mBuilder.setMessage("Η ΛΕΞΗ ΔΕΝ ΒΡΕΘΗΚΕ. ΘΕΛΕΤΕ ΝΑ ΤΗΝ ΠΡΟΤΕΙΝΕΤΕ;");
                mBuilder.setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference suggestionsDatabase= FirebaseDatabase.getInstance().getReference().child("Suggestions");
                        progressDialog=new ProgressDialog(getContext());
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setMessage("Uploading...");
                        progressDialog.show();

                        BufferedReader reader1=null;
                        boolean x1=false;
                        try {
                            FileInputStream fin1 = getContext().openFileInput("plantsuggestions.txt");
                            reader1=new BufferedReader(new InputStreamReader(fin1,"UTF-8"));
                            //do reading usually loop until end of file reading
                            String mLine;
                            while ((mLine = reader1.readLine()) != null) {
                                if (x1 = mLine.equals(name)){
                                    Toast toast=Toast.makeText(getContext(),"Έχεις προτείνει ήδη αυτή τη λέξη",Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                                    toast.show();
                                    break;
                                }
                            }
                            if(!x1){
                                Suggestion suggestion = new Suggestion(mID,name1);
                                suggestionsDatabase.child("Plants").push().setValue(suggestion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        CategoryTools categoryTools=new CategoryTools(getActivity().getApplicationContext());
                                        Plant plant=new Plant(name);
                                        categoryTools.addPlant(plant,"plantssuggestions.txt");
                                        Toast toast=Toast.makeText(getContext(),"Ευχαριστούμε για την πρόταση. Θα ενημερωθείτε για την αξιολόγησή της.",Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                                        toast.show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast toast=Toast.makeText(getContext(),"Η προτασή σας δεν ολοκληρώθηκε.Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                                        toast.show();
                                    }
                                });
                            }else{
                                progressDialog.dismiss();
                            }
                        } catch (FileNotFoundException e) {
                            LisgarTools myTools=new LisgarTools(getActivity().getApplicationContext());
                            myTools.clearFile("plantsuggestions.txt");
                            Toast toast=Toast.makeText(getContext(),"Η προτασή σας δεν ολοκληρώθηκε.Ξαναπροσπαθήστε",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            toast.show();
                            progressDialog.dismiss();
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }





                    }
                });
                mBuilder.setNegativeButton("OXI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mBuilder.show();
            }
            b1.setEnabled(true);

        }catch(IOException e){

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
    public void keyboardClick(String letter){

        mEditText.append(letter);
    }
    public void backspaceClicked(){
        String name=mEditText.getText().toString();
        if(name.length()>=1) {
            mEditText.setText(name.substring(0, name.length() - 1));
        }
    }

}
