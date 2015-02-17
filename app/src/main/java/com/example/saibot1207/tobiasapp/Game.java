package com.example.saibot1207.tobiasapp;

import java.util.Arrays;

/**
 * Created by saibot1207 on 27.01.15.
 */
public class Game {

    private LEDMatrixBTConn BT;
    private int sendDelay;

    private int playerPosX = 5;
    private int playerPosY = 23;
    private int hitpoints = 3;
    private boolean confirmed = false;

    public Game() {

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
            this.playerPosY = 23;
        }
        else if (playerPosY > 23) {
            this.playerPosY = 0;
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



    public void intro() {

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

    public void intro2() {
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
            boolean playing = true;
            setHitpoints(3);
            int counter2 = 0;


            while (playing && getHitpoints() > 0) {
                loop = true;
                setPlayerPosX(5);
                setPlayerPosY(23);
                while (loop) {

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

                    generateObstacles(msgBuffer, 8, 20, (int) (0.3* counter2 + 1), 200, 0);
                    generateObstacles(msgBuffer, 8, 19, (int) (0.3* counter2 + 1), 200, 0);
                    generateObstacles(msgBuffer, 3, 13, (int) (0.2* counter2 + 4), 200, 0);
                    generateObstacles(msgBuffer, 3, 15, (int) (0.2* counter2 + 4), 200, 0);
                    generateObstacles(msgBuffer, 3, 14, (int) (0.2* counter2 + 4), 200, 0);
                    generateObstacles(msgBuffer, 2, 13, (int) (0.2* counter2 + 6), 200, 0);
                    generateObstacles(msgBuffer, 2, 15, (int) (0.2* counter2 + 6), 200, 0);
                    generateObstacles(msgBuffer, 2, 14, (int) (0.2* counter2 + 6), 200, 0);
                    generateObstacles(msgBuffer, 1, 9, (int) (0.2* counter2), 200, 0);
                    generateObstacles(msgBuffer, 1, 9, (int) (0.2* counter2 + 5), 200, 0);
                    generateObstacles(msgBuffer, 1, 9, (int) (0.2* counter2 + 9), 200, 0);
                    generateObstacles(msgBuffer, 1, 8, (int) (0.2* counter2 + 8), 200, 0);
                    generateObstacles(msgBuffer, 7, 5, (int) (0.2* counter2 + 3), 200, 0);
                    generateObstacles(msgBuffer, 7, 4, (int) (0.2* counter2 + 3), 200, 0);
                    generateObstacles(msgBuffer, 2, 2, (int) (0.2* counter2 + 6), 200, 0);

                    for (int i = 0; i < (24 * 24); i++) {
                        if ((i/24 == getPlayerPosY() || i/24 == getPlayerPosY() + 1) && (i % 24 == getPlayerPosX() || i % 24 == getPlayerPosX() +1 ) ) {
                            if (msgBuffer[i] == 0) {
                                msgBuffer[i] = (byte) 200;
                            }
                            else {
                                setHitpoints(getHitpoints() - 1);
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
                                    if (!BT.write(msgBuffer3)) {
                                        loop = false;
                                        playing = false;
                                    }
                                }
                                setPlayerPosX(5);
                                setPlayerPosY(23);
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

            //restart Muster
        }
    }

    private void generateObstacles(byte[] target, int length, int row, int posi, int intensity, int column) {
        for (int j = 0; j < length; j++) {
            target[24 * row + (posi + j) % 12 + column * 12] = (byte) intensity;
        }
    }


}
