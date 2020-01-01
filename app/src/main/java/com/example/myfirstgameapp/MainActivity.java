package com.example.myfirstgameapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_slowGame, btn_fastGame, btn_sensorGame,btn_MapActivity;


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

    public void initComponents() {
        btn_slowGame = findViewById(R.id.btn_slowGame);
        btn_fastGame = findViewById(R.id.btn_fastGame);
        btn_sensorGame = findViewById(R.id.btn_sensorGame);
        btn_MapActivity= findViewById(R.id.btn_MapActivity);
    }

    public void playFunctions() {

        btn_slowGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToIntent(Consts.SLOW_INDEX);
            }
        });
        btn_fastGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToIntent(Consts.FAST_INDEX);
            }
        });
        btn_sensorGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToIntent(Consts.SENSOR_INDEX);
            }
        });
        btn_MapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Map_Activity.class);
                startActivity(intent);
            }
        });
    }

    private void goToIntent(int gameType) {
        Intent myIntent = new Intent(MainActivity.this, MyGameWindow.class);
        Bundle b = new Bundle();
        b.putInt(Consts.GAME_SPEED, gameType);
        myIntent.putExtras(b);
        MainActivity.this.startActivity(myIntent);
    }
}
