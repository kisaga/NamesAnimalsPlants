package com.example.user.summerproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.summerproject.myTools.NetworkChangeReceiver;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SetNickname extends AppCompatActivity {

    private EditText nicknameText;
    private TextView nicknameTitle;
    private Button setButton;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserList;
    private String nickname="";
    private String user_id="";
    private boolean insertbyMe=false;
    private boolean success=false;

    private StorageReference storageReference;
    private NetworkChangeReceiver networkChangeReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nickname);


        networkChangeReceiver=new NetworkChangeReceiver();
        mAuth=FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();


        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        mUserList=FirebaseDatabase.getInstance().getReference().child("UsersList");
        mUserList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(insertbyMe) {
                    if (dataSnapshot.hasChild(nickname) && !success) {
                        Toast toast=Toast.makeText(getApplicationContext(), "Το ψευδώνυμο υπάρχει ήδη", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                        toast.show();
                        mUserList.child("testNicknametestNicknametestNickname").removeValue();
                        insertbyMe = false;
                    } else {
                        DatabaseReference userDatabase = mDatabaseUsers.child(user_id);
                        userDatabase.child("Name").setValue(nickname).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mUserList.child(nickname).setValue(user_id);
                                Intent intent = new Intent();
                                intent.putExtra("nickname", nickname);
                                setResult(RESULT_OK, intent);
                                mUserList.child("testNicknametestNicknametestNickname").removeValue();
                                success=true;
                                finish();
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String font=getResources().getString(R.string.font_name);
        Typeface mTypeface= Typeface.createFromAsset(getApplicationContext().getAssets(),font);
        nicknameText=(EditText)findViewById(R.id.nickname_text);

        nicknameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().contains(".") || s.toString().contains("#") || s.toString().contains("$") ||
                        s.toString().contains("[") || s.toString().contains("]") || s.toString().contains("/")){
                    Toast.makeText(getApplicationContext(),"Αυτό που εισάγατε δεν επιτρέπεται",Toast.LENGTH_SHORT).show();
                    s.clear();
                }
            }
        });
        nicknameText.setTypeface(mTypeface);
        nicknameText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        nicknameTitle=(TextView)findViewById(R.id.nickname_title);
        nicknameTitle.setTypeface(mTypeface);
        setButton=(Button)findViewById(R.id.set_nickname_button);
        setButton.setTypeface(mTypeface);

        FirebaseUser user = mAuth.getCurrentUser();
        user_id=user.getUid();
        setButton.setTypeface(mTypeface);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.hasInternet) {
                    nickname = nicknameText.getText().toString().trim();

                    insertbyMe = true;
                    mUserList.child("testNicknametestNicknametestNickname").setValue(nickname);
                }else{
                    Toast.makeText(getBaseContext(),"Ελέγξτε την σύνδεσή σας",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
