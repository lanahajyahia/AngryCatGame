package com.example.myfirstgameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;


public class MyGameWindow extends AppCompatActivity {
    // final paramaters

    final int LEFT_POS_DOG = 0, RIGHT_POS_DOG = 2, CAT_ROWS = 14, MAX = 2, MIN = 0, CAT_STEPS = 13;


    // Image views
    private ImageView[] hearts, pomeranians;
    private ImageView[][] angryCats;
    private ImageButton moveRight, moveLeft;
    private TextView txt_score;

    //init paramaters
    int scoreCount = 0;
    private int dogPos = 1; // start dog in  the middle
    private int[][] posMatrixForCats;
    private int dogLifes = 3;
    private String scoreString;

    //flags
    boolean game_on = true, gameOver = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_game_window);
        loopOne();
        initComponents();

        // loopFunc();

    }


    private void initComponents() {
        int rows = getScreenHeightInDPs(getApplicationContext()) / 15;
        rows = rows * 12;
        txt_score = findViewById(R.id.txt_score);
        moveLeft = findViewById(R.id.btn_moveLeft);
        moveRight = findViewById(R.id.btn_moveRight);

        initAndMoveDog(); //show car and move
        initAngryCats(); //draw cats
        catsInvisible(); // clear all cats at first
        initHearts(); //init hearts
        //  playGame();

    }

    private void initHearts() {
        hearts = new ImageView[]{
                findViewById(R.id.img_heart1),
                findViewById(R.id.img_heart2),
                findViewById(R.id.img_heart3)};
    }

    private void initAngryCats() {
        posMatrixForCats = new int[3][CAT_ROWS];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < CAT_ROWS; j++) {
                posMatrixForCats[i][j] = 0;
            }
        }
        angryCats = new ImageView[][]{
                {findViewById(R.id.img_cat11), findViewById(R.id.img_cat21),
                        findViewById(R.id.img_cat31), findViewById(R.id.img_cat41),
                        findViewById(R.id.img_cat51), findViewById(R.id.img_cat61),
                        findViewById(R.id.img_cat71), findViewById(R.id.img_cat81),
                        findViewById(R.id.img_cat91), findViewById(R.id.img_cat101),
                        findViewById(R.id.img_cat111), findViewById(R.id.img_cat121),
                        findViewById(R.id.img_cat131), findViewById(R.id.img_cat141)},

                {findViewById(R.id.img_cat12), findViewById(R.id.img_cat22),
                        findViewById(R.id.img_cat32), findViewById(R.id.img_cat42),
                        findViewById(R.id.img_cat52), findViewById(R.id.img_cat62),
                        findViewById(R.id.img_cat72), findViewById(R.id.img_cat82),
                        findViewById(R.id.img_cat92), findViewById(R.id.img_cat102),
                        findViewById(R.id.img_cat112), findViewById(R.id.img_cat122),
                        findViewById(R.id.img_cat132), findViewById(R.id.img_cat142)},

                {findViewById(R.id.img_cat13), findViewById(R.id.img_cat23),
                        findViewById(R.id.img_cat33), findViewById(R.id.img_cat43),
                        findViewById(R.id.img_cat53), findViewById(R.id.img_cat63),
                        findViewById(R.id.img_cat73), findViewById(R.id.img_cat83),
                        findViewById(R.id.img_cat93), findViewById(R.id.img_cat103),
                        findViewById(R.id.img_cat113), findViewById(R.id.img_cat123),
                        findViewById(R.id.img_cat133), findViewById(R.id.img_cat143)}};
    }


    private void initAndMoveDog() {
        pomeranians = new ImageView[]{
                findViewById(R.id.img_dog1),
                findViewById(R.id.img_dog2),
                findViewById(R.id.img_dog3)
        };

        for (int i = 0; i < pomeranians.length; i++) {
            if (dogPos == i) {
                pomeranians[i].setVisibility(View.VISIBLE);
            } else {
                pomeranians[i].setVisibility(View.INVISIBLE);
            }
        }

        moveLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDogLeft();
            }
        });

        moveRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDogRight();
            }
        });
    }

    private void moveDogLeft() { //move dog one cell left
        if (dogPos != LEFT_POS_DOG) {
            dogPos--;
        }
        for (int i = 0; i < pomeranians.length; i++) {
            if (dogPos == i) {
                pomeranians[i].setVisibility(View.VISIBLE);
            } else {
                pomeranians[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void moveDogRight() { // move one cell right

        if (dogPos != RIGHT_POS_DOG) {
            dogPos++;
        }
        for (int i = 0; i < pomeranians.length; i++) {
            if (dogPos == i) {
                pomeranians[i].setVisibility(View.VISIBLE);
            } else {
                pomeranians[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void catsInvisible() {  //default invisible for all cats
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < CAT_ROWS; j++) {
                angryCats[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void moveCatInCol(int col, int row) {

        if (row != 0)
            angryCats[col][row - 1].setVisibility(View.INVISIBLE);
        if (row < CAT_ROWS)
            angryCats[col][row].setVisibility(View.VISIBLE);
        if (row == CAT_STEPS) { //10 should change!
            checkCollision(col);
        }
//            if (row == CAT_ROWS) {
//                return;
//            }


    }

    private void loopTwo(final int col, final int row) {

        final Handler handler = new Handler();
        Runnable myRun = new Runnable() {
            @Override
            public void run() {
                if (row <= CAT_ROWS && !gameOver) {
                    loopTwo(col, row + 1);
                    moveCatInCol(col, row);
                }
            }
        };
        handler.postDelayed(
                myRun, 1000);
    }

    private void loopOne() {

        final Handler handler = new Handler();
        Runnable myRun = new Runnable() {
            @Override
            public void run() {
                if (!gameOver) {
                    scoreCount();
                    int catShowCol = (int) ((Math.random() * ((MAX - MIN) + 1)) + MIN);
                    loopTwo(catShowCol, 0);
                    loopOne();

                } else if (gameOver) {
                    startGameOverActivity();
                    finish();
                }
            }
        };
        handler.postDelayed(
                myRun, 1000);

    }

    private void checkCollision(int col) {
        if (dogPos == col && pomeranians[dogPos].getVisibility() == View.VISIBLE
                && angryCats[col][CAT_STEPS].getVisibility() == View.VISIBLE && !gameOver) {
            removeHeart();
            animateDog(pomeranians[dogPos]);
            vibrate();
            playSoundWhenCrash();
        }
    }

    private void playSoundWhenCrash(){
        MediaPlayer glassDogBark = MediaPlayer.create(this,R.raw.dog_bark);
        glassDogBark.start();
    }

    private void removeHeart() {
        if (hearts[0].getVisibility() == View.VISIBLE) {
            hearts[0].setVisibility(View.INVISIBLE);
        } else if (hearts[1].getVisibility() == View.VISIBLE) {
            hearts[1].setVisibility(View.INVISIBLE);
        } else if (hearts[2].getVisibility() == View.VISIBLE) {
            hearts[2].setVisibility(View.INVISIBLE);
            gameOver = true;
        }
    }


    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(300);
        }
    }

    private void scoreCount() {
        scoreString = String.format("%05d", scoreCount++);
        txt_score.setText(scoreString);
    }

    private void startGameOverActivity() {
        Intent gameOverIntent = new Intent(MyGameWindow.this, GameOverActivity.class);
        gameOverIntent.putExtra("score_key", scoreString);
        startActivity(gameOverIntent);
        finish();
    }


    private void animateDog(ImageView img) {
        img.setScaleX(0);
        img.setScaleY(0);
        img.setRotation(0);
        img.animate()
                .rotation(360)
                .scaleX(1)
                .scaleY(1)
                .setDuration(300)
                .setInterpolator(new AccelerateInterpolator())
                .start();
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public int getScreenHeightInDPs(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }
}

