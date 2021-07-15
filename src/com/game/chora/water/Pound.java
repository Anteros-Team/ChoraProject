package com.game.chora.water;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.water.SimpleWaterProcessor;


public class Pound {
    
    protected SimpleWaterProcessor waterProcessor;
    protected Vector3f waterLocation;
    protected Geometry water;
    
    public Pound(AssetManager assetManager, Node rootNode, ViewPort viewPort) {
        this.setWaterProcessor(assetManager, rootNode, viewPort);
        this.setWaterLocation();
        this.setWater(rootNode);
    }
    
    public Geometry getPound() {
        return this.water;
    }
    
    private void setWaterProcessor(AssetManager assetManager, Node rootNode, ViewPort viewPort) {
        this.waterProcessor = new SimpleWaterProcessor(assetManager);
        this.waterProcessor.setReflectionScene(rootNode);
        this.waterProcessor.setDebug(false);
        //this.waterProcessor.setLightPosition(sky.getSunDirection());
        this.waterProcessor.setRefractionClippingOffset(1.0f);
        viewPort.addProcessor(this.waterProcessor);
    }
    
    private void setWaterLocation() {
        this.waterLocation = new Vector3f(0,-20,0);
        this.waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, this.waterLocation.dot(Vector3f.UNIT_Y)));       
        this.waterProcessor.setWaterColor(ColorRGBA.Brown);
        this.waterProcessor.setDebug(false);
    }
    
    private void setWater(Node rootNode) {
        Quad quad = new Quad(400,400);
        
        quad.scaleTextureCoordinates(new Vector2f(6f,6f));

        this.water = new Geometry("water", quad);
        this.water.setShadowMode(RenderQueue.ShadowMode.Receive);
        this.water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        this.water.setMaterial(waterProcessor.getMaterial());
        this.water.scale(0.5f);
        this.water.setLocalTranslation(150, -10, 300);
       
        rootNode.attachChild(water);
    }
}
