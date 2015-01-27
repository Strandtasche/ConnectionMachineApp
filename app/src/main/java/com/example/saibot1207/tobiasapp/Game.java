package com.example.saibot1207.tobiasapp;

/**
 * Created by saibot1207 on 27.01.15.
 */
public class Game {

    private LEDMatrixBTConn BT;
    private int sendDelay;

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

    private boolean confirmed = false;

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Game() {

    }

    public Game(LEDMatrixBTConn BT1, int sendDelay1) {
        this.BT = BT1;
        this.sendDelay = sendDelay1;
    }

    public void draw() {

    }

    public void intro() {

        boolean loop = true;
        // Prepare variables for making the pattern.
        int counter = 0;
        int a = 255;
        int b = 0;
        int[] iarray = new int[24];
        for (int i = 0; i < iarray.length; i++) {
            iarray[i] = i* 24 + i;
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
                }
                else if (i % 24 == 0 || i % 24 == 23) {
                    msgBuffer[i] = (byte) 200;
                }
                else if (i > 551) {
                    msgBuffer[i] = (byte) 200;
                }
                else if (i % 24 == 11 || i % 24 == 12) {
                    msgBuffer[i] = (byte) 200;
                }
                else if (i > 264  && i < 312) {
                    msgBuffer[i] = (byte) 200;
                }
                else if (i / 24 == i % 24 && i* 200/576 < counter) {
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
            iarray[i] = i* 24 + i;
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
