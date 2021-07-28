package com.game.chora;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;

public class Player implements Savable {
   
    protected String name;
    protected int waterBucket;
    protected int apple;
    
    public Player(){
        this.name = "";
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
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(this.getName(), "PlayerName", "");
        capsule.write(this.getApple(), "PlayerApples", 0);
        capsule.write(this.getWaterBucket(), "PlayerWaterBuckets", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        this.setName(capsule.readString("PlayerName", ""));
        this.setApple(capsule.readInt("PlayerApples", 0));
        this.setWaterBucket(capsule.readInt("PlayerWaterBuckets", 0));
    }
}
