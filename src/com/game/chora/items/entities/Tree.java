package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Tree is an extention of Entity.
 * As Tree objects spawn, grass appears all
 * around them.
 * <p>
 * Trees only purpose is to produce apples.
 * Apples are always produced, based on a specific timer.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class Tree extends Entity implements Serializable {
    
    private List<Apple> apples;
    private float time;
    
    /**
     * class constructor with parameters
     * @param position location in the 3D space
     * @param scale model size 
     * @param hitboxSize size of the clickable area
     */
    public Tree(Vector3f position, float scale, Vector3f hitboxSize) {
        super(position, scale, hitboxSize);
        this.apples = new ArrayList<>();
        this.time = 0;
    }
    
    /**
     * 
     * @return list of dropped apples
     */
    public List<Apple> getApples() {
        return this.apples;
    }
    
    /**
     * load models into the game
     * @param assetManager
     * @param rootNode
     * @param pathModel
     * @param shootables
     */
    @Override
    public void setModel(AssetManager assetManager, Node rootNode, String pathModel, Node shootables) {
        this.entity = assetManager.loadModel(pathModel);
        this.entity.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        this.entity.move(this.getPosition());
        this.entity.scale(this.getScale());
        
        this.cube = new ArrayList<>();
        this.cube.add(new Box(this.pickboxSize.x / 15, this.pickboxSize.y / 1.5f, this.pickboxSize.z / 15)); // truck
        this.cube.add(new Box(this.pickboxSize.x / 3f, this.pickboxSize.y / 4f, this.pickboxSize.z / 3f)); // leaves
        
        this.pickbox = new ArrayList<>();
        this.pickbox.add(new Geometry("PickBox0" + this.hashCode(), cube.get(0)));
        this.pickbox.add(new Geometry("PickBox1" + this.hashCode(), cube.get(1)));
        
        this.pickbox.get(0).setShadowMode(RenderQueue.ShadowMode.Off);
        this.pickbox.get(0).setLocalTranslation(this.getPosition().add(new Vector3f(0, this.pickboxSize.y, 0)));
        this.pickbox.get(0).setCullHint(Spatial.CullHint.Always); // no visible pickbox
        
        this.pickbox.get(1).setShadowMode(RenderQueue.ShadowMode.Off);
        this.pickbox.get(1).setLocalTranslation(this.getPosition().add(new Vector3f(0, this.pickboxSize.y * 1.8f, 0)));
        this.pickbox.get(1).setCullHint(Spatial.CullHint.Always); // no visible pickbox
        
        this.matPickBox = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        this.matPickBox.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        this.matPickBox.setColor("Color", new ColorRGBA(0, 1, 0, 0.2f));
        this.pickbox.get(0).setQueueBucket(RenderQueue.Bucket.Transparent);
        this.pickbox.get(1).setQueueBucket(RenderQueue.Bucket.Transparent);
        this.pickbox.get(0).setMaterial(matPickBox);
        this.pickbox.get(1).setMaterial(matPickBox);
    }
    
    /**
     * spawn the object on the map
     * @param rootNode
     * @param shootables
     */
    @Override
    public void spawn(Node rootNode, Node shootables) {
        rootNode.attachChild(this.entity);
        shootables.attachChild(this.pickbox.get(0));
        shootables.attachChild(this.pickbox.get(1));
    }
    
    /**
     * despawn the object
     * @param rootNode
     * @param shootables
     */
    @Override
    public void despawn(Node rootNode, Node shootables) {
        rootNode.detachChild(this.entity);
        shootables.detachChild(this.pickbox.get(0));
        shootables.detachChild(this.pickbox.get(1));
    }
    
    /**
     *
     * @return time remainig
     */
    public float getTime() {
        return this.time;
    }
    
    /**
     * update the time remaining.
     * @param tpf time per frame
     */
    public void increaseTime(float tpf) {
        this.time += tpf;
    }
    
    /**
     * reset time to 0.
     */
    public void resetTime() {
        this.time = 0;
    }
    
    /**
     * spawn an apple as the timer ends.
     * @param assetManager
     * @param rootNode
     * @param shootables
     */
    public void newApple(AssetManager assetManager, Node rootNode, Node shootables) {
        Apple a = new Apple(this.getPosition(), 11, new Vector3f(5, 5, 5));
        a.setModel(assetManager, rootNode, "Models/apple01/apple01.j3o", shootables);

        Vector3f reset = new Vector3f(-this.getPickboxSize().x, 0, -this.getPickboxSize().z);
        a.modifyPosition(a.getPosition().add(reset));
        
        Vector2f truckMin = new Vector2f(), truckMax = new Vector2f();
        truckMin.x = this.getPosition().x - this.pickboxSize.x / 8;
        truckMin.y = this.getPosition().z - this.pickboxSize.z / 8;
        truckMax.x = this.getPosition().x + this.pickboxSize.x / 8;
        truckMax.y = this.getPosition().z + this.pickboxSize.z / 8;
        
        float x, z;
        
        do {
            x = (float) Math.random();
            z = (float) Math.random();
        } while (x > truckMin.x - 5 && x < truckMax.x + 5 && z > truckMin.y - 5 && z < truckMax.y + 5);
        
        
        
        Vector3f offset = new Vector3f(x * this.getPickboxSize().x * 2, this.getPickboxSize().y / 1.2f, z * this.getPickboxSize().z * 2);
        
        a.modifyPosition(a.getPosition().add(offset));
        a.spawn(rootNode, shootables);
        this.apples.add(a);
    }
    
    /**
     *
     * @return string version of the object
     */
    @Override 
    public String toString() {
        return new StringBuffer(" Position: ").append(this.getPosition().toString()).append(" Scale: ").append(this.getScale()).append(" PickBoxSize: ").append(this.getPickboxSize().toString()).append(" Model: Models/tree/tree.j3o").toString();
    }
    
}
