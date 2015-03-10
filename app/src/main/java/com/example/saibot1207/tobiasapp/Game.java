package com.example.saibot1207.tobiasapp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by saibot1207 on 27.01.15.
 */
public class Game {

    private LEDMatrixBTConn BT;
    private int sendDelay;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;

    private int playerPosX = 5;
    private int playerPosY = 22;
    private int hitpoints = 3;
    private boolean confirmed = false;


    boolean displayed = false;


    public boolean isDisplayed() {
        return displayed;
    }

    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }

    public Game() {

    }

    public static Handler UIHandler;

    static
    {
        UIHandler = new Handler(Looper.getMainLooper());
    }
    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }

    public Game(LEDMatrixBTConn BT1, int sendDelay1) {
        this.BT = BT1;
        this.sendDelay = sendDelay1;
    }

    public int getPlayerPosX() {
        return playerPosX;
    }

    public void setPlayerPosX(int playerPosX) {
        if (playerPosX >= 0 && playerPosX  <= 23) {
            this.playerPosX = playerPosX;
        }
        else if (playerPosX < 0) {
            this.playerPosX = 23;
        }
        else if (playerPosX > 23) {
            this.playerPosX = 0;
        }

    }

    public int getPlayerPosY() {
        return playerPosY;
    }

    public void setPlayerPosY(int playerPosY) {
        if (playerPosY >= 0 && playerPosY  <= 23) {
            this.playerPosY = playerPosY;
        }
        else if (playerPosY < 0) {
            this.playerPosY = 0; // make it not go around.
        }
        else if (playerPosY > 23) {
            this.playerPosY = 23; // make it not go around.
        }
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public LEDMatrixBTConn getBT() {
        return BT;
    }

    public void setBT(LEDMatrixBTConn BT) {
        this.BT = BT;
    }

    public int getSendDelay() {
        return sendDelay;
    }

    public void setSendDelay(int sendDelay) {
        this.sendDelay = sendDelay;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }



    public void intro(boolean skip) {

        if ( skip ) {
            return;
        }
        boolean loop = true;
        // Prepare variables for making the pattern.
        int counter = 0;
        int a = 255;
        int b = 0;
        int[] iarray = new int[24];
        for (int i = 0; i < iarray.length; i++) {
            iarray[i] = i * 24 + i;
        }


        // Main sending loop.
        while (loop && counter < 200) {
            counter++;

            // Change pattern every 10 frames.
            if (counter % 20 == 0) {
                if (a == 255) {
                    a = 0;
                    b = 200;
                } else {
                    a = 255;
                    b = 0;
                }

            }

            // Fill message buffer.
            byte[] msgBuffer = new byte[24 * 24];
            for (int i = 0; i < (24 * 24); i++) {
                if (i < 24) {
                    msgBuffer[i] = (byte) 200;
                } else if (i % 24 == 0 || i % 24 == 23) {
                    msgBuffer[i] = (byte) 200;
                } else if (i > 551) {
                    msgBuffer[i] = (byte) 200;
                } else if (i % 24 == 11 || i % 24 == 12) {
                    msgBuffer[i] = (byte) 200;
                } else if (i > 264 && i < 312) {
                    msgBuffer[i] = (byte) 200;
                } else if (i / 24 == i % 24 && i * 200 / 576 < counter) {
                    msgBuffer[i] = (byte) 200;
                    msgBuffer[i - 2 * (i % 24) + 23] = (byte) 200;
                }

            }

            // If write fails, the connection was probably closed by the server.
            if (!BT.write(msgBuffer)) {
                loop = false;
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

    public void intro2(boolean skip) {

        if (skip){
            return;
        }
        int counter2 = 0;
        boolean loop = true;
        int counter = 0;
        int a = 255;
        int b = 0;
        int[] iarray = new int[24];
        for (int i = 0; i < iarray.length; i++) {
            iarray[i] = i * 24 + i;
        }


        while (loop && !confirmed) {
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

            // Fill message buffer.
            byte[] msgBuffer = new byte[24 * 24];
            for (int i = 0; i < (24 * 24); i++) {
                if (i % 6 < 3) {
                    msgBuffer[i] = (byte) a;
                } else {
                    msgBuffer[i] = (byte) b;
                }
                if ((i / 24) % 6 < 3) {
                    if (msgBuffer[i] == (byte) a) {
                        msgBuffer[i] = (byte) b;
                    } else {
                        msgBuffer[i] = (byte) a;
                    }
                }
            }

            // If write fails, the connection was probably closed by the server.
            if (!BT.write(msgBuffer)) {
                loop = false;
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

    public void printPlay() { // schreibt "press play" auf die Machine

        byte[] msgBuffer = new byte[24 * 24];
        for( int i = 2; i < 7; i++ ) {
            msgBuffer[2 + i * 24] = (byte) 200;
            msgBuffer[3 + i * 24] = (byte) 200;
            msgBuffer[4 + i * 24] = (byte) 200;

            msgBuffer[6 + i * 24] = (byte) 200;
            msgBuffer[7 + i * 24] = (byte) 200;
            msgBuffer[8 + i * 24] = (byte) 200;

            msgBuffer[12 + i * 24] = (byte) 200;
            msgBuffer[13 + i * 24] = (byte) 200;
            msgBuffer[14 + i * 24] = (byte) 200;

            msgBuffer[16 + i * 24] = (byte) 200;
            msgBuffer[17 + i * 24] = (byte) 200;
            msgBuffer[18 + i * 24] = (byte) 200;

            msgBuffer[20 + i * 24] = (byte) 200;
            msgBuffer[21 + i * 24] = (byte) 200;
            msgBuffer[22 + i * 24] = (byte) 200;
        }

        msgBuffer[3 + 3 * 24] = (byte) 0;
        msgBuffer[3 + 5 * 24] = (byte) 0;
        msgBuffer[3 + 6 * 24] = (byte) 0;
        msgBuffer[4 + 5 * 24] = (byte) 0;
        msgBuffer[4 + 6 * 24] = (byte) 0;
        msgBuffer[7 + 3 * 24] = (byte) 0;
        msgBuffer[7 + 6 * 24] = (byte) 0;
        msgBuffer[8 + 5 * 24] = (byte) 0;
        msgBuffer[13 + 3 * 24] = (byte) 0;
        msgBuffer[14 + 3 * 24] = (byte) 0;
        msgBuffer[17 + 3 * 24] = (byte) 0;
        msgBuffer[18 + 3 * 24] = (byte) 0;
        msgBuffer[21 + 3 * 24] = (byte) 0;
        msgBuffer[22 + 3 * 24] = (byte) 0;
        msgBuffer[13 + 5 * 24] = (byte) 0;
        msgBuffer[14 + 5 * 24] = (byte) 0;
        msgBuffer[16 + 5 * 24] = (byte) 0;
        msgBuffer[17 + 5 * 24] = (byte) 0;
        msgBuffer[20 + 5 * 24] = (byte) 0;
        msgBuffer[21 + 5 * 24] = (byte) 0;

        for( int j = 14; j < 21; j++){
            msgBuffer[3 + j * 24] = (byte) 200;
            msgBuffer[8 + j * 24] = (byte) 200;
            msgBuffer[13 + j * 24] = (byte) 200;
            msgBuffer[16 + j * 24] = (byte) 200;
            msgBuffer[13 + j * 24] = (byte) 200;
            msgBuffer[20 + j * 24] = (byte) 200;
        }

        msgBuffer[13 + 14 * 24] = (byte) 0;
        msgBuffer[16 + 14 * 24] = (byte) 0;
        msgBuffer[20 + 14 * 24] = (byte) 0;
        msgBuffer[20 + 15 * 24] = (byte) 0;

        msgBuffer[4 + 14 * 24] = (byte) 200;
        msgBuffer[5 + 14 * 24] = (byte) 200;
        msgBuffer[6 + 14 * 24] = (byte) 200;
        msgBuffer[7 + 14 * 24] = (byte) 200;
        msgBuffer[7 + 15 * 24] = (byte) 200;
        msgBuffer[7 + 16 * 24] = (byte) 200;
        msgBuffer[7 + 17 * 24] = (byte) 200;
        msgBuffer[6 + 17 * 24] = (byte) 200;
        msgBuffer[5 + 17 * 24] = (byte) 200;
        msgBuffer[4 + 17 * 24] = (byte) 200;
        msgBuffer[9 + 20 * 24] = (byte) 200;
        msgBuffer[10 + 20 * 24] = (byte) 200;
        msgBuffer[11 + 20 * 24] = (byte) 200;
        msgBuffer[14 + 14 * 24] = (byte) 200;
        msgBuffer[15 + 14 * 24] = (byte) 200;
        msgBuffer[14 + 18 * 24] = (byte) 200;
        msgBuffer[15 + 18 * 24] = (byte) 200;
        msgBuffer[18 + 14 * 24] = (byte) 200;
        msgBuffer[22 + 14 * 24] = (byte) 200;
        msgBuffer[19 + 15 * 24] = (byte) 200;
        msgBuffer[21 + 15 * 24] = (byte) 200;



        for( int k = 0; k < 50; k++){
            if (!BT.write(msgBuffer)) {
                //loop = false; //Fehlerbehandlung?
            }
            try {
                // Delay for a moment.
                // Note: Delaying the same amount of time every frame will not give you constant FPS.
                Thread.sleep(sendDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        try {
            // Delay for a moment.
            // Note: Delaying the same amount of time every frame will not give you constant FPS.
            Thread.sleep(sendDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printWin() { // schreibt "Win" auf die Machine

        byte[] msgBuffer = new byte[24 * 24];
        for( int i = 2; i < 8; i++){
            msgBuffer[8 + i * 24] = (byte) 200;
            msgBuffer[11 + i * 24] = (byte) 200;
            msgBuffer[12 + i * 24] = (byte) 200;

        }
        msgBuffer[2 + 2 * 24] = (byte) 200;
        msgBuffer[6 + 2 * 24] = (byte) 200;
        msgBuffer[9 + 2 * 24] = (byte) 200;
        msgBuffer[10 + 2 * 24] = (byte) 200;
        msgBuffer[3 + 3 * 24] = (byte) 200;
        msgBuffer[5 + 3 * 24] = (byte) 200;
        msgBuffer[4 + 4 * 24] = (byte) 200;
        msgBuffer[4 + 5 * 24] = (byte) 200;
        msgBuffer[4 + 6 * 24] = (byte) 200;
        msgBuffer[4 + 7 * 24] = (byte) 200;
        msgBuffer[9 + 7 * 24] = (byte) 200;
        msgBuffer[10 + 7 * 24] = (byte) 200;
        msgBuffer[13 + 7 * 24] = (byte) 200;
        msgBuffer[14 + 7 * 24] = (byte) 200;

        for( int i = 14; i < 21; i++){
            msgBuffer[11 + i * 24] = (byte) 200;
            msgBuffer[12 + i * 24] = (byte) 200;
            msgBuffer[16 + i * 24] = (byte) 200;
        }

        msgBuffer[2 + 14 * 24] = (byte) 200;
        msgBuffer[2 + 15 * 24] = (byte) 200;
        msgBuffer[3 + 16 * 24] = (byte) 200;
        msgBuffer[3 + 17 * 24] = (byte) 200;
        msgBuffer[4 + 18 * 24] = (byte) 200;
        msgBuffer[4 + 19 * 24] = (byte) 200;
        msgBuffer[5 + 16 * 24] = (byte) 200;
        msgBuffer[5 + 17 * 24] = (byte) 200;
        msgBuffer[6 + 18 * 24] = (byte) 200;
        msgBuffer[6 + 19 * 24] = (byte) 200;
        msgBuffer[7 + 16 * 24] = (byte) 200;
        msgBuffer[7 + 17 * 24] = (byte) 200;
        msgBuffer[8 + 14 * 24] = (byte) 200;
        msgBuffer[9 + 15 * 24] = (byte) 200;

        msgBuffer[13 + 15 * 24] = (byte) 200;
        msgBuffer[15 + 18 * 24] = (byte) 200;
        msgBuffer[14 + 16 * 24] = (byte) 200;
        msgBuffer[14 + 17 * 24] = (byte) 200;



        for( int k = 0; k < 50; k++){
            if (!BT.write(msgBuffer)) {
                //loop = false; //Fehlerbehandlung?
            }
            try {
                // Delay for a moment.
                // Note: Delaying the same amount of time every frame will not give you constant FPS.
                Thread.sleep(sendDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        try {
            // Delay for a moment.
            // Note: Delaying the same amount of time every frame will not give you constant FPS.
            Thread.sleep(sendDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startgame() {
        boolean loop = true;
        boolean restarted = true;
        int counter = 0;
        int a = 255;
        int b = 0;
        int[] iarray = new int[24];
        for (int i = 0; i < iarray.length; i++) {
            iarray[i] = i * 24 + i;
        }

        while (restarted) {
            setDisplayed(false);
            boolean playing = true;
            setHitpoints(3);
            Tab2.setDisplayedHP(getHitpoints());
            int counter2 = 0;


            while (playing && getHitpoints() > 0) {
                loop = true;
                setPlayerPosX(5);
                setPlayerPosY(22);
                while (playing && getHitpoints() > 0) {

                    counter2++;

                    // Change pattern every 10 frames.
                    if (counter2 >= 240) {
                        if (a == 200) {
                            a = 0;
                            b = 200;
                        } else {
                            a = 200;
                            b = 0;
                        }
                        counter2 = 0;

                    }


                    // Fill message buffer.
                    byte[] msgBuffer = new byte[24 * 24];
                    /*int speed1 = (int) (Math.random() * 3);
                    int speed2 = (int) (Math.random() * 3);
                    int speed3 = (int) (Math.random() * 3); */
                    generateObstacles(msgBuffer, 8, 20, (int) (0.3* counter2 + 1), 200, 0);
                    generateObstacles(msgBuffer, 8, 19, (int) (0.3* counter2 + 1), 200, 0);
                    generateObstacles(msgBuffer, 3, 13, (int) (0.2 * counter2 + 4), 200, 0);
                    generateObstacles(msgBuffer, 3, 15, (int) (0.2* counter2 + 4), 200, 0);
                    generateObstacles(msgBuffer, 3, 14, (int) (0.2* counter2 + 4), 200, 0);
                    generateObstacles(msgBuffer, 2, 13, (int) (0.2 * counter2 + 6), 200, 0);
                    generateObstacles(msgBuffer, 2, 15, (int) (0.2 * counter2 + 6), 200, 0);
                    generateObstacles(msgBuffer, 2, 14, (int) (0.2 * counter2 + 6), 200, 0);
                    generateObstacles(msgBuffer, 1, 9, (int) (0.2* counter2), 200, 0);
                    generateObstacles(msgBuffer, 1, 9, (int) (0.2* counter2 + 5), 200, 0);
                    generateObstacles(msgBuffer, 1, 9, (int) (0.2* counter2 + 9), 200, 0);
                    generateObstacles(msgBuffer, 1, 8, (int) (0.2* counter2 + 8), 200, 0);
                    generateObstacles(msgBuffer, 7, 5, (int) (0.2* counter2 + 3), 200, 0);
                    generateObstacles(msgBuffer, 7, 4, (int) (0.2* counter2 + 3), 200, 0);
                    generateObstacles(msgBuffer, 2, 2, (int) (0.2* counter2 + 6), 200, 0);
                    generateObstacles(msgBuffer, 3, 18, (int) (0.2* ((240 - counter2) % 240) + 6), 200, 0);
                    generateObstacles(msgBuffer, 3, 2, (int) (0.2* ((240 - counter2) % 240) + 15), 200, 0);
                    generateObstacles(msgBuffer, 3, 14, (int) (0.2 * ((240 - counter2) % 240) + 11), 200, 0);


                    for (int i = 0; i < (24 * 24); i++) {
                        if ((i/24 == getPlayerPosY() || i/24 == getPlayerPosY() + 1) && (i % 24 == getPlayerPosX() || i % 24 == getPlayerPosX() +1 ) ) {
                            if (msgBuffer[i] == 0) {
                                msgBuffer[i] = (byte) 200;
                                // collision detection
                            }
                            else {
                                setHitpoints(getHitpoints() - 1);
                                Tab2.setDisplayedHP(getHitpoints());
                                byte[] msgBuffer2 = new byte[24 * 24];
                                byte[] msgBuffer3 = new byte[24 * 24];
                                if ( i > 22 && i < 551 && i % 24 != 0 && i % 24 != 23) {
                                    msgBuffer3[i] = (byte) 200;
                                    msgBuffer3[i+1] = (byte) 200;
                                    msgBuffer3[i-1] = (byte) 200;
                                    msgBuffer3[i-25] = (byte) 200;
                                    msgBuffer3[i-24] = (byte) 200;
                                    msgBuffer3[i-23] = (byte) 200;
                                    msgBuffer3[i+23] = (byte) 200;
                                    msgBuffer3[i+24] = (byte) 200;
                                    msgBuffer3[i+25] = (byte) 200;
                                }
                                else {
                                    for( int m = 0; m < 576; m++) {
                                        if (m % 5 == 0) {
                                            msgBuffer3[3] = (byte) 200;
                                        }
                                    }
                                }


                                for( int k = 0; k < 15; k++) {
                                    if (!BT.write(msgBuffer2)) {
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

                                    if (!BT.write(msgBuffer3)) {
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
                                setPlayerPosX(5);
                                setPlayerPosY(22);

                            }
                        }
                    }
                    if (getPlayerPosY() == 0) {
                        printWin();
                        Log.d("you should win here", "right here");
                        break;
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

                if (getHitpoints() <= 0 && !isDisplayed()) {
                    runOnUI(new Runnable() {
                        @Override
                        public void run() {
                            CharSequence text = "You used up all your Lives. Try again!";
                            int duration = Toast.LENGTH_SHORT;


                            Toast toast = Toast.makeText(context, text, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            setDisplayed(true);
                        }
                    });
                }




            }

            //restart Muster
        }
    }

    private void generateObstacles(byte[] target, int length, int row, int posi, int intensity, int column) {
        for (int j = 0; j < length; j++) {
            target[24 * row + (posi + j) % 24 + column * 24] = (byte) intensity;
        }
    }





}
