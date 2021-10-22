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

/**
 * Pound is a class that create and manage the starting pound.
 * This element is the game first source of water.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class Pound {
    
    private SimpleWaterProcessor waterProcessor;
    private Vector3f waterLocation;
    private Geometry water;
    private Vector3f hitboxSize;
    private Box cube;
    private Geometry pickbox;
    private Material matPickBox;
    private boolean DEBUG = false;
    
    /**
     * class constructor with parameters.
     * @param assetManager
     * @param rootNode
     * @param viewPort
     * @param shootables
     * @param hitboxSize
     * @param poundTaken quantity of water already taken from the pound
     */
    public Pound(AssetManager assetManager, Node rootNode, ViewPort viewPort, Node shootables, Vector3f hitboxSize, int poundTaken) {
        this.hitboxSize = hitboxSize;
        this.setWaterProcessor(assetManager, rootNode, viewPort);
        this.setWater(assetManager, rootNode, shootables, poundTaken);
    }
    
    /**
     *
     * @return pound object
     */
    public Geometry getPound() {
        return this.water;
    }
    
    /**
     *
     * @return pound pickbox
     */
    public Geometry getPickBox() {
        return this.pickbox;
    }
    
    /**
     *
     * @return pound location
     */
    public Vector3f getWaterLocation() {
        return this.waterLocation;
    }
    
    /**
     *
     * set pound location.
     * @param waterLocation
     */
    public void setWaterLocation(Vector3f waterLocation) {
        this.waterLocation = waterLocation;
        this.water.setLocalTranslation(waterLocation);
    }
    
    /**
     * set code debug.
     * @param bool
     */
    public void setDebug(boolean bool) {
        this.DEBUG = bool;
    }
        
    /**
     *
     * setup water processor.
     */
    private void setWaterProcessor(AssetManager assetManager, Node rootNode, ViewPort viewPort) {
        this.waterProcessor = new SimpleWaterProcessor(assetManager);
        this.waterProcessor.setReflectionScene(rootNode);
        this.waterProcessor.setDebug(false);
        this.waterProcessor.setRefractionClippingOffset(1.0f);
        viewPort.addProcessor(this.waterProcessor);
        this.waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, new Vector3f(0,-20,0).dot(Vector3f.UNIT_Y)));       
        this.waterProcessor.setWaterColor(ColorRGBA.Brown);
        this.waterProcessor.setDebug(false);
    }
    
    /**
     *
     * setup water object and pickbox.
     */
    private void setWater(AssetManager assetManager, Node rootNode, Node shootables, int poundTaken) {
        Quad quad = new Quad(200, 200);
        
        quad.scaleTextureCoordinates(new Vector2f(6f,6f));

        this.water = new Geometry("water", quad);
        this.water.setShadowMode(RenderQueue.ShadowMode.Receive);
        this.water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        this.water.setMaterial(waterProcessor.getMaterial());
        this.water.scale(0.5f);
        this.setWaterLocation(new Vector3f(150, (-1 - ((float)0.5 * (float)poundTaken)), 350));
        
        this.cube = new Box(this.hitboxSize.x, this.hitboxSize.y, this.hitboxSize.z);
        this.pickbox = new Geometry("PickBox" + this.hashCode(), cube);
        this.pickbox.setShadowMode(RenderQueue.ShadowMode.Off);
        this.pickbox.setLocalTranslation(210, -1, 300);
        
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
    
    /**
     * lower pound level when player takes it.
     */
    public void takeWater() {
        Vector3f tmp = new Vector3f(this.getWaterLocation().x, this.getWaterLocation().y - 0.5f, this.getWaterLocation().z);
        this.setWaterLocation(tmp);
    }
    
    /**
     * spawn pound.
     * @param rootNode
     * @param shootables
     */
    public void spawn(Node rootNode, Node shootables) {
        rootNode.attachChild(this.water);
        shootables.attachChild(this.pickbox);
    }
    
    /**
     * despawn pound.
     * @param rootNode
     * @param shootables
     */
    public void despawn(Node rootNode, Node shootables) {
        rootNode.detachChild(this.water);
        shootables.detachChild(this.pickbox);
    }
}
