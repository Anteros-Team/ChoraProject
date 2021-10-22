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

/**
 * View is a class that manages light and shadow renderers and
 * all post processors filters.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class View {
    
    private FilterPostProcessor fpp;
    private DirectionalLightShadowRenderer dlsrSun;
    private DirectionalLightShadowFilter dlsfSun;
    private DirectionalLightShadowRenderer dlsrMoon;
    private DirectionalLightShadowFilter dlsfMoon;
    private final int SHADOWMAP_SIZE = 512;
    private AmbientLight al;
    
    /**
     * class constructor with parameters.
     * @param assetManager
     * @param rootNode
     * @param viewPort
     * @param sky
     */
    public View(AssetManager assetManager, Node rootNode, ViewPort viewPort, DynamicSky sky) {
        this.fpp = new FilterPostProcessor(assetManager);
        this.setSunLightShadow(assetManager, viewPort, sky);
        this.setMoonLightShadow(assetManager, viewPort, sky);
        this.setAmbientLight(rootNode);
    }
    
    /**
     *
     * @return filter post processor
     */
    public FilterPostProcessor getFilterPostProcessor() {
        return this.fpp;
    }
    
    /**
     *
     * @return sun light shadow filter
     */
    public DirectionalLightShadowFilter getSunLightShadowFilter() {
        return this.dlsfSun;
    }
    
    /**
     * setup sun light shadows.
     * @param assetManager
     * @param viewPort
     * @param sky 
     */
    private void setSunLightShadow(AssetManager assetManager, ViewPort viewPort, DynamicSky sky) {
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
    
    /**
     *
     * @return moon light shadow filter
     */
    public DirectionalLightShadowFilter getMoonLightShadowFilter() {
        return this.dlsfMoon;
    }
    
    /**
     * setup moon light shadows.
     * @param assetManager
     * @param viewPort
     * @param sky 
     */
    private void setMoonLightShadow(AssetManager assetManager, ViewPort viewPort, DynamicSky sky) {
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
    
    /**
     * update light shadow filters for sun and moon.
     * @param sky
     */
    public void updateLight(DynamicSky sky) {
        this.dlsfSun.setLight(sky.getSunLight());
        this.dlsfMoon.setLight(sky.getMoonLight());
    }
}
