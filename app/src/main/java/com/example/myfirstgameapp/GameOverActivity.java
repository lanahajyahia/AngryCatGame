package com.example.myfirstgameapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GameOverActivity extends AppCompatActivity {

    private Button button_playAgain, button_highScoreLayout;
    private EditText editTxt_playerName;
    private Intent intent;
    private int score;
    private double latitude;
    private double longitude;
    private UserManager userManager;
    private TextView txt_yourScore,txt_notHighScore;
    private boolean newHighScore = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        initComponents();

        manageButtons();
    }

    private void initComponents() {
        txt_notHighScore = findViewById(R.id.txt_notHighScore);
        button_playAgain = findViewById(R.id.button_playAgain);
        button_highScoreLayout = findViewById(R.id.button_highScoreLayout);
        editTxt_playerName = findViewById(R.id.editTxt_playerName);
        txt_yourScore = findViewById(R.id.txt_yourScore);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            score = b.getInt(Consts.SCORE_KEY);
            latitude = b.getDouble(Consts.LATITUDE_KEY, latitude);
            longitude = b.getDouble(Consts.LONGITUDE_KEY, longitude);
        }
        txt_yourScore.setText(txt_yourScore.getText().toString() + score);

        userManager = new UserManager(GameOverActivity.this);
    }

    public void manageButtons() {
        button_playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // clickOnPlayAgainBtn();
               finish();
            }
        });

        button_highScoreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnHighScoreBtn();
            }
        });
    }


    public void clickOnHighScoreBtn() {
        intent = new Intent(GameOverActivity.this, Map_Activity.class);

        DateFormat df = new SimpleDateFormat(Consts.DATE_FORMAT);
        String date = df.format(Calendar.getInstance().getTime());
        if (newHighScore) {

            User user = new User(editTxt_playerName.getText().toString(), score, date, latitude, longitude);
            userManager.addUser(user);
        }

        GameOverActivity.this.startActivity(intent);
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (score > userManager.getLastPlace() || userManager.getRecords().size() < Consts.ARRAY_MAX_SIZE) {
            txt_notHighScore.setVisibility(View.INVISIBLE);
            newHighScore = true;
        }else{
            txt_notHighScore.setVisibility(View.VISIBLE);
            newHighScore=false;
        }
    }
}

