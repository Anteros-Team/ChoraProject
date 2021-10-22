package com.game.chora;

import com.game.chora.items.sky.DynamicMoon;
import com.game.chora.items.sky.DynamicSkyBackground;
import com.game.chora.items.sky.DynamicStars;
import com.game.chora.items.sky.DynamicSun;
import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

/**
 * DynamicSky is a class that merge all sky-releted items.
 *
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class DynamicSky extends Node {
    private ViewPort viewPort = null;
    private AssetManager assetManager = null;
    
    private DynamicSun dynamicSun = null;
    private DynamicMoon dynamicMoon = null;
    private DynamicStars dynamicStars = null;
    private DynamicSkyBackground dynamicBackground = null;
    
    private boolean dayTime;
    private float dayChanging;
    
    private float scaling = 20000;
    
    /**
     * class constructor with parameters.
     * @param assetManager
     * @param viewPort
     * @param rootNode
     */
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
    
    /**
     *
     * @return sun direction
     */
    public Vector3f getSunDirection(){
        return dynamicSun.getSunDirection();
    }
    
    /**
     *
     * @return sun light
     */
    public DirectionalLight getSunLight(){
        return dynamicSun.getSunLight();
    }
    
    /**
     *
     * @return moon direction
     */
    public Vector3f getMoonDirection() {
        return dynamicMoon.getSunDirection();
    }
    
    /**
     *
     * @return moon light
     */
    public DirectionalLight getMoonLight(){
        return dynamicMoon.getSunLight();
    }
        
    /**
     * 
     * @return day state
     */
    public boolean isDayTime() {
        return this.dayTime;
    }
    
    /**
     *
     * @return day state changing
     */
    public float getDayChanging() {
        return this.dayChanging;
    }
    
    /**
     * update sun and moon position, light position and direction, and stars position.
     */
    public void updateTime(){
        dynamicSun.updateTime(dynamicSun.getSunSystem().getDirection());
        dynamicMoon.updateTime(dynamicMoon.getSunSystem().getDirection());
        
        this.dayTime = dynamicSun.getSunSystem().getPosition().y >= 0;
        if (dynamicSun.getSunSystem().getPosition().y > 100 || dynamicSun.getSunSystem().getPosition().y < -100) {
            this.dayChanging = 1f;
        } else {
            this.dayChanging = (float) Math.abs(dynamicSun.getSunSystem().getPosition().y) / 100f;
        }
        
        dynamicBackground.updateLightPosition(dynamicSun.getSunSystem().getPosition());
        dynamicStars.update(dynamicSun.getSunSystem().getDirection());        
        dynamicStars.lookAt(dynamicSun.getSunSystem().getPosition(), Vector3f.ZERO);
    }
    
}