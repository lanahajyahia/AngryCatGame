package com.example.myfirstgameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    private Button newGameBtn;
    private String finalScore;
    private TextView txt_finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        setFinalScore(); //show final score for a single game
        clickOnPlayAgainBtn(); //new game - back to menu


    }

    public void clickOnPlayAgainBtn() {
        newGameBtn = findViewById(R.id.button_playAgain);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newGameIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(newGameIntent);
            }
        });

    }

    public void setFinalScore() {
        txt_finalScore = findViewById(R.id.txt_finalScore);
        finalScore = getIntent().getStringExtra("score_key");
        txt_finalScore.setText("Final Score: " + finalScore);

    }

}
