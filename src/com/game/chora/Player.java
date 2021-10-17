package com.game.chora;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;
import java.io.Serializable;

public class Player {
    
    protected String name;
    protected int waterBucket;
    protected int apple;
    protected int well;
    protected int mill;
    protected int takePound;
    protected boolean ambientVolume;
    protected boolean musicVolume;
    protected int tutorial;
    
    public Player(){
        this.name = "ciao";
        this.waterBucket = 0;
        this.apple = 0;
        this.well = 0;
        this.mill = 0;
        this.takePound = 0;
        this.ambientVolume = true;
        this.musicVolume = true;
        this.tutorial = 1; // 0 = no tutorial / 1 = first step / 2 = second step / ...
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getWaterBucket() {
        return this.waterBucket;
    }
    
    public int getApple() {
        return this.apple;
    }
    
    public int getWell() {
        return this.well;
    }
    
    public int getMill() {
        return this.mill;
    }
    
    public int getTakePound() {
        return this.takePound;
    }
    
    public boolean getAmbientVolume() {
        return this.ambientVolume;
    }
    
    public boolean getMusicVolume() {
        return this.musicVolume;
    }
    
    public int getTutorial() {
        return this.tutorial;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setWaterBucket(int waterBucket) {
        this.waterBucket = waterBucket;
    }
    
    public void setApple(int apple) {
        this.apple = apple;
    }
    
    public void setWell(int well) {
        this.well = well;
    }
    
    public void setMill(int mill) {
        this.mill = mill;
    }
    
    public void setTakePound(int takePound) {
        this.takePound = takePound;
    }
    
    public void setAmbientVolume(boolean av) {
        this.ambientVolume = av;
    }
    
    public void setMusicVolume(boolean mv) {
        this.musicVolume = mv;
    }
    public void setTutorial(int tutorial) {
        this.tutorial = tutorial;
    }
}
