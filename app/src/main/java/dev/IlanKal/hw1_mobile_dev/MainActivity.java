package dev.IlanKal.hw1_mobile_dev;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

import dev.IlanKal.hw1_mobile_dev.Logic.GameController;

public class MainActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton main_FAB_arrow_left;
    private ExtendedFloatingActionButton main_FAB_arrow_right;
    private AppCompatImageView[] main_IMG_hearts;
    private AppCompatImageView[] main_IMG_rocket;
    private AppCompatImageView[][] main_IMG_matrix_flaming_meteor;
    private GameController gameController;
    private long startTime;
    private boolean timerOn = false;
    private Timer timer;
    private static final long DELAY = 1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initViews();

        gameController = new GameController(main_IMG_hearts.length);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    private void findViews() {
        // arrows buttons
        main_FAB_arrow_left = findViewById(R.id.main_FAB_arrow_left);
        main_FAB_arrow_right = findViewById(R.id.main_FAB_arrow_right);

        // array of hearts
        main_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)
        };

        // array of rocket
        main_IMG_rocket = new AppCompatImageView[]{
                findViewById(R.id.main_rocket_0),
                findViewById(R.id.main_rocket_1),
                findViewById(R.id.main_rocket_2)
        };

        // 2D array of flaming meteor
        main_IMG_matrix_flaming_meteor = new AppCompatImageView[][]{
                {
                        findViewById(R.id.main_index_00),
                        findViewById(R.id.main_index_01),
                        findViewById(R.id.main_index_02)
                },
                {
                        findViewById(R.id.main_index_10),
                        findViewById(R.id.main_index_11),
                        findViewById(R.id.main_index_12)
                },
                {
                        findViewById(R.id.main_index_20),
                        findViewById(R.id.main_index_21),
                        findViewById(R.id.main_index_22)
                },
                {
                        findViewById(R.id.main_index_30),
                        findViewById(R.id.main_index_31),
                        findViewById(R.id.main_index_32)
                },
                {
                        findViewById(R.id.main_index_40),
                        findViewById(R.id.main_index_41),
                        findViewById(R.id.main_index_42)
                }
        };
    }

    private void initViews() {
        main_FAB_arrow_left.setOnClickListener(v -> moveByClick("left"));
        main_FAB_arrow_right.setOnClickListener(v -> moveByClick("right"));
    }

    private void reLoadMatrixTime() {
        gameController.moveMatrixSpot();
        reLoadUI();
    }

    private void moveByClick(String dir) {
        gameController.moveRocket(dir); // move the rocket according to the arrow
        reLoadUI();
    }

    private void startTimer() {
        if (!timerOn) {
            Log.d("startTimer", "startTimer: Timer Started");
            startTime = System.currentTimeMillis();
            timerOn = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread((() -> reLoadMatrixTime()));
                }
            }, DELAY, DELAY); // Start after DELAY, repeat every DELAY
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timerOn = false;
            Log.d("stopTimer", "stopTimer: Timer Stopped");
            timer.cancel();
        }
    }

    private void reLoadUI() {
        if (gameController.lostGame()) {
            stopTimer();
            Log.d("lostGame", "You lost the game!!!");
            return;
        } else {
            //update matrix
            updateMatrixUI();
            //move rocket
            displayRocketUI();
            //update hearts
            updateHeartsUI();
        }
    }

    private void updateMatrixUI() {
        for (int i = 0; i < gameController.getMatrixRows(); i++) {
            for (int j = 0; j < gameController.getMatrixCols(); j++) {
                String currentCol = gameController.getMatrix()[i][j];
                if (currentCol.equals(gameController.getFLAMING_METEOR())) {
                    main_IMG_matrix_flaming_meteor[i][j].setVisibility(View.VISIBLE);
                } else if (currentCol.equals(gameController.getBLANK())) {
                    main_IMG_matrix_flaming_meteor[i][j].setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void displayRocketUI() {
        Log.d("Position", "" + gameController.getRocketPos());
        for (int i = 0; i < gameController.getMatrixCols(); i++) {
            if (i == gameController.getRocketPos()) {
                main_IMG_rocket[i].setVisibility(View.VISIBLE);
            } else {
                main_IMG_rocket[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void updateHeartsUI() {
        if (gameController.checkCollision()) {
            if(gameController.getLife() == 0){
                toastAndVibrate("You lost the game!");
            }
            toastAndVibrate("Collision number " + gameController.getAmountOfDisqualifications());
            main_IMG_hearts[gameController.getAmountOfDisqualifications() - 1].setVisibility(View.INVISIBLE);
        }
    }

    private void toastAndVibrate(String text) {
        vibrate();
        toast(text);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }
}
