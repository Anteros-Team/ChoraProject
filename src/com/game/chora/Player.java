package com.game.chora;

public class Player {
   
    protected String name;
    protected int waterBucket;
    protected int apple;
    
    public Player(String name){
        this.name = name;
        this.waterBucket = 2;
        this.apple = 0;
    }
    
    public int getWaterBucket() {
        return this.waterBucket;
    }
    
    public int getApple() {
        return this.apple;
    }
    
    public void setWaterBucket(int waterBucket) {
        this.waterBucket = waterBucket;
    }
    
    public void setApple(int apple) {
        this.apple = apple;
    }
    
}
