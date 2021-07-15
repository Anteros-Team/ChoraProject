package com.game.chora;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;

public class View {
    
    protected FilterPostProcessor fpp;
    protected DirectionalLightShadowFilter dlsfSun;
    protected DirectionalLightShadowFilter dlsfMoon;
    protected final int SHADOWMAP_SIZE = 512;
    protected AmbientLight al;
    
    public View(AssetManager assetManager, Node rootNode) {
        this.fpp = new FilterPostProcessor(assetManager);
        this.setSunLightShadowFilter(assetManager);
        this.setMoonLightShadowFilter(assetManager);
        this.setAmbientLight(rootNode);
        
    }
    
    public FilterPostProcessor getFilterPostProcessor() {
        return this.fpp;
    }
    
    public DirectionalLightShadowFilter getSunLightShadowFilter() {
        return this.dlsfSun;
    }
    
    private void setSunLightShadowFilter(AssetManager assetManager) {
        this.dlsfSun = new DirectionalLightShadowFilter(assetManager, this.SHADOWMAP_SIZE, 3);
        this.dlsfSun.setLambda(0.2f);
        this.dlsfSun.setShadowIntensity(0.5f);
        this.dlsfSun.setRenderBackFacesShadows(true);
        this.dlsfSun.setEnabledStabilization(true);
        this.dlsfSun.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);
        
        this.dlsfSun.setShadowZExtend(650);
        this.dlsfSun.setShadowZFadeLength(150);
        this.fpp.addFilter(this.dlsfSun);
    }
    
    public DirectionalLightShadowFilter getMoonLightShadowFilter() {
        return this.dlsfMoon;
    }
    
    private void setMoonLightShadowFilter(AssetManager assetManager) {
        this.dlsfMoon = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        this.dlsfMoon.setLambda(0.2f);
        this.dlsfMoon.setShadowIntensity(0.3f);
        this.dlsfMoon.setRenderBackFacesShadows(true);
        this.dlsfMoon.setEnabledStabilization(true);
        this.dlsfMoon.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);

        this.dlsfMoon.setShadowZExtend(650);
        this.dlsfMoon.setShadowZFadeLength(150);
        this.fpp.addFilter(this.dlsfMoon);
    }
    
    private void setAmbientLight(Node rootNode) {
        this.al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(.3f));        
        rootNode.addLight(al);
    }
    
    public void updateLight(DynamicSky sky) {
        this.dlsfSun.setLight(sky.getSunLight());
        this.dlsfMoon.setLight(sky.getMoonLight());
    }
}
