package com.example.user.summerproject;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.summerproject.myTools.FriendsTextView;
import com.example.user.summerproject.myTools.LevelHandler;
import com.example.user.summerproject.myTools.LisgarTools;
import com.example.user.summerproject.myTools.NetworkChangeReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private EditText friendNameText;
    private TextView addFriendButton;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserList;
    private DatabaseReference userDatabase;


    private int windowHeight;
    private int windowWidth;

    private PopupWindow alertPopUp;
    private View alertPopView;
    private DatabaseReference userFriends;
    private DatabaseReference userRequests;
    private Typeface chalkTypeface;

    private NetworkChangeReceiver networkChangeReceiver;

    private String playerName;
    private int playerLvl;
    private String playerDistString;
    private int opLvl;
    private String[] friendIds;
    private boolean insertbyMe=false;
    private String friendNickname="";
    private String user_id="";
    private boolean friendExist=false;
    private GridView friendsGridView;
    private GridView requestsGridView;

    private String deleteID;

    private LisgarTools myTools;

    private boolean sounds;




    private ArrayList<String> friendNameArrayList =new ArrayList<>();
    private ArrayAdapter<String> friendArrayAdapter;

    private ArrayList<String> friendIDArrayList=new ArrayList<>();

    private ArrayList<String> requestNameArrayList =new ArrayList<>();
    private ArrayAdapter<String> requestArrayAdapter;

    private ArrayList<String> requestIDArrayList=new ArrayList<>();

    private LayoutInflater layoutInflater;


    private ChildEventListener friendListener;
    private ChildEventListener requestListener;
    private int previousLength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        myTools = new LisgarTools(this);

        networkChangeReceiver=new NetworkChangeReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(networkChangeReceiver,filter);

        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);


        //=======getting window size============
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        windowWidth= size.x;
        windowHeight = size.y;


        stopService(new Intent(FriendsActivity.this,FriendNotificationService.class));

        Bundle extras=getIntent().getExtras();
        playerName=extras.getString("name");
        sounds=extras.getBoolean("sounds");
        playerLvl=extras.getInt("playerLevel");
        playerDistString=extras.getString("playerDistString");

        chalkTypeface=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/romanscript.ttf");

        friendNameText=(EditText)findViewById(R.id.friendname_text);
        friendNameText.clearFocus();
        friendNameText.setTypeface(chalkTypeface);

        TextView requestMessage=(TextView)findViewById(R.id.request_message);
        requestMessage.setTypeface(chalkTypeface);

        TextView requestTitle=(TextView)findViewById(R.id.request_title);
        requestTitle.setTypeface(chalkTypeface);

        TextView deleteTitle=(TextView)findViewById(R.id.delete_title);
        deleteTitle.setTypeface(chalkTypeface);

        TextView deleteMessage=(TextView)findViewById(R.id.delete_message);
        deleteMessage.setTypeface(chalkTypeface);

        TextView friendsTitle=(TextView)findViewById(R.id.friends_title);
        friendsTitle.setTypeface(chalkTypeface);

        TextView findFriends=(TextView)findViewById(R.id.find_friends_text);
        findFriends.setTypeface(chalkTypeface);

        findFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FriendsActivity.this, FindFriendsActivity.class);
                intent.putExtra("mID",user_id);
                intent.putExtra("playerName",playerName);
                String[] friendNames=new String[friendNameArrayList.size()];
                for(int i=0;i<friendNameArrayList.size();i++){
                    friendNames[i]=friendNameArrayList.get(i);
                }
                intent.putExtra("friendNames",friendNames);
                startActivity(intent);
            }
        });


        alertPopView=layoutInflater.inflate(R.layout.alert_dialog_custom,null);

        final String font=getResources().getString(R.string.font_name);
        final Typeface mTypeface= Typeface.createFromAsset(getApplicationContext().getAssets(),font);

        addFriendButton=(TextView)findViewById(R.id.friend_add_button);

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.hasInternet) {
                    friendNickname = friendNameText.getText().toString().trim();
                    if (friendNameArrayList != null) {

                        for (int i = 0; i < friendNameArrayList.size(); i++) {

                            if (friendNickname.equals(friendNameArrayList.get(i))) {
                                friendExist = true;
                            }

                        }


                    }

                    //==========
                    insertbyMe = true;
                    mUserList.child("asdfasasdfdfasdfasdfasdf").setValue("asdfasdfasdfasasdfdfasdf");
                    //==========
                }else{
                    Toast toast=Toast.makeText(getBaseContext(),"Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                }
            }
        });




        final MediaPlayer mpWriting = MediaPlayer.create(this, R.raw.chalkwriting);
        final MediaPlayer mpErasing = MediaPlayer.create(this, R.raw.erasingblackboard);
        final MediaPlayer mpFriendAdded = MediaPlayer.create(this, R.raw.friendadded);


        friendNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currentLength=s.length();
                if(currentLength>previousLength){
                    if(sounds) {
                        mpWriting.start();
                    }
                }else if(currentLength<previousLength){
                    if(sounds) {
                        mpErasing.start();
                    }
                }
                previousLength=s.length();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        friendNameText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================
        //=====================================================

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        user_id=user.getUid();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserList=FirebaseDatabase.getInstance().getReference().child("UsersList");
        userDatabase=mDatabaseUsers.child(user_id);
        userFriends=userDatabase.child("Friends");
        userRequests=userDatabase.child("Requests").child("RequestsReceived");











        //================CHECK EXISTENCE OF NAME + EXTISTENCE OF REQUEST============================================================
        mUserList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(insertbyMe) {
                    if(dataSnapshot.hasChild(friendNickname)){

                        if(friendNickname.equals(playerName)){
                            Toast toast=Toast.makeText(getBaseContext(),"Δεν μπορείς να κάνεις φίλο τον εαυτό σου",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            toast.show();
                        }else if(friendExist){
                            Toast toast=Toast.makeText(getBaseContext(),"Ο φίλος υπάρχει ήδη",Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            toast.show();
                            friendExist=false;
                        }else{


                            final String id=dataSnapshot.child(friendNickname).getValue().toString();
                            userDatabase.child("Requests").child("RequestsSent").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(id)){
                                        Toast toast=Toast.makeText(getBaseContext(),"Το αίτημα έχει αποσταλεί. Περιμένετε την απάντηση.",Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                                        toast.show();
                                    }else{
                                        boolean inExistingRequests=false;
                                        for(int i=0;i<requestNameArrayList.size();i++){
                                            if(friendNickname.equals(requestNameArrayList.get(i))){
                                                inExistingRequests=true;
                                            }
                                        }
                                        if(inExistingRequests){
                                            Toast toast=Toast.makeText(getBaseContext(),"Έχετε δεχτεί αίτημα απο "+friendNickname+".  Απαντήστε.",Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                                            toast.show();
                                        }else{
                                            if(!friendNickname.equals("")) {

                                                myTools.sendRequest(playerName,user_id,friendNickname,id);
                                                Toast toast=Toast.makeText(getBaseContext(),"Στάλθηκε αίτημα φιλίας",Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                                                toast.show();

                                            }
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                    }else{
                        Toast toast=Toast.makeText(getBaseContext(),"Δεν βρέθηκε παίκτης με αυτό το ψευδώνυμο",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                        toast.show();
                    }
                    mUserList.child("asdfasasdfdfasdfasdfasdf").removeValue();
                    insertbyMe=false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //==================POPULATING FRIEND LIST VIEW================

        friendsGridView=(GridView)findViewById(R.id.friends_grid_view);
        friendArrayAdapter=new ArrayAdapter<>(this,R.layout.friend_text_view, friendNameArrayList);
        friendsGridView.setAdapter( friendArrayAdapter);
        friendArrayAdapter.notifyDataSetChanged();


        //==================POPULATING REQUEST LIST VIEW================

        requestsGridView=(GridView)findViewById(R.id.requests_grid_view);
        requestArrayAdapter=new ArrayAdapter<>(this,R.layout.request_text_view, requestNameArrayList);
        requestsGridView.setAdapter(requestArrayAdapter);
        requestArrayAdapter.notifyDataSetChanged();







        friendListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name=dataSnapshot.child("Name").getValue(String.class).toString();
                friendNameArrayList.add(name);
                friendArrayAdapter.notifyDataSetChanged();

                String id=dataSnapshot.child("Id").getValue(String.class).toString();
                friendIDArrayList.add(id);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String name=dataSnapshot.child("Name").getValue(String.class).toString();
                friendArrayAdapter.notifyDataSetChanged();

                String id=dataSnapshot.child("Id").getValue(String.class).toString();
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

        requestListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String requestName=dataSnapshot.child("name").getValue(String.class);
                requestNameArrayList.add(requestName);

                String requestId=dataSnapshot.child("Id").getValue(String.class);
                requestIDArrayList.add(requestId);

                requestArrayAdapter.notifyDataSetChanged();
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

        userFriends.addChildEventListener(friendListener);
        userRequests.addChildEventListener(requestListener);


    }


    @Override
    public void onBackPressed(){

        finish();
        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }

    public void backClicked(View v){
        finish();
        overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {


        userFriends.removeEventListener(friendListener);
        userRequests.removeEventListener(requestListener);

        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public void deleteFriend(View v){
        FriendsTextView tview= (FriendsTextView) v;
        final String name=tview.getText().toString();

        alertPopUp=new PopupWindow(alertPopView,(int)(0.95*windowWidth),(int)(0.3*windowHeight),true);
        alertPopUp.setAnimationStyle(R.style.AnimationStyle);
        alertPopUp.setOutsideTouchable(false);
        alertPopUp.setBackgroundDrawable(new BitmapDrawable());

        TextView alertText=(TextView)alertPopView.findViewById(R.id.alert_message);
        alertText.setTypeface(chalkTypeface);
        alertText.setText("Διαγραφή του \n' "+name+" ' ;");

        Button yesButton=(Button)alertPopView.findViewById(R.id.yes_button);
        yesButton.setTypeface(chalkTypeface);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.hasInternet) {
                    for (int i = 0; i < friendNameArrayList.size(); i++) {
                        if (name.equals(friendNameArrayList.get(i))) {
                            deleteID = friendIDArrayList.get(i);
                            myTools.deleteFriend(user_id,playerName, deleteID,name);
                            if (sounds) {
                                MediaPlayer mpErasing = MediaPlayer.create(getApplicationContext(), R.raw.erasingblackboard);
                                mpErasing.start();
                            }
                            friendNameArrayList.remove(friendNameArrayList.indexOf(name));
                            friendArrayAdapter.notifyDataSetChanged();
                            friendIDArrayList.remove(friendIDArrayList.indexOf(deleteID));
                            break;
                        }
                    }
                }else{
                    Toast toast=Toast.makeText(getBaseContext(),"Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                }
                alertPopUp.dismiss();
            }
        });

        Button noButton=(Button)alertPopView.findViewById(R.id.no_button);
        noButton.setTypeface(chalkTypeface);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertPopUp.dismiss();
            }
        });


        alertPopUp.showAtLocation(findViewById(R.id.friends),Gravity.CENTER,0,0);





    }




    public void answerRequest(View v){
        FriendsTextView tview= (FriendsTextView) v;
        final String name=tview.getText().toString();


        alertPopUp=new PopupWindow(alertPopView,(int)(0.95*windowWidth),(int)(0.3*windowHeight),true);
        alertPopUp.setAnimationStyle(R.style.AnimationStyle);
        alertPopUp.setOutsideTouchable(false);
        alertPopUp.setBackgroundDrawable(new BitmapDrawable());

        TextView alertText=(TextView)alertPopView.findViewById(R.id.alert_message);
        alertText.setTypeface(chalkTypeface);
        alertText.setText("Αίτημα φιλίας από \n' "+name+" '");

        Button yesButton=(Button)alertPopView.findViewById(R.id.yes_button);
        yesButton.setTypeface(chalkTypeface);
        yesButton.setText("Αποδοχή ");
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.hasInternet) {
                    for (int i = 0; i < requestNameArrayList.size(); i++) {
                        if (name.equals(requestNameArrayList.get(i))) {
                            final String requestID = requestIDArrayList.get(i);
                            final LevelHandler levelHandler = new LevelHandler();

                            mDatabaseUsers.child(requestID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("ExpPoints")) {
                                        int expPoints = dataSnapshot.child("ExpPoints").getValue(Integer.class);
                                        opLvl = levelHandler.getLevel(expPoints);
                                        String opDistString;
                                        if(dataSnapshot.hasChild("DistString")){
                                            opDistString=dataSnapshot.child("DistString").getValue(String.class);
                                        }else{
                                            opDistString="ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ";
                                        }

                                        myTools.acceptRequest(playerName, user_id,playerDistString, playerLvl, name, requestID,opDistString, opLvl);
                                    } else {
                                        opLvl = 1;
                                        String opDistString;
                                        if(dataSnapshot.hasChild("DistString")){
                                            opDistString=dataSnapshot.child("DistString").getValue(String.class);
                                        }else{
                                            opDistString="ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ";
                                        }
                                        myTools.acceptRequest(playerName, user_id,playerDistString,playerLvl, name, requestID,opDistString, opLvl);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            requestNameArrayList.remove(requestNameArrayList.indexOf(name));
                            requestIDArrayList.remove(requestIDArrayList.indexOf(requestID));
                            if (sounds) {
                                MediaPlayer mpErasingWriting = MediaPlayer.create(getApplicationContext(), R.raw.erasingwritingblackboard);
                                mpErasingWriting.start();
                            }
                            requestArrayAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }else{
                    Toast toast=Toast.makeText(getBaseContext(),"Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                }
                alertPopUp.dismiss();
            }
        });

        Button noButton=(Button)alertPopView.findViewById(R.id.no_button);
        noButton.setTypeface(chalkTypeface);
        noButton.setText("Απόρριψη ");
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.hasInternet) {
                    for (int i = 0; i < requestNameArrayList.size(); i++) {
                        if (name.equals(requestNameArrayList.get(i))) {
                            String requestID = requestIDArrayList.get(i);
                            myTools.declineRequest(playerName, user_id, name, requestID);
                            if (sounds) {
                                MediaPlayer mpErasing = MediaPlayer.create(getApplicationContext(), R.raw.erasingblackboard);
                                mpErasing.start();
                            }
                            requestNameArrayList.remove(requestNameArrayList.indexOf(name));
                            requestIDArrayList.remove(requestIDArrayList.indexOf(requestID));
                            requestArrayAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }else{
                    Toast toast=Toast.makeText(getApplicationContext(),"Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                }
                alertPopUp.dismiss();
            }
        });


        alertPopUp.showAtLocation(findViewById(R.id.friends),Gravity.CENTER,0,0);




    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(networkChangeReceiver);
        super.onDestroy();
    }
}
