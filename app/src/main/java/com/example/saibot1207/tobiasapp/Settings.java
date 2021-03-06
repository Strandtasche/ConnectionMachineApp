package com.example.saibot1207.tobiasapp;

/**
 * Created by saibot1207 on 24.02.15.
 */
public class Settings {

    public Settings(){
        this.difficulty = 2;
        this.controls = false;
        this.intro = true;
        this.device = "";
    }

    public Settings(int difficulty, boolean controls, boolean intro, String device) {
        this.difficulty = difficulty;
        this.controls = controls;
        this.intro = intro;
        this.device = device;
    }

    private int difficulty;
    private boolean controls;
    private boolean intro;
    private String device;

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public boolean getControls() {
        return controls;
    }

    public void setControls(boolean controls) {
        this.controls = controls;
    }

    public boolean getIntro() {
        return intro;
    }

    public void setIntro(boolean intro) {
        this.intro = intro;
    }
}
