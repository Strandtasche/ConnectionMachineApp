package com.example.saibot1207.tobiasapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.MotionEvent;
import com.example.saibot1207.tobiasapp.Game;

import java.util.Arrays;

public class MainActivity extends Activity {

    private LEDMatrixBTConn BT;
    protected static final String REMOTE_BT_DEVICE_NAME = "ledpi-teco";

    // Remote display x and y size.
    protected static final int X_SIZE = 24;
    protected static final int Y_SIZE = 24;

    // Remote display color mode. 0 = red, 1 = green, 2 = blue, 3 = RGB.
    protected static final int COLOR_MODE = 0;

    // The name this app uses to identify with the server.
    protected static final String APP_NAME = "TestApp";

    private Game game = new Game();

    // The start button.
    private Button mStartButton;
    private Button mPlayButton;
    private Button mXBotton;
    private Button mButtonUp;
    private Button mButtonDown;
    private Button mButtonLeft;
    private Button mButtonRight;
    private int sendDelay;
    private boolean proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mStartButton = (Button) findViewById(R.id.startButton);
        mPlayButton = (Button) findViewById(R.id.playButton);
        mButtonDown = (Button) findViewById(R.id.buttonDown);
        mButtonUp = (Button) findViewById(R.id.buttonUp);
        mButtonLeft = (Button) findViewById(R.id.buttonLeft);
        mButtonRight = (Button) findViewById(R.id.buttonRight);
        /* mXBotton = (Button) findViewById(R.id.buttonX);
        mXBotton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub


                if (event.getAction() == MotionEvent.ACTION_MOVE) {


                    float x = (int) event.getRawX();
                    float y = (int) event.getRawY();

                    //x -= v.getWidth()/2;
                    String test = "x - test: " + Float.toString(x);
                    //y -= v.getHeight()/2;
                    String test2 = "y - test: " + Float.toString(y);

                    Log.d("x pos", test);
                    Log.d("y pos", test2);
                    Log.d("p1 pos", Integer.toString(game.getPlayerPosX()));
                    Log.d("p2 pos", Integer.toString(game.getPlayerPosY()));
                    if ( Math.abs(x) > Math.abs(y)) {
                        if (x > 0) {
                            game.moveLeft();

                        }
                        else {
                            game.moveRight();

                        }

                    }
                    else {
                        if (y > 0) {
                            game.moveUp();

                        }
                        else {
                            game.moveDown();

                        }
                    }
                }
                return true;
            }
        }); */

    }



    public void displayCredits(View v) {
        Context context = getApplicationContext();
        CharSequence text = "App for Lecture \"Mobile Computing & Internet of Things\" ";
        int duration = Toast.LENGTH_SHORT;


        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void start(View v) {
        mStartButton.setEnabled(false);
        proceed = false;

        // Set up BT connection.
        // Set up BT connection.
        BT = new LEDMatrixBTConn(this, REMOTE_BT_DEVICE_NAME, X_SIZE, Y_SIZE, COLOR_MODE, APP_NAME);

        if (!BT.prepare() || !BT.checkIfDeviceIsPaired()) {
            mStartButton.setEnabled(true);
            return;
        }

        // Start BT sending thread.
        Thread sender = new Thread() {

            boolean loop = true;
            public void run() {

                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

                // Try to connect.
                if (!BT.connect()) {
                    loop = false;
                }

                // Connected. Calculate and set send delay from maximum FPS.
                // Negative maxFPS should not happen.
                int maxFPS = BT.getMaxFPS();
                if (maxFPS > 0) {
                    sendDelay = (int) (1000.0 / maxFPS);
                } else {
                    loop = false;
                }

                // Prepare variables for making the pattern.
                int counter = 0;
                int a = 255;
                int b = 0;
                int[] iarray = new int[24];
                for (int i = 0; i < iarray.length; i++) {
                    iarray[i] = i* 24 + i;
                }

                game.setBT(BT);
                game.setSendDelay(sendDelay);

                //game.intro();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPlayButton.setEnabled(true);
                    }
                });

                game.intro2();

                game.startgame();






                // Connection terminated or lost.
                BT.closeConnection();
            }
        };

        // Start sending thread.
        sender.start();
    }

    public void play(View v){
        game.setConfirmed(true);

    }

    public boolean getProceed() {
        return proceed;
    }

    public void setProceed(boolean futProceed) {
        proceed = futProceed;
    }

    public void coordination(View v) {
        // ask people about pixel coordinates
    }

    @Override
    public void onPause() {
        super.onPause();

        mStartButton.setEnabled(true);

        // Avoid crash if user exits the app before pressing start.
        if (BT != null) {
            BT.onPause();
        }
    }

    public void moveUp(View v) {
        game.setPlayerPosY(game.getPlayerPosY() - 1);
        Log.d("mov", "up");
    }

    public void moveDown(View v) {
        game.setPlayerPosY(game.getPlayerPosY() + 1);
        Log.d("mov", "down");
    }
    public void moveLeft(View v) {
        game.setPlayerPosX(game.getPlayerPosX() - 1);
        Log.d("mov", "left");
    }

    public void moveRight(View v) {
        game.setPlayerPosX(game.getPlayerPosX() + 1);
        Log.d("mov", "right");
    }


}
