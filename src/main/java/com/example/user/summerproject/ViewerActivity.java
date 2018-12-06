package com.example.user.summerproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.summerproject.Game.GameActivity;
import com.example.user.summerproject.myTools.CompletedGame;
import com.example.user.summerproject.myTools.Friend;
import com.example.user.summerproject.myTools.Game;
import com.example.user.summerproject.myTools.GameAdapter;
import com.example.user.summerproject.myTools.HalfGame;
import com.example.user.summerproject.myTools.LetterChooser;
import com.example.user.summerproject.myTools.LevelHandler;
import com.example.user.summerproject.myTools.LevelMessage;
import com.example.user.summerproject.myTools.LisgarTools;
import com.example.user.summerproject.myTools.Message;
import com.example.user.summerproject.myTools.NetworkChangeReceiver;
import com.example.user.summerproject.myTools.Stats;
import com.example.user.summerproject.myTools.SuggestionMessage;
import com.example.user.summerproject.myTools.UserMessage;
import com.example.user.summerproject.word_tester.Tester_Activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class ViewerActivity extends AppCompatActivity {




    private PopupWindow popupWindow;
    private PopupWindow messagePopUp;
    private Switch SoundsSwitch;
    private Switch MusicSwitch;
    private View mView;

    private LayoutInflater layoutInflater;
    private StorageReference storageReference;
    private Typeface chalkTypeface;
    private Typeface pencilTypeface;
    private String playerName;
    private boolean mIsFull;
    private boolean previousIsFull=false;
    private String opponentName;
    private LisgarTools myTools;
    private View[] gameViews=new View[12];
    private TextView[] letterViews=new TextView[12];
    private TextView[] nameViews=new TextView[12];
    private TextView[] levelViews=new TextView[12];
    private ArrayList<Game> games=new ArrayList<>();
    private ArrayList<Friend> friends=new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();
    private static final int NEW_GAME_RESULT_CODE=0;
    private static final int ANSWER_GAME_REQUEST_RESULT_CODE=1;
    private static final int FRIENDS_RESULT_CODE=2;
    private int lastIndexClicked;
    private int currentGameCount;
    private Button logoutButton;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogFiles;
    private PopupWindow preGamePopup;
    private PopupWindow levelPopup;
    private PopupWindow alertPopUp;
    private ChildEventListener gameListener;
    private ChildEventListener friendListener;
    private ChildEventListener completedGameListener;
    private ChildEventListener messageListener;
    private NetworkChangeReceiver networkChangeReceiver;
    private int rightGreen;
    private int pencilColor;
    private int redChalk;
    private int greenChalk;
    private int whiteChalk;
    private View levelPopView;
    private View alertPopView;
    private int windowHeight;
    private int windowWidth;
    private boolean sounds;
    private boolean gameSounds;
    public static boolean publicNickname;
    private long expPoints;
    private int playerLvl;
    private String playerTag;
    private String playerDistString;
    private int playerUpdateIndex;
    private int opLvl;
    private LevelHandler levelHandler=new LevelHandler();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference userDatabase;
    private DatabaseReference userGames;
    private DatabaseReference userCompletedGames;
    private DatabaseReference userFriends;
    private DatabaseReference dbMessages;
    private String mId;
    private String opId;
    private String opDistString;
    private boolean emptyness=false;
    private boolean friendsLoaded=false;
    private int friendsCount=0;
    private boolean logOut=false;
    private LetterChooser letterChooser;
    private GameAdapter gameAdapter;


    private int index;

    public void newGame(View v){
        if(MainActivity.hasInternet) {
            TextView textView = (TextView) v;
            String name = textView.getText().toString();
            name = name.substring(0, name.indexOf('\n'));
            boolean haveSent = false;
            boolean haveReceived=false;
            for (int i = 0; i < friends.size(); i++) {
                if (name.equals(friends.get(i).Name)) {
                    name = friends.get(i).Name;
                    opId = friends.get(i).Id;
                    opLvl = friends.get(i).Lvl;
                    opDistString=friends.get(i).DistString;
                    break;
                }
            }
            boolean haveRead = true;
            for (int i = 0; i < games.size(); i++) {
                if (games.get(i) instanceof CompletedGame) {
                    CompletedGame completedGame = (CompletedGame) games.get(i);
                    if (completedGame.id2.equals(opId)) {
                        haveRead = false;
                        break;
                    }
                }else{
                    HalfGame halfGame= (HalfGame) games.get(i);
                    if(halfGame.player2.equals(name) && halfGame.player1.equals(playerName)){
                        haveSent=true;
                    }
                    if(halfGame.player2.equals(playerName) && halfGame.player1.equals(name)){
                        haveReceived=true;
                    }
                }
            }
            if (haveSent) {
                Toast.makeText(getBaseContext(), "Έχεις ήδη στείλει παιχνίδι στο συγκεκριμένο φίλο", Toast.LENGTH_SHORT).show();
            }else if(haveReceived){

                Toast.makeText(getBaseContext(), "Έχεις παιχνίδι απο τον συγκεκριμένο φίλο. Απάντησε και μετά στείλε νέο.", Toast.LENGTH_SHORT).show();

            } else {
                if (haveRead) {
                    Intent newGame = new Intent(ViewerActivity.this, GameActivity.class);
                    newGame.putExtra("opName", name);
                    newGame.putExtra("opId", opId);
                    newGame.putExtra("playerName", playerName);
                    newGame.putExtra("playerID", mId);
                    newGame.putExtra("gameSounds", gameSounds);
                    newGame.putExtra("opLevel", opLvl);
                    newGame.putExtra("playerLevel", playerLvl);
                    newGame.putExtra("playerDistString",playerDistString);
                    newGame.putExtra("opDistString",opDistString);


                    preGamePopup.dismiss();
                    if (sounds) {
                        MediaPlayer pageFlipping = MediaPlayer.create(getApplicationContext(), R.raw.pageflippinglong);
                        pageFlipping.start();
                    }
                    startActivityForResult(newGame, NEW_GAME_RESULT_CODE);
                    overridePendingTransition(R.anim.game_fade_in, R.anim.noanim);
                } else {
                    Toast toast=Toast.makeText(getBaseContext(), "Διάβασε τα ολοκληρωμένα παιχνίδια για να στείλεις νέο", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                }

            }
        }else{

            Toast toast=Toast.makeText(getBaseContext(),"Ελέγξε την σύνδεσή σου και ξαναπροσπάθησε",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            preGamePopup.dismiss();
        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==NEW_GAME_RESULT_CODE){

            if(resultCode==RESULT_OK){


            }
        }else if(requestCode==ANSWER_GAME_REQUEST_RESULT_CODE){

            //preGamePopup.dismiss();
        }else if(requestCode==FRIENDS_RESULT_CODE){



        }
    }


    public void sentStorage(CompletedGame c1){

        String storageName = playerName;
        myTools.addCompletedGame(c1);
        String date = DateFormat.getDateTimeInstance().format(new Date());
        StorageReference cook = storageReference.child("/UsersCompletedGames/"+playerName+"/"+date+".txt");
        try {
            InputStream c = myTools.inputFileString("completedgames.txt");
            cook.putStream(c);

        } catch (IOException e) {
            e.printStackTrace();

        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.viewr_activity, null);
        setContentView(mView);
        myTools=new LisgarTools(getApplicationContext());


        networkChangeReceiver=new NetworkChangeReceiver();

        storageReference = FirebaseStorage.getInstance().getReference();


        rightGreen=getResources().getColor(R.color.rightGreen);
        pencilColor=getResources().getColor(R.color.pencilColor);
        redChalk=getResources().getColor(R.color.redChalk);
        greenChalk=getResources().getColor(R.color.greenChalk);
        whiteChalk=getResources().getColor(R.color.chalkColor);

        levelPopView=layoutInflater.inflate(R.layout.level_popup,null);
        alertPopView=layoutInflater.inflate(R.layout.alert_dialog_custom,null);

        chalkTypeface=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/romanscript.ttf");

        //========Get friends Data from Main========//
        Bundle extras=getIntent().getExtras();
        playerName=extras.getString("name");


        Stats stats = new Stats(myTools.readOldGames());
        myTools.writeDistribution(stats.distFromMin);
        letterChooser=new LetterChooser();

        playerDistString = letterChooser.getSortedStringFromIntArray(myTools.getDistribution("distribution.txt"));




        final String font=getResources().getString(R.string.font_name);
        pencilTypeface= Typeface.createFromAsset(getApplicationContext().getAssets(),font);
        //=========Set player name to the textView=========//
        final TextView nameView=(TextView)findViewById(R.id.nameView);
        nameView.setTypeface(pencilTypeface,Typeface.BOLD_ITALIC);
        nameView.setText(playerName+" ");


        //=======getting window size============
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        windowWidth= size.x;
        windowHeight = size.y;



        gameListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.hasChild("p2a1")){


                    int time=dataSnapshot.child("time2").getValue(Integer.class);
                    String letter=dataSnapshot.child("letter").getValue().toString();
                    String key=dataSnapshot.getKey();


                    opponentName=dataSnapshot.child("opName").getValue().toString();

                    for(int i=0;i<friends.size();i++){
                        if(friends.get(i).Name.equals(opponentName)){
                            opId=friends.get(i).Id;
                            break;
                        }
                    }


                    if(dataSnapshot.hasChild("opLevel")) {
                        opLvl = dataSnapshot.child("opLevel").getValue(Integer.class);
                    }else{
                        opLvl=1;
                    }


                    String[] answers=new String[6];

                    answers[0]=dataSnapshot.child("p2a1").getValue().toString();
                    answers[1]=dataSnapshot.child("p2a2").getValue().toString();
                    answers[2]=dataSnapshot.child("p2a3").getValue().toString();
                    answers[3]=dataSnapshot.child("p2a4").getValue().toString();
                    answers[4]=dataSnapshot.child("p2a5").getValue().toString();
                    answers[5]=dataSnapshot.child("p2a6").getValue().toString();
                    boolean[] isTrue=new boolean[6];
                    isTrue[0]=dataSnapshot.child("p2t1").getValue(boolean.class);
                    isTrue[1]=dataSnapshot.child("p2t2").getValue(boolean.class);
                    isTrue[2]=dataSnapshot.child("p2t3").getValue(boolean.class);
                    isTrue[3]=dataSnapshot.child("p2t4").getValue(boolean.class);
                    isTrue[4]=dataSnapshot.child("p2t5").getValue(boolean.class);
                    isTrue[5]=dataSnapshot.child("p2t6").getValue(boolean.class);


                    HalfGame halfGame=new HalfGame(time,answers,isTrue,letter,opponentName,opId,opLvl,playerName,mId,playerLvl);

                    games.add(halfGame);

                    int [] array = myTools.getDistribution("distribution.txt");

                    for (int i = 0 ; i<24 ;i++){
                        if(halfGame.letter.equals(letterChooser.getLetter(i))){
                            array[i]+=2;
                            break;
                        }
                    }
                    myTools.writeDistribution(array);

                    String newDistString=letterChooser.getSortedStringFromIntArray(letterChooser.getIncreasedDist(games,myTools.getDistribution("distribution.txt")));
                    if(!newDistString.equals(playerDistString)){
                        playerDistString=newDistString;
                        userDatabase.child("DistString").setValue(playerDistString);
                        for(int j=0;j<friends.size();j++){
                            mDatabaseUsers.child(friends.get(j).Id).child("Friends").child(mId).child("DistString").setValue(playerDistString);
                        }
                    }

                    if(dataSnapshot.hasChild("notificationHasSent")){
                        userGames.child(key).child("notificationHasSent").setValue(true);
                    }

                    updateGameList(games);
                }else  if(dataSnapshot.hasChild("p1a1")){


                    int time=dataSnapshot.child("time1").getValue(Integer.class);
                    String letter=dataSnapshot.child("letter").getValue().toString();
                    String key=dataSnapshot.getKey();

                    opponentName=dataSnapshot.child("opName").getValue().toString();
                    for(int i=0;i<friends.size();i++){
                        if(friends.get(i).Name.equals(opponentName)){
                            opId=friends.get(i).Id;
                            break;
                        }
                    }
                    if(dataSnapshot.hasChild("opLevel")) {
                        opLvl = dataSnapshot.child("opLevel").getValue(Integer.class);
                    }else{
                        opLvl=1;
                    }



                    String[] answers=new String[6];

                    answers[0]=dataSnapshot.child("p1a1").getValue().toString();
                    answers[1]=dataSnapshot.child("p1a2").getValue().toString();
                    answers[2]=dataSnapshot.child("p1a3").getValue().toString();
                    answers[3]=dataSnapshot.child("p1a4").getValue().toString();
                    answers[4]=dataSnapshot.child("p1a5").getValue().toString();
                    answers[5]=dataSnapshot.child("p1a6").getValue().toString();
                    boolean[] isTrue=new boolean[6];
                    isTrue[0]=dataSnapshot.child("p1t1").getValue(boolean.class);
                    isTrue[1]=dataSnapshot.child("p1t2").getValue(boolean.class);
                    isTrue[2]=dataSnapshot.child("p1t3").getValue(boolean.class);
                    isTrue[3]=dataSnapshot.child("p1t4").getValue(boolean.class);
                    isTrue[4]=dataSnapshot.child("p1t5").getValue(boolean.class);
                    isTrue[5]=dataSnapshot.child("p1t6").getValue(boolean.class);

                    HalfGame halfGame=new HalfGame(time,answers,isTrue,letter,playerName,mId,playerLvl,opponentName,opId,opLvl);

                    games.add(halfGame);




                    int [] array = myTools.getDistribution("distribution.txt");

                    for (int i = 0 ; i<24 ;i++){
                        if(halfGame.letter.equals(letterChooser.getLetter(i))){
                            array[i]+=2;
                            break;
                        }
                    }
                    myTools.writeDistribution(array);

                    String newDistString=letterChooser.getSortedStringFromIntArray(letterChooser.getIncreasedDist(games,myTools.getDistribution("distribution.txt")));
                    if(!newDistString.equals(playerDistString)){
                        playerDistString=newDistString;
                        userDatabase.child("DistString").setValue(playerDistString);
                        for(int j=0;j<friends.size();j++){
                            mDatabaseUsers.child(friends.get(j).Id).child("Friends").child(mId).child("DistString").setValue(playerDistString);
                        }
                    }

                    updateGameList(games);

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("opName")) {

                    String opName = dataSnapshot.child("opName").getValue().toString();
                    for(int i=0;i<friends.size();i++){
                        if(friends.get(i).Name.equals(opName)){
                            opId=friends.get(i).Id;
                            break;
                        }
                    }
                    for (int i = 0; i < games.size(); i++) {
                        if (games.get(i) instanceof HalfGame) {
                            HalfGame halfGame = (HalfGame) games.get(i);
                            if (halfGame.player1.equals(opName)) {
                                games.remove(i);
                                break;
                            } else if (halfGame.id2.equals(opId)) {
                                games.remove(i);
                                break;
                            }
                        }
                    }

                }

                updateGameList(games);
                //trust the process
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        completedGameListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.hasChild("p2a1") && dataSnapshot.hasChild("p1a1")){

                    //========HALF GAME  1====================//

                    int time1=dataSnapshot.child("time1").getValue(Integer.class);
                    String letter=dataSnapshot.child("letter").getValue().toString();

                    opponentName=dataSnapshot.child("opName").getValue().toString();
                    opId=dataSnapshot.getKey();
                    if(dataSnapshot.hasChild("opLevel")) {
                        opLvl = dataSnapshot.child("opLevel").getValue(Integer.class);
                    }else{
                        opLvl=1;
                    }


                    String[] answers1=new String[6];
                    answers1[0]=dataSnapshot.child("p1a1").getValue().toString();
                    answers1[1]=dataSnapshot.child("p1a2").getValue().toString();
                    answers1[2]=dataSnapshot.child("p1a3").getValue().toString();
                    answers1[3]=dataSnapshot.child("p1a4").getValue().toString();
                    answers1[4]=dataSnapshot.child("p1a5").getValue().toString();
                    answers1[5]=dataSnapshot.child("p1a6").getValue().toString();
                    boolean[] isTrue1=new boolean[6];
                    isTrue1[0]=dataSnapshot.child("p1t1").getValue(boolean.class);
                    isTrue1[1]=dataSnapshot.child("p1t2").getValue(boolean.class);
                    isTrue1[2]=dataSnapshot.child("p1t3").getValue(boolean.class);
                    isTrue1[3]=dataSnapshot.child("p1t4").getValue(boolean.class);
                    isTrue1[4]=dataSnapshot.child("p1t5").getValue(boolean.class);
                    isTrue1[5]=dataSnapshot.child("p1t6").getValue(boolean.class);

                    HalfGame halfGame1=new HalfGame(time1,answers1,isTrue1,letter,playerName,mId,playerLvl,opponentName,opId,opLvl);


                    //========HALF GAME  2====================//

                    int time2=dataSnapshot.child("time2").getValue(Integer.class);

                    String[] answers2=new String[6];
                    answers2[0]=dataSnapshot.child("p2a1").getValue().toString();
                    answers2[1]=dataSnapshot.child("p2a2").getValue().toString();
                    answers2[2]=dataSnapshot.child("p2a3").getValue().toString();
                    answers2[3]=dataSnapshot.child("p2a4").getValue().toString();
                    answers2[4]=dataSnapshot.child("p2a5").getValue().toString();
                    answers2[5]=dataSnapshot.child("p2a6").getValue().toString();
                    boolean[] isTrue2=new boolean[6];
                    isTrue2[0]=dataSnapshot.child("p2t1").getValue(boolean.class);
                    isTrue2[1]=dataSnapshot.child("p2t2").getValue(boolean.class);
                    isTrue2[2]=dataSnapshot.child("p2t3").getValue(boolean.class);
                    isTrue2[3]=dataSnapshot.child("p2t4").getValue(boolean.class);
                    isTrue2[4]=dataSnapshot.child("p2t5").getValue(boolean.class);
                    isTrue2[5]=dataSnapshot.child("p2t6").getValue(boolean.class);

                    HalfGame halfGame2=new HalfGame(time2,answers2,isTrue2,letter,opponentName,opId,opLvl,playerName,mId,playerLvl);


                    for(int i=0;i<games.size();i++){
                        if(games.get(i) instanceof HalfGame){
                            HalfGame halfGame=(HalfGame)games.get(i);
                            if( halfGame.player1.equals(opponentName) ||  halfGame.player2.equals(opponentName) ){
                                games.remove(i);
                                break;
                            }
                        }
                    }

                    CompletedGame completedGame=new CompletedGame(halfGame1,halfGame2);


                    games.add(completedGame);


                    if(dataSnapshot.hasChild("notificationHasSent")){
                        userCompletedGames.child(opId).child("notificationHasSent").setValue(true);
                    }


                    updateGameList(games);


                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                String opName=dataSnapshot.child("opName").getValue().toString();
                String opId=dataSnapshot.getKey();
                for(int i=0;i<games.size();i++){
                    if( games.get(i) instanceof CompletedGame ){
                        CompletedGame completedGame=(CompletedGame) games.get(i);
                        if( completedGame.player2.equals(opName) ){
                            games.remove(i);
                            break;
                        }
                    }
                }

                updateGameList(games);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        friendListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String id=dataSnapshot.getKey();
                String name=dataSnapshot.child("Name").getValue().toString();


                int Lvl;
                if(dataSnapshot.hasChild("Lvl")){
                    Lvl=dataSnapshot.child("Lvl").getValue(Integer.class);
                }else{
                    Lvl=1;
                }
                String DistString="";
                if(dataSnapshot.hasChild("DistString")){
                    DistString=dataSnapshot.child("DistString").getValue(String.class);
                }else{
                    for(int i=0;i<24;i++){
                        DistString+=letterChooser.getLetter(i);
                    }
                }


                Friend friend=new Friend(name,id,Lvl,DistString);
                friends.add(friend);
                if(friendsCount==friends.size()){
                    userGames.addChildEventListener(gameListener);
                    userCompletedGames.addChildEventListener(completedGameListener);
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                //================NEW CODE===================//
                String id=dataSnapshot.getKey();
                String name=dataSnapshot.child("Name").getValue().toString();


                int Lvl;
                if(dataSnapshot.hasChild("Lvl")){
                    Lvl=dataSnapshot.child("Lvl").getValue(Integer.class);
                }else{
                    Lvl=1;
                }
                String DistString="";
                if(dataSnapshot.hasChild("DistString")){
                    DistString=dataSnapshot.child("DistString").getValue(String.class);
                }else{
                    for(int i=0;i<24;i++){
                        DistString+=letterChooser.getLetter(i);
                    }
                }
                Friend friend=new Friend(name,id,Lvl,DistString);






            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String id=dataSnapshot.getKey();
                String name=dataSnapshot.child("Name").getValue().toString();


                int Lvl;
                if(dataSnapshot.hasChild("Lvl")){
                    Lvl=dataSnapshot.child("Lvl").getValue(Integer.class);
                }else{
                    Lvl=1;
                }
                String DistString="";
                if(dataSnapshot.hasChild("DistString")){
                    DistString=dataSnapshot.child("DistString").getValue(String.class);
                }else{
                    for(int i=0;i<24;i++){
                        DistString+=letterChooser.getLetter(i);
                    }
                }
                Friend friend=new Friend(name,id,Lvl,DistString);


                for(int i=0;i<friends.size();i++){
                    if(friends.get(i).Id.equals(friend.Id)){
                        friends.remove(i);
                        break;
                    }
                }
                //===========================================//
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        messageListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.hasChild("points")){
                    String playerSuggestion = dataSnapshot.child("playerSuggestion").getValue(String.class);
                    String categorySuggestion = dataSnapshot.child("categorySuggestion").getValue(String.class);
                    String ourMessage = dataSnapshot.child("ourMessage").getValue(String.class);
                    int points = dataSnapshot.child("points").getValue(Integer.class);
                    String hash=dataSnapshot.getKey();
                    Message message = new SuggestionMessage(playerSuggestion,categorySuggestion,ourMessage,points,hash);
                    messages.add(message);
                }else if(dataSnapshot.hasChild("levelMessage")) {

                    String levelMessage = dataSnapshot.child("levelMessage").getValue(String.class);
                    String TAGmessage = dataSnapshot.child("TAGmessage").getValue(String.class);
                    String hash=dataSnapshot.getKey();
                    Message message = new LevelMessage(levelMessage,TAGmessage,hash);
                    messages.add(message);
                }else if(dataSnapshot.hasChild("sender")){
                    String hash=dataSnapshot.getKey();
                    String sender=dataSnapshot.child("sender").getValue(String.class);
                    String message=dataSnapshot.child("message").getValue(String.class);
                    Message message1=new UserMessage(sender,message,hash);
                    messages.add(message1);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String hash=dataSnapshot.getKey();
                int index=0;
                for(int i=0;i<messages.size();i++){
                    if(hash.equals(dataSnapshot.getKey())){
                        index=i;
                        break;
                    }
                }

                messages.remove(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


                String hash=dataSnapshot.getKey();
                int index=0;
                for(int i=0;i<messages.size();i++){
                    if(hash.equals(dataSnapshot.getKey())){
                        index=i;
                        break;
                    }
                }

                messages.remove(index);


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        mAuth=FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mId=mAuth.getCurrentUser().getUid();
        userDatabase=mDatabaseUsers.child(mId);
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("ExpPoints")){

                    expPoints=(long)dataSnapshot.child("ExpPoints").getValue(Integer.class);
                    playerLvl=levelHandler.getLevel(expPoints);
                    playerTag=levelHandler.getTag(playerLvl);
                    if(playerLvl<20){
                        nameView.setTypeface(pencilTypeface,Typeface.NORMAL);
                    }else{
                        nameView.setTypeface(pencilTypeface,Typeface.BOLD_ITALIC);
                    }
                }else{
                    expPoints=0;
                    userDatabase.child("ExpPoints").setValue(expPoints);
                }
                if(dataSnapshot.hasChild("Settings")){
                    sounds=dataSnapshot.child("Settings").child("sounds").getValue(boolean.class);
                    gameSounds=dataSnapshot.child("Settings").child("gameSounds").getValue(boolean.class);
                }else{
                    sounds=true;
                    gameSounds=true;
                    userDatabase.child("Settings").child("sounds").setValue(true);
                    userDatabase.child("Settings").child("gameSounds").setValue(true);
                }
                if(dataSnapshot.hasChild("DistString")){
                    String dist=dataSnapshot.child("DistString").getValue(String.class);
                    if(!dist.equals(playerDistString)){
                        userDatabase.child("DistString").setValue(playerDistString);
                    }
                }else{
                    userDatabase.child("DistString").setValue(playerDistString);
                }
                if(dataSnapshot.hasChild("UpdateIndex")){
                    playerUpdateIndex=dataSnapshot.child("UpdateIndex").getValue(Integer.class);
                }else {
                    playerUpdateIndex=0;
                    emptyness=true;
                }
                if(dataSnapshot.hasChild("publicNickname")){
                    publicNickname=dataSnapshot.child("publicNickname").getValue(boolean.class);
                    uploadAndCheckTimes();
                }else{
                    userDatabase.child("publicNickname").setValue(false);
                    publicNickname=false;
                    uploadAndCheckTimes();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        userGames=userDatabase.child("Games");
        userCompletedGames=userGames.child("CompletedGames");
        userFriends=userDatabase.child("Friends");
        dbMessages = userDatabase.child("Messages");


        dbMessages.addChildEventListener(messageListener);

        userFriends.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsCount= (int) dataSnapshot.getChildrenCount();
                userFriends.addChildEventListener(friendListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //===================NEW GAME BUTTON==============================

        final Button newGameButton = (Button) findViewById(R.id.newGame);
        newGameButton.setTypeface(pencilTypeface);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.hasInternet) {
                    if (friendsCount == 0) {
                        CountDownTimer countDownTimer=new CountDownTimer(200,100) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Toast.makeText(getBaseContext(), "Πρέπει να κάνεις φίλους για να μπορείς να παίξεις", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFinish() {
                                Intent friendsIntent=new Intent(ViewerActivity.this,FriendsActivity.class);
                                friendsIntent.putExtra("name",playerName);
                                friendsIntent.putExtra("sounds",sounds);
                                friendsIntent.putExtra("playerLevel",playerLvl);
                                friendsIntent.putExtra("playerDistString",playerDistString);
                                startActivityForResult(friendsIntent,FRIENDS_RESULT_CODE);
                                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
                            }
                        }.start();

                    } else {
                        View pregameView = (View) layoutInflater.inflate(R.layout.pre_game_popup, null);


                        preGamePopup = new PopupWindow(pregameView, (int) (0.9 * windowWidth), (int) (0.7 * windowHeight), true);
                        preGamePopup.setAnimationStyle(R.style.PreGameAnimationStyle);
                        preGamePopup.setOutsideTouchable(false);
                        preGamePopup.setBackgroundDrawable(new BitmapDrawable());
                        preGamePopup.showAtLocation(findViewById(R.id.relative), Gravity.CENTER, 0, 0);
                        GridView gridView = (GridView) pregameView.findViewById(R.id.grid_view);
                        List<String> names = new ArrayList<String>();
                        for (int i = 0; i < friends.size(); i++) {
                            String friendName = friends.get(i).Name;
                            String Level = friends.get(i).Lvl + "";
                            names.add(friendName + "\n" + "(" + friends.get(i).Lvl + ")");
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(pregameView.getContext(), R.layout.pre_game_friend_text_view, names);

                        gridView.setAdapter(arrayAdapter);

                    }
                }else{
                    Toast.makeText(getBaseContext(),"Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT).show();
                }
            }
        });


        //====================WORD TESTER=================================

        Button wordTesterButton = (Button) findViewById(R.id.wordTester);
        wordTesterButton.setTypeface(pencilTypeface);
        wordTesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newTester = new Intent(ViewerActivity.this, Tester_Activity.class);
                if(sounds) {
                    MediaPlayer pageFlipping = MediaPlayer.create(getApplicationContext(), R.raw.pageflipping);
                    pageFlipping.start();
                }
                newTester.putExtra("sounds",sounds);
                newTester.putExtra("mID",mId);
                startActivity(newTester);
                overridePendingTransition(R.anim.fade_in,R.anim.noanim);
            }
        });

        //======================FRIENDS BUTTON===============================
        Button friendsButton=(Button)findViewById(R.id.friends_button);
        friendsButton.setTypeface(pencilTypeface);
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friendsIntent=new Intent(ViewerActivity.this,FriendsActivity.class);
                friendsIntent.putExtra("name",playerName);
                friendsIntent.putExtra("sounds",sounds);
                friendsIntent.putExtra("playerLevel",playerLvl);
                friendsIntent.putExtra("playerDistString",playerDistString);
                startActivityForResult(friendsIntent,FRIENDS_RESULT_CODE);
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
            }
        });


        //==================STATISTICS====================================





        final Button statsButton=(Button)findViewById(R.id.stats_button);
        statsButton.setTypeface(pencilTypeface);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent friendsIntent=new Intent(ViewerActivity.this,StatsActivity.class);
                friendsIntent.putExtra("name",playerName);
                startActivityForResult(friendsIntent,FRIENDS_RESULT_CODE);
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);

            }
        });





        //==========================SETTINGS==============================
        Button settingButton = (Button) findViewById(R.id.settings_button);
        settingButton.setTypeface(pencilTypeface);
        final RelativeLayout container = (RelativeLayout) layoutInflater.inflate(R.layout.settings_popup, null);
        TextView settingTitle=(TextView)container.findViewById(R.id.setting_title);
        settingTitle.setTypeface(chalkTypeface);
        TextView soundsTitle=(TextView)container.findViewById(R.id.textSounds);
        soundsTitle.setTypeface(chalkTypeface);
        TextView gameSoundsTitle=(TextView)container.findViewById(R.id.gameSounds);
        gameSoundsTitle.setTypeface(chalkTypeface);

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.hasInternet) {
                    popupWindow = new PopupWindow(container, (int) (0.85 * windowWidth), (int) (0.6 * windowHeight), true);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setAnimationStyle(R.style.SettingsAnimationStyle);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.showAtLocation(findViewById(R.id.relative), Gravity.CENTER, 0, 0);
                    SoundsSwitch = (Switch) container.findViewById(R.id.switchSounds);
                    SoundsSwitch.setChecked(sounds);
                    SoundsSwitch.setTextOn(" ");
                    SoundsSwitch.setTextOff(" ");
                    MusicSwitch = (Switch) container.findViewById(R.id.switchGameSounds);
                    MusicSwitch.setTextOn(" ");
                    MusicSwitch.setTextOff(" ");
                    MusicSwitch.setChecked(gameSounds);
                    SoundsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                sounds = true;
                                userDatabase.child("Settings").child("sounds").setValue(true);
                            } else {
                                sounds = false;
                                userDatabase.child("Settings").child("sounds").setValue(false);
                            }
                        }
                    });
                    MusicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                gameSounds = true;
                                userDatabase.child("Settings").child("gameSounds").setValue(true);
                            } else {
                                gameSounds = false;
                                userDatabase.child("Settings").child("gameSounds").setValue(false);
                            }
                        }
                    });
                }else{
                    Toast.makeText(getBaseContext(),"Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT).show();
                }
            }
        });



        //==========================INFO==============================
        Button infoButton=(Button)findViewById(R.id.info_button);
        infoButton.setTypeface(pencilTypeface);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Intent tutorialIntent = new Intent(ViewerActivity.this, InfoActivity.class);
                startActivity(tutorialIntent);
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
                */
                Intent tutorialIntent = new Intent(ViewerActivity.this, TutorialActivity.class);
                tutorialIntent.putExtra("name", playerName);
                startActivity(tutorialIntent);
            }
        });

        //===================GAME VIEWS===================================



        //===================LOG OUT===================================
        logoutButton=(Button)findViewById(R.id.logout_button);
        logoutButton.setTypeface(pencilTypeface);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertPopUp=new PopupWindow(alertPopView,(int)(0.95*windowWidth),(int)(0.3*windowHeight),true);
                alertPopUp.setAnimationStyle(R.style.AnimationStyle);
                alertPopUp.setOutsideTouchable(false);
                alertPopUp.setBackgroundDrawable(new BitmapDrawable());

                TextView alertText=(TextView)alertPopView.findViewById(R.id.alert_message);
                alertText.setTypeface(chalkTypeface);
                alertText.setText("Αποσύνδεση;");

                Button yesButton=(Button)alertPopView.findViewById(R.id.yes_button);
                yesButton.setTypeface(chalkTypeface);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertPopUp.dismiss();
                        mAuth.signOut();
                        logOut=true;
                        finish();
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


                alertPopUp.showAtLocation(findViewById(R.id.relative),Gravity.CENTER,0,0);

            }
        });


        //=================== EXIT ===================================
        Button exitBtn=(Button)findViewById(R.id.exit_button);

        exitBtn.setTypeface(pencilTypeface);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        GridView gridView= (GridView) findViewById(R.id.game_grid_view);
        gameAdapter=new GameAdapter(this,games,playerName);
        gridView.setAdapter(gameAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPostPopUp(position);
            }
        });


    }

    public void uploadAndCheckTimes(){
        DatabaseReference publicUserDatabase= FirebaseDatabase.getInstance().getReference().child("PublicUsers").child(mId);
        if(publicNickname){
            publicUserDatabase.child("Name").setValue(playerName);
            publicUserDatabase.child("timeLogged").setValue(System.currentTimeMillis());
        }else{
            publicUserDatabase.setValue(null);
        }

    }

    public void showPostPopUp(int index){
        final View postGame=(View)layoutInflater.inflate(R.layout.post_game,null);


        final PopupWindow postPopUp=new PopupWindow(postGame,(int)(0.95*windowWidth),(int)(0.9*windowHeight),true);
        postPopUp.setAnimationStyle(R.style.AnimationStyle);
        postPopUp.setTouchable(true);
        postPopUp.setFocusable(false);
        postPopUp.setOutsideTouchable(false);




        TextView name1Text=(TextView)postGame.findViewById(R.id.player_name_1);
        name1Text.setTypeface(chalkTypeface);
        TextView name2Text=(TextView)postGame.findViewById(R.id.player_name_2);
        name2Text.setTypeface(chalkTypeface);



        TextView[] p1ans=new TextView[6];
        p1ans[0]=(TextView)postGame.findViewById(R.id.p1ans1);
        p1ans[1]=(TextView)postGame.findViewById(R.id.p1ans2);
        p1ans[2]=(TextView)postGame.findViewById(R.id.p1ans3);
        p1ans[3]=(TextView)postGame.findViewById(R.id.p1ans4);
        p1ans[4]=(TextView)postGame.findViewById(R.id.p1ans5);
        p1ans[5]=(TextView)postGame.findViewById(R.id.p1ans6);
        TextView[] p1pts=new TextView[6];
        p1pts[0]=(TextView)postGame.findViewById(R.id.p1pts1);
        p1pts[1]=(TextView)postGame.findViewById(R.id.p1pts2);
        p1pts[2]=(TextView)postGame.findViewById(R.id.p1pts3);
        p1pts[3]=(TextView)postGame.findViewById(R.id.p1pts4);
        p1pts[4]=(TextView)postGame.findViewById(R.id.p1pts5);
        p1pts[5]=(TextView)postGame.findViewById(R.id.p1pts6);
        TextView[] p2ans=new TextView[6];
        p2ans[0]=(TextView)postGame.findViewById(R.id.p2ans1);
        p2ans[1]=(TextView)postGame.findViewById(R.id.p2ans2);
        p2ans[2]=(TextView)postGame.findViewById(R.id.p2ans3);
        p2ans[3]=(TextView)postGame.findViewById(R.id.p2ans4);
        p2ans[4]=(TextView)postGame.findViewById(R.id.p2ans5);
        p2ans[5]=(TextView)postGame.findViewById(R.id.p2ans6);
        TextView[] p2pts=new TextView[6];
        p2pts[0]=(TextView)postGame.findViewById(R.id.p2pts1);
        p2pts[1]=(TextView)postGame.findViewById(R.id.p2pts2);
        p2pts[2]=(TextView)postGame.findViewById(R.id.p2pts3);
        p2pts[3]=(TextView)postGame.findViewById(R.id.p2pts4);
        p2pts[4]=(TextView)postGame.findViewById(R.id.p2pts5);
        p2pts[5]=(TextView)postGame.findViewById(R.id.p2pts6);

        for(int i=0;i<6;i++){
            p1ans[i].setTypeface(chalkTypeface);
            p1ans[i].setText("");
            p1pts[i].setTypeface(chalkTypeface);
            p1pts[i].setText("");
            p1pts[i].setTextColor(whiteChalk);
            p2ans[i].setTypeface(chalkTypeface);
            p2ans[i].setText("");
            p2pts[i].setTypeface(chalkTypeface);
            p2pts[i].setText("");
            p2pts[i].setTextColor(whiteChalk);
        }

        TextView timeView1=(TextView)postGame.findViewById(R.id.time1);
        timeView1.setTypeface(chalkTypeface);
        timeView1.setText("");
        TextView letterView=(TextView)postGame.findViewById(R.id.letter_view);
        letterView.setTypeface(chalkTypeface);
        letterView.setTextColor(whiteChalk);
        final TextView resultView=(TextView)postGame.findViewById(R.id.result_message);
        resultView.setTypeface(chalkTypeface);
        resultView.setText("");
        TextView timeView2=(TextView)postGame.findViewById(R.id.time2);
        timeView2.setText("     ");
        TextView totalpointView1=(TextView)postGame.findViewById(R.id.total_points1);
        TextView totalpointView2=(TextView)postGame.findViewById(R.id.total_points2);
        totalpointView1.setText("    ");
        totalpointView2.setText("    ");

        if(games.get(index) instanceof CompletedGame){

            if(MainActivity.hasInternet) {

                postPopUp.showAtLocation(findViewById(R.id.relative), Gravity.CENTER, 0, (int) (0.025 * windowHeight));

                final CompletedGame completedGame = (CompletedGame) games.get(index);

                myTools.removeGameFirebase(completedGame);
                sentStorage(completedGame);
                final int exp = levelHandler.getExp(completedGame);
                userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        expPoints = (long) dataSnapshot.child("ExpPoints").getValue(Integer.class);
                        if (expPoints + exp < 0) {
                            expPoints = 0;
                        } else {
                            expPoints = expPoints + exp;
                        }
                        userDatabase.child("ExpPoints").setValue(expPoints);
                        if (playerLvl != levelHandler.getLevel(expPoints)) {
                            if (playerLvl < levelHandler.getLevel(expPoints)) {
                                if (playerTag.equals(levelHandler.getTag(levelHandler.getLevel(expPoints)))) {
                                    myTools.sentLevelMessage(mId, "Πηρες Level :)\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "");
                                } else {
                                    myTools.sentLevelMessage(mId, "Πηρες Level :)\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "Αναβαθμίστηκες σε \n" + levelHandler.getTag(levelHandler.getLevel(expPoints)));
                                }
                            } else {
                                if (playerTag.equals(levelHandler.getTag(levelHandler.getLevel(expPoints)))) {
                                    myTools.sentLevelMessage(mId, "Έπεσες Level :(\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "");
                                } else {
                                    myTools.sentLevelMessage(mId, "Έπεσες Level :(\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "Υποβιβάστηκες σε \n" + levelHandler.getTag(levelHandler.getLevel(expPoints)));
                                }
                            }

                            playerLvl = levelHandler.getLevel(expPoints);
                            myTools.sendNewLevelToFriends(playerName, mId, friends, playerLvl);

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                name1Text.setText(completedGame.player1);
                name2Text.setText(completedGame.player2);
                letterView.setText("" + completedGame.letter);

                for (int j = 0; j < 6; j++) {
                    p1ans[j].setText(completedGame.answers1[j]);
                    if (completedGame.isTrue1[j]) {
                        p1ans[j].setTextColor(greenChalk);
                    } else {
                        p1ans[j].setTextColor(redChalk);
                    }
                    p1pts[j].setText(completedGame.points1[j] + " ");
                    p2ans[j].setText(completedGame.answers2[j]);
                    if (completedGame.isTrue2[j]) {
                        p2ans[j].setTextColor(greenChalk);
                    } else {
                        p2ans[j].setTextColor(redChalk);
                    }
                    p2pts[j].setText(completedGame.points2[j] + " ");
                }


                totalpointView1.setTypeface(chalkTypeface);
                totalpointView2.setTypeface(chalkTypeface);

                timeView2.setTypeface(chalkTypeface);
                totalpointView1.setText(completedGame.sumPoints1 + " ");
                totalpointView2.setText(completedGame.sumPoints2 + " ");
                timeView1.setText(myTools.timeString(completedGame.time1) + " ");
                timeView2.setText(myTools.timeString(completedGame.time2) + " ");


                String proshmo = "";
                if (exp > 0) {
                    proshmo = "+";
                } else if (exp < 0) {
                    proshmo = "-";
                }
                final Button button = (Button) postGame.findViewById(R.id.post_button);
                if (completedGame.sumPoints1 > completedGame.sumPoints2) {
                    resultView.setText("Κέρδισες το παιχνιδι . " + proshmo + exp + " exp");
                } else if (completedGame.sumPoints1 < completedGame.sumPoints2) {
                    resultView.setText("Έχασες το παιχνιδι . " + proshmo + exp + " exp");
                } else {

                    if (completedGame.time1 < completedGame.time2) {

                        resultView.setText("Έχασες το παιχνιδι . " + proshmo + exp + " exp");
                    } else if (completedGame.time1 > completedGame.time2) {
                        resultView.setText("Κέρδισες το παιχνιδι . " + proshmo + exp + " exp");
                    } else {
                        resultView.setText("Δεν κέρδισε κανείς το παιχνίδι");
                    }

                }

                if (completedGame.sumPoints1 > completedGame.sumPoints2) {
                    myTools.addOldGame(true, completedGame.points1, completedGame.letter, completedGame.time1);

                    int [] array = myTools.getDistribution("distribution.txt");

                    for (int i = 0 ; i<24 ;i++){
                        if(completedGame.letter.equals(letterChooser.getLetter(i))){
                            array[i]++;
                            break;
                        }
                    }
                    myTools.writeDistribution(array);


                    String newDistString=letterChooser.getSortedStringFromIntArray(letterChooser.getIncreasedDist(games,myTools.getDistribution("distribution.txt")));
                    if(!newDistString.equals(playerDistString)){
                        playerDistString=newDistString;
                        userDatabase.child("DistString").setValue(playerDistString);
                        for(int j=0;j<friends.size();j++){
                            mDatabaseUsers.child(friends.get(j).Id).child("Friends").child(mId).child("DistString").setValue(playerDistString);
                        }
                    }

                } else if (completedGame.sumPoints1 < completedGame.sumPoints2) {
                    myTools.addOldGame(false, completedGame.points1, completedGame.letter, completedGame.time1);

                    int [] array = myTools.getDistribution("distribution.txt");

                    for (int i = 0 ; i<24 ;i++){
                        if(completedGame.letter.equals(letterChooser.getLetter(i))){
                            array[i]++;
                            break;
                        }
                    }
                    myTools.writeDistribution(array);


                    String newDistString=letterChooser.getSortedStringFromIntArray(letterChooser.getIncreasedDist(games,myTools.getDistribution("distribution.txt")));
                    if(!newDistString.equals(playerDistString)){
                        playerDistString=newDistString;
                        userDatabase.child("DistString").setValue(playerDistString);
                        for(int j=0;j<friends.size();j++){
                            mDatabaseUsers.child(friends.get(j).Id).child("Friends").child(mId).child("DistString").setValue(playerDistString);
                        }
                    }

                } else {

                    if (completedGame.time1 < completedGame.time2) {
                        myTools.addOldGame(false, completedGame.points1, completedGame.letter, completedGame.time1);

                        int [] array = myTools.getDistribution("distribution.txt");

                        for (int i = 0 ; i<24 ;i++){
                            if(completedGame.letter.equals(letterChooser.getLetter(i))){
                                array[i]++;
                                break;
                            }
                        }
                        myTools.writeDistribution(array);

                        String newDistString=letterChooser.getSortedStringFromIntArray(letterChooser.getIncreasedDist(games,myTools.getDistribution("distribution.txt")));
                        if(!newDistString.equals(playerDistString)){
                            playerDistString=newDistString;
                            userDatabase.child("DistString").setValue(playerDistString);
                            for(int j=0;j<friends.size();j++){
                                mDatabaseUsers.child(friends.get(j).Id).child("Friends").child(mId).child("DistString").setValue(playerDistString);
                            }

                        }
                    } else if (completedGame.time1 > completedGame.time2) {
                        myTools.addOldGame(true, completedGame.points1, completedGame.letter, completedGame.time1);


                        int [] array = myTools.getDistribution("distribution.txt");

                        for (int i = 0 ; i<24 ;i++){
                            if(completedGame.letter.equals(letterChooser.getLetter(i))){
                                array[i]++;
                                break;
                            }
                        }
                        myTools.writeDistribution(array);


                        String newDistString=letterChooser.getSortedStringFromIntArray(letterChooser.getIncreasedDist(games,myTools.getDistribution("distribution.txt")));
                        if(!newDistString.equals(playerDistString)){
                            playerDistString=newDistString;
                            userDatabase.child("DistString").setValue(playerDistString);
                            for(int j=0;j<friends.size();j++){
                                mDatabaseUsers.child(friends.get(j).Id).child("Friends").child(mId).child("DistString").setValue(playerDistString);
                            }
                        }
                    } else {

                    }

                }
                button.setText("Μήνυμα ");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(playerLvl<10){
                            Toast.makeText(getBaseContext(),"Πρέπει να είσαι τουλάχιστον 10 level",Toast.LENGTH_SHORT).show();
                        }else{
                            postPopUp.dismiss();
                            sendMessage(completedGame.player2,completedGame.id2);
                        }
                    }
                });
                button.setTypeface(chalkTypeface);
                Button closeBtn = (Button) postGame.findViewById(R.id.close_button);
                closeBtn.setTypeface(chalkTypeface);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postPopUp.dismiss();
                        button.setVisibility(View.VISIBLE);
                        checkForMessages();
                    }
                });

            }else{
                Toast.makeText(getBaseContext(),"Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT).show();
            }
        }else if(games.get(index) instanceof HalfGame){



            postPopUp.showAtLocation(findViewById(R.id.relative),Gravity.CENTER,0,(int)(0.025*windowHeight));



            final HalfGame halfGame=(HalfGame)games.get(index);
            String letter=halfGame.letter;

            if( halfGame.player1.equals(playerName) ){
                letterView.setText(letter);
                name1Text.setText(halfGame.player1);
                name2Text.setText(halfGame.player2);
                timeView1.setText(myTools.timeString(halfGame.time)+" ");
                for (int j=0;j<6;j++){
                    p1ans[j].setText(halfGame.answers[j]);
                    p1pts[j].setText("");
                    if(halfGame.isTrue[j]){
                        p1ans[j].setTextColor(rightGreen);
                    }else{
                        p1ans[j].setTextColor(Color.RED);
                    }
                    p2ans[j].setText("");
                    p2pts[j].setText("");
                }
                final Button button=(Button)postGame.findViewById(R.id.post_button);
                button.setText("Ακύρωση ");
                button.setTypeface(chalkTypeface);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(MainActivity.hasInternet) {

                            alertPopUp=new PopupWindow(alertPopView,(int)(0.95*windowWidth),(int)(0.3*windowHeight),true);
                            alertPopUp.setAnimationStyle(R.style.AnimationStyle);
                            alertPopUp.setOutsideTouchable(false);
                            alertPopUp.setBackgroundDrawable(new BitmapDrawable());

                            TextView alertText=(TextView)alertPopView.findViewById(R.id.alert_message);
                            alertText.setTextSize(25);
                            alertText.setTypeface(chalkTypeface);

                            final int losePoints;
                            int[] bounds=levelHandler.getBounds(expPoints);
                            int dBounds=bounds[1]-bounds[0];

                            losePoints=dBounds*10/100;


                            alertText.setText("Θα χάσεις "+losePoints+" πόντους.\n Συνέχεια;");

                            Button yesButton=(Button)alertPopView.findViewById(R.id.yes_button);
                            yesButton.setTypeface(chalkTypeface);
                            yesButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(MainActivity.hasInternet) {
                                        if (expPoints - losePoints < 0) {
                                            expPoints = 0;
                                        } else {
                                            expPoints = expPoints - losePoints;
                                        }

                                        myTools.cancelGameFirebase(halfGame);

                                        userDatabase.child("ExpPoints").setValue(expPoints);
                                        if (playerLvl != levelHandler.getLevel(expPoints)) {
                                            if (playerLvl < levelHandler.getLevel(expPoints)) {
                                                if (playerTag.equals(levelHandler.getTag(levelHandler.getLevel(expPoints)))) {
                                                    myTools.sentLevelMessage(mId, "Πηρες Level :)\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "");
                                                } else {
                                                    myTools.sentLevelMessage(mId, "Πηρες Level :)\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "Αναβαθμίστηκες σε \n" + levelHandler.getTag(levelHandler.getLevel(expPoints)));
                                                }
                                            } else {
                                                if (playerTag.equals(levelHandler.getTag(levelHandler.getLevel(expPoints)))) {
                                                    myTools.sentLevelMessage(mId, "Έπεσες Level :(\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "");
                                                } else {
                                                    myTools.sentLevelMessage(mId, "Έπεσες Level :(\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "Υποβιβάστηκες σε \n" + levelHandler.getTag(levelHandler.getLevel(expPoints)));
                                                }
                                            }

                                            playerLvl = levelHandler.getLevel(expPoints);
                                            myTools.sendNewLevelToFriends(playerName, mId, friends, playerLvl);

                                        }

                                        alertPopUp.dismiss();
                                        postPopUp.dismiss();
                                        updateGameList(games);
                                    }else{
                                        Toast.makeText(getBaseContext(),"Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT).show();
                                    }
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


                            alertPopUp.showAtLocation(findViewById(R.id.relative),Gravity.CENTER,0,0);


                        }else{
                            Toast.makeText(getBaseContext(),"Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                Button closeBtn=(Button)postGame.findViewById(R.id.close_button);
                closeBtn.setTypeface(chalkTypeface);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postPopUp.dismiss();
                        checkForMessages();
                    }
                });

            }else{
                letterView.setText("?");
                timeView1.setText("");
                timeView2.setText("");
                name1Text.setText(halfGame.player2);
                name2Text.setText(halfGame.player1);
                for (int j=0;j<6;j++){
                    p1ans[j].setText("");
                    p1pts[j].setText("");
                    p2ans[j].setText("");
                    p2pts[j].setText("");
                }
                Button button=(Button)postGame.findViewById(R.id.post_button);
                button.setText("Παίξε ");
                button.setTypeface(chalkTypeface);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(MainActivity.hasInternet) {
                            boolean haveRead = true;
                            for (int i = 0; i < games.size(); i++) {
                                if (games.get(i) instanceof CompletedGame) {
                                    CompletedGame completedGame = (CompletedGame) games.get(i);
                                    if (completedGame.id2.equals(opId)) {
                                        haveRead = false;
                                        break;
                                    }
                                }
                            }
                            if (haveRead) {
                                String myChar = halfGame.letter;
                                Intent newGame = new Intent(ViewerActivity.this, GameActivity.class);
                                newGame.putExtra("playerName", playerName);
                                newGame.putExtra("playerID", mId);
                                newGame.putExtra("letter", myChar);
                                newGame.putExtra("opName", halfGame.player1);
                                newGame.putExtra("opId", halfGame.id1);
                                newGame.putExtra("isTrue", halfGame.isTrue);
                                newGame.putExtra("answers", halfGame.answers);
                                newGame.putExtra("opTime", halfGame.time);
                                newGame.putExtra("gameSounds", gameSounds);
                                newGame.putExtra("opLevel", halfGame.level1);
                                newGame.putExtra("playerLevel", playerLvl);
                                if (sounds) {
                                    MediaPlayer pageFlipping = MediaPlayer.create(getApplicationContext(), R.raw.pageflippinglong);
                                    pageFlipping.start();
                                }
                                startActivityForResult(newGame, ANSWER_GAME_REQUEST_RESULT_CODE);
                                overridePendingTransition(R.anim.game_fade_in, R.anim.noanim);

                                postPopUp.dismiss();
                            } else {
                                Toast.makeText(getBaseContext(), "Διάβασε το προηγούμενο παιχνίδι για να στείλεις νέο", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getBaseContext(),"Ελέγξτε την σύνδεσή σας και ξαναπροσπαθήστε",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Button closeBtn=(Button)postGame.findViewById(R.id.close_button);
                closeBtn.setTypeface(chalkTypeface);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postPopUp.dismiss();
                        checkForMessages();
                    }
                });

            }



        }
    }

    public void levelViewClick(View v){

        levelPopup=new PopupWindow(levelPopView,(int)(0.95*windowWidth),(int)(0.3*windowHeight),true);
        levelPopup.setAnimationStyle(R.style.LevelAnimationStyle);
        levelPopup.setOutsideTouchable(false);
        levelPopup.setBackgroundDrawable(new BitmapDrawable());
        levelPopup.showAtLocation(findViewById(R.id.relative),Gravity.CENTER,0,0);

        TextView expTag=(TextView)levelPopView.findViewById(R.id.exp_tag);
        expTag.setTypeface(chalkTypeface);

        TextView lvlTag=(TextView)levelPopView.findViewById(R.id.lvl_tag);
        lvlTag.setTypeface(chalkTypeface);

        ProgressBar progressBar=(ProgressBar)levelPopView.findViewById(R.id.progress);
        TextView nameText=(TextView)levelPopView.findViewById(R.id.nameViewLevel);
        nameText.setTypeface(chalkTypeface);
        nameText.setText(playerName+"");


        TextView tagText=(TextView)levelPopView.findViewById(R.id.tag_text);
        tagText.setTypeface(chalkTypeface);
        tagText.setText(levelHandler.getTag(levelHandler.getLevel(expPoints)));

        TextView levelText=(TextView)levelPopView.findViewById(R.id.level_text);
        levelText.setTypeface(chalkTypeface);
        levelText.setText(levelHandler.getLevel(expPoints)+" ");




        TextView expText=(TextView)levelPopView.findViewById(R.id.total_exp);
        expText.setTypeface(chalkTypeface);

        expText.setText(expPoints+" ");
        //expText.setText("10222222 ");

        int[] bounds=levelHandler.getBounds(expPoints);


        float percentage=1.f*(expPoints-bounds[0])/(1.f*bounds[1]-1.f*bounds[0]);
        int progressQuantity=Math.round(percentage*100);
        progressBar.setProgress(progressQuantity);


        TextView lowBoundText=(TextView)levelPopView.findViewById(R.id.bound0);
        lowBoundText.setTypeface(chalkTypeface);
        lowBoundText.setText((expPoints-bounds[0])+" / "+(bounds[1]-bounds[0])+"      ( "+progressQuantity+"% )");





    }

    public void checkForMessages(){


        dbMessages.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()>0){
                    if (messages.size()>0 && messagePopUp==null ){
                        index=0;
                        final int maxIndex=messages.size()-1;
                        final View messagePop=(View)layoutInflater.inflate(R.layout.message_layout,null);
                        messagePopUp = new PopupWindow(messagePop,(int)(0.95*windowWidth),(int)(0.4*windowHeight),true);
                        messagePopUp.setAnimationStyle(R.style.AnimationStyle);
                        messagePopUp.setTouchable(true);
                        messagePopUp.setFocusable(false);
                        messagePopUp.setOutsideTouchable(false);
                        messagePopUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {

                            }
                        });


                        final TextView indexTag=(TextView)messagePop.findViewById(R.id.indexTag);
                        indexTag.setTypeface(pencilTypeface);

                        final LinearLayout suggestionLayout=(LinearLayout)messagePop.findViewById(R.id.suggestion_layout);
                        final TextView suggestionTag=(TextView)messagePop.findViewById(R.id.suggestion_tag);
                        suggestionTag.setTypeface(pencilTypeface);


                        final TextView categoryTag=(TextView)messagePop.findViewById(R.id.category_tag);
                        categoryTag.setTypeface(pencilTypeface);

                        final TextView pointTag=(TextView)messagePop.findViewById(R.id.point_tag);
                        pointTag.setTypeface(pencilTypeface);

                        TextView suggestionIndex=(TextView)messagePop.findViewById(R.id.suggestion_index);
                        suggestionIndex.setTypeface(pencilTypeface);

                        final TextView ourMessage=(TextView)messagePop.findViewById(R.id.our_message);
                        ourMessage.setTypeface(pencilTypeface,Typeface.ITALIC);


                        final LinearLayout levelLayout=(LinearLayout)messagePop.findViewById(R.id.level_layout);
                        final TextView levelTag=(TextView)messagePop.findViewById(R.id.level_tag);
                        levelTag.setTypeface(pencilTypeface);

                        final TextView tagTag=(TextView)messagePop.findViewById(R.id.tag_tag);
                        tagTag.setTypeface(pencilTypeface);

                        final Button button = (Button)messagePop.findViewById(R.id.msgButton);
                        button.setTypeface(pencilTypeface);



                        final LinearLayout userLayout=(LinearLayout)messagePop.findViewById(R.id.user_layout);

                        final TextView senderText=(TextView)messagePop.findViewById(R.id.sender_tag);
                        senderText.setTypeface(pencilTypeface, Typeface.BOLD);

                        final TextView userText=(TextView)messagePop.findViewById(R.id.user_message_tag);
                        userText.setTypeface(pencilTypeface);

                        if(messages.get(index) instanceof LevelMessage){


                            levelLayout.setVisibility(View.VISIBLE);
                            suggestionLayout.setVisibility(View.INVISIBLE);
                            userLayout.setVisibility(View.INVISIBLE);

                            LevelMessage levelMessage=(LevelMessage)messages.get(index);

                            levelTag.setText(levelMessage.levelMessage);
                            tagTag.setText(levelMessage.TAGmessage);



                        }else if(messages.get(index)instanceof SuggestionMessage){

                            levelLayout.setVisibility(View.INVISIBLE);
                            suggestionLayout.setVisibility(View.VISIBLE);
                            userLayout.setVisibility(View.INVISIBLE);

                            SuggestionMessage suggestionMessage=(SuggestionMessage)messages.get(index);

                            suggestionTag.setText(suggestionMessage.playerSuggestion);
                            ourMessage.setText(suggestionMessage.ourMessage);
                            categoryTag.setText(suggestionMessage.categorySuggestion);


                            int[] bounds=levelHandler.getBounds(expPoints);
                            int dBounds=bounds[1]-bounds[0];

                            int finalPoints=dBounds*suggestionMessage.points/100;


                            if(finalPoints<0){
                                pointTag.setText("Έχασες "+-1*finalPoints+" exp");
                            }else if(suggestionMessage.points>0){
                                pointTag.setText("Πήρες "+finalPoints+" exp");
                            }else {
                                pointTag.setText("");
                            }
                            if (expPoints + finalPoints < 0) {
                                expPoints = 0;
                            } else {
                                expPoints = expPoints + finalPoints;
                            }
                            userDatabase.child("ExpPoints").setValue(expPoints);
                            if (playerLvl != levelHandler.getLevel(expPoints)) {
                                if (playerLvl < levelHandler.getLevel(expPoints)) {
                                    if (playerTag.equals(levelHandler.getTag(levelHandler.getLevel(expPoints)))) {
                                        myTools.sentLevelMessage(mId, "Πηρες Level :)\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "");
                                    } else {
                                        myTools.sentLevelMessage(mId, "Πηρες Level :)\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "Αναβαθμίστηκες σε \n" + levelHandler.getTag(levelHandler.getLevel(expPoints)));
                                    }
                                } else {
                                    if (playerTag.equals(levelHandler.getTag(levelHandler.getLevel(expPoints)))) {
                                        myTools.sentLevelMessage(mId, "Έπεσες Level :(\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "");
                                    } else {
                                        myTools.sentLevelMessage(mId, "Έπεσες Level :(\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "Υποβιβάστηκες σε \n" + levelHandler.getTag(levelHandler.getLevel(expPoints)));
                                    }
                                }

                                playerLvl = levelHandler.getLevel(expPoints);
                                myTools.sendNewLevelToFriends(playerName, mId, friends, playerLvl);

                            }
                        }else if(messages.get(index)instanceof UserMessage){
                            levelLayout.setVisibility(View.INVISIBLE);
                            suggestionLayout.setVisibility(View.INVISIBLE);
                            userLayout.setVisibility(View.VISIBLE);

                            UserMessage userMessage=(UserMessage)messages.get(index);
                            senderText.setText(userMessage.sender);
                            userText.setText(userMessage.message);


                        }

                        if (maxIndex > 0) {
                            indexTag.setText(index + 1 + "/" + (maxIndex + 1));
                        }else {
                            indexTag.setText("");
                        }

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(index==maxIndex) {
                                    for(int i=0;i<=maxIndex;i++){

                                        if(messages.get(i) instanceof LevelMessage){

                                            LevelMessage levelMessage=(LevelMessage)messages.get(i);

                                            dbMessages.child(levelMessage.hash).setValue(null);
                                        }else if(messages.get(i)instanceof SuggestionMessage){

                                            SuggestionMessage suggestionMessage=(SuggestionMessage)messages.get(i);

                                            dbMessages.child(suggestionMessage.hash).setValue(null);
                                        }else{

                                            UserMessage userMessage=(UserMessage)messages.get(i);
                                            dbMessages.child(userMessage.hash).setValue(null);
                                        }

                                    }

                                    messagePopUp.dismiss();
                                    messagePopUp=null;

                                }else{
                                    index++;

                                    if(messages.get(index) instanceof LevelMessage){

                                        levelLayout.setVisibility(View.VISIBLE);
                                        suggestionLayout.setVisibility(View.INVISIBLE);
                                        userLayout.setVisibility(View.INVISIBLE);

                                        LevelMessage levelMessage=(LevelMessage)messages.get(index);

                                        levelTag.setText(levelMessage.levelMessage);
                                        tagTag.setText(levelMessage.TAGmessage);

                                    }else if(messages.get(index)instanceof SuggestionMessage){

                                        levelLayout.setVisibility(View.INVISIBLE);
                                        suggestionLayout.setVisibility(View.VISIBLE);
                                        userLayout.setVisibility(View.INVISIBLE);

                                        SuggestionMessage suggestionMessage=(SuggestionMessage)messages.get(index);

                                        suggestionTag.setText(suggestionMessage.playerSuggestion);
                                        ourMessage.setText(suggestionMessage.ourMessage);
                                        categoryTag.setText(suggestionMessage.categorySuggestion);


                                        int[] bounds=levelHandler.getBounds(expPoints);
                                        int dBounds=bounds[1]-bounds[0];

                                        int finalPoints=dBounds*suggestionMessage.points/100;


                                        if(finalPoints<0){
                                            pointTag.setText("Έχασες "+-1*finalPoints+" exp");
                                        }else if(suggestionMessage.points>0){
                                            pointTag.setText("Πήρες "+finalPoints+" exp");
                                        }else {
                                            pointTag.setText("");
                                        }
                                        if (expPoints + finalPoints < 0) {
                                            expPoints = 0;
                                        } else {
                                            expPoints = expPoints + finalPoints;
                                        }
                                        userDatabase.child("ExpPoints").setValue(expPoints);
                                        if (playerLvl != levelHandler.getLevel(expPoints)) {
                                            if (playerLvl < levelHandler.getLevel(expPoints)) {
                                                if (playerTag.equals(levelHandler.getTag(levelHandler.getLevel(expPoints)))) {
                                                    myTools.sentLevelMessage(mId, "Πηρες Level :)\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "");
                                                } else {
                                                    myTools.sentLevelMessage(mId, "Πηρες Level :)\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "Αναβαθμίστηκες σε \n" + levelHandler.getTag(levelHandler.getLevel(expPoints)));
                                                }
                                            } else {
                                                if (playerTag.equals(levelHandler.getTag(levelHandler.getLevel(expPoints)))) {
                                                    myTools.sentLevelMessage(mId, "Έπεσες Level :(\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "");
                                                } else {
                                                    myTools.sentLevelMessage(mId, "Έπεσες Level :(\nΠλέον είσαι " + levelHandler.getLevel(expPoints) + " Lvl", "Υποβιβάστηκες σε \n" + levelHandler.getTag(levelHandler.getLevel(expPoints)));
                                                }
                                            }

                                            playerLvl = levelHandler.getLevel(expPoints);
                                            myTools.sendNewLevelToFriends(playerName, mId, friends, playerLvl);

                                        }
                                    }else if(messages.get(index)instanceof UserMessage){
                                        levelLayout.setVisibility(View.INVISIBLE);
                                        suggestionLayout.setVisibility(View.INVISIBLE);
                                        userLayout.setVisibility(View.VISIBLE);


                                        UserMessage userMessage=(UserMessage)messages.get(index);
                                        senderText.setText(userMessage.sender);
                                        userText.setText(userMessage.message);

                                    }
                                    indexTag.setText(index+1+"/"+(maxIndex+1));
                                }

                            }
                        });








                        messagePopUp.showAtLocation(findViewById(R.id.relative),Gravity.CENTER,0,0);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void sendMessage(String opName, final String opID){
        final View messagePop=(View)layoutInflater.inflate(R.layout.send_message_layout,null);
        final PopupWindow messagePopUp = new PopupWindow(messagePop,(int)(0.95*windowWidth),(int)(0.4*windowHeight),true);
        messagePopUp.setAnimationStyle(R.style.AnimationStyle);
        messagePopUp.setOutsideTouchable(false);

        TextView receiverText=(TextView)messagePop.findViewById(R.id.sender_tag);
        receiverText.setTypeface(pencilTypeface);

        receiverText.setText(opName);

        final EditText message=(EditText)messagePop.findViewById(R.id.user_message_tag);
        message.setFilters(new InputFilter[]{new InputFilter.LengthFilter(80)});
        message.setTypeface(pencilTypeface);




        Button okButton=(Button)messagePop.findViewById(R.id.msgButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String msg=message.getText().toString();


                UserMessage userMessage=new UserMessage(playerName,msg);

                progressDialogFiles=new ProgressDialog(ViewerActivity.this,ProgressDialog.THEME_HOLO_DARK);
                progressDialogFiles.setCancelable(false);
                progressDialogFiles.setCanceledOnTouchOutside(false);
                progressDialogFiles.setMessage("Αποστολή μηνύματος");
                progressDialogFiles.show();

                mDatabaseUsers.child(opID).child("Messages").push().setValue(userMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("WrongViewCast")
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialogFiles.dismiss();
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager != null) {
                            inputMethodManager.hideSoftInputFromWindow(findViewById(R.id.relative).getWindowToken(),0);
                        }
                        messagePopUp.dismiss();
                        checkForMessages();
                    }
                });
            }
        });


        Button cancelButton=(Button)messagePop.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messagePopUp.dismiss();
                checkForMessages();
            }
        });

        messagePopUp.showAtLocation(findViewById(R.id.relative),Gravity.CENTER,0,-100);
    }

    public void checkForFileUpdates(){

        if(emptyness){
            populateFiles(0);
            emptyness=false;
        }else{
            final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Updates").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("UpdateIndex")){
                        int UpdateIndex=dataSnapshot.child("UpdateIndex").getValue(Integer.class);

                        if(UpdateIndex!=playerUpdateIndex){
                            populateFiles(UpdateIndex);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



    }

    public void populateFiles(final int lastUpdate){

        progressDialogFiles=new ProgressDialog(ViewerActivity.this,ProgressDialog.THEME_HOLO_DARK);
        progressDialogFiles.setCancelable(false);
        progressDialogFiles.setCanceledOnTouchOutside(false);
        progressDialogFiles.setMessage("Αναβάθμιση λέξεων...0/6");
        progressDialogFiles.show();

        StorageReference namesReference=storageReference.child("/CategoryFiles/names.txt");
        namesReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String path="names.txt";
                try {
                    FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                    fos.write(bytes);
                    fos.close();
                    progressDialogFiles.setMessage("Αναβάθμιση λέξεων...1/6");
                    StorageReference animalsReference=storageReference.child("/CategoryFiles/animals.txt");
                    animalsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            String path="animals.txt";
                            try {
                                FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                fos.write(bytes);
                                fos.close();
                                progressDialogFiles.setMessage("Αναβάθμιση λέξεων...2/6");
                                StorageReference plantsReference=storageReference.child("/CategoryFiles/plants.txt");
                                plantsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        String path="plants.txt";
                                        try {
                                            FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                            fos.write(bytes);
                                            fos.close();
                                            progressDialogFiles.setMessage("Αναβάθμιση λέξεων...3/6");
                                            StorageReference professionsReference=storageReference.child("/CategoryFiles/professions.txt");
                                            professionsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                @Override
                                                public void onSuccess(byte[] bytes) {
                                                    String path="professions.txt";
                                                    try {
                                                        FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                                        fos.write(bytes);
                                                        fos.close();
                                                        progressDialogFiles.setMessage("Αναβάθμιση λέξεων...4/6");
                                                        StorageReference colorsReference=storageReference.child("/CategoryFiles/colors.txt");
                                                        colorsReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                            @Override
                                                            public void onSuccess(byte[] bytes) {
                                                                String path="colors.txt";
                                                                try {
                                                                    FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                                                    fos.write(bytes);
                                                                    fos.close();
                                                                    progressDialogFiles.setMessage("Αναβάθμιση λέξεων...5/6");
                                                                    StorageReference citiesReference=storageReference.child("/CategoryFiles/cities.txt");
                                                                    citiesReference.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                                        @Override
                                                                        public void onSuccess(byte[] bytes) {
                                                                            String path="cities.txt";
                                                                            try {
                                                                                FileOutputStream fos=getApplicationContext().openFileOutput(path,MODE_PRIVATE);
                                                                                fos.write(bytes);
                                                                                fos.close();

                                                                                userDatabase.child("UpdateIndex").setValue(lastUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        progressDialogFiles.setMessage("Αναβάθμιση λέξεων...6/6");
                                                                                        playerUpdateIndex=lastUpdate;
                                                                                        CountDownTimer countDownTimer=new CountDownTimer(1000,1000) {
                                                                                            @Override
                                                                                            public void onTick(long millisUntilFinished) {

                                                                                            }

                                                                                            @Override
                                                                                            public void onFinish() {
                                                                                                myTools.clearFile("namesuggestions.txt");
                                                                                                myTools.clearFile("animalsuggestions.txt");
                                                                                                myTools.clearFile("plantsuggestions.txt");
                                                                                                myTools.clearFile("professionsuggestions.txt");
                                                                                                myTools.clearFile("colorsuggestions.txt");
                                                                                                myTools.clearFile("citysuggestions.txt");
                                                                                                progressDialogFiles.dismiss();
                                                                                                Toast.makeText(getBaseContext(),"Η αναβάθμιση ολοκληρώθηκε",Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }.start();
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
                                                                            Toast.makeText(getBaseContext(),"Η αναβάθμιση απέτυχε",Toast.LENGTH_SHORT).show();
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
                                                                Toast.makeText(getBaseContext(),"Η αναβάθμιση απέτυχε",Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(getBaseContext(),"Η αναβάθμιση απέτυχε",Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getBaseContext(),"Η αναβάθμιση απέτυχε",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getBaseContext(),"Η αναβάθμιση απέτυχε",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getBaseContext(),"Η αναβάθμιση απέτυχε",Toast.LENGTH_SHORT).show();
            }
        });


















    }

    @Override
    protected void onResume(){

        super.onResume();


    }

    @Override
    public void onBackPressed(){


        alertPopUp=new PopupWindow(alertPopView,(int)(0.95*windowWidth),(int)(0.3*windowHeight),true);
        alertPopUp.setAnimationStyle(R.style.AnimationStyle);
        alertPopUp.setOutsideTouchable(false);
        alertPopUp.setBackgroundDrawable(new BitmapDrawable());

        TextView alertText=(TextView)alertPopView.findViewById(R.id.alert_message);
        alertText.setTypeface(chalkTypeface);
        alertText.setText("Έξοδος;");

        Button yesButton=(Button)alertPopView.findViewById(R.id.yes_button);
        yesButton.setTypeface(chalkTypeface);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertPopUp.dismiss();
                finishAffinity();
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


        alertPopUp.showAtLocation(findViewById(R.id.relative),Gravity.CENTER,0,0);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onStop() {

        dbMessages.removeEventListener(messageListener);
        userCompletedGames.removeEventListener(completedGameListener);
        userGames.removeEventListener(gameListener);
        userFriends.removeEventListener(friendListener);

        Stats stats = new Stats(myTools.readOldGames());
        myTools.writeDistribution(stats.distFromMin);

        if(messagePopUp!=null){
            messagePopUp.dismiss();
            messagePopUp=null;
        }

        stopService(new Intent(ViewerActivity.this,FriendNotificationService.class));

        if(!logOut) {
            Intent intent = new Intent(ViewerActivity.this, NotificationService.class);
            intent.putExtra("user_id", mId);
            startService(intent);
            Intent intent1 = new Intent(ViewerActivity.this,FriendNotificationService.class);
            intent1.putExtra("user_id",mId);
            startService(intent1);
        }

        unregisterReceiver(networkChangeReceiver);
        games.clear();
        friends.clear();
        messages.clear();
        super.onStop();

    }




    @Override
    protected void onRestart() {

        super.onRestart();


        if (!MainActivity.hasInternet){
            Toast.makeText(getBaseContext(),"Ελέγξτε την σύνδεσή σας",Toast.LENGTH_SHORT).show();
        }


        dbMessages.addChildEventListener(messageListener);

        userFriends.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsCount= (int) dataSnapshot.getChildrenCount();
                userFriends.addChildEventListener(friendListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        uploadAndCheckTimes();
        updateGameList(games);


    }

    @Override
    protected void onStart(){

        super.onStart();

        IntentFilter filter=new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(networkChangeReceiver,filter);
        //unregisterReceiver(networkChangeReceiver);

        checkForFileUpdates();



        stopService(new Intent(ViewerActivity.this,NotificationService.class));
        stopService(new Intent(ViewerActivity.this,FriendNotificationService.class));

        Intent intent = new Intent(ViewerActivity.this,FriendNotificationService.class);
        intent.putExtra("user_id",mId);
        startService(intent);


        updateGameList(games);


        CountDownTimer countDownTimer=new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                checkForMessages();
            }
        }.start();

        //checkForMessages();

    }

    public void updateGameList(ArrayList<Game> newGames){
        gameAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}


