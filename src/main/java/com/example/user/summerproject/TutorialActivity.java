package com.example.user.summerproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TutorialActivity extends AppCompatActivity {

    private String playerName;
    private Typeface chalkTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);



        chalkTypeface= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/romanscript.ttf");
        Bundle extras=getIntent().getExtras();
        playerName=extras.getString("name");

        Button navigationButton=(Button)findViewById(R.id.start_navigation_button);
        navigationButton.setTypeface(chalkTypeface);
        navigationButton.setText("Έναρξη Περιήγησης");
        navigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent tutorialIntent=new Intent(TutorialActivity.this,TutorialViewer.class);
                tutorialIntent.putExtra("name",playerName);
                finish();
                startActivity(tutorialIntent);
                overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
            }
        });




    }

    @Override
    public void onBackPressed() {

    }
}
