package com.game.chora.items;

import com.game.chora.utils.SkyBillboardItem;
import com.game.chora.utils.LunarSystem;
import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import java.util.Date;

public class DynamicMoon extends Node {
    private static final Sphere sphereMesh = new Sphere(40, 40, 900, false, true);
    
    private ViewPort viewPort = null;
    private AssetManager assetManager = null;
    
    private LunarSystem moonSystem = new LunarSystem(new Date(), 0, 0, 0);
    private SkyBillboardItem moon;
    
    private DirectionalLight moonLight = null;
    private Vector3f lightDir = moonSystem.getPosition();
    private Vector3f lightPosition = new Vector3f();
    
    private float scaling = 1200;
    
    public DynamicMoon(AssetManager assetManager, ViewPort viewPort, Node rootNode, Float scaling) {
        this.assetManager = assetManager;
        this.viewPort = viewPort;
        this.scaling = scaling;
        
        moonLight = getSunLight();
        rootNode.addLight(moonLight);
                
        moonSystem.setSiteLatitude(46.32f);
        moonSystem.setSiteLongitude(6.38f);
        updateLightPosition();
        
              
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/sky/moon.png"));
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        mat.getAdditionalRenderState().setDepthWrite(false);
                
        moon = new SkyBillboardItem("moon", 120f);
        moon.setMaterial(mat);
        attachChild(moon);
        
        setQueueBucket(Bucket.Sky);
        setCullHint(CullHint.Never);
        
    }
    
    public LunarSystem getSunSystem(){
        return moonSystem;
    }
    
    protected DirectionalLight getSunLight(){
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(lightDir);
        dl.setColor(ColorRGBA.White.mult(0.5f));
        return dl;
    }
        
    protected void updateLightPosition(){
        lightDir = moonSystem.getDirection();
        lightPosition = moonSystem.getPosition();
    }
    
    public Vector3f getSunDirection(){
        return moonSystem.getPosition();
    }

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

