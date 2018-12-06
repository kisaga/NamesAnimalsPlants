package com.example.user.summerproject.word_tester;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.user.summerproject.R;


public class PasswordActivity extends AppCompatActivity {
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        password= (EditText) findViewById(R.id.password);

    }
    public void GoClick(View v){
        String password=this.password.getText().toString();
        if(password.equals("atdeafsdoorknockasmuchasyouwant") || password.equals("atdeafsdoor")){
            Intent newAdmin = new Intent(PasswordActivity.this, AdminActivity.class);
            startActivity(newAdmin);
        }
    }
}
