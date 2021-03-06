package com.game.chora.items.sky;

import com.game.chora.utils.SkyBillboardItem;
import com.game.chora.utils.SunSystem;
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
 * DynamicSun is a class that manages model, light and position
 * of the sun.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class DynamicSun extends Node {
    
    private static final Sphere sphereMesh = new Sphere(40, 40, 900, false, true);
    
    private ViewPort viewPort = null;
    private AssetManager assetManager = null;
    
    private SunSystem sunSystem = new SunSystem();
    private SkyBillboardItem sun;
    
    private DirectionalLight sunLight = null;
    private Vector3f lightDir = sunSystem.getPosition();
    private Vector3f lightPosition = new Vector3f();
    
    private float scaling = 900;
    private boolean DEBUG = false;
    
    /**
     * class constructor with parameters.
     * @param assetManager
     * @param viewPort
     * @param rootNode
     * @param scaling
     */
    public DynamicSun(AssetManager assetManager, ViewPort viewPort, Node rootNode, Float scaling) {
        this.assetManager = assetManager;
        this.viewPort = viewPort;
        this.scaling = scaling;
        
        sunLight = new DirectionalLight();
        sunLight.setName("Sun");
        sunLight.setDirection(lightDir);
        sunLight.setColor(ColorRGBA.White);
        rootNode.addLight(sunLight);
                
        sunSystem.setSiteLatitude(89.32f);
        sunSystem.setSiteLongitude(6.38f);
        updateLightPosition();
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/sky/sun.png"));
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.getAdditionalRenderState().setDepthWrite(false);
        
        sun = new SkyBillboardItem("sun", 170f);
        sun.setMaterial(mat);
        sun.setShadowMode(shadowMode.Off);
        attachChild(sun);
        
        setQueueBucket(Bucket.Sky);
        setCullHint(CullHint.Never);
    }
    
    /**
     *
     * @return sun rotation class
     */
    public SunSystem getSunSystem(){
        return sunSystem;
    }
    
    /**
     *
     * @return sun light object
     */
    public DirectionalLight getSunLight(){
        return this.sunLight;
    }
        
    /**
     * update sun light position and direction.
     */
    protected void updateLightPosition(){
        lightDir = sunSystem.getDirection();
        lightPosition = sunSystem.getPosition();
        sunLight.setDirection(lightDir);
    }
    
    /**
     *
     * @return sun position
     */
    public Vector3f getSunDirection(){
        return sunSystem.getPosition();
    }

    /**
     * update sun position and speed, also turn off
     * the light at night.
     * @param sunDir
     */
    public void updateTime(Vector3f sunDir) {
        // make everything follow the camera
        setLocalTranslation(viewPort.getCamera().getLocation());
        
        sunSystem.updateSunPosition(); // increment by 30 seconds
        updateLightPosition();
        
               
        sunLight.setDirection(lightDir);
        sun.setLocalTranslation(lightPosition.mult(0.9f));
        
        if (this.DEBUG == true) {
            System.out.println("sunDir: " + sunDir);
        }
        if(sunDir.y > 0.15 && sunDir.y < 0.95){
            sunLight.setColor(ColorRGBA.White.mult(0f));
        }
        else{
             sunLight.setColor(ColorRGBA.White.mult((-sunDir.y + 0.2f) * 0.9f));
        }
    }
}
