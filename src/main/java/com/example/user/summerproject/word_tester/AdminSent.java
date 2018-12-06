package com.example.user.summerproject.word_tester;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AdminSent extends AppCompatActivity{

    private TextView category,playerSuggestion,indicator;
    private Spinner myMessage,points;
    private Button next;
    private EditText myMessageText;


    private ProgressDialog progressDialogFiles;

    private CheckBox ok;
    private int index;
    private int maxIndex;
    private DatabaseReference suggestionsReference;
    private DatabaseReference categoryReference;
    private String TAG;
    private int childCount;
    private int maxChildCount;
    private int suggestionCount;
    private boolean last=false;
    private LisgarTools myTools;
    private CategoryTools categoryTools=new CategoryTools(this);

    private Suggestion[] suggestions;


    private ArrayList<Suggestion> suggestionsConfirmed = new ArrayList<>();
    private ArrayList<SuggestionMessageAdmin> suggestionMessageConfirmed=new ArrayList<>();
    private ArrayList<Suggestion> suggestionsDenied=new ArrayList<>();
    private ArrayList<SuggestionMessageAdmin> suggestionMessageDenied=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sent);

        Bundle extras = getIntent().getExtras();
        TAG = extras.getString("TAG");
        childCount=0;
        maxChildCount=extras.getInt("maxChildCount");
        suggestions=new Suggestion[maxChildCount];



        category = (TextView) findViewById(R.id.textView);
        playerSuggestion = (TextView) findViewById(R.id.textView7);
        points = (Spinner) findViewById(R.id.spinner2);
        myMessage = (Spinner) findViewById(R.id.spinner);
        ok = (CheckBox) findViewById(R.id.checkBox);
        next = (Button) findViewById(R.id.next);
        myMessageText = (EditText) findViewById(R.id.my_message);
        indicator=(TextView)findViewById(R.id.indicator);



        myMessage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myMessageText.setText(myMessage.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ok.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    points.setSelection(4);
                    myMessageText.setText("Σε ευχαριστούμε για την πρόταση");
                }else {
                    myMessageText.setText("");
                }
            }
        });

        myTools = new LisgarTools(this);
        category.setText(translate(TAG));

        suggestionsReference = FirebaseDatabase.getInstance().getReference().child("Suggestions");
        categoryReference=suggestionsReference.child(TAG);


        suggestionCount=0;
        indicator.setText(0+"/"+(maxChildCount));
        next.setText("Εναρξη");



        points.setSelection(2);

        getCategorySuggestions(TAG);

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


    public void nextSuggestion(View v){


        if(next.getText().equals("Εναρξη")) {

            next.setText("Επομενο");
            indicator.setText((suggestionCount+1)+"/"+(maxChildCount));
            if (suggestionCount+1==maxChildCount){
                last=true;
                next.setText("Τελος");
            }
            showNextSuggestion();

        }else if(suggestionCount+1==maxChildCount){

            handlePreviousSuggestion();

        } else{

            handlePreviousSuggestion();


        }

    }


    public void handlePreviousSuggestion(){


        String playerSuggestion=suggestions[suggestionCount].suggestion;
        final String categorySuggestion=translate(TAG);
        String ourMessage=myMessageText.getText().toString();
        int playerPoints=getIntFromSpinnerValue(points.getSelectedItem().toString());
        String ID=suggestions[suggestionCount].ID;

        SuggestionMessageAdmin suggestionMessageAdmin=new SuggestionMessageAdmin(playerSuggestion,categorySuggestion,ourMessage,playerPoints,ID);


        if(ok.isChecked()){
            //check if in suggestionsConfirmed and do the rest
            boolean existsInConfirmed=false;
            for(int i=0;i<suggestionsConfirmed.size();i++){
                if(suggestionsConfirmed.get(i).suggestion.equals(playerSuggestion)){
                    existsInConfirmed=true;
                    break;
                }
            }
            if(!existsInConfirmed){
                suggestionsConfirmed.add(suggestions[suggestionCount]);
                suggestionMessageConfirmed.add(suggestionMessageAdmin);
                addElement(TAG,playerSuggestion);
            }
        }else {
            //check if in suggestionsDenied and do the rest
            boolean existsInDenied=false;
            for(int i=0;i<suggestionsDenied.size();i++){
                if(suggestionsDenied.get(i).suggestion.equals(playerSuggestion)){
                    existsInDenied=true;
                    break;
                }
            }
            if(!existsInDenied){
                suggestionsDenied.add(suggestions[suggestionCount]);
                suggestionMessageDenied.add(suggestionMessageAdmin);
            }
        }

        categoryTools.addSuggestionMessage(suggestionMessageAdmin);




        progressDialogFiles=new ProgressDialog(AdminSent.this,ProgressDialog.THEME_HOLO_DARK);
        progressDialogFiles.setCancelable(false);
        progressDialogFiles.setCanceledOnTouchOutside(false);
        progressDialogFiles.setMessage("Deleting Suggestion...");
        progressDialogFiles.show();

        categoryReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild("ID")){
                    String hash=dataSnapshot.getKey();
                    String ID=dataSnapshot.child("ID").getValue(String.class);
                    String sugg=dataSnapshot.child("suggestion").getValue(String.class);

                    if( ID.equals(suggestions[suggestionCount].ID) && sugg.equals(suggestions[suggestionCount].suggestion)){
                        categoryReference.removeEventListener(this);
                        categoryReference.child(hash).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                if(last){
                                    progressDialogFiles.dismiss();
                                    finish();
                                }else{
                                    progressDialogFiles.dismiss();
                                    suggestionCount++;
                                    indicator.setText((suggestionCount+1)+"/"+(maxChildCount));
                                    if (suggestionCount+1==maxChildCount){
                                        last=true;
                                        next.setText("Τελος");
                                    }

                                    showNextSuggestion();
                                }


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

            }
        });

    }


    public void showNextSuggestion(){
        String playerSuggestion=suggestions[suggestionCount].suggestion;
        String categorySuggestion=translate(TAG);
        String ourMessage=myMessageText.getText().toString();
        int playerPoints=getIntFromSpinnerValue(points.getSelectedItem().toString());
        String ID=suggestions[suggestionCount].ID;


        this.playerSuggestion.setText(playerSuggestion);

        //check if in suggestionsConfirmed and populate the next view
        int confirmedIndex=0;
        boolean existsInConfirmed=false;
        for(int i=0;i<suggestionsConfirmed.size();i++){
            if(suggestionsConfirmed.get(i).suggestion.equals(playerSuggestion)){
                existsInConfirmed=true;
                confirmedIndex=i;
                break;
            }
        }

        if(existsInConfirmed){
            //populate the next view with messageConfirmed[confirmedIndex]
            this.ok.setChecked(true);
            this.myMessageText.setText( suggestionMessageConfirmed.get(confirmedIndex).ourMessage );
            this.points.setSelection(    getSpinnerIndexFromPointsValue(  suggestionMessageConfirmed.get(confirmedIndex).points  )    );



            Toast.makeText(getBaseContext(),"Απλα πατα next. Απαντησες ηδη σε ιδια προταση.",Toast.LENGTH_SHORT).show();
        }else{
            int deniedIndex=0;
            boolean existsInDenied=false;
            for(int i=0;i<suggestionsDenied.size();i++){
                if(suggestionsDenied.get(i).suggestion.equals(playerSuggestion)){
                    existsInDenied=true;
                    deniedIndex=i;
                    break;
                }
            }
            if(existsInDenied){
                //populate the next view with messageDenied[confirmedIndex]
                this.ok.setChecked(false);
                this.myMessageText.setText( suggestionMessageDenied.get(deniedIndex).ourMessage );
                this.points.setSelection(    getSpinnerIndexFromPointsValue(  suggestionMessageDenied.get(deniedIndex).points  )    );




                Toast.makeText(getBaseContext(),"Απλα πατα next. Απαντησες ηδη σε ιδια προταση.",Toast.LENGTH_SHORT).show();
            }else{
                this.ok.setChecked(false);
                this.myMessageText.setText(" ");
                this.points.setSelection( 2 );

            }
        }

    }



    public void addElement(String TAG,String playerSuggestion){
        switch (TAG){
            case "Names":
                Name name=new Name(playerSuggestion);
                categoryTools.addName(name,"tempnames.txt");
                break;
            case "Animals":
                Animal animal=new Animal(playerSuggestion);
                categoryTools.addAnimal(animal,"tempanimals.txt");
                break;
            case "Plants":
                Plant plant=new Plant(playerSuggestion);
                categoryTools.addPlant(plant,"tempplants.txt");
                break;
            case "Professions":
                Profession profession=new Profession(playerSuggestion);
                categoryTools.addProfession(profession,"tempprofessions.txt");
                break;
            case "Colors":
                Color color=new Color(playerSuggestion);
                categoryTools.addColor(color,"tempcolors.txt");
                break;
            case "Cities":
                City city=new City(playerSuggestion);
                categoryTools.addCity(city,"tempcities.txt");
                break;
        }
    }

    public int getIntFromSpinnerValue(String spinnerValue){
        int pointsValue=0;
        switch (spinnerValue){
            case "-20%":
                pointsValue=-20;
                break;
            case "-10%":
                pointsValue=-10;
                break;
            case "0%":
                pointsValue=-0;
                break;
            case "10%":
                pointsValue=10;
                break;
            case "20%":
                pointsValue=20;
                break;
        }
        return pointsValue;
    }

    public int getSpinnerIndexFromPointsValue(int points){
        int index=0;
        switch (points){
            case -20:
                index=0;
                break;
            case -10:
                index=1;
                break;
            case 0:
                index=2;
                break;
            case 10:
                index=3;
                break;
            case 20:
                index=4;
                break;
        }
        return index;
    }

    public void getCategorySuggestions(String TAG){

        progressDialogFiles=new ProgressDialog(AdminSent.this,ProgressDialog.THEME_HOLO_DARK);
        progressDialogFiles.setCancelable(false);
        progressDialogFiles.setCanceledOnTouchOutside(false);
        progressDialogFiles.setMessage("Downloading Suggestion...");
        progressDialogFiles.show();
        categoryReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild("ID")){
                    String ID=dataSnapshot.child("ID").getValue(String.class);
                    String sugg=dataSnapshot.child("suggestion").getValue(String.class);
                    Suggestion suggestion=new Suggestion(ID,sugg);
                    suggestions[childCount]=suggestion;
                    childCount++;
                    if(childCount==maxChildCount){
                        categoryReference.removeEventListener(this);
                        progressDialogFiles.dismiss();
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

            }
        });

    }

    public String translate(String categoryName){
        String name="";
        switch (categoryName){
            case "Names":
                name= "Όνομα";
                break;
            case "Animals":
                name= "Ζώο";
            break;
            case "Plants":
                name= "Φυτό";
            break;
            case "Professions":
                name= "Επάγγελμα";
            break;
            case "Colors":
                name= "Χρώμα";
            break;
            case "Cities":
                name= "Χώρα/Πόλη";
            break;
        }
        return name;
    }



    public void openTester(View v){
        Intent newTester = new Intent(AdminSent.this, Tester_Activity.class);
        newTester.putExtra("sounds",true);
        newTester.putExtra("mID","admin");
        startActivity(newTester);
    }
}
