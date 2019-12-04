package com.example.myfirstgameapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class MainActivity extends AppCompatActivity {

    private Button btn_startGame, btn_howToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initComponents();
        playFunctions();
    }

    public void playFunctions() {
        pressButtonStartGame();
        pressButtonHowToPlay();
    }

    public void initComponents() {
        btn_howToPlay = findViewById(R.id.btn_howToPlay);
        btn_startGame = findViewById(R.id.btn_startGame);
    }

    public void pressButtonHowToPlay() {
        btn_howToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "not working for now :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pressButtonStartGame() {
        btn_startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGameIntent = new Intent(getApplicationContext(), MyGameWindow.class);
                startActivity(startGameIntent);
            }
        });
    }
}
