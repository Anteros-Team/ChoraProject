package com.game.chora;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;

public class View {
    
    protected FilterPostProcessor fpp;
    protected DirectionalLightShadowRenderer dlsrSun;
    protected DirectionalLightShadowFilter dlsfSun;
    protected DirectionalLightShadowRenderer dlsrMoon;
    protected DirectionalLightShadowFilter dlsfMoon;
    protected final int SHADOWMAP_SIZE = 512;
    protected AmbientLight al;
    
    public View(AssetManager assetManager, Node rootNode, ViewPort viewPort, DynamicSky sky) {
        this.fpp = new FilterPostProcessor(assetManager);
        this.setSunLightShadow(assetManager, viewPort, sky);
        this.setMoonLightShadow(assetManager, viewPort, sky);
        this.setAmbientLight(rootNode);
        
    }
    
    public FilterPostProcessor getFilterPostProcessor() {
        return this.fpp;
    }
    
    public DirectionalLightShadowFilter getSunLightShadowFilter() {
        return this.dlsfSun;
    }
    
    private void setSunLightShadow(AssetManager assetManager, ViewPort viewPort, DynamicSky sky) {
        /*this.dlsrSun = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        this.dlsrSun.setLight(sky.getSunLight());
        System.out.println("Sun light : " + sky.getSunLight());
        viewPort.addProcessor(dlsrSun);*/
        
        this.dlsfSun = new DirectionalLightShadowFilter(assetManager, this.SHADOWMAP_SIZE, 3);
        this.dlsfSun.setLambda(0.2f);
        this.dlsfSun.setShadowIntensity(0.5f);
        this.dlsfSun.setRenderBackFacesShadows(true);
        this.dlsfSun.setEnabledStabilization(true);
        this.dlsfSun.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);
        
        this.dlsfSun.setShadowZExtend(3000);
        this.dlsfSun.setShadowZFadeLength(50);
        this.fpp.addFilter(this.dlsfSun);
    }
    
    public DirectionalLightShadowFilter getMoonLightShadowFilter() {
        return this.dlsfMoon;
    }
    
    private void setMoonLightShadow(AssetManager assetManager, ViewPort viewPort, DynamicSky sky) {
        /*this.dlsrMoon = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        this.dlsrMoon.setLight(sky.getSunLight());
        viewPort.addProcessor(dlsrMoon);*/
        
        this.dlsfMoon = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        this.dlsfMoon.setLambda(0.2f);
        this.dlsfMoon.setShadowIntensity(0.3f);
        this.dlsfMoon.setRenderBackFacesShadows(true);
        this.dlsfMoon.setEnabledStabilization(true);
        this.dlsfMoon.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);

        this.dlsfMoon.setShadowZExtend(3000);
        this.dlsfMoon.setShadowZFadeLength(50);
        this.fpp.addFilter(this.dlsfMoon);
    }
    
    private void setAmbientLight(Node rootNode) {
        this.al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(.8f));        
        rootNode.addLight(al);
    }
    
    public void updateLight(DynamicSky sky) {
        this.dlsfSun.setLight(sky.getSunLight());
        this.dlsfMoon.setLight(sky.getMoonLight());
    }
}
