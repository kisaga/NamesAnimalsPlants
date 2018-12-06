package com.example.user.summerproject;



import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class InfoActivity extends AppCompatActivity {

    private Button backInfo,nextInfo;
    private String [] titles ;
    private TextView titleInfo;
    private int indicatorSection = 0;
    private TextView indicatorInfo;
    private RelativeLayout[] relativeLayouts=new RelativeLayout[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        backInfo = (Button) findViewById(R.id.backInfo);
        nextInfo = (Button) findViewById(R.id.nextInfo);

        relativeLayouts[0] = (RelativeLayout)findViewById(R.id.scoreNgame);
        relativeLayouts[1]= (RelativeLayout) findViewById(R.id.exp_points_levels);
        relativeLayouts[2] = (RelativeLayout) findViewById(R.id.suggestionInfo);
        relativeLayouts[3]= (RelativeLayout) findViewById(R.id.aboutUs);

        titleInfo = (TextView) findViewById(R.id.titleInfo);

        Typeface chalkTypeface=Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/romanscript.ttf");
        backInfo.setTypeface(chalkTypeface);
        nextInfo.setTypeface(chalkTypeface);




        titles = getResources().getStringArray(R.array.infoTitles);
        titleInfo.setText(titles[0]);
        relativeLayouts[0].setVisibility(View.VISIBLE);
        relativeLayouts[0].bringToFront();


        backInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indicatorSection--;
                if (indicatorSection<0){
                    finish();
                    overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
                }else {
                    titleInfo.setText(titles[indicatorSection]);
                    relativeLayouts[indicatorSection].setVisibility(View.VISIBLE);
                    relativeLayouts[indicatorSection+1].setVisibility(View.GONE);
                }

            }
        });

        nextInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indicatorSection++;
                if (indicatorSection<4){
                    titleInfo.setText(titles[indicatorSection]);
                    relativeLayouts[indicatorSection].setVisibility(View.VISIBLE);
                    relativeLayouts[indicatorSection-1].setVisibility(View.GONE);

                }else {
                    finish();
                    overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
                }

            }
        });



    }



}
