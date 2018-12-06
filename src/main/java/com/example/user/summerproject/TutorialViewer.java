package com.example.user.summerproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class TutorialViewer extends AppCompatActivity {

    private String playerName;
    private PopupWindow levelPopup;
    private int index;
    private int maxIndex;
    private TextView messageText;
    private Button showingButton;
    private TextView buttonDescription;
    private String stringToShow;
    private int timeStep=100;
    private int stringIndex;
    private int stringLength;
    private CountDownTimer countDownTimer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_viewer);


        Bundle extras=getIntent().getExtras();
        playerName=extras.getString("name");

        final Typeface pencilTypeface= Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/ghoststory.otf");

        final Button newGameButton = (Button) findViewById(R.id.newGame);

        final Button wordTesterButton = (Button) findViewById(R.id.wordTester);

        final Button friendsButton=(Button)findViewById(R.id.friends_button);

        final Button statsButton=(Button)findViewById(R.id.stats_button);

        final Button settingButton = (Button) findViewById(R.id.settings_button);

        final Button infoButton=(Button)findViewById(R.id.info_button);

        final Button logoutButton=(Button)findViewById(R.id.logout_button);

        final Button exitButton=(Button)findViewById(R.id.exit_button);

        TextView nameView=(TextView)findViewById(R.id.nameView);
        nameView.setTypeface(pencilTypeface);




        buttonDescription=(TextView)findViewById(R.id.button_description_text);
        buttonDescription.setTypeface(pencilTypeface);

        messageText=(TextView)findViewById(R.id.message_text);
        messageText.setTypeface(pencilTypeface);

        final TextView indicatorText=(TextView)findViewById(R.id.indicator_text);
        indicatorText.setTypeface(pencilTypeface);

        final Button nextButton=(Button)findViewById(R.id.next_button);

        showingButton=(Button)findViewById(R.id.showing_button);


        index=0;
        maxIndex=10;
















        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                if(index==1){

                }

                if(maxIndex<index){
                    indicatorText.setText("");
                }else{
                    indicatorText.setText((index)+" /"+maxIndex);
                }
                switch (index){
                    case 1:
                        messageText.setText("");
                        nextButton.setClickable(false);
                        nextButton.setAlpha(0.1f);
                        newGameButton.setAlpha(0.1f);
                        wordTesterButton.setAlpha(0.1f);
                        friendsButton.setAlpha(0.1f);
                        statsButton.setAlpha(0.1f);
                        settingButton.setAlpha(0.1f);
                        infoButton.setAlpha(0.1f);
                        logoutButton.setAlpha(0.1f);
                        exitButton.setAlpha(0.1f);
                        buttonDescription.setText("Διαχείριση Φίλων");
                        CountDownTimer countDownTimer=new CountDownTimer(700,700) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                showingButton.setBackground(getResources().getDrawable(R.drawable.friends_button));
                                stringToShow="Εδώ μπορείς:\n\n-να βρεις νέους φίλους αν\nδεν ξέρεις κάποιον\n\n-να στείλεις και να\nαποδεχτείς αιτήματα φιλίας\n\n-να δείς όλους τους φίλους\n\n-να διαγράψεις κάποιον     ";
                                stringLength=stringToShow.length();
                                stringIndex = 0;
                                countDownTimer1=new CountDownTimer((stringLength)*timeStep,timeStep) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        stringIndex++;
                                        messageText.setText(stringToShow.substring(0, stringIndex));
                                    }

                                    @Override
                                    public void onFinish() {
                                        nextButton.setClickable(true);
                                        nextButton.setAlpha(0.7f);
                                    }
                                }.start();
                                friendsButton.setAlpha(0.7f);
                            }
                        }.start();



                        break;
                    case 2:
                        showingButton.setBackground(getResources().getDrawable(R.drawable.suggestion_button));
                        buttonDescription.setText("Αναζήτηση Λέξεων");
                        messageText.setText("");
                        nextButton.setClickable(false);
                        nextButton.setAlpha(0.1f);
                        stringToShow="Για να δείς ποιές λέξεις έχει το λεξικό μπαίνεις εδώ. Αν\nδεν βρείς κάποια λέξη\nμπορείς να μας την στείλεις για αξιολόγηση. Το\nαποτέλεσμα της αξιολόγησης σου προσθέτει ή σου αφαιρεί πόντους. Οπότε πρόσεχε\nτους τόνους και τα\nορθογραφικά λάθη.     ";
                        stringLength=stringToShow.length();
                        stringIndex = 0;
                        countDownTimer1=new CountDownTimer((stringLength)*timeStep,timeStep) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                stringIndex++;
                                messageText.setText(stringToShow.substring(0, stringIndex));
                            }

                            @Override
                            public void onFinish() {
                                nextButton.setClickable(true);
                                nextButton.setAlpha(0.7f);
                            }
                        }.start();
                        wordTesterButton.setAlpha(0.7f);
                        friendsButton.setAlpha(0.1f);
                        break;
                    case 3:
                        nextButton.setClickable(false);
                        nextButton.setAlpha(0.1f);
                        showingButton.setBackground(null);
                        showingButton.setBackground(getResources().getDrawable(R.drawable.new_game_button));
                        buttonDescription.setText("Νέο Παιχνίδι");
                        messageText.setText("");
                        wordTesterButton.setAlpha(0.1f);
                        newGameButton.setAlpha(0.7f);
                        nextButton.setClickable(false);
                        stringToShow="Επιλέγεις με ποιόν θες να\nπαίξεις και το παιχνίδι\nξεκινάει.\nΠΡΟΣΟΧΗ ! Οι τόνοι δεν\nεπιτρέπονται και δεν\nυπάρχουν όλες οι λέξεις στο λεξικό. \nΜπορείς στο τέλος του\nπαιχνιδιού να προτείνεις\nόποια λέξη θέλεις να\nεισαχθεί.   ";
                        stringLength=stringToShow.length();
                        stringIndex = 0;
                        countDownTimer1=new CountDownTimer((stringLength)*timeStep,timeStep) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                stringIndex++;
                                messageText.setText(stringToShow.substring(0, stringIndex));
                            }

                            @Override
                            public void onFinish() {
                                nextButton.setClickable(true);
                                nextButton.setAlpha(0.7f);
                            }
                        }.start();
                        break;

                    case 4:
                        showingButton.setBackground(getResources().getDrawable(R.drawable.stats_button));
                        buttonDescription.setText("Στατιστικά");
                        messageText.setText("");
                        stringToShow="Εδώ μπορείς να δείς τις\nεπιδόσεις σου.\nΝίκες και ήττες για κάθε\nγράμμα σε ποσοστά και\nπλήθος.\nΠόσους πόντους μαζεύεις σε κάθε κατηγορία.    ";
                        nextButton.setClickable(false);
                        nextButton.setAlpha(0.1f);
                        stringLength=stringToShow.length();
                        stringIndex = 0;
                        countDownTimer1=new CountDownTimer((stringLength)*timeStep,timeStep) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                stringIndex++;
                                messageText.setText(stringToShow.substring(0, stringIndex));
                            }

                            @Override
                            public void onFinish() {
                                nextButton.setClickable(true);
                                nextButton.setAlpha(0.7f);
                            }
                        }.start();
                        newGameButton.setAlpha(0.1f);
                        statsButton.setAlpha(0.7f);
                        break;
                    case 5:
                        showingButton.setBackground(getResources().getDrawable(R.drawable.settings_button));
                        buttonDescription.setText("Ρυθμίσεις");
                        messageText.setText("");
                        stringToShow="Μπορείς να\n(απ)ενεργοποιήσεις\nξεχωριστά τους ήχους της\nεφαρμογής και τους ήχους\nκατά τη διάρκεια του\nπαιχνιδιού   ";
                        nextButton.setClickable(false);
                        nextButton.setAlpha(0.1f);
                        stringLength=stringToShow.length();
                        stringIndex = 0;
                        countDownTimer1=new CountDownTimer((stringLength)*timeStep,timeStep) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                stringIndex++;
                                messageText.setText(stringToShow.substring(0, stringIndex));
                            }

                            @Override
                            public void onFinish() {
                                nextButton.setClickable(true);
                                nextButton.setAlpha(0.7f);
                            }
                        }.start();
                        statsButton.setAlpha(0.1f);
                        settingButton.setAlpha(0.7f);
                        break;
                    case 6:
                        showingButton.setBackground(getResources().getDrawable(R.drawable.info_button));
                        buttonDescription.setText("Πληροφορίες");
                        messageText.setText("");
                        stringToShow="Εδώ υπάρχουν αναλυτικά\nπληροφορίες για:\n-τους κανόνες του παιχνιδιού αν δεν έχεις παίξει ποτέ\n-πως παίρνεις πόντους για\nνα ανέβεις level\n-πως λειτουργεί το σύστημα\nτων προτάσεων λέξεων\n-και διάφορα άλλα      ";
                        nextButton.setClickable(false);
                        nextButton.setAlpha(0.1f);
                        stringLength=stringToShow.length();
                        stringIndex = 0;
                        countDownTimer1=new CountDownTimer((stringLength)*timeStep,timeStep) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                stringIndex++;
                                messageText.setText(stringToShow.substring(0, stringIndex));
                            }

                            @Override
                            public void onFinish() {
                                nextButton.setClickable(true);
                                nextButton.setAlpha(0.7f);
                            }
                        }.start();
                        settingButton.setAlpha(0.1f);
                        infoButton.setAlpha(0.7f);
                        break;
                    case 7:
                        showingButton.setBackground(getResources().getDrawable(R.drawable.log_out_button));
                        buttonDescription.setText("Αποσύνδεση");
                        messageText.setText("");
                        stringToShow=" Πατάς και αποσυνδέεσαι\nέτσι ώστε να μην λαμβάνεις ειδοποιήσεις. Αν θες να βγείς απ' το παιχνίδι και να\nλαμβάνεις ειδοποιήσεις τότε δεν πρέπει να\nαποσυνδεθείς.    ";
                        nextButton.setClickable(false);
                        nextButton.setAlpha(0.1f);
                        stringLength=stringToShow.length();
                        stringIndex = 0;
                        countDownTimer1=new CountDownTimer((stringLength)*timeStep,timeStep) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                stringIndex++;
                                messageText.setText(stringToShow.substring(0, stringIndex));
                            }

                            @Override
                            public void onFinish() {
                                nextButton.setClickable(true);
                                nextButton.setAlpha(0.7f);
                            }
                        }.start();
                        infoButton.setAlpha(0.1f);
                        logoutButton.setAlpha(0.7f);
                        break;
                    case 8:
                        showingButton.setBackground(getResources().getDrawable(R.drawable.exit_button));
                        buttonDescription.setText("Έξοδος");
                        messageText.setText("");
                        stringToShow=" Για να βγείς απ' το παιχνίδι αλλά να λαμβάνεις\nειδοποιήσεις πατάς εδώ.   ";
                        nextButton.setClickable(false);
                        nextButton.setAlpha(0.1f);
                        stringLength=stringToShow.length();
                        stringIndex = 0;
                        countDownTimer1=new CountDownTimer((stringLength)*timeStep,timeStep) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                stringIndex++;
                                messageText.setText(stringToShow.substring(0, stringIndex));
                            }

                            @Override
                            public void onFinish() {
                                nextButton.setClickable(true);
                                nextButton.setAlpha(0.7f);
                            }
                        }.start();
                        logoutButton.setAlpha(0.1f);
                        exitButton.setAlpha(0.7f);
                        break;
                    case 9:
                        showingButton.setBackground(null);
                        buttonDescription.setText("    Levels");
                        messageText.setText("");
                        buttonDescription.setGravity(Gravity.START);
                        stringToShow="Πάνω δεξία υπάρχει το\nψευδώνυμο. Κλικάροντάς\nεμφανίζεται ένα παράθυρο\nμε περισσότερες\nπληροφορίες για τα levels.    ";
                        nextButton.setClickable(false);
                        nextButton.setAlpha(0.1f);
                        stringLength=stringToShow.length();
                        stringIndex = 0;
                        countDownTimer1=new CountDownTimer((stringLength)*timeStep,timeStep) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                stringIndex++;
                                messageText.setText(stringToShow.substring(0, stringIndex));
                            }

                            @Override
                            public void onFinish() {
                                nextButton.setClickable(true);
                                nextButton.setAlpha(0.7f);
                            }
                        }.start();
                        buttonDescription.setTextSize(45);
                        buttonDescription.setTypeface(pencilTypeface,Typeface.BOLD);
                        exitButton.setAlpha(0.1f);
                        break;
                    case 10:
                        buttonDescription.setTypeface(pencilTypeface,Typeface.NORMAL);
                        logoutButton.setAlpha(0.1f);
                        messageText.setText("");
                        buttonDescription.setText("   Παιχνίδια");
                        stringToShow="Έδω που διαβάζεις τόση ώρα τις οδηγίες εμφανίζονται τα\nενεργά παιχνίδια. Τα\nπαιχνίδια που έχεις δεχτεί\nεμφανίζονται ως ερωτηματικό για να μην ξέρεις το γράμμα. Τα ολοκληρωμένα είναι\nπράσινα αν νικήσεις και\nκόκκινα αν χάσεις.\nΚλικάροντας ένα παιχνίδι\nμπορείς να δείς λεπτομέριες.   ";
                        nextButton.setClickable(false);
                        nextButton.setAlpha(0.1f);
                        stringLength=stringToShow.length();
                        stringIndex = 0;
                        countDownTimer1=new CountDownTimer((stringLength)*timeStep,timeStep) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                stringIndex++;
                                messageText.setText(stringToShow.substring(0, stringIndex));
                            }

                            @Override
                            public void onFinish() {
                                nextButton.setClickable(true);
                                nextButton.setAlpha(0.7f);
                            }
                        }.start();
                        break;
                    case 11:
                        buttonDescription.setText("");
                        indicatorText.setText("");
                        stringToShow="Η περιήγηση έφτασε στο\nτέλος της. Θα μεταφερθείς\nστο κεντρικό παράθυρο της\nεφαρμογής. Κάνε τον πρώτο\nσου φίλο και παίξε το πρώτο παιχνίδι.\n\nΚαλή Διασκέδαση!!!   ";
                        nextButton.setClickable(false);
                        nextButton.setAlpha(0.1f);
                        stringLength=stringToShow.length();
                        stringIndex = 0;
                        countDownTimer1=new CountDownTimer((stringLength)*timeStep,timeStep) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                stringIndex++;
                                messageText.setText(stringToShow.substring(0, stringIndex));
                            }

                            @Override
                            public void onFinish() {
                                nextButton.setClickable(true);
                                nextButton.setAlpha(0.7f);
                            }
                        }.start();
                        break;
                    case 12:
                        finish();
                        Intent viewrIntent=new Intent(TutorialViewer.this,ViewerActivity.class);
                        MediaPlayer pageFlipping = MediaPlayer.create(getApplicationContext(), R.raw.pageflipping);
                        pageFlipping.start();
                        viewrIntent.putExtra("name",playerName);
                        startActivity(viewrIntent);
                        overridePendingTransition(R.anim.fade_in,R.anim.noanim);
                        break;

                }


            }
        });


        stringToShow="Βλέπεις το κεντρικό\nπαράθυρο της εφαρμογής.\nΓια να παίξεις το πρώτο\nσου παιχνίδι πρέπει να έχεις φίλους οπότε ας ξεκινήσουμε την περιήγηση με τους\nφίλους.    ";
        nextButton.setClickable(false);
        nextButton.setAlpha(0.1f);
        stringLength=stringToShow.length();
        stringIndex = 0;
        countDownTimer1=new CountDownTimer((stringLength)*timeStep,timeStep) {
            @Override
            public void onTick(long millisUntilFinished) {
                stringIndex++;
                messageText.setText(stringToShow.substring(0, stringIndex));
            }

            @Override
            public void onFinish() {
                nextButton.setClickable(true);
                nextButton.setAlpha(0.7f);
            }
        }.start();

    }

    @Override
    public void onBackPressed() {

    }
}
