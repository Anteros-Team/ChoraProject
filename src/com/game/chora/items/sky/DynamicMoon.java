package com.game.chora.items.sky;

import com.game.chora.utils.SkyBillboardItem;
import com.game.chora.utils.LunarSystem;
import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import java.util.Date;

/**
 * DynamicMoon is a class that manages model, light and position
 * of the moon.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class DynamicMoon extends Node {
    
    private static final Sphere sphereMesh = new Sphere(40, 40, 900, false, true);
    
    private ViewPort viewPort = null;
    private AssetManager assetManager = null;
    
    private LunarSystem moonSystem = new LunarSystem();
    private SkyBillboardItem moon;
    
    private DirectionalLight moonLight = null;
    private Vector3f lightDir = moonSystem.getPosition();
    private Vector3f lightPosition = new Vector3f();
    
    private float scaling = 1500;
    
    /**
     * class constuctor with parameters 
     * @param assetManager
     * @param viewPort
     * @param rootNode
     * @param scaling
     */
    public DynamicMoon(AssetManager assetManager, ViewPort viewPort, Node rootNode, Float scaling) {
        this.assetManager = assetManager;
        this.viewPort = viewPort;
        this.scaling = scaling;
        
        moonLight = new DirectionalLight();
        moonLight.setName("Moon");
        moonLight.setDirection(lightDir);
        moonLight.setColor(ColorRGBA.White.mult(0.5f));
        rootNode.addLight(moonLight);
                
        moonSystem.setSiteLatitude(46.32f);
        moonSystem.setSiteLongitude(6.38f);
        updateLightPosition();
        
              
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/sky/moon.png"));
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.getAdditionalRenderState().setDepthWrite(false);
                
        moon = new SkyBillboardItem("moon", 120f);
        moon.setShadowMode(shadowMode.Off);
        moon.setMaterial(mat);
        attachChild(moon);
        
        setQueueBucket(Bucket.Sky);
        setCullHint(CullHint.Never);
        
    }
    
    /**
     *
     * @return moon rotation class
     */
    public LunarSystem getSunSystem(){
        return moonSystem;
    }
    
    /**
     *
     * @return moon light object
     */
    public DirectionalLight getSunLight(){
        return this.moonLight;
    }
        
    /**
     * update moon light position and direction.
     */
    protected void updateLightPosition(){
        lightDir = moonSystem.getDirection();
        lightPosition = moonSystem.getPosition();
        this.moonLight.setDirection(lightDir);
    }
    
    /**
     *
     * @return moon position
     */
    public Vector3f getSunDirection(){
        return moonSystem.getPosition();
    }

    /**
     * update moon position and speed, also turn off
     * the light on daytime.
     * @param moonDir
     */
    public void updateTime(Vector3f moonDir) {
        // make everything follow the camera
        setLocalTranslation(viewPort.getCamera().getLocation());
        
        moonSystem.updateSunPosition(); // increment by 30 seconds
        updateLightPosition();
        
        moonLight.setDirection(lightDir);
        moon.setLocalTranslation(lightPosition.mult(0.95f));
        
        if(moonDir.y > 0.15 && moonDir.y < 0.9){
            moonLight.setColor(ColorRGBA.White.mult(0f));
        }
        else{
             moonLight.setColor(ColorRGBA.White.mult((-moonDir.y + 0.2f) * 0.35f));
        }
    }
}

