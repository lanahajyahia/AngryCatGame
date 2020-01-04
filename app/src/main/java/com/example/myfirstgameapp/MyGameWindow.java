package com.example.myfirstgameapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class MyGameWindow extends AppCompatActivity {

    private double latitude;
    private double longitude;
    private BroadcastReceiver broadcastReceiver;
    // Image views
    private ImageView[] hearts, pomeranians;
    private ImageView[][] angryCats, bones;
    private ImageButton moveRight, moveLeft;
    private TextView txt_score;
    //sensor
    private SensorManager sensorManager;
    private Sensor sensor;
    //init paramaters
    private int scoreCount = 0;
    private int gameType = -1;
    private int dogPos = 2; // start dog in  the middle
    private String scoreString;
    private int gameSpeed = 0;
    //flags
    boolean gameOver = false, continue_game = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_game_window);

        startGpsService();

        initComponents();

        manageGameSpeed();


    }

    private void initComponents() {
        txt_score = findViewById(R.id.txt_score);
        moveLeft = findViewById(R.id.btn_moveLeft);
        moveRight = findViewById(R.id.btn_moveRight);

        initAndMoveDog(); //show car and move
        initAngryCatsandBones(); //draw cats
        initHearts(); //init hearts


    }


    private void manageGameSpeed() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            gameType = bundle.getInt(Consts.GAME_SPEED);
            if (gameType == Consts.SLOW_INDEX) {
                gameSpeed = Consts.SLOW_SPEED;

            } else if (gameType == Consts.FAST_INDEX) {
                gameSpeed = Consts.FAST_SPEED;
            } else if (gameType == Consts.SENSOR_INDEX) {
                sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                moveRight.setVisibility(View.INVISIBLE);
                moveLeft.setVisibility(View.INVISIBLE);
                gameSpeed = Consts.SLOW_SPEED;

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        continue_game = false;

        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        continue_game = true;

        if (gameType == Consts.SENSOR_INDEX) {
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        catsInvisible();

        loopOne();


        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    latitude = (double) intent.getExtras().get(Consts.LATITUDE_KEY);
                    longitude = (double) intent.getExtras().get(Consts.LONGITUDE_KEY);

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter(Consts.ACTION_KEY));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            Log.d(x + "", "lttt");
            float y = event.values[1];

            if (Math.abs(x) > Math.abs(y)) {

                if (x < 0) {
                    moveDogRight();
                }
                if (x > 0) {
                    moveDogLeft();
                    Log.d(x + "", "pttt");
                }
                if (y < 0) {
                    gameSpeed = Consts.FAST_SPEED;
                }
                if (y > 0) {
                    gameSpeed = Consts.SLOW_SPEED;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void initHearts() {
        hearts = new ImageView[]{
                findViewById(R.id.img_heart1),
                findViewById(R.id.img_heart2),
                findViewById(R.id.img_heart3)};
    }

    private void initAngryCatsandBones() {

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
                        findViewById(R.id.img_cat133), findViewById(R.id.img_cat143)},

                {findViewById(R.id.img_cat14), findViewById(R.id.img_cat24),
                        findViewById(R.id.img_cat34), findViewById(R.id.img_cat44),
                        findViewById(R.id.img_cat54), findViewById(R.id.img_cat64),
                        findViewById(R.id.img_cat74), findViewById(R.id.img_cat84),
                        findViewById(R.id.img_cat94), findViewById(R.id.img_cat104),
                        findViewById(R.id.img_cat114), findViewById(R.id.img_cat124),
                        findViewById(R.id.img_cat134), findViewById(R.id.img_cat144)},

                {findViewById(R.id.img_cat15), findViewById(R.id.img_cat25),
                        findViewById(R.id.img_cat35), findViewById(R.id.img_cat45),
                        findViewById(R.id.img_cat55), findViewById(R.id.img_cat65),
                        findViewById(R.id.img_cat75), findViewById(R.id.img_cat85),
                        findViewById(R.id.img_cat95), findViewById(R.id.img_cat105),
                        findViewById(R.id.img_cat115), findViewById(R.id.img_cat125),
                        findViewById(R.id.img_cat135), findViewById(R.id.img_cat145)}};

        bones = new ImageView[][]{
                {findViewById(R.id.coin11), findViewById(R.id.coin21),
                        findViewById(R.id.coin31), findViewById(R.id.coin41),
                        findViewById(R.id.coin51), findViewById(R.id.coin61),
                        findViewById(R.id.coin71), findViewById(R.id.coin81),
                        findViewById(R.id.coin91), findViewById(R.id.coin101),
                        findViewById(R.id.coin111), findViewById(R.id.coin121),
                        findViewById(R.id.coin131), findViewById(R.id.coin141)},

                {findViewById(R.id.coin12), findViewById(R.id.coin22),
                        findViewById(R.id.coin32), findViewById(R.id.coin42),
                        findViewById(R.id.coin52), findViewById(R.id.coin62),
                        findViewById(R.id.coin72), findViewById(R.id.coin82),
                        findViewById(R.id.coin92), findViewById(R.id.coin102),
                        findViewById(R.id.coin112), findViewById(R.id.coin122),
                        findViewById(R.id.coin132), findViewById(R.id.coin142)},

                {findViewById(R.id.coin13), findViewById(R.id.coin23),
                        findViewById(R.id.coin33), findViewById(R.id.coin43),
                        findViewById(R.id.coin53), findViewById(R.id.coin63),
                        findViewById(R.id.coin73), findViewById(R.id.coin83),
                        findViewById(R.id.coin93), findViewById(R.id.coin103),
                        findViewById(R.id.coin113), findViewById(R.id.coin123),
                        findViewById(R.id.coin133), findViewById(R.id.coin143)},

                {findViewById(R.id.coin14), findViewById(R.id.coin24),
                        findViewById(R.id.coin34), findViewById(R.id.coin44),
                        findViewById(R.id.coin54), findViewById(R.id.coin64),
                        findViewById(R.id.coin74), findViewById(R.id.coin84),
                        findViewById(R.id.coin94), findViewById(R.id.coin104),
                        findViewById(R.id.coin114), findViewById(R.id.coin124),
                        findViewById(R.id.coin134), findViewById(R.id.coin144)},

                {findViewById(R.id.coin15), findViewById(R.id.coin25),
                        findViewById(R.id.coin35), findViewById(R.id.coin45),
                        findViewById(R.id.coin55), findViewById(R.id.coin65),
                        findViewById(R.id.coin75), findViewById(R.id.coin85),
                        findViewById(R.id.coin95), findViewById(R.id.coin105),
                        findViewById(R.id.coin115), findViewById(R.id.coin125),
                        findViewById(R.id.coin135), findViewById(R.id.coin145)}};
    }


    private void initAndMoveDog() {
        pomeranians = new ImageView[]{
                findViewById(R.id.img_dog1),
                findViewById(R.id.img_dog2),
                findViewById(R.id.img_dog3),
                findViewById(R.id.img_dog4),
                findViewById(R.id.img_dog5)
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
        if (dogPos != Consts.LEFT_POS_DOG0) {
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

        if (dogPos != Consts.RIGHT_POS_DOG4) {
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

    private void catsInvisible() {  //default invisible for all cats and bones
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < Consts.CAT_ROWS; j++) {
                angryCats[i][j].setVisibility(View.INVISIBLE);
            }
        }

        for (int i = 0; i < 5; i++) { //bones
            for (int j = 0; j < Consts.CAT_ROWS; j++) {
                bones[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void moveCatInCol(int col, int row) {

        if (!continue_game)
            return;

        if (row != 0)
            angryCats[col][row - 1].setVisibility(View.INVISIBLE);
        if (row < Consts.CAT_ROWS)
            angryCats[col][row].setVisibility(View.VISIBLE);
        if (row == Consts.CAT_ROWS - 1) {
            checkCollision(col);
        }
    }

    private void moveBoneInCol(int col, int row) {

        if (!continue_game)
            return;

        if (row != 0)
            bones[col][row - 1].setVisibility(View.INVISIBLE);
        if (row < Consts.CAT_ROWS)
            bones[col][row].setVisibility(View.VISIBLE);
        if (row == Consts.CAT_ROWS - 1) {
            checkCollision(col);
        }
    }

    private void loopTwo(final int col, final int row) {

        if (!continue_game)
            return;
        if (gameOver) {
            startGameOverActivity();
            finish();
        }

        final Handler handler = new Handler();
        Runnable myRun = new Runnable() {
            @Override
            public void run() {
                if (row <= Consts.CAT_ROWS && !gameOver) {
                    moveCatInCol(col, row);
                    loopTwo(col, row + 1);
                }
            }
        };
        handler.postDelayed(
                myRun, gameSpeed);
    }


    private void loopThree(final int col, final int row) {

        if (!continue_game)
            return;
        if (gameOver) {
            startGameOverActivity();
            finish();
        }

        final Handler handler = new Handler();
        Runnable myRun = new Runnable() {
            @Override
            public void run() {
                if (row <= Consts.CAT_ROWS && !gameOver) {
                    moveBoneInCol(col, row);
                    loopThree(col, row + 1);
                }
            }
        };
        handler.postDelayed(
                myRun, gameSpeed);
    }

    private void loopOne() {
        if (!continue_game)
            return;
        if (gameOver) {
            startGameOverActivity();
            finish();
        }

        final Handler handler = new Handler();
        Runnable myRun = new Runnable() {
            @Override
            public void run() {
                scoreCount();
                int catShowCol = (int) ((Math.random() * ((Consts.MAX_DOG - Consts.MIN_DOG) + 1))
                        + Consts.MIN_DOG);
                int meatShowCol = (int) ((Math.random() * ((Consts.MAX_DOG - Consts.MIN_DOG) + 1))
                        + Consts.MIN_DOG);
                if (catShowCol != meatShowCol) {
                    loopTwo(catShowCol, 0);
                    loopThree(meatShowCol, 0);
                }
                loopOne();
            }
        };
        handler.postDelayed(
                myRun, gameSpeed);
    }

    private void checkCollision(int col) {
        if (dogPos == col && pomeranians[dogPos].getVisibility() == View.VISIBLE
                && angryCats[col][Consts.CAT_ROWS - 1].getVisibility() == View.VISIBLE && !gameOver) {
            vibrate();
            animateDog(pomeranians[dogPos]);
            playSoundWhenCrash();
            removeHeart();
        }
        if (dogPos == col && pomeranians[dogPos].getVisibility() == View.VISIBLE
                && bones[col][Consts.CAT_ROWS - 1].getVisibility() == View.VISIBLE && !gameOver) {
            scoreCount += 9;
            Toast.makeText(getApplicationContext(), "ten points added", Toast.LENGTH_SHORT).show();
        }
    }

    public void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 300 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(300);
        }
    }

    public void playSoundWhenCrash() {
        MediaPlayer glassDogBark = MediaPlayer.create(this, R.raw.dog_bark);
        glassDogBark.start();
    }

    public void animateDog(ImageView img) {
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

    private void scoreCount() {
        scoreString = String.format("%05d", scoreCount++);
        txt_score.setText(scoreString);
    }

    private void startGameOverActivity() {
        Bundle bundle = new Bundle();
        Intent gameOverIntent = new Intent(MyGameWindow.this, GameOverActivity.class);

        bundle.putInt(Consts.SCORE_KEY, scoreCount);
        bundle.putDouble(Consts.LATITUDE_KEY, latitude);
        bundle.putDouble(Consts.LONGITUDE_KEY, longitude);

        gameOverIntent.putExtras(bundle);
        startActivity(gameOverIntent);
        finish();
        return;
    }

    private void startGpsService() {
        if (!runtime_permissions()) {
            Intent i = new Intent(getApplicationContext(), GPS_Service.class);
            startService(i);
        }

    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startGpsService();
            } else {
                runtime_permissions();
            }
        }
    }

}
