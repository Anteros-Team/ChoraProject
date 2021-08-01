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
    
    public Player(){
        this.name = "ciao";
        this.waterBucket = 2;
        this.apple = 0;
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
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setWaterBucket(int waterBucket) {
        this.waterBucket = waterBucket;
    }
    
    public void setApple(int apple) {
        this.apple = apple;
    }
}
