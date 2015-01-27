package com.example.saibot1207.tobiasapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
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

    private int playerPosX = 5;
    private int playerPosY = 24;
    private int hitpoints = 3;

    // The start button.
    private Button mStartButton;
    private Button mPlayButton;
    private int sendDelay;
    private boolean proceed;

    public int getPlayerPosX() {
        return playerPosX;
    }

    public int getPlayerPosY() {
        return playerPosY;
    }

    public void setPlayerPosX(int playerPosX) {
        this.playerPosX = playerPosX;
    }

    public void setPlayerPosY(int playerPosY) {
        this.playerPosY = playerPosY;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mStartButton = (Button) findViewById(R.id.startButton);
        mPlayButton = (Button) findViewById(R.id.playButton);
    }

    public void displayCredits(View v) {
        Context context = getApplicationContext();
        CharSequence text = "App for Lecture \"Mobile Computing & Internet of Things\" ";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
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

                game.intro();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPlayButton.setEnabled(true);
                    }
                });

                game.intro2();


                boolean restarted = true;

                while(restarted) {
                    boolean playing = true;
                    setHitpoints(3);
                    while (playing && hitpoints < 3) {
                        loop = true;
                        setPlayerPosX(5);
                        setPlayerPosY(24);
                        while (loop) {
                            /*
                            counter2++;

                            // Change pattern every 10 frames.
                            if (counter2 >= 7) {
                                if (a == 200) {
                                    a = 0;
                                    b = 200;
                                } else {
                                    a = 200;
                                    b = 0;
                                }
                                counter2 = 0;

                            }
                            */

                            // Fill message buffer.
                            byte[] msgBuffer = new byte[24 * 24];
                            for (int i = 0; i < (24 * 24); i++) {
                                if (i % 6 < 3) {
                                    msgBuffer[i] = (byte) a;
                                }
                                else {
                                    msgBuffer[i] = (byte) b;
                                }
                                if ( (i/24) % 6 < 3 ) {
                                    if ( msgBuffer[i] == (byte) a) {
                                        msgBuffer[i] = (byte) b;
                                    }
                                    else {
                                        msgBuffer[i] = (byte) a;
                                    }
                                }
                            }

                            // If write fails, the connection was probably closed by the server.
                            if (!BT.write(msgBuffer)) {
                                loop = false;
                                playing = false;
                            }

                            try {
                                // Delay for a moment.
                                // Note: Delaying the same amount of time every frame will not give you constant FPS.
                                Thread.sleep(sendDelay);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }




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

    @Override
    public void onPause() {
        super.onPause();

        mStartButton.setEnabled(true);

        // Avoid crash if user exits the app before pressing start.
        if (BT != null) {
            BT.onPause();
        }
    }


}
