package com.example.user.summerproject;

import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.summerproject.myTools.LisgarTools;
import com.example.user.summerproject.myTools.Quicksort;
import com.example.user.summerproject.myTools.TimeHandler;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindFriendsActivity extends AppCompatActivity {

    private DatabaseReference userDatabase;
    private DatabaseReference publicUserDatabase;
    private String mID="";
    private String playerName;
    private String[] friendNames;
    private LisgarTools myTools;
    private PopupWindow alertPopUp;
    private View popView;
    private LayoutInflater layoutInflater;
    private int windowHeight;
    private int windowWidth;

    private Typeface chalkTypeface;
    private DatabaseReference publicUsers;
    private GridView publicGridView;
    private ArrayList<String> publicArrayList=new ArrayList<>();
    private ArrayList<String> publicIDArrayList=new ArrayList<>();
    private ArrayList<Long>   publicTimeArrayList=new ArrayList<>();
    private ArrayAdapter<String> publicArrayAdapter;
    private ChildEventListener publicListener;
    private int publicUsersCount;
    private String[] names,IDs;
    private long[] times;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        Bundle extras=getIntent().getExtras();
        mID=extras.getString("mID");
        playerName=extras.getString("playerName");
        friendNames=extras.getStringArray("friendNames");

        myTools=new LisgarTools(this);


        userDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(mID);
        publicUserDatabase=FirebaseDatabase.getInstance().getReference().child("PublicUsers").child(mID);
        publicUsers=FirebaseDatabase.getInstance().getReference().child("PublicUsers");


        chalkTypeface= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/romanscript.ttf");


        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popView=layoutInflater.inflate(R.layout.alert_dialog_custom,null);

        //=======getting window size============
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        windowWidth= size.x;
        windowHeight = size.y;

        Switch showSwitch=(Switch)findViewById(R.id.show_me_switch);
        showSwitch.setChecked(ViewerActivity.publicNickname);
        showSwitch.setTextOn(" ");
        showSwitch.setTextOff(" ");
        showSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ViewerActivity.publicNickname=true;
                    userDatabase.child("publicNickname").setValue(true);
                    publicUserDatabase.child("Name").setValue(playerName);
                    publicUserDatabase.child("timeLogged").setValue(System.currentTimeMillis());
                }else{
                    ViewerActivity.publicNickname=false;
                    userDatabase.child("publicNickname").setValue(false);
                    publicUserDatabase.setValue(null);
                }
            }
        });



        publicListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name=dataSnapshot.child("Name").getValue(String.class);
                publicArrayList.add(name);
                String id=dataSnapshot.getKey();
                publicIDArrayList.add(id);
                Long timeLogged=dataSnapshot.child("timeLogged").getValue(Long.class);
                publicTimeArrayList.add(timeLogged);
                if(publicUsersCount==publicArrayList.size()){
                    publicUsers.removeEventListener(this);

                    sortAndShow();
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
        };


        publicUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                publicUsersCount=(int)dataSnapshot.getChildrenCount();
                publicUsers.addChildEventListener(publicListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        publicGridView=(GridView)findViewById(R.id.public_grid_view);
        publicArrayAdapter=new ArrayAdapter<String>(this, R.layout.public_text_view,publicArrayList);
        publicGridView.setAdapter(publicArrayAdapter);
        publicArrayAdapter.notifyDataSetChanged();

    }

    public void sortAndShow(){

        long now=System.currentTimeMillis();
        int size=publicArrayList.size();
        names=new String[size];
        IDs=new String[size];
        times=new long[size];
        for(int i=0;i<size;i++){
            names[i]=publicArrayList.get(i);
            IDs[i]=publicIDArrayList.get(i);
            times[i]=publicTimeArrayList.get(i);

        }




        //delete those that are not usefull
        ArrayList<Integer> toRemoveIndeces=new ArrayList<>();
        for(int i=0;i<size;i++){
            boolean inFriends=false;
            for(int j=0;j<friendNames.length;j++){
                if(names[i].equals(friendNames[j])){
                    inFriends=true;
                }
            }
            if(TimeHandler.DateTimeDifference(now,times[i], TimeHandler.TimeDifference.MINUTE)>=1440){
                toRemoveIndeces.add(i);
                publicUsers.child(IDs[i]).setValue(null);
            }else if(names[i].equals(playerName)){
                toRemoveIndeces.add(i);
            }else if(inFriends){
                toRemoveIndeces.add(i);
            }
        }



        //construct again the 3 arrays
        size=publicArrayList.size()-toRemoveIndeces.size();
        names=new String[size];
        IDs=new String[size];
        times=new long[size];
        int index=0;
        for(int i=0;i<publicArrayList.size();i++){
            boolean inRemoveIndeces=false;
            for(int j=0;j<toRemoveIndeces.size();j++){
                if(i==toRemoveIndeces.get(j)){
                    inRemoveIndeces=true;
                }
            }
            if(!inRemoveIndeces){
                names[index]=publicArrayList.get(i);
                IDs[index]=publicIDArrayList.get(i);
                times[index]=publicTimeArrayList.get(i);
                times[index]= TimeHandler.DateTimeDifference(now,times[index], TimeHandler.TimeDifference.MINUTE);
                index++;
            }


        }
        //sort them by time logged in and print
        Quicksort quicksort=new Quicksort();
        quicksort.sort(times,IDs,names);

        publicArrayList.clear();
        for(int i=0;i<size;i++){
            String trash=names[i]+"\n"+getTimeString(times[i]);
            publicArrayList.add(trash);
        }




        publicArrayAdapter.notifyDataSetChanged();
    }

    private String getTimeString(long time){
        String timeString="";
        int hours=(int)time/60;
        int minutes=(int)time%60;
        if(hours==0){
            timeString=minutes+" λεπτ.";
        }else{
            timeString=hours+" ωρ. "+minutes+" λε.";
        }
        return timeString;
    }

    public void public_click(View view){
        TextView textView=(TextView)view;
        String mixedText=textView.getText().toString();
        final String requestName=mixedText.split("\n")[0];
        final String requestID;

        alertPopUp=new PopupWindow(popView,(int)(0.95*windowWidth),(int)(0.3*windowHeight),true);
        alertPopUp.setAnimationStyle(R.style.AnimationStyle);
        alertPopUp.setOutsideTouchable(false);
        alertPopUp.setBackgroundDrawable(new BitmapDrawable());

        for(int i=0;i<names.length;i++){
            if(names[i].equals(requestName)){
                requestID=IDs[i];
                userDatabase.child("Requests").child("RequestsSent").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(requestID)){
                            Toast toast=Toast.makeText(getBaseContext(),"Το αίτημα έχει αποσταλεί. Περιμένετε την απάντηση.",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            toast.show();
                        }else{
                            userDatabase.child("Requests").child("RequestsReceived").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(requestID)){
                                        Toast toast=Toast.makeText(getBaseContext(),"Έχεις δεχτεί αίτημα από "+requestName,Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                                        toast.show();
                                    }else{
                                        if(!requestName.equals("")) {
                                            TextView alertText=(TextView)popView.findViewById(R.id.alert_message);
                                            alertText.setTypeface(chalkTypeface);
                                            alertText.setText("Αίτημα σε \n' "+requestName+" ' ;");

                                            Button yesButton=(Button)popView.findViewById(R.id.yes_button);
                                            yesButton.setTypeface(chalkTypeface);
                                            yesButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if(MainActivity.hasInternet) {
                                                        myTools.sendRequest(playerName,mID,requestName,requestID);
                                                        Toast toast=Toast.makeText(getBaseContext(),"Στάλθηκε αίτημα φιλίας",Toast.LENGTH_SHORT);
                                                        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                                                        toast.show();
                                                    }else{
                                                        Toast toast=Toast.makeText(getBaseContext(),"Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT);
                                                        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                                                        toast.show();
                                                    }
                                                    alertPopUp.dismiss();
                                                }
                                            });

                                            Button noButton=(Button)popView.findViewById(R.id.no_button);
                                            noButton.setTypeface(chalkTypeface);
                                            noButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    alertPopUp.dismiss();
                                                }
                                            });




                                            alertPopUp.showAtLocation(findViewById(R.id.friends),Gravity.CENTER,0,0);


                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            }
        }

    }
    public void backClicked(View v){
        finish();
    }
}
