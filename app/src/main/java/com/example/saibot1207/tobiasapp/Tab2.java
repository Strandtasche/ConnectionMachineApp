package com.example.saibot1207.tobiasapp;

/**
 * Created by saibot1207 on 23.02.15.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Tab2 extends Activity {

    private LEDMatrixBTConn BT;
    protected String REMOTE_BT_DEVICE_NAME = "ledpi-teco";

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
    private static TextView mTextView;
    private static Vibrator v;
    private int sendDelay;
    private boolean proceed;
    private boolean intro;
    private boolean hardmode;
    private static SharedPreferences sharedPref;
    private boolean connected;



    public static Handler UIHandler;

    static
    {
        UIHandler = new Handler(Looper.getMainLooper());
    }
    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab2);

        Log.d("is this called?", "yes it is");

        mStartButton = (Button) findViewById(R.id.startButton);
        mPlayButton = (Button) findViewById(R.id.playButton);
        mButtonDown = (Button) findViewById(R.id.buttonDown);
        mButtonUp = (Button) findViewById(R.id.buttonUp);
        mButtonLeft = (Button) findViewById(R.id.buttonLeft);
        mButtonRight = (Button) findViewById(R.id.buttonRight);
        mTextView = (TextView) findViewById(R.id.hpText);
        game = new Game();
        connected = false;

        v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);


        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        REMOTE_BT_DEVICE_NAME = sharedPref.getString(Tab1.BT_SELECT_DEVICE_KEY, "");
        intro = sharedPref.getBoolean("intro", false);
        hardmode = sharedPref.getBoolean("difficulty", false);

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

    public static void setDisplayedHP(final int i)
    {
        runOnUI(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(i + " Hitpoints");
                if (!sharedPref.getBoolean(Tab1.GAMEPLAY_VIBRATE, true) ) {
                    v.vibrate(40l);
                }
            }
        });
    }

    public void start(View v) {

        if (connected) {

            mStartButton.setText(R.string.startText);
            mPlayButton.setEnabled(false);
            connected = false;
            game = new Game();
            onPause();
            onStop();


            return;
        }




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
                game.setContext(getApplicationContext());
                game.setHardmode(hardmode);

                game.intro(intro);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPlayButton.setEnabled(true);
                        connected = true;
                        mStartButton.setText(R.string.startText2);
                        mStartButton.setEnabled(true);
                    }
                });

                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText("true");
                    }
                });*/

                game.printPlay();

                game.intro2(intro);


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
        mPlayButton.setEnabled(false);

        Log.d("called", "yes it is");

        // Avoid crash if user exits the app before pressing start.
//        if (BT != null) {
//            BT.onPause();
//        }
    }

    @Override
    public void onStop() {
        if ( BT != null) {
            BT.closeConnection();
            connected = false;
        }
        super.onStop();

        mStartButton.setEnabled(true);
        mPlayButton.setEnabled(false);

        Log.d("stopped", "so true man");
        if (BT != null) {
            BT.onPause();
        }


    }

    @Override
    public void onDestroy() {
        if ( BT != null) {
            BT.closeConnection();
            connected = false;
        }

        mStartButton.setEnabled(true);
        mPlayButton.setEnabled(false);

        super.onDestroy();

        Log.d("Destroyed.", "so dead man");


    }

    @Override
    public void onResume() {
        super.onResume();

        REMOTE_BT_DEVICE_NAME = sharedPref.getString(Tab1.BT_SELECT_DEVICE_KEY, "");
        intro = sharedPref.getBoolean("intro", false);
        hardmode = sharedPref.getBoolean("difficulty", false);

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

