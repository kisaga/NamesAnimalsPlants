package com.example.user.summerproject.myTools;


import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;



import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

public class LisgarTools{


    private Context c;
    public LisgarTools(Context c){
        this.c=c;
    }



    public void clearFile(String name){
        try {
            FileOutputStream fos;
            fos = c.openFileOutput(name, MODE_PRIVATE);
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String fileString(String name)  {
        String neuText ="";
        try {

            BufferedReader reader=null;


            FileInputStream fin = c.openFileInput(name);


            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            //do reading usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {

                neuText +=mLine+"\n";
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return neuText;

    }

    public InputStream inputFileString(String name) throws IOException {


        BufferedReader reader=null;
        String neuText ="";

        InputStream fin = c.openFileInput(name);



        //do reading usually loop until end of file reading

        return fin;

    }


    public String timeString(int time){

        int deciseconds=time%100;

        int seconds=(time-deciseconds)/100;

        String secondsString;
        if(seconds<10){
            secondsString="0"+seconds;
        }else{
            secondsString=""+seconds;
        }
        String decisecondsString;
        if(deciseconds<10){
            decisecondsString="0"+deciseconds;
        }else{
            decisecondsString=""+deciseconds;
        }

        return secondsString+":"+decisecondsString;


    }


    public void addOldGame(boolean won, int[] points, String letter, int time) {
        String wonString;
        if (won) {
            wonString = "1 ";
        } else {
            wonString = "0 ";
        }
        String pointsString = "";
        for (int i = 0; i < points.length; i++) {
            pointsString += points[i] + " ";
        }
        String letterString = letter + " ";
        String timeString = time + " ";
        String mLine = wonString + letterString + timeString + pointsString;


        try {

            FileOutputStream fos;
            fos = c.openFileOutput("oldgames.txt", MODE_APPEND);
            //out.write(mLine);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRequestedGame(String letter, String player1, String player2, int time, String[] answers, boolean[] isTrue) {
        String letterString = letter + " ";
        String nameString = player1 + " " + player2 + " ";
        String timeString = time + " ";

        String answersString = "";
        for (int i = 0; i < answers.length; i++) {

            answersString += answers[i] + " ";

        }

        String isTrueString = "";
        for (int i = 0; i < isTrue.length; i++) {
            if (isTrue[i]){
                isTrueString += "1" + " ";
            }else{
                isTrueString += "0" + " ";
            }


        }


        String mLine = letterString + nameString +timeString+ answersString + isTrueString;


        try {
            FileOutputStream fos;
            fos = c.openFileOutput("requestedgames.txt", MODE_APPEND);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRequestedGame(HalfGame halfGame){
        String letterString = halfGame.letter + " ";
        String nameString =  halfGame.player1 + " " +  halfGame.player2 + " ";
        String timeString =  halfGame.time + " ";

        String answersString = "";
        for (int i = 0; i <  halfGame.answers.length; i++) {

            answersString +=  halfGame.answers[i] + " ";

        }

        String isTrueString = "";
        for (int i = 0; i <  halfGame.isTrue.length; i++) {

            if ( halfGame.isTrue[i]){
                isTrueString += "1" + " ";
            }else{
                isTrueString += "0" + " ";
            }

        }


        String mLine = letterString + nameString +timeString+ answersString + isTrueString;


        try {
            FileOutputStream fos;
            fos = c.openFileOutput("requestedgames.txt", MODE_APPEND);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSentGame(String letter, String player1, String player2, int time, String[] answers, boolean[] isTrue) {
        String letterString = letter + " ";
        String nameString = player1 + " " + player2 + " ";
        String timeString = time + " ";

        String answersString = "";
        for (int i = 0; i < answers.length; i++) {

            answersString += answers[i] + " ";

        }

        String isTrueString = "";
        for (int i = 0; i < isTrue.length; i++) {

            if (isTrue[i]){
                isTrueString += "1" + " ";
            }else{
                isTrueString += "0" + " ";
            }

        }


        String mLine = letterString + nameString +timeString+ answersString + isTrueString;


        try {
            FileOutputStream fos;
            fos = c.openFileOutput("sentgames.txt", MODE_APPEND);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSentGame(HalfGame halfGame){
        String letterString = halfGame.letter + " ";
        String nameString =  halfGame.player1 + " " +  halfGame.player2 + " ";
        String timeString =  halfGame.time + " ";

        String answersString = "";
        for (int i = 0; i <  halfGame.answers.length; i++) {

            answersString +=  halfGame.answers[i] + " ";

        }

        String isTrueString = "";
        for (int i = 0; i <  halfGame.isTrue.length; i++) {

            if ( halfGame.isTrue[i]){
                isTrueString += "1" + " ";
            }else{
                isTrueString += "0" + " ";
            }

        }


        String mLine = letterString + nameString +timeString+ answersString + isTrueString;


        try {
            FileOutputStream fos;
            fos = c.openFileOutput("sentgames.txt", MODE_APPEND);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCompletedGame(HalfGame firstHalf, HalfGame secondHalf) {
        String letterString = firstHalf.letter + " ";
        String nameString1 = firstHalf.player1 + " ";
        String nameString2 = secondHalf.player2 + " ";
        String timeString1 = firstHalf.time + " ";
        String timeString2 = secondHalf.time + " ";


        int[] points1 = new int[6];
        int[] points2 = new int[6];

        int reducedPoints = 5;
        int standardPoints = 10;
        int raisedPoints = 20;

        for (int i = 0; i <= 5; i++) {

            if (firstHalf.isTrue[i] && secondHalf.isTrue[i]) {

                if (firstHalf.answers[i].equals(secondHalf.answers[i])) {

                    points1[i] = reducedPoints;
                    points2[i] = reducedPoints;

                } else {

                    points1[i] = standardPoints;
                    points2[i] = standardPoints;

                }

            } else if (firstHalf.isTrue[i] && !secondHalf.isTrue[i]) {

                points1[i] = raisedPoints;
                points2[i] = 0;

            } else if (!firstHalf.isTrue[i] && secondHalf.isTrue[i]) {

                points1[i] = 0;
                points2[i] = raisedPoints;

            } else {

                points1[i] = 0;
                points2[i] = 0;

            }

        }
        String answerString1="";
        String isTrueString1="";
        String pointsString1="";
        String answerString2="";
        String isTrueString2="";
        String pointsString2="";
        for(int i=0; i<6; i++){
            answerString1+=firstHalf.answers[i]+" ";
            if(firstHalf.isTrue[i]){
                isTrueString1+="1"+" ";
            }else{
                isTrueString1+="0"+" ";
            }

            pointsString1+=points1[i]+" ";
            answerString2+=secondHalf.answers[i]+" ";
            if(secondHalf.isTrue[i]){
                isTrueString2+="1"+" ";
            }else{
                isTrueString2+="0"+" ";
            }
            pointsString2+=points2[i]+" ";
        }
        String mLine=letterString+nameString1+timeString1+answerString1+isTrueString1+pointsString1+nameString2+timeString2+answerString2+isTrueString2+pointsString2;
        try {
            FileOutputStream fos;
            fos = c.openFileOutput("completedgames.txt", MODE_PRIVATE);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCompletedGame(CompletedGame completedGame){
        String letterString = completedGame.letter + "¨";
        String nameString1 = completedGame.player1 + "¨";
        String nameString2 = completedGame.player2 + "¨";
        String timeString1 = completedGame.time1 + "¨";
        String timeString2 = completedGame.time2 + "¨";
        String levelString1 = completedGame.level1+"¨";
        String levelString2 = completedGame.level2+"¨";


        int[] points1 = new int[6];
        int[] points2 = new int[6];

        int reducedPoints = 5;
        int standardPoints = 10;
        int raisedPoints = 20;

        for (int i = 0; i <= 5; i++) {

            if (completedGame.isTrue1[i] && completedGame.isTrue2[i]) {

                if (completedGame.answers1[i].equals(completedGame.answers2[i])) {

                    points1[i] = reducedPoints;
                    points2[i] = reducedPoints;

                } else {

                    points1[i] = standardPoints;
                    points2[i] = standardPoints;

                }

            } else if (completedGame.isTrue1[i] && !completedGame.isTrue2[i]) {

                points1[i] = raisedPoints;
                points2[i] = 0;

            } else if (!completedGame.isTrue1[i] && completedGame.isTrue2[i]) {

                points1[i] = 0;
                points2[i] = raisedPoints;

            } else {

                points1[i] = 0;
                points2[i] = 0;

            }

        }
        String answerString1="";
        String isTrueString1="";
        String pointsString1="";
        String answerString2="";
        String isTrueString2="";
        String pointsString2="";
        for(int i=0; i<6; i++){
            answerString1+=completedGame.answers1[i]+"¨";
            if(completedGame.isTrue1[i]){
                isTrueString1+="1"+"¨";
            }else{
                isTrueString1+="0"+"¨";
            }

            pointsString1+=points1[i]+"¨";
            answerString2+=completedGame.answers2[i]+"¨";
            if(completedGame.isTrue2[i]){
                isTrueString2+="1"+"¨";
            }else{
                isTrueString2+="0"+"¨";
            }
            pointsString2+=points2[i]+"¨";
        }
        String mLine=letterString+nameString1+levelString1+timeString1+answerString1+isTrueString1+pointsString1+nameString2+levelString2+timeString2+answerString2+isTrueString2+pointsString2;
        try {
            FileOutputStream fos;
            fos = c.openFileOutput("completedgames.txt", MODE_PRIVATE);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public void writeDistribution(int [] array){

        String mLine = "";

        for (int i=0;i<24;i++){
            mLine+=array[i]+"\n";


        }

        try {

            FileOutputStream fos;
            fos = c.openFileOutput("distribution.txt", MODE_PRIVATE);
            //out.write(mLine);
            fos.write(mLine.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public int [] getDistribution(String path){

       String FILE_NAME = path;
       int sizeDist =0;

       try{
           FileInputStream fin = c.openFileInput(FILE_NAME);
           int size;
           while ((size = fin.read()) != -1) {
               // add & append content
               if (Character.toString((char)size).equals("\n")) {
                   sizeDist++;
               }

           }


           fin.close();

       } catch (Exception error) {
           // Exception
           error.printStackTrace();
       }

       int [] dist = new int[sizeDist];


       try{
           BufferedReader reader=null;
           FileInputStream fin = c.openFileInput(FILE_NAME);
           reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
           int size;
           int lnIndex = 0;
           String trash="";
           String character;
           int elementIndex = 0;
           // read inside if it is not null (-1 means empty)
           while ((size = reader.read()) != -1) {
               character=Character.toString((char)size);
               if( character.equals("\n") ){
                   dist[lnIndex]=Integer.parseInt(trash);
                   lnIndex++;
                   elementIndex=0;
                   trash="";
               }else{
                   trash += character;
               }
           }
           // Set text to TextView
           reader.close();
           return dist;
       } catch (Exception error) {
           // Exception

           error.printStackTrace();
           return dist;

       }



   }


    public void writeLocation(String path,int mnc,int lac){


        String mLine=mnc+","+lac;
        try {
            FileOutputStream fos;
            fos = c.openFileOutput(path, MODE_PRIVATE);
            fos.write(mLine.getBytes());
            fos.write("\n".getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addSentGameFirebase(String letter,String id1,String playerName,int playerLevel,String id2,String opponentName,int opLevel, int time, String[] answers, boolean[] isTrue){

        DatabaseReference mDatabaseUsers;
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        DatabaseReference userDatabase=mDatabaseUsers.child(id1);
        DatabaseReference opponentDatabase=mDatabaseUsers.child(id2);

        DatabaseReference userGames=userDatabase.child("Games");
        DatabaseReference opponentGames=opponentDatabase.child("Games");



        //========user game data=======
        HalfGameFirebase1 halfGameFirebase1=new HalfGameFirebase1(letter,opponentName,opLevel,time,answers,isTrue);
        userGames.push().setValue(halfGameFirebase1);

        //===opponent game data ========
        HalfGameFirebase2 halfGameFirebase2=new HalfGameFirebase2(letter,playerName,playerLevel,time,answers,isTrue);
        opponentGames.push().setValue(halfGameFirebase2);


    }

    public void answerGameFirebase(String id1, final String player1, String id2, final String player2, int time, String[] answers, boolean[] isTrue, HalfGame halfGame){
        DatabaseReference mDatabaseUsers;
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        DatabaseReference userDatabase=mDatabaseUsers.child(id1);
        DatabaseReference opponentDatabase=mDatabaseUsers.child(id2);

        final DatabaseReference userGames=userDatabase.child("Games");
        DatabaseReference userCompletedGames=userGames.child("CompletedGames");
        final DatabaseReference opponentGames=opponentDatabase.child("Games");
        DatabaseReference opponentCompletedGames=opponentGames.child("CompletedGames");





        userGames.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild("p2a1") && !dataSnapshot.hasChild("p1a1")){
                    String name=dataSnapshot.child("opName").getValue(String.class);
                    if(name.equals(player2)){
                        String key=dataSnapshot.getKey();
                        userGames.child(key).setValue(null);
                        userGames.removeEventListener(this);
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

        opponentGames.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild("p1a1") && !dataSnapshot.hasChild("p2a1")){
                    String name=dataSnapshot.child("opName").getValue(String.class);
                    if(name.equals(player1)){
                        String key=dataSnapshot.getKey();
                        opponentGames.child(key).setValue(null);
                        opponentGames.removeEventListener(this);
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

        DatabaseReference userCompletedGame=userCompletedGames.child(id2);
        DatabaseReference opponentCompletedGame=opponentCompletedGames.child(id1);


        //========user game data========
        CompletedGameFirebase1 completedGameFirebase1=new CompletedGameFirebase1(time,answers,isTrue,halfGame);
        userCompletedGame.setValue(completedGameFirebase1);



        //===opponent game data ========
        CompletedGameFirebase2 completedGameFirebase2=new CompletedGameFirebase2(time,answers,isTrue,halfGame);
        opponentCompletedGame.setValue(completedGameFirebase2);





    }

    public void removeGameFirebase(CompletedGame completedGame){
        DatabaseReference mDatabaseUsers;
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        DatabaseReference userDatabase=mDatabaseUsers.child(completedGame.id1);

        DatabaseReference userGames=userDatabase.child("Games");

        DatabaseReference userGame=userGames.child("CompletedGames").child(completedGame.id2);

        //================NEW CODE===================//
        DatabaseReference opponentFriends=mDatabaseUsers.child(completedGame.id2).child("Friends");
        //===========================================//


        userGame.setValue(null);

    }

    public void cancelGameFirebase(final HalfGame halfGame){
        DatabaseReference mDatabaseUsers;
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        DatabaseReference userDatabase=mDatabaseUsers.child(halfGame.id1);
        DatabaseReference opponentDatabase=mDatabaseUsers.child(halfGame.id2);

        final DatabaseReference userGames=userDatabase.child("Games");
        final DatabaseReference opponentGames=opponentDatabase.child("Games");

        //================NEW CODE===================//
        DatabaseReference userFriends=userDatabase.child("Friends");
        DatabaseReference opponentFriends=opponentDatabase.child("Friends");
        //===========================================//


        userGames.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String opName=dataSnapshot.child("opName").getValue(String.class);
                if(opName.equals(halfGame.player2)){
                    userGames.child(dataSnapshot.getKey()).setValue(null);
                    userGames.removeEventListener(this);
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


        opponentGames.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String opName=dataSnapshot.child("opName").getValue(String.class);
                if(opName.equals(halfGame.player1)){
                    opponentGames.child(dataSnapshot.getKey()).setValue(null);
                    opponentGames.removeEventListener(this);
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

    public void sentLevelMessage(String mID, String levelMessage, String TAGmessage){

        Message message = new LevelMessage(levelMessage,TAGmessage);

        DatabaseReference mDatabaseUsers;
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        DatabaseReference userDatabase=mDatabaseUsers.child(mID);
        DatabaseReference userMessages=userDatabase.child("Messages");

        userMessages.push().setValue(message);



    }



    public void sendNewLevelToFriends(String mName,String mId,ArrayList<Friend> friends,int playerLevel){

        DatabaseReference mDatabaseUsers;
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");


        DatabaseReference userFriends=mDatabaseUsers.child(mId).child("Friends");

        for(int i=0;i<friends.size();i++){
            DatabaseReference opponentFriendsMyDirectory=mDatabaseUsers.child(friends.get(i).Id).child("Friends").child(mId);
            opponentFriendsMyDirectory.child("Lvl").setValue(playerLevel);
        }
    }


    public void addFriends(String mName,String mId,Friend[] newFriends){


        DatabaseReference mDatabaseUsers;
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");


        DatabaseReference userFriends=mDatabaseUsers.child(mId).child("Friends");



        Friend me=new Friend(mName,mId,1,"");


        for(int i=0; i<newFriends.length;i++){

            userFriends.child(newFriends[i].Id).setValue(newFriends[i]);

            DatabaseReference opponentFriends=mDatabaseUsers.child(newFriends[i].Id).child("Friends");

            opponentFriends.child(mId).setValue(me);

        }




    }

    public void addFriend(String mName,String mId,String DistString,Friend newFriend){
        DatabaseReference mDatabaseUsers;
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        DatabaseReference userFriends=mDatabaseUsers.child(mId).child("Friends");

        Friend me=new Friend(mName,mId,1,DistString);



        userFriends.child(newFriend.Id).setValue(newFriend);

        DatabaseReference opponentFriends=mDatabaseUsers.child(newFriend.Id).child("Friends");

        opponentFriends.child(mId).setValue(me);


    }

    public void sendRequest(String mName,String mId,String opName,String opId){
        DatabaseReference mDatabaseUsers;
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        Request requestSent=new Request(opName,opId);
        DatabaseReference userRequests=mDatabaseUsers.child(mId).child("Requests").child("RequestsSent");
        userRequests.child(opId).setValue(requestSent);


        Request requestReceived=new Request(mName,mId);
        DatabaseReference opponentRequests=mDatabaseUsers.child(opId).child("Requests").child("RequestsReceived");
        opponentRequests.child(mId).setValue(requestReceived);


    }

    public void acceptRequest(String mName,String mId,String mDistString,int mLvl,String opName,String opId,String opDistString,int opLevel){
        DatabaseReference mDatabaseUsers;
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");


        DatabaseReference userFriends=mDatabaseUsers.child(mId).child("Friends");

        DatabaseReference opponentFriends=mDatabaseUsers.child(opId).child("Friends");

        Friend me=new Friend(mName,mId,mLvl,mDistString);

        Friend op=new Friend(opName,opId,opLevel,opDistString);

        op.notificationHasSent=true;

        userFriends.child(opId).setValue(op);



        opponentFriends.child(mId).setValue(me);


        DatabaseReference userRequests=mDatabaseUsers.child(mId).child("Requests").child("RequestsReceived");
        userRequests.child(opId).setValue(null);


        DatabaseReference opponentRequests=mDatabaseUsers.child(opId).child("Requests").child("RequestsSent");
        opponentRequests.child(mId).setValue(null);


    }

    public void declineRequest(String mName,String mId,String opName,String opId){
        DatabaseReference mDatabaseUsers;
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        DatabaseReference userRequests=mDatabaseUsers.child(mId).child("Requests").child("RequestsReceived");
        userRequests.child(opId).setValue(null);


        DatabaseReference opponentRequests=mDatabaseUsers.child(opId).child("Requests").child("RequestsSent");
        opponentRequests.child(mId).setValue(null);
    }

    public void deleteFriend(String mId, final String playerName, String friendId, final String friendName){
        DatabaseReference mDatabaseUsers;
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        DatabaseReference userDatabase=mDatabaseUsers.child(mId);
        DatabaseReference opponentDatabase=mDatabaseUsers.child(friendId);

        final DatabaseReference userGames=userDatabase.child("Games");
        final DatabaseReference opponentGames=opponentDatabase.child("Games");


        DatabaseReference userFriends=userDatabase.child("Friends");
        DatabaseReference opponentFriends=opponentDatabase.child("Friends");

        userFriends.child(friendId).setValue(null);
        opponentFriends.child(mId).setValue(null);

        userGames.child("CompletedGames").child(friendId).setValue(null);
        userGames.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String opName=dataSnapshot.child("opName").getValue(String.class);
                if(opName.equals(friendName)){
                    String hash=dataSnapshot.getKey();
                    userGames.child(hash).setValue(null);
                    userGames.removeEventListener(this);
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


        opponentGames.child("CompletedGames").child(mId).setValue(null);
        opponentGames.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String opName=dataSnapshot.child("opName").getValue(String.class);
                if(opName.equals(playerName)){
                    String hash=dataSnapshot.getKey();
                    opponentGames.child(hash).setValue(null);
                    opponentGames.removeEventListener(this);
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




    public OldGame[] readOldGames(){

        String FILE_NAME = "oldgames.txt";
        int sizeOldGames =0;

        try{
            FileInputStream fin = c.openFileInput(FILE_NAME);
            int size;
            while ((size = fin.read()) != -1) {
                // add & append content
                if (Character.toString((char)size).equals("\n")) {
                    sizeOldGames++;
                }

            }


            fin.close();

        } catch (Exception error) {
            // Exception
            error.printStackTrace();
        }

        OldGame[] oldGame = new OldGame[sizeOldGames];
        for(int i=0;i<sizeOldGames; i++){
            oldGame[i]=new OldGame();
            oldGame[i].points=new int[6];
        }

        try{
            BufferedReader reader=null;
            FileInputStream fin = c.openFileInput(FILE_NAME);
            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            int size;
            int lnIndex = 0;
            String trash="";
            String character;
            int elementIndex = 0;
            // read inside if it is not null (-1 means empty)
            while ((size = reader.read()) != -1) {
                character=Character.toString((char)size);
                if( character.equals("\n") ){
                    lnIndex++;
                    elementIndex=0;
                    trash="";
                }else if(character.equals(" ")){
                    switch (elementIndex){
                        case 0:

                            if(trash.equals("0")){
                                oldGame[lnIndex].won=false;
                            }else{
                                oldGame[lnIndex].won=true;
                            }
                            break;
                        case 1:
                            oldGame[lnIndex].letter=trash;
                            break;
                        case 2:
                            oldGame[lnIndex].time=Integer.parseInt(trash);
                            break;
                        case 3:
                            oldGame[lnIndex].points[0]=Integer.parseInt(trash);
                            break;
                        case 4:
                            oldGame[lnIndex].points[1]=Integer.parseInt(trash);
                            break;
                        case 5:
                            oldGame[lnIndex].points[2]=Integer.parseInt(trash);
                            break;
                        case 6:
                            oldGame[lnIndex].points[3]=Integer.parseInt(trash);
                            break;
                        case 7:
                            oldGame[lnIndex].points[4]=Integer.parseInt(trash);
                            break;
                        case 8:
                            oldGame[lnIndex].points[5]=Integer.parseInt(trash);
                            break;
                    }
                    elementIndex++;
                    trash="";
                }else{
                    trash += character;
                }
            }
            // Set text to TextView
            reader.close();
            return oldGame;
        } catch (Exception error) {
            // Exception

            error.printStackTrace();
            return oldGame;

        }

    }

    public HalfGame[] readRequestedGames(){

        String FILE_NAME = "requestedgames.txt";

        HalfGame[] halfGames;
        int sizeHalfGames=0;

        try{
            FileInputStream fis=c.openFileInput(FILE_NAME);
            int size;
            while( ( size = fis.read() )!= -1){
                if(Character.toString((char)size).equals("\n")){
                    sizeHalfGames+=1;
                }
            }

        }catch(IOException e){
            e.printStackTrace();
        }

        halfGames=new HalfGame[sizeHalfGames];
        for(int i=0;i<sizeHalfGames;i++){
            halfGames[i]=new HalfGame();
            halfGames[i].answers=new String[6];
            halfGames[i].isTrue=new boolean[6];

        }

        try{
            BufferedReader reader=null;
            FileInputStream fin = c.openFileInput(FILE_NAME);
            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            int size;
            int lnIndex=0;
            String trash="";
            String character;
            int elementIndex=0;

            while( ( size = reader.read() )!= -1){

                character=Character.toString((char)size);

                if( character.equals("\n") ){
                    lnIndex++;
                    elementIndex=0;
                    trash="";
                }else if( character.equals(" ") ){
                    switch (elementIndex){
                        case 0:
                            halfGames[lnIndex].letter=trash;
                            break;
                        case 1:
                            halfGames[lnIndex].player1=trash;
                            break;
                        case 2:
                            halfGames[lnIndex].player2=trash;
                            break;
                        case 3:
                            halfGames[lnIndex].time=Integer.parseInt(trash);
                            break;
                        case 4:
                            halfGames[lnIndex].answers[0]=trash;
                            break;
                        case 5:
                            halfGames[lnIndex].answers[1]=trash;
                            break;
                        case 6:
                            halfGames[lnIndex].answers[2]=trash;
                            break;
                        case 7:
                            halfGames[lnIndex].answers[3]=trash;
                            break;
                        case 8:
                            halfGames[lnIndex].answers[4]=trash;
                            break;
                        case 9:
                            halfGames[lnIndex].answers[5]=trash;
                            break;
                        case 10:
                            if(trash.equals("0")){
                                halfGames[lnIndex].isTrue[0]=false;
                            }else{
                                halfGames[lnIndex].isTrue[0]=true;
                            }
                            break;
                        case 11:
                            if(trash.equals("0")){
                                halfGames[lnIndex].isTrue[1]=false;
                            }else{
                                halfGames[lnIndex].isTrue[1]=true;
                            }
                            break;
                        case 12:
                            if(trash.equals("0")){
                                halfGames[lnIndex].isTrue[2]=false;
                            }else{
                                halfGames[lnIndex].isTrue[2]=true;
                            }
                            break;
                        case 13:
                            if(trash.equals("0")){
                                halfGames[lnIndex].isTrue[3]=false;
                            }else{
                                halfGames[lnIndex].isTrue[3]=true;
                            }
                            break;
                        case 14:
                            if(trash.equals("0")){
                                halfGames[lnIndex].isTrue[4]=false;
                            }else{
                                halfGames[lnIndex].isTrue[4]=true;
                            }
                            break;
                        case 15:
                            if(trash.equals("0")){
                                halfGames[lnIndex].isTrue[5]=false;
                            }else{
                                halfGames[lnIndex].isTrue[5]=true;
                            }
                            break;
                    }
                    elementIndex++;
                    trash="";

                }else{

                    trash += character;
                }
            }
            reader.close();
            return halfGames;
        }catch(IOException e){
            e.printStackTrace();
            return halfGames;
        }
    }

    public HalfGame[] readSentGames(){

        String FILE_NAME = "sentgames.txt";

        HalfGame[] halfGames;
        int sizeHalfGames=0;

        try{
            FileInputStream fis=c.openFileInput(FILE_NAME);
            int size;
            while( ( size = fis.read() )!= -1){
                if(Character.toString((char)size).equals("\n")){
                    sizeHalfGames+=1;
                }
            }

        }catch(IOException e){
            e.printStackTrace();
        }

        halfGames=new HalfGame[sizeHalfGames];
        for(int i=0;i<sizeHalfGames;i++){
            halfGames[i]=new HalfGame();
            halfGames[i].answers=new String[6];
            halfGames[i].isTrue=new boolean[6];

        }

        try{
            BufferedReader reader=null;
            FileInputStream fin = c.openFileInput(FILE_NAME);
            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            int size;
            int lnIndex=0;
            String trash="";
            String character;
            int elementIndex=0;

            while( ( size = reader.read() )!= -1){

                character=Character.toString((char)size);

                if( character.equals("\n") ){
                    lnIndex++;
                    elementIndex=0;
                    trash="";
                }else if( character.equals(" ") ){
                    switch (elementIndex){
                        case 0:
                            halfGames[lnIndex].letter=trash;
                            break;
                        case 1:
                            halfGames[lnIndex].player1=trash;
                            break;
                        case 2:
                            halfGames[lnIndex].player2=trash;
                            break;
                        case 3:
                            halfGames[lnIndex].time=Integer.parseInt(trash);
                            break;
                        case 4:
                            halfGames[lnIndex].answers[0]=trash;
                            break;
                        case 5:
                            halfGames[lnIndex].answers[1]=trash;
                            break;
                        case 6:
                            halfGames[lnIndex].answers[2]=trash;
                            break;
                        case 7:
                            halfGames[lnIndex].answers[3]=trash;
                            break;
                        case 8:
                            halfGames[lnIndex].answers[4]=trash;
                            break;
                        case 9:
                            halfGames[lnIndex].answers[5]=trash;
                            break;
                        case 10:
                            if(trash.equals("0")){
                                halfGames[lnIndex].isTrue[0]=false;
                            }else{
                                halfGames[lnIndex].isTrue[0]=true;
                            }
                            break;
                        case 11:
                            if(trash.equals("0")){
                                halfGames[lnIndex].isTrue[1]=false;
                            }else{
                                halfGames[lnIndex].isTrue[1]=true;
                            }
                            break;
                        case 12:
                            if(trash.equals("0")){
                                halfGames[lnIndex].isTrue[2]=false;
                            }else{
                                halfGames[lnIndex].isTrue[2]=true;
                            }
                            break;
                        case 13:
                            if(trash.equals("0")){
                                halfGames[lnIndex].isTrue[3]=false;
                            }else{
                                halfGames[lnIndex].isTrue[3]=true;
                            }
                            break;
                        case 14:
                            if(trash.equals("0")){
                                halfGames[lnIndex].isTrue[4]=false;
                            }else{
                                halfGames[lnIndex].isTrue[4]=true;
                            }
                            break;
                        case 15:
                            if(trash.equals("0")){
                                halfGames[lnIndex].isTrue[5]=false;
                            }else{
                                halfGames[lnIndex].isTrue[5]=true;
                            }
                            break;
                    }
                    elementIndex++;
                    trash="";

                }else{

                    trash += character;
                }
            }
            reader.close();
            return halfGames;
        }catch(IOException e){
            e.printStackTrace();
            return halfGames;
        }
    }

    public CompletedGame[] readCompletedGames(){
        String FILE_NAME = "completedgames.txt";
        CompletedGame[] completedGames;
        int sizeCompletedGames=0;

        try{
            int size;
            FileInputStream fis=c.openFileInput(FILE_NAME);

            while( ( size = fis.read() )!= -1){
                if( Character.toString((char)size).equals("\n")){
                    sizeCompletedGames+=1;
                }
            }

        }catch(IOException e){
            e.printStackTrace();
        }

        completedGames=new CompletedGame[sizeCompletedGames];
        for(int i=0;i<sizeCompletedGames;i++){
            completedGames[i]=new CompletedGame();
            completedGames[i].answers1=new String[6];
            completedGames[i].answers2=new String[6];
            completedGames[i].isTrue1=new boolean[6];
            completedGames[i].isTrue2=new boolean[6];
            completedGames[i].points1=new int[6];
            completedGames[i].points2=new int[6];
        }

        try{
            BufferedReader reader=null;
            FileInputStream fin = c.openFileInput(FILE_NAME);
            reader=new BufferedReader(new InputStreamReader(fin,"UTF-8"));
            int size;
            int lnIndex=0;
            String trash="";
            String character;
            int elementIndex=0;

            while( ( size = reader.read() )!= -1){

                character=Character.toString((char)size);

                if( character.equals("\n") ){
                    lnIndex++;
                    elementIndex=0;
                    trash="";
                }else if( character.equals(".") ){
                    switch (elementIndex){
                        case 0:
                            completedGames[lnIndex].letter=trash;
                            break;
                        case 1:
                            completedGames[lnIndex].player1=trash;
                            break;
                        case 2:
                            completedGames[lnIndex].time1=Integer.parseInt(trash);
                            break;
                        case 3:
                            completedGames[lnIndex].answers1[0]=trash;
                            break;
                        case 4:
                            completedGames[lnIndex].answers1[1]=trash;
                            break;
                        case 5:
                            completedGames[lnIndex].answers1[2]=trash;
                            break;
                        case 6:
                            completedGames[lnIndex].answers1[3]=trash;
                            break;
                        case 7:
                            completedGames[lnIndex].answers1[4]=trash;
                            break;
                        case 8:
                            completedGames[lnIndex].answers1[5]=trash;
                            break;
                        case 9:
                            if(trash.equals("0")){
                                completedGames[lnIndex].isTrue1[0]=false;
                            }else{
                                completedGames[lnIndex].isTrue1[0]=true;
                            }
                            break;
                        case 10:
                            if(trash.equals("0")){
                                completedGames[lnIndex].isTrue1[1]=false;
                            }else{
                                completedGames[lnIndex].isTrue1[1]=true;
                            }
                            break;
                        case 11:
                            if(trash.equals("0")){
                                completedGames[lnIndex].isTrue1[2]=false;
                            }else{
                                completedGames[lnIndex].isTrue1[2]=true;
                            }
                            break;
                        case 12:
                            if(trash.equals("0")){
                                completedGames[lnIndex].isTrue1[3]=false;
                            }else{
                                completedGames[lnIndex].isTrue1[3]=true;
                            }
                            break;
                        case 13:
                            if(trash.equals("0")){
                                completedGames[lnIndex].isTrue1[4]=false;
                            }else{
                                completedGames[lnIndex].isTrue1[4]=true;
                            }
                            break;
                        case 14:
                            if(trash.equals("0")){
                                completedGames[lnIndex].isTrue1[5]=false;
                            }else{
                                completedGames[lnIndex].isTrue1[5]=true;
                            }
                            break;
                        case 15:
                            completedGames[lnIndex].points1[0]=Integer.parseInt(trash);
                            break;
                        case 16:
                            completedGames[lnIndex].points1[1]=Integer.parseInt(trash);
                            break;
                        case 17:
                            completedGames[lnIndex].points1[2]=Integer.parseInt(trash);
                            break;
                        case 18:
                            completedGames[lnIndex].points1[3]=Integer.parseInt(trash);
                            break;
                        case 19:
                            completedGames[lnIndex].points1[4]=Integer.parseInt(trash);
                            break;
                        case 20:
                            completedGames[lnIndex].points1[5]=Integer.parseInt(trash);
                            break;
                        case 21:
                            completedGames[lnIndex].player2=trash;
                            break;
                        case 22:
                            completedGames[lnIndex].time2=Integer.parseInt(trash);
                            break;
                        case 23:
                            completedGames[lnIndex].answers2[0]=trash;
                            break;
                        case 24:
                            completedGames[lnIndex].answers2[1]=trash;
                            break;
                        case 25:
                            completedGames[lnIndex].answers2[2]=trash;
                            break;
                        case 26:
                            completedGames[lnIndex].answers2[3]=trash;
                            break;
                        case 27:
                            completedGames[lnIndex].answers2[4]=trash;
                            break;
                        case 28:
                            completedGames[lnIndex].answers2[5]=trash;
                            break;
                        case 29:
                            if(trash.equals("0")){
                                completedGames[lnIndex].isTrue2[0]=false;
                            }else{
                                completedGames[lnIndex].isTrue2[0]=true;
                            }
                            break;
                        case 30:
                            if(trash.equals("0")){
                                completedGames[lnIndex].isTrue2[1]=false;
                            }else{
                                completedGames[lnIndex].isTrue2[1]=true;
                            }
                            break;
                        case 31:
                            if(trash.equals("0")){
                                completedGames[lnIndex].isTrue2[2]=false;
                            }else{
                                completedGames[lnIndex].isTrue2[2]=true;
                            }
                            break;
                        case 32:
                            if(trash.equals("0")){
                                completedGames[lnIndex].isTrue2[3]=false;
                            }else{
                                completedGames[lnIndex].isTrue2[3]=true;
                            }
                            break;
                        case 33:
                            if(trash.equals("0")){
                                completedGames[lnIndex].isTrue2[4]=false;
                            }else{
                                completedGames[lnIndex].isTrue2[4]=true;
                            }
                            break;
                        case 34:
                            if(trash.equals("0")){
                                completedGames[lnIndex].isTrue2[5]=false;
                            }else{
                                completedGames[lnIndex].isTrue2[5]=true;
                            }
                            break;
                        case 35:
                            completedGames[lnIndex].points2[0]=Integer.parseInt(trash);
                            break;
                        case 36:
                            completedGames[lnIndex].points2[1]=Integer.parseInt(trash);
                            break;
                        case 37:
                            completedGames[lnIndex].points2[2]=Integer.parseInt(trash);
                            break;
                        case 38:
                            completedGames[lnIndex].points2[3]=Integer.parseInt(trash);
                            break;
                        case 39:
                            completedGames[lnIndex].points2[4]=Integer.parseInt(trash);
                            break;
                        case 40:
                            completedGames[lnIndex].points2[5]=Integer.parseInt(trash);
                            break;


                    }
                    elementIndex++;
                    trash="";

                }else{

                    trash += character;
                }
            }
            reader.close();
            for(int i=0;i<completedGames.length;i++){
                completedGames[i].sumPoints1=0;
                completedGames[i].sumPoints2=0;
                for(int j=0;j<6;j++){
                    completedGames[i].sumPoints1+=completedGames[i].points1[i];
                    completedGames[i].sumPoints2+=completedGames[i].points2[i];
                }
            }
            return completedGames;
        }catch(IOException e){
            e.printStackTrace();
            return completedGames;
        }
    }

}
