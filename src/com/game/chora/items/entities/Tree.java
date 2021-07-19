package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class Tree extends Entity {
    
    protected List<Apple> apples;
    protected float time;
    
    public Tree(Vector3f position, float scale, Vector3f hitboxSize) {
        super(position, scale, hitboxSize);
        this.apples = new ArrayList<>();
        this.time = 0;
    }
    
    public float getTime() {
        return this.time;
    }
    
    public void increaseTime(float tpf) {
        this.time += tpf;
        System.out.println(this.time);
    }
    
    public void resetTime() {
        this.time = 0;
    }
    
    public void newApple(Apple a) {
        this.apples.add(a);
    }
    
}
