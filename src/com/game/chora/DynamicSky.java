package com.game.chora;

import com.game.chora.items.sky.DynamicMoon;
import com.game.chora.items.sky.DynamicSkyBackground;
import com.game.chora.items.sky.DynamicStars;
import com.game.chora.items.sky.DynamicSun;
import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

public class DynamicSky extends Node {
    private ViewPort viewPort = null;
    private AssetManager assetManager = null;
    
    private DynamicSun dynamicSun = null;
    private DynamicMoon dynamicMoon = null;
    private DynamicStars dynamicStars = null;
    private DynamicSkyBackground dynamicBackground = null;
    
    private boolean dayTime;
    private float dayChanging;
    
    private float scaling = 900;
    
    public DynamicSky(AssetManager assetManager, ViewPort viewPort, Node rootNode) {
        super("Sky");
        this.assetManager = assetManager;
        this.viewPort = viewPort;      
        
        this.dayTime = true;
        this.dayChanging = 1f;
               
        dynamicSun = new DynamicSun(assetManager, viewPort, rootNode, scaling);
        rootNode.attachChild(dynamicSun);

        dynamicStars = new DynamicStars(assetManager, viewPort, scaling);  
        rootNode.attachChild(dynamicStars);    
        
        dynamicMoon = new DynamicMoon(assetManager, viewPort, rootNode, scaling);
        rootNode.attachChild((dynamicMoon));        
        
        
        dynamicBackground = new DynamicSkyBackground(assetManager, viewPort, rootNode);
    }
    
    public Vector3f getSunDirection(){
        return dynamicSun.getSunDirection();
    }
    
    public DirectionalLight getSunLight(){
        return dynamicSun.getSunLight();
    }
    
    public Vector3f getMoonDirection() {
        return dynamicMoon.getSunDirection();
    }
    
    public DirectionalLight getMoonLight(){
        return dynamicMoon.getSunLight();
    }
        
    public boolean isDayTime() {
        return this.dayTime;
    }
    
    public float getDayChanging() {
        return this.dayChanging;
    }
    
    public void updateTime(){
        dynamicSun.updateTime(dynamicSun.getSunSystem().getDirection());
        dynamicMoon.updateTime(dynamicMoon.getSunSystem().getDirection());
        
        this.dayTime = dynamicSun.getSunSystem().getPosition().y >= 0;
        if (dynamicSun.getSunSystem().getPosition().y > 100 || dynamicSun.getSunSystem().getPosition().y < -100) {
            this.dayChanging = 1f;
        } else {
            this.dayChanging = (float) Math.abs(dynamicSun.getSunSystem().getPosition().y) / 100f;
        }
        //System.out.println(dynamicSun.getSunSystem().getPosition().y);
        //System.out.println(this.dayChanging);
        
        dynamicBackground.updateLightPosition(dynamicSun.getSunSystem().getPosition());
        dynamicStars.update(dynamicSun.getSunSystem().getDirection());        
        dynamicStars.lookAt(dynamicSun.getSunSystem().getPosition(), Vector3f.ZERO);
    }
    
}