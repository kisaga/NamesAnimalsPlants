package com.example.user.summerproject.word_tester;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.summerproject.R;
import com.example.user.summerproject.categoryTools.Animal;
import com.example.user.summerproject.categoryTools.CategoryTools;
import com.example.user.summerproject.categoryTools.City;
import com.example.user.summerproject.categoryTools.Color;
import com.example.user.summerproject.categoryTools.Name;
import com.example.user.summerproject.categoryTools.Plant;
import com.example.user.summerproject.categoryTools.Profession;
import com.example.user.summerproject.myTools.LisgarTools;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private Button names,animals,plants,profs,colors,cities;

    private LisgarTools myTools;
    private CategoryTools categoryTools;

    private ProgressDialog progressDialogFiles;

    private int nChild=0;
    private int totalChilds=0;

    private int[] nSuggestions=new int[6];

    private TextView[] numberViews=new TextView[6];

    private DatabaseReference suggestionsReference,nameSuggestions,animalSuggestions,plantSuggestions,professionSuggestions,colorSuggestions,citySuggestions;

    private DatabaseReference databaseReference;

    private SuggestionMessageAdmin[] suggestionMessageAdmins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        myTools=new LisgarTools(this);
        categoryTools=new CategoryTools(this);


        numberViews[0]= (TextView) findViewById(R.id.number_names);
        numberViews[1]= (TextView) findViewById(R.id.number_animals);
        numberViews[2]= (TextView) findViewById(R.id.number_plants);
        numberViews[3]= (TextView) findViewById(R.id.number_professions);
        numberViews[4]= (TextView) findViewById(R.id.number_colors);
        numberViews[5]= (TextView) findViewById(R.id.number_cities);

        names = (Button) findViewById(R.id.button2);
        animals = (Button) findViewById(R.id.zwa);
        plants = (Button) findViewById(R.id.fyta);
        profs = (Button) findViewById(R.id.profs);
        colors = (Button) findViewById(R.id.button3);
        cities = (Button) findViewById(R.id.button);

        for (int i=0;i<6;i++){
            nSuggestions[i]=0;
            numberViews[i].setText("0");
        }








        names.setOnClickListener(this);
        animals.setOnClickListener(this);
        plants.setOnClickListener(this);
        profs.setOnClickListener(this);
        colors.setOnClickListener(this);
        cities.setOnClickListener(this);

        databaseReference=FirebaseDatabase.getInstance().getReference();
        suggestionsReference=FirebaseDatabase.getInstance().getReference().child("Suggestions");
        nameSuggestions=suggestionsReference.child("Names");
        animalSuggestions=suggestionsReference.child("Animals");
        plantSuggestions=suggestionsReference.child("Plants");
        professionSuggestions=suggestionsReference.child("Professions");
        colorSuggestions=suggestionsReference.child("Colors");
        citySuggestions=suggestionsReference.child("Cities");



        calculateNumberOfSuggestions();





    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        String TAG = ((Button)v).getText().toString();
        if(nSuggestions[getCategoryIndex(TAG)]>0){
            Intent intent = new Intent(AdminActivity.this, AdminSent.class);
            intent.putExtra("maxChildCount",nSuggestions[getCategoryIndex(TAG)]);
            intent.putExtra("TAG",TAG);
            startActivity(intent);
        }else{
            Toast.makeText(getBaseContext(),"No suggestions man",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        nChild=0;
        totalChilds=0;
        for (int i=0;i<6;i++){
            nSuggestions[i]=0;
            numberViews[i].setText("0");
        }
        calculateNumberOfSuggestions();
    }

    @Override
    public void onBackPressed() {

        final AlertDialog.Builder mBuilder=new AlertDialog.Builder(this,R.style.Theme_AppCompat_Dialog_Alert);
        mBuilder.setMessage("Έξοδος;");
        mBuilder.setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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

    public void clickCloseAndExit(View v){
        final AlertDialog.Builder mBuilder=new AlertDialog.Builder(this,R.style.Theme_AppCompat_Dialog_Alert);
        mBuilder.setMessage("Όντως???");
        mBuilder.setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            sendMessages();

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

    public void clickUpdateDatabase(View v){
        final AlertDialog.Builder mBuilder=new AlertDialog.Builder(this,R.style.Theme_AppCompat_Dialog_Alert);
        mBuilder.setMessage("Update Database now???");
        mBuilder.setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            updateDatabase();

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


    public void closeAndExit(){
       populateAndUpdateTempFiles();

    }

    public void updateDatabase(){
        addTempToPermFilesAndUpdate();
        //emptyTempFileFromDb();
    }


    public int getCategoryIndex(String TAG){
        int index=0;
        switch (TAG){
            case "Names":
                index= 0;
                break;
            case "Animals":
                index= 1;
                break;
            case "Plants":
                index= 2;
                break;
            case "Professions":
                index= 3;
                break;
            case "Colors":
                index= 4;
                break;
            case "Cities":
                index= 5;
                break;
        }
        return index;
    }


    public void calculateNumberOfSuggestions(){

        progressDialogFiles=new ProgressDialog(AdminActivity.this,ProgressDialog.THEME_HOLO_DARK);
        progressDialogFiles.setCancelable(false);
        progressDialogFiles.setCanceledOnTouchOutside(false);
        progressDialogFiles.setMessage("Calculating...");
        progressDialogFiles.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Suggestions")){

                    databaseReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                if (dataSnapshot.getKey().equals("Suggestions")) {
                                    totalChilds= (int) dataSnapshot.getChildrenCount();

                                    databaseReference.removeEventListener(this);
                                    suggestionsReference.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                            nChild++;
                                            if (dataSnapshot.getKey().equals("Names")) {
                                                nSuggestions[0] = (int) dataSnapshot.getChildrenCount();
                                            } else if (dataSnapshot.getKey().equals("Animals")) {
                                                nSuggestions[1] = (int) dataSnapshot.getChildrenCount();
                                            } else if (dataSnapshot.getKey().equals("Plants")) {
                                                nSuggestions[2] = (int) dataSnapshot.getChildrenCount();
                                            } else if (dataSnapshot.getKey().equals("Professions")) {
                                                nSuggestions[3] = (int) dataSnapshot.getChildrenCount();
                                            } else if (dataSnapshot.getKey().equals("Colors")) {
                                                nSuggestions[4] = (int) dataSnapshot.getChildrenCount();
                                            } else if (dataSnapshot.getKey().equals("Cities")) {
                                                nSuggestions[5] = (int) dataSnapshot.getChildrenCount();
                                            }
                                            if (nChild == totalChilds) {
                                                suggestionsReference.removeEventListener(this);
                                                progressDialogFiles.dismiss();
                                                numberViews[0].setText(nSuggestions[0] + "");
                                                numberViews[1].setText(nSuggestions[1] + "");
                                                numberViews[2].setText(nSuggestions[2] + "");
                                                numberViews[3].setText(nSuggestions[3] + "");
                                                numberViews[4].setText(nSuggestions[4] + "");
                                                numberViews[5].setText(nSuggestions[5] + "");

                                            }


                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialogFiles.dismiss();
                        }
                    });
                }else{
                    Toast.makeText(getBaseContext(),"NO Suggestions Child",Toast.LENGTH_SHORT).show();
                    progressDialogFiles.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild("Suggestions")) {
                    if (dataSnapshot.getKey().equals("Suggestions")) {
                        totalChilds= (int) dataSnapshot.getChildrenCount();
                        Toast.makeText(getBaseContext(),totalChilds+"",Toast.LENGTH_SHORT).show();

                        databaseReference.removeEventListener(this);
                        suggestionsReference.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                nChild++;
                                if (dataSnapshot.getKey().equals("Names")) {
                                    nSuggestions[0] = (int) dataSnapshot.getChildrenCount();
                                } else if (dataSnapshot.getKey().equals("Animals")) {
                                    nSuggestions[1] = (int) dataSnapshot.getChildrenCount();
                                } else if (dataSnapshot.getKey().equals("Plants")) {
                                    nSuggestions[2] = (int) dataSnapshot.getChildrenCount();
                                } else if (dataSnapshot.getKey().equals("Professions")) {
                                    nSuggestions[3] = (int) dataSnapshot.getChildrenCount();
                                } else if (dataSnapshot.getKey().equals("Colors")) {
                                    nSuggestions[4] = (int) dataSnapshot.getChildrenCount();
                                } else if (dataSnapshot.getKey().equals("Cities")) {
                                    nSuggestions[5] = (int) dataSnapshot.getChildrenCount();
                                }
                                if (nChild == totalChilds) {
                                    suggestionsReference.removeEventListener(this);
                                    progressDialogFiles.dismiss();
                                    numberViews[0].setText(nSuggestions[0] + "");
                                    numberViews[1].setText(nSuggestions[1] + "");
                                    numberViews[2].setText(nSuggestions[2] + "");
                                    numberViews[3].setText(nSuggestions[3] + "");
                                    numberViews[4].setText(nSuggestions[4] + "");
                                    numberViews[5].setText(nSuggestions[5] + "");

                                }


                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialogFiles.dismiss();
            }
        });




    }

    public void populateAndUpdateTempFiles(){

        progressDialogFiles=new ProgressDialog(AdminActivity.this,ProgressDialog.THEME_HOLO_DARK);
        progressDialogFiles.setCancelable(false);
        progressDialogFiles.setCanceledOnTouchOutside(false);
        progressDialogFiles.setMessage("Downloading...0/6");
        progressDialogFiles.show();



        final StorageReference storageReference= FirebaseStorage.getInstance().getReference();

        StorageReference namesReference=storageReference.child("/toUpdateData/names.txt");
        namesReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String path="tempnamesDb.txt";
                try {
                    FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                    fos.write(bytes);
                    fos.close();
                    progressDialogFiles.setMessage("Downloading...1/6");
                    StorageReference animalsReference=storageReference.child("/toUpdateData/animals.txt");
                    animalsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            String path="tempanimalsDb.txt";
                            try {
                                FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                fos.write(bytes);
                                fos.close();
                                progressDialogFiles.setMessage("Downloading...2/6");
                                StorageReference plantsReference=storageReference.child("/toUpdateData/plants.txt");
                                plantsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        String path="tempplantsDb.txt";
                                        try {
                                            FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                            fos.write(bytes);
                                            fos.close();
                                            progressDialogFiles.setMessage("Downloading...3/6");
                                            StorageReference professionsReference=storageReference.child("/toUpdateData/professions.txt");
                                            professionsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                @Override
                                                public void onSuccess(byte[] bytes) {
                                                    String path="tempprofessionsDb.txt";
                                                    try {
                                                        FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                                        fos.write(bytes);
                                                        fos.close();
                                                        progressDialogFiles.setMessage("Downloading...4/6");
                                                        StorageReference colorsReference=storageReference.child("/toUpdateData/colors.txt");
                                                        colorsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                            @Override
                                                            public void onSuccess(byte[] bytes) {
                                                                String path="tempcolorsDb.txt";
                                                                try {
                                                                    FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                                                    fos.write(bytes);
                                                                    fos.close();
                                                                    progressDialogFiles.setMessage("Downloading...5/6");
                                                                    StorageReference citiesReference=storageReference.child("/toUpdateData/cities.txt");
                                                                    citiesReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                                        @Override
                                                                        public void onSuccess(byte[] bytes) {
                                                                            String path="tempcitiesDb.txt";
                                                                            try {
                                                                                FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                                                                fos.write(bytes);
                                                                                fos.close();
                                                                                progressDialogFiles.setMessage("Downloading...6/6");
                                                                                CountDownTimer countDownTimer=new CountDownTimer(1000,1000) {
                                                                                    @Override
                                                                                    public void onTick(long millisUntilFinished) {

                                                                                    }

                                                                                    @Override
                                                                                    public void onFinish() {
                                                                                        Toast.makeText(getBaseContext(),"Download Complete",Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }.start();
                                                                                CategoryTools categoryTools=new CategoryTools(getApplicationContext());
                                                                                Name[] names=categoryTools.readNames("tempnames.txt");
                                                                                for(int i=0;i<names.length;i++){
                                                                                    categoryTools.addName(names[i],"tempnamesDb.txt");
                                                                                }
                                                                                Toast.makeText(getApplicationContext(),myTools.fileString("tempnamesDb.txt"),Toast.LENGTH_SHORT).show();

                                                                                Animal[] animals=categoryTools.readAnimals("tempanimals.txt");
                                                                                for(int i=0;i<animals.length;i++){
                                                                                    categoryTools.addAnimal(animals[i],"tempanimalsDb.txt");
                                                                                }


                                                                                Plant[] plants=categoryTools.readPlants("tempplants.txt");
                                                                                for(int i=0;i<plants.length;i++){
                                                                                    categoryTools.addPlant(plants[i],"tempplantsDb.txt");
                                                                                }


                                                                                Profession[] professions=categoryTools.readProfessions("tempprofessions.txt");

                                                                                for(int i=0;i<professions.length;i++){
                                                                                    categoryTools.addProfession(professions[i],"tempprofessionsDb.txt");
                                                                                }


                                                                                Color[] colors=categoryTools.readColors("tempcolors.txt");
                                                                                for(int i=0;i<colors.length;i++){
                                                                                    categoryTools.addColor(colors[i],"tempcolorsDb.txt");
                                                                                }

                                                                                City[] cities=categoryTools.readCities("tempcities.txt");
                                                                                for(int i=0;i<cities.length;i++){
                                                                                    categoryTools.addCity(cities[i],"tempcitiesDb.txt");
                                                                                }
                                                                                progressDialogFiles.setMessage("Uploading...0/6");
                                                                                StorageReference namesReference=storageReference.child("toUpdateData/names.txt");
                                                                                try {
                                                                                    InputStream c=myTools.inputFileString("tempnamesDb.txt");
                                                                                    namesReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                            progressDialogFiles.setMessage("Uploading...1/6");
                                                                                            StorageReference animalReference=storageReference.child("toUpdateData/animals.txt");
                                                                                            try {
                                                                                                InputStream c=myTools.inputFileString("tempanimalsDb.txt");
                                                                                                animalReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                        progressDialogFiles.setMessage("Uploading...2/6");
                                                                                                        StorageReference plantReference=storageReference.child("toUpdateData/plants.txt");
                                                                                                        try {
                                                                                                            InputStream c=myTools.inputFileString("tempplantsDb.txt");
                                                                                                            plantReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                                    progressDialogFiles.setMessage("Uploading...3/6");
                                                                                                                    StorageReference professionReference=storageReference.child("toUpdateData/professions.txt");
                                                                                                                    try {
                                                                                                                        InputStream c=myTools.inputFileString("tempprofessionsDb.txt");
                                                                                                                        professionReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                                                progressDialogFiles.setMessage("Uploading...4/6");
                                                                                                                                StorageReference colorsReference=storageReference.child("toUpdateData/colors.txt");
                                                                                                                                try {
                                                                                                                                    InputStream c=myTools.inputFileString("tempcolorsDb.txt");
                                                                                                                                    colorsReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                                                        @Override
                                                                                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                                                            progressDialogFiles.setMessage("Uploading...5/6");
                                                                                                                                            StorageReference citiesReference=storageReference.child("toUpdateData/cities.txt");
                                                                                                                                            try {
                                                                                                                                                InputStream c=myTools.inputFileString("tempcitiesDb.txt");
                                                                                                                                                citiesReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                                                                        progressDialogFiles.setMessage("Uploading...6/6");
                                                                                                                                                        Toast.makeText(getBaseContext(),"Uploading Complete",Toast.LENGTH_SHORT).show();
                                                                                                                                                        myTools.clearFile("tempnames.txt");
                                                                                                                                                        myTools.clearFile("tempanimals.txt");
                                                                                                                                                        myTools.clearFile("tempplants.txt");
                                                                                                                                                        myTools.clearFile("tempprofessions.txt");
                                                                                                                                                        myTools.clearFile("tempcolors.txt");
                                                                                                                                                        myTools.clearFile("tempcities.txt");
                                                                                                                                                        progressDialogFiles.dismiss();
                                                                                                                                                    }
                                                                                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                                                                                        Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                                                                                                                                        progressDialogFiles.dismiss();
                                                                                                                                                    }
                                                                                                                                                });
                                                                                                                                            } catch (IOException e) {
                                                                                                                                                e.printStackTrace();
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                                                                        @Override
                                                                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                                                                            Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                                                                                                                            progressDialogFiles.dismiss();
                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                } catch (IOException e) {
                                                                                                                                    e.printStackTrace();
                                                                                                                                }

                                                                                                                            }
                                                                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                                                                            @Override
                                                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                                                Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                                                                                                                progressDialogFiles.dismiss();
                                                                                                                            }
                                                                                                                        });
                                                                                                                    } catch (IOException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                    }

                                                                                                                }
                                                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                                                @Override
                                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                                    Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                                                                                                    progressDialogFiles.dismiss();
                                                                                                                }
                                                                                                            });
                                                                                                        } catch (IOException e) {
                                                                                                            e.printStackTrace();
                                                                                                        }

                                                                                                    }
                                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                                    @Override
                                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                                        Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                                                                                        progressDialogFiles.dismiss();
                                                                                                    }
                                                                                                });
                                                                                            } catch (IOException e) {
                                                                                                e.printStackTrace();
                                                                                            }

                                                                                        }
                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Toast.makeText(getBaseContext(),"Fail",Toast.LENGTH_SHORT).show();
                                                                                            progressDialogFiles.dismiss();
                                                                                        }
                                                                                    });
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                }

                                                                            } catch (FileNotFoundException e) {
                                                                                e.printStackTrace();
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            progressDialogFiles.dismiss();
                                                                            Toast.makeText(getBaseContext(),"Download Failed",Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                } catch (FileNotFoundException e) {
                                                                    e.printStackTrace();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                progressDialogFiles.dismiss();
                                                                Toast.makeText(getBaseContext(),"Download Failed",Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    } catch (FileNotFoundException e) {
                                                        e.printStackTrace();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialogFiles.dismiss();
                                                    Toast.makeText(getBaseContext(),"Download Failed",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialogFiles.dismiss();
                                        Toast.makeText(getBaseContext(),"Download Failed",Toast.LENGTH_SHORT).show();
                                    }
                                });



                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialogFiles.dismiss();
                            Toast.makeText(getBaseContext(),"Download Failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialogFiles.dismiss();
                Toast.makeText(getBaseContext(),"Download Failed",Toast.LENGTH_SHORT).show();
            }
        });


















    }

    public void addTempDBToPermFilesAndUpdate(){
        progressDialogFiles=new ProgressDialog(AdminActivity.this,ProgressDialog.THEME_HOLO_DARK);
        progressDialogFiles.setCancelable(false);
        progressDialogFiles.setCanceledOnTouchOutside(false);
        progressDialogFiles.setMessage("Downloading...0/6");
        progressDialogFiles.show();



        final StorageReference storageReference= FirebaseStorage.getInstance().getReference();

        StorageReference namesReference=storageReference.child("/toUpdateData/names.txt");
        namesReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String path="tempnamesDb.txt";
                try {
                    FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                    fos.write(bytes);
                    fos.close();
                    progressDialogFiles.setMessage("Downloading...1/6");
                    StorageReference animalsReference=storageReference.child("/toUpdateData/animals.txt");
                    animalsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            String path="tempanimalsDb.txt";
                            try {
                                FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                fos.write(bytes);
                                fos.close();
                                progressDialogFiles.setMessage("Downloading...2/6");
                                StorageReference plantsReference=storageReference.child("/toUpdateData/plants.txt");
                                plantsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        String path="tempplantsDb.txt";
                                        try {
                                            FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                            fos.write(bytes);
                                            fos.close();
                                            progressDialogFiles.setMessage("Downloading...3/6");
                                            StorageReference professionsReference=storageReference.child("/toUpdateData/professions.txt");
                                            professionsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                @Override
                                                public void onSuccess(byte[] bytes) {
                                                    String path="tempprofessionsDb.txt";
                                                    try {
                                                        FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                                        fos.write(bytes);
                                                        fos.close();
                                                        progressDialogFiles.setMessage("Downloading...4/6");
                                                        StorageReference colorsReference=storageReference.child("/toUpdateData/colors.txt");
                                                        colorsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                            @Override
                                                            public void onSuccess(byte[] bytes) {
                                                                String path="tempcolorsDb.txt";
                                                                try {
                                                                    FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                                                    fos.write(bytes);
                                                                    fos.close();
                                                                    progressDialogFiles.setMessage("Downloading...5/6");
                                                                    StorageReference citiesReference=storageReference.child("/toUpdateData/cities.txt");
                                                                    citiesReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                                        @Override
                                                                        public void onSuccess(byte[] bytes) {
                                                                            String path="tempcitiesDb.txt";
                                                                            try {
                                                                                FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                                                                fos.write(bytes);
                                                                                fos.close();
                                                                                progressDialogFiles.setMessage("Downloading...6/6");
                                                                                CountDownTimer countDownTimer=new CountDownTimer(1000,1000) {
                                                                                    @Override
                                                                                    public void onTick(long millisUntilFinished) {

                                                                                    }

                                                                                    @Override
                                                                                    public void onFinish() {
                                                                                        Toast.makeText(getBaseContext(),"Download Complete",Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }.start();
                                                                                CategoryTools categoryTools=new CategoryTools(getApplicationContext());
                                                                                Name[] names=categoryTools.readNames("tempnamesDb.txt");
                                                                                for(int i=0;i<names.length;i++){
                                                                                    categoryTools.addName(names[i],"names.txt");
                                                                                }


                                                                                Animal[] animals=categoryTools.readAnimals("tempanimalsDb.txt");
                                                                                for(int i=0;i<animals.length;i++){
                                                                                    categoryTools.addAnimal(animals[i],"animals.txt");
                                                                                }


                                                                                Plant[] plants=categoryTools.readPlants("tempplantsDb.txt");
                                                                                for(int i=0;i<plants.length;i++){
                                                                                    categoryTools.addPlant(plants[i],"plants.txt");
                                                                                }


                                                                                Profession[] professions=categoryTools.readProfessions("tempprofessionsDb.txt");

                                                                                for(int i=0;i<professions.length;i++){
                                                                                    categoryTools.addProfession(professions[i],"professions.txt");
                                                                                }


                                                                                Color[] colors=categoryTools.readColors("tempcolorsDb.txt");
                                                                                for(int i=0;i<colors.length;i++){
                                                                                    categoryTools.addColor(colors[i],"colors.txt");
                                                                                }

                                                                                City[] cities=categoryTools.readCities("tempcitiesDb.txt");
                                                                                for(int i=0;i<cities.length;i++){
                                                                                    categoryTools.addCity(cities[i],"cities.txt");
                                                                                }
                                                                                progressDialogFiles.setMessage("Uploading...0/6");
                                                                                StorageReference namesReference=storageReference.child("/CategoryFiles/names.txt");
                                                                                try {
                                                                                    InputStream c=myTools.inputFileString("names.txt");
                                                                                    namesReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                            progressDialogFiles.setMessage("Uploading...1/6");
                                                                                            StorageReference animalReference=storageReference.child("/CategoryFiles/animals.txt");
                                                                                            try {
                                                                                                InputStream c=myTools.inputFileString("animals.txt");
                                                                                                animalReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                        progressDialogFiles.setMessage("Uploading...2/6");
                                                                                                        StorageReference plantReference=storageReference.child("/CategoryFiles/plants.txt");
                                                                                                        try {
                                                                                                            InputStream c=myTools.inputFileString("plants.txt");
                                                                                                            plantReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                                    progressDialogFiles.setMessage("Uploading...3/6");
                                                                                                                    StorageReference professionReference=storageReference.child("/CategoryFiles/professions.txt");
                                                                                                                    try {
                                                                                                                        InputStream c=myTools.inputFileString("professions.txt");
                                                                                                                        professionReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                                                progressDialogFiles.setMessage("Uploading...4/6");
                                                                                                                                StorageReference colorsReference=storageReference.child("/CategoryFiles/colors.txt");
                                                                                                                                try {
                                                                                                                                    InputStream c=myTools.inputFileString("colors.txt");
                                                                                                                                    colorsReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                                                        @Override
                                                                                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                                                            progressDialogFiles.setMessage("Uploading...5/6");
                                                                                                                                            StorageReference citiesReference=storageReference.child("/CategoryFiles/cities.txt");
                                                                                                                                            try {
                                                                                                                                                InputStream c=myTools.inputFileString("cities.txt");
                                                                                                                                                citiesReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                                                                                        progressDialogFiles.setMessage("Uploading...6/6");
                                                                                                                                                        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Updates");
                                                                                                                                                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                                            @Override
                                                                                                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                                                                                                int currentUpdateIndex=dataSnapshot.child("UpdateIndex").getValue(Integer.class);
                                                                                                                                                                currentUpdateIndex++;
                                                                                                                                                                databaseReference.child("UpdateIndex").setValue(currentUpdateIndex).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                    @Override
                                                                                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                                                                                        Toast.makeText(getBaseContext(),"Uploading Complete",Toast.LENGTH_SHORT).show();
                                                                                                                                                                        progressDialogFiles.dismiss();
                                                                                                                                                                    }
                                                                                                                                                                });
                                                                                                                                                            }

                                                                                                                                                            @Override
                                                                                                                                                            public void onCancelled(DatabaseError databaseError) {
                                                                                                                                                            }
                                                                                                                                                        });
                                                                                                                                                    }
                                                                                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                                                                                        Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                                                                                                                                        progressDialogFiles.dismiss();
                                                                                                                                                    }
                                                                                                                                                });
                                                                                                                                            } catch (IOException e) {
                                                                                                                                                e.printStackTrace();
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                                                                        @Override
                                                                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                                                                            Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                                                                                                                            progressDialogFiles.dismiss();
                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                } catch (IOException e) {
                                                                                                                                    e.printStackTrace();
                                                                                                                                }

                                                                                                                            }
                                                                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                                                                            @Override
                                                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                                                Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                                                                                                                progressDialogFiles.dismiss();
                                                                                                                            }
                                                                                                                        });
                                                                                                                    } catch (IOException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                    }

                                                                                                                }
                                                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                                                @Override
                                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                                    Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                                                                                                    progressDialogFiles.dismiss();
                                                                                                                }
                                                                                                            });
                                                                                                        } catch (IOException e) {
                                                                                                            e.printStackTrace();
                                                                                                        }

                                                                                                    }
                                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                                    @Override
                                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                                        Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                                                                                        progressDialogFiles.dismiss();
                                                                                                    }
                                                                                                });
                                                                                            } catch (IOException e) {
                                                                                                e.printStackTrace();
                                                                                            }

                                                                                        }
                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Toast.makeText(getBaseContext(),"Fail",Toast.LENGTH_SHORT).show();
                                                                                            progressDialogFiles.dismiss();
                                                                                        }
                                                                                    });
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                }

                                                                            } catch (FileNotFoundException e) {
                                                                                e.printStackTrace();
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            progressDialogFiles.dismiss();
                                                                            Toast.makeText(getBaseContext(),"Download Failed",Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                } catch (FileNotFoundException e) {
                                                                    e.printStackTrace();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                progressDialogFiles.dismiss();
                                                                Toast.makeText(getBaseContext(),"Download Failed",Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    } catch (FileNotFoundException e) {
                                                        e.printStackTrace();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialogFiles.dismiss();
                                                    Toast.makeText(getBaseContext(),"Download Failed",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialogFiles.dismiss();
                                        Toast.makeText(getBaseContext(),"Download Failed",Toast.LENGTH_SHORT).show();
                                    }
                                });



                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialogFiles.dismiss();
                            Toast.makeText(getBaseContext(),"Download Failed",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialogFiles.dismiss();
                Toast.makeText(getBaseContext(),"Download Failed",Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void addTempToPermFilesAndUpdate(){
        progressDialogFiles=new ProgressDialog(AdminActivity.this,ProgressDialog.THEME_HOLO_DARK);
        progressDialogFiles.setCancelable(false);
        progressDialogFiles.setCanceledOnTouchOutside(false);
        progressDialogFiles.setMessage("Downloading...0/6");
        progressDialogFiles.show();



        final StorageReference storageReference= FirebaseStorage.getInstance().getReference();


        CategoryTools categoryTools=new CategoryTools(getApplicationContext());
        Name[] names=categoryTools.readNames("tempnames.txt");
        Toast.makeText(getBaseContext(),"tempnamesLength "+ names.length,Toast.LENGTH_SHORT).show();
        for(int i=0;i<names.length;i++){
            categoryTools.addName(names[i],"names.txt");
        }


        Animal[] animals=categoryTools.readAnimals("tempanimals.txt");
        Toast.makeText(getBaseContext(),"tempanimalsLength "+ animals.length,Toast.LENGTH_SHORT).show();
        for(int i=0;i<animals.length;i++){
            categoryTools.addAnimal(animals[i],"animals.txt");
        }


        Plant[] plants=categoryTools.readPlants("tempplants.txt");
        Toast.makeText(getBaseContext(),"tempplantsLength "+ plants.length,Toast.LENGTH_SHORT).show();
        for(int i=0;i<plants.length;i++){
            categoryTools.addPlant(plants[i],"plants.txt");
        }


        Profession[] professions=categoryTools.readProfessions("tempprofessions.txt");
        Toast.makeText(getBaseContext(),"tempprofessionsLength "+ professions.length,Toast.LENGTH_SHORT).show();
        for(int i=0;i<professions.length;i++){
            categoryTools.addProfession(professions[i],"professions.txt");
        }


        Color[] colors=categoryTools.readColors("tempcolors.txt");
        Toast.makeText(getBaseContext(),"tempcolorsLength "+ colors.length,Toast.LENGTH_SHORT).show();
        for(int i=0;i<colors.length;i++){
            categoryTools.addColor(colors[i],"colors.txt");
        }

        City[] cities=categoryTools.readCities("tempcities.txt");
        Toast.makeText(getBaseContext(),"tempcitiesLength "+ cities.length,Toast.LENGTH_SHORT).show();
        for(int i=0;i<cities.length;i++){
            categoryTools.addCity(cities[i],"cities.txt");
        }
        progressDialogFiles.setMessage("Uploading...0/6");
        StorageReference namesReference=storageReference.child("/CategoryFiles/names.txt");
        try {
            InputStream c=myTools.inputFileString("names.txt");
            namesReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialogFiles.setMessage("Uploading...1/6");
                    StorageReference animalReference=storageReference.child("/CategoryFiles/animals.txt");
                    try {
                        InputStream c=myTools.inputFileString("animals.txt");
                        animalReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialogFiles.setMessage("Uploading...2/6");
                                StorageReference plantReference=storageReference.child("/CategoryFiles/plants.txt");
                                try {
                                    InputStream c=myTools.inputFileString("plants.txt");
                                    plantReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            progressDialogFiles.setMessage("Uploading...3/6");
                                            StorageReference professionReference=storageReference.child("/CategoryFiles/professions.txt");
                                            try {
                                                InputStream c=myTools.inputFileString("professions.txt");
                                                professionReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        progressDialogFiles.setMessage("Uploading...4/6");
                                                        StorageReference colorsReference=storageReference.child("/CategoryFiles/colors.txt");
                                                        try {
                                                            InputStream c=myTools.inputFileString("colors.txt");
                                                            colorsReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    progressDialogFiles.setMessage("Uploading...5/6");
                                                                    StorageReference citiesReference=storageReference.child("/CategoryFiles/cities.txt");
                                                                    try {
                                                                        InputStream c=myTools.inputFileString("cities.txt");
                                                                        citiesReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                progressDialogFiles.setMessage("Uploading...6/6");
                                                                                final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Updates");
                                                                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                        int currentUpdateIndex=dataSnapshot.child("UpdateIndex").getValue(Integer.class);
                                                                                        currentUpdateIndex++;

                                                                                        databaseReference.child("UpdateIndex").setValue(currentUpdateIndex).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                Toast.makeText(getBaseContext(),"Uploading Complete",Toast.LENGTH_SHORT).show();
                                                                                                progressDialogFiles.dismiss();
                                                                                                myTools.clearFile("tempnames.txt");
                                                                                                myTools.clearFile("tempanimals.txt");
                                                                                                myTools.clearFile("tempplants.txt");
                                                                                                myTools.clearFile("tempprofessions.txt");
                                                                                                myTools.clearFile("tempcolors.txt");
                                                                                                myTools.clearFile("tempcities.txt");

                                                                                            }
                                                                                        });
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(DatabaseError databaseError) {
                                                                                    }
                                                                                });
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                                                                progressDialogFiles.dismiss();
                                                                            }
                                                                        });
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                                                    progressDialogFiles.dismiss();
                                                                }
                                                            });
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                                        progressDialogFiles.dismiss();
                                                    }
                                                });
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                            progressDialogFiles.dismiss();
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getBaseContext(),"Uploading Fail",Toast.LENGTH_SHORT).show();
                                progressDialogFiles.dismiss();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(),"Fail",Toast.LENGTH_SHORT).show();
                    progressDialogFiles.dismiss();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public void sendMessages(){
        suggestionMessageAdmins=categoryTools.readSuggestionMessages();

        progressDialogFiles=new ProgressDialog(AdminActivity.this,ProgressDialog.THEME_HOLO_DARK);
        progressDialogFiles.setCancelable(false);
        progressDialogFiles.setCanceledOnTouchOutside(false);
        progressDialogFiles.setMessage("Sending Messages...0/"+suggestionMessageAdmins.length);
        progressDialogFiles.show();


        sendMessage(0);

    }

    public void sendMessage(final int i){
        String ID=suggestionMessageAdmins[i].ID;
        DatabaseReference messageReceiverDatabase=databaseReference.child("Users").child(ID).child("Messages");
        messageReceiverDatabase.push().setValue(suggestionMessageAdmins[i]).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(i==suggestionMessageAdmins.length-1){
                    progressDialogFiles.dismiss();
                    myTools.clearFile("suggestionmessages.txt");
                    Toast.makeText(getBaseContext(),"Messages Sending completed succesfully",Toast.LENGTH_LONG).show();
                }else{
                    progressDialogFiles.setMessage("Sending Messages..."+(i+1)+"/"+suggestionMessageAdmins.length);
                    sendMessage(i+1);
                }
            }
        });
    }






    public void emptyTempFileFromDb(){
        myTools.clearFile("tempnames.txt");
        myTools.clearFile("tempanimals.txt");
        myTools.clearFile("tempplants.txt");
        myTools.clearFile("tempprofessions.txt");
        myTools.clearFile("tempcolors.txt");
        myTools.clearFile("tempcities.txt");


        final StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        progressDialogFiles.setMessage("Deleting Temp...0/6");
        progressDialogFiles.show();
        StorageReference namesReference=storageReference.child("/CategoryFiles/names.txt");
        try {
            InputStream c=myTools.inputFileString("tempnames.txt");
            namesReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialogFiles.setMessage("Deleting Temp...1/6");
                    StorageReference animalReference=storageReference.child("/CategoryFiles/animals.txt");
                    try {
                        InputStream c=myTools.inputFileString("tempanimals.txt");
                        animalReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialogFiles.setMessage("Deleting Temp...2/6");
                                StorageReference plantReference=storageReference.child("/CategoryFiles/plants.txt");
                                try {
                                    InputStream c=myTools.inputFileString("tempplants.txt");
                                    plantReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            progressDialogFiles.setMessage("Deleting Temp...3/6");
                                            StorageReference professionReference=storageReference.child("/CategoryFiles/professions.txt");
                                            try {
                                                InputStream c=myTools.inputFileString("tempprofessions.txt");
                                                professionReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        progressDialogFiles.setMessage("Deleting Temp...4/6");
                                                        StorageReference colorsReference=storageReference.child("/CategoryFiles/colors.txt");
                                                        try {
                                                            InputStream c=myTools.inputFileString("tempcolors.txt");
                                                            colorsReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    progressDialogFiles.setMessage("Deleting Temp...5/6");
                                                                    StorageReference citiesReference=storageReference.child("/CategoryFiles/cities.txt");
                                                                    try {
                                                                        InputStream c=myTools.inputFileString("tempcities.txt");
                                                                        citiesReference.putStream(c).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                progressDialogFiles.setMessage("Deleting Temp...6/6");
                                                                                Toast.makeText(getBaseContext(),"Deleting Temp Complete",Toast.LENGTH_SHORT).show();
                                                                                progressDialogFiles.dismiss();

                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(getBaseContext(),"Deleting Temp Fail",Toast.LENGTH_SHORT).show();
                                                                                progressDialogFiles.dismiss();
                                                                            }
                                                                        });
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getBaseContext(),"Deleting Temp Fail",Toast.LENGTH_SHORT).show();
                                                                    progressDialogFiles.dismiss();
                                                                }
                                                            });
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getBaseContext(),"Deleting Temp Fail",Toast.LENGTH_SHORT).show();
                                                        progressDialogFiles.dismiss();
                                                    }
                                                });
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getBaseContext(),"Deleting Temp Fail",Toast.LENGTH_SHORT).show();
                                            progressDialogFiles.dismiss();
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getBaseContext(),"Deleting Temp Fail",Toast.LENGTH_SHORT).show();
                                progressDialogFiles.dismiss();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(),"Deleting Temp Fail",Toast.LENGTH_SHORT).show();
                    progressDialogFiles.dismiss();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


}
