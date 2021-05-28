package com.game.chora;

import com.game.chora.items.DynamicMoon;
import com.game.chora.items.DynamicSkyBackground;
import com.game.chora.items.DynamicStars;
import com.game.chora.items.DynamicSun;
import com.jme3.asset.AssetManager;
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
    
    private float scaling = 900;
    
    public DynamicSky(AssetManager assetManager, ViewPort viewPort, Node rootNode) {
        super("Sky");
        this.assetManager = assetManager;
        this.viewPort = viewPort;        
               
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
    
    public Vector3f getMoonDirection() {
        return dynamicMoon.getSunDirection();
    }
        
    public void updateTime(){
        dynamicSun.updateTime(dynamicSun.getSunSystem().getDirection());
        dynamicMoon.updateTime(dynamicMoon.getSunSystem().getDirection());
        dynamicBackground.updateLightPosition(dynamicSun.getSunSystem().getPosition());
        dynamicStars.update(dynamicSun.getSunSystem().getDirection());        
        dynamicStars.lookAt(dynamicSun.getSunSystem().getPosition(), Vector3f.ZERO);
    }
    
}