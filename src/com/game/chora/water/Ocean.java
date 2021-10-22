package com.game.chora.water;

import com.game.chora.DynamicSky;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.water.WaterFilter;

/**
 * Ocean is a class that create and manage world ocean.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class Ocean {
    
    private WaterFilter water;
    
    /**
     * class constructor with parameters.
     * @param rootNode
     * @param viewPort
     * @param fpp
     * @param sky
     */
    public Ocean(Node rootNode, ViewPort viewPort, FilterPostProcessor fpp, DynamicSky sky) {
        setOcean(rootNode, viewPort, fpp, sky);
    }
    
    /**
     *
     * @return ocean object
     */
    public WaterFilter getOcean() {
        return this.water;
    }
    
    /**
     * setup ocean.
     * @param rootNode
     * @param viewPort
     * @param fpp
     * @param sky 
     */
    private void setOcean(Node rootNode, ViewPort viewPort, FilterPostProcessor fpp, DynamicSky sky) {
        water = new WaterFilter(rootNode, sky.getSunDirection());
        water.setWaterHeight(-10);
        water.setMaxAmplitude(2);
        water.setFoamHardness(2);
        water.setFoamIntensity(2);
        water.setColorExtinction(new Vector3f(5, 10, 15));
        fpp.addFilter(water);
        viewPort.addProcessor(fpp);
    }
    
}
