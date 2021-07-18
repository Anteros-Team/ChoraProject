package com.game.chora.water;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
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
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.water.SimpleWaterProcessor;


public class Pound {
    
    protected SimpleWaterProcessor waterProcessor;
    protected Vector3f waterLocation;
    protected Geometry water;
    protected Vector3f hitboxSize;
    protected Box cube;
    protected Geometry pickbox;
    protected Material matPickBox;
    protected boolean DEBUG = false;
    
    public Pound(AssetManager assetManager, Node rootNode, ViewPort viewPort, Node shootables, Vector3f hitboxSize) {
        this.hitboxSize = hitboxSize;
        this.setWaterProcessor(assetManager, rootNode, viewPort);
        this.setWater(assetManager, rootNode, shootables);
    }
    
    public Geometry getPound() {
        return this.water;
    }
    
    public Geometry getPickBox() {
        return this.pickbox;
    }
    
    public Vector3f getWaterLocation() {
        return this.waterLocation;
    }
    
    public void setWaterLocation(Vector3f waterLocation) {
        this.waterLocation = waterLocation;
        this.water.setLocalTranslation(waterLocation);
    }
    
    public void setDebug(boolean bool) {
        this.DEBUG = bool;
    }
        
    private void setWaterProcessor(AssetManager assetManager, Node rootNode, ViewPort viewPort) {
        this.waterProcessor = new SimpleWaterProcessor(assetManager);
        this.waterProcessor.setReflectionScene(rootNode);
        this.waterProcessor.setDebug(false);
        //this.waterProcessor.setLightPosition(sky.getSunDirection());
        this.waterProcessor.setRefractionClippingOffset(1.0f);
        viewPort.addProcessor(this.waterProcessor);
        this.waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, new Vector3f(0,-20,0).dot(Vector3f.UNIT_Y)));       
        this.waterProcessor.setWaterColor(ColorRGBA.Brown);
        this.waterProcessor.setDebug(false);
    }
    
    private void setWater(AssetManager assetManager, Node rootNode, Node shootables) {
        Quad quad = new Quad(400,400);
        
        quad.scaleTextureCoordinates(new Vector2f(6f,6f));

        this.water = new Geometry("water", quad);
        this.water.setShadowMode(RenderQueue.ShadowMode.Receive);
        this.water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        this.water.setMaterial(waterProcessor.getMaterial());
        this.water.scale(0.5f);
        this.setWaterLocation(new Vector3f(150, -10, 300));
        
        this.cube = new Box(this.hitboxSize.x, this.hitboxSize.y, this.hitboxSize.z);
        this.pickbox = new Geometry("PickBox" + this.hashCode(), cube);
        this.pickbox.setShadowMode(RenderQueue.ShadowMode.Off);
        this.pickbox.setLocalTranslation(230, -8, 235);
        
        this.matPickBox = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        this.matPickBox.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        
        if (this.DEBUG == true) {
            this.matPickBox.setColor("Color", new ColorRGBA(0, 1, 0, 0.5f));
        } else {
            this.pickbox.setCullHint(Spatial.CullHint.Always);
            this.pickbox.setQueueBucket(RenderQueue.Bucket.Transparent);
        }
        
        this.pickbox.setMaterial(this.matPickBox);
        this.spawn(rootNode, shootables);
    }
    
    public void takeWater() {
        Vector3f tmp = new Vector3f(this.getWaterLocation().x, this.getWaterLocation().y - 1, this.getWaterLocation().z);
        this.setWaterLocation(tmp);
    }
    
    public void spawn(Node rootNode, Node shootables) {
        rootNode.attachChild(this.water);
        shootables.attachChild(this.pickbox);
    }
    
    public void despawn(Node rootNode, Node shootables) {
        rootNode.detachChild(this.water);
        shootables.detachChild(this.pickbox);
    }
}
