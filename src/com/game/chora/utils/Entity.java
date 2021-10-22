package com.game.chora.utils;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity is a base class for every entity of the game.
 * This class defines common elements of all entities:
 * <ul>
 * <li> Spatial object associated with the 3D model
 * <li> Boxes that create the PickBox
 * <li> Material definition
 * <li> Position vector
 * <li> Scale of the model
 * </ul>
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class Entity {
    
    protected Spatial entity;
    protected Vector3f pickboxSize;
    protected List<Box> cube;
    protected List<Geometry> pickbox;
    protected Material matPickBox;
    private Vector3f position;
    private float scale;
    
    /**
     * class constructor.
     */
    public Entity() {
        this.position = new Vector3f(0, 0, 0);
        this.scale = 1;
        this.pickboxSize = new Vector3f(0, 0, 0);
    }
    
    /**
     * class constructor with parameters.
     * @param position location in the 3D space
     * @param scale model size 
     * @param pickboxSize size of the clickable area
     */
    public Entity(Vector3f position, float scale, Vector3f pickboxSize) {
        this.position = position;
        this.scale = scale;
        this.pickboxSize = pickboxSize;
    }
    
    /**
     *
     * @return 3D position vector
     */
    public Vector3f getPosition() {
        return this.position;
    }
    
    /**
     * 
     * set entity position.
     * @param position 
     */
    public void setPosition(Vector3f position) { 
        this.position = position;
    }
    
    /**
     *
     * @return entity scale
     */
    public float getScale() {
        return this.scale;
    }
    
    /**
     * set entity scale.
     * @param scale
     */
    public void setScale(Float scale) {
        this.scale = scale;
    }
    
    /**
     * 
     * @return entity pickbox size
     */
    public Vector3f getPickboxSize() {
        return this.pickboxSize;
    }
    
    /**
     * set entity pickbox size.
     * @param pickboxSize
     */
    public void setPickBoxSize(Vector3f pickboxSize) {
        this.pickboxSize = pickboxSize;
    }
    
    /**
     *
     * @return entity object
     */
    public Spatial getEntity() {
        return this.entity;
    }
    
    /**
     *
     * @return list of the pickbox boxes
     */
    public List<Geometry> getPickBox() {
        return this.pickbox;
    }
    
    /**
     * change entity and pickbox position.
     * @param position 
     */
    public void modifyPosition(Vector3f position) {
        this.position = position;
        this.entity.setLocalTranslation(this.position);
        this.pickbox.get(0).setLocalTranslation(position.add(new Vector3f(0, this.pickboxSize.y, 0)));
    }
    
    /**
     * setup entity model, material and pickbox.
     * @param assetManager
     * @param rootNode
     * @param pathModel
     * @param shootables
     */
    public void setModel(AssetManager assetManager, Node rootNode, String pathModel, Node shootables) {
        this.entity = assetManager.loadModel(pathModel);
        this.entity.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        this.entity.move(this.position);
        this.entity.rotate(0, 90f, 0);
        this.entity.scale(this.scale);
        
        this.cube = new ArrayList<>();
        this.cube.add(new Box(this.pickboxSize.x, this.pickboxSize.y, this.pickboxSize.z));
        this.pickbox = new ArrayList<>();
        this.pickbox.add(new Geometry("PickBox" + this.hashCode(), cube.get(0)));
        this.pickbox.get(0).setShadowMode(RenderQueue.ShadowMode.Off);
        this.pickbox.get(0).setLocalTranslation(this.position.add(new Vector3f(0, this.pickboxSize.y, 0)));
        this.pickbox.get(0).setCullHint(Spatial.CullHint.Always); // no visible pickbox
        
        this.matPickBox = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        this.matPickBox.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        this.pickbox.get(0).setQueueBucket(RenderQueue.Bucket.Transparent);
        this.matPickBox.setColor("Color", new ColorRGBA(0, 1, 0, 0.2f));  
        pickbox.get(0).setMaterial(matPickBox);
    }
    
    /**
     * complete an action on click.
     * @param rootNode
     * @param shootables
     */
    public void onAction(Node rootNode, Node shootables) {
        
    }
    
    /**
     * spawn the entity.
     * @param rootNode
     * @param shootables
     */
    public void spawn(Node rootNode, Node shootables) {
        rootNode.attachChild(this.entity);
        shootables.attachChild(this.pickbox.get(0));
    }
    
    /**
     * despawn the entity.
     * @param rootNode
     * @param shootables
     */
    public void despawn(Node rootNode, Node shootables) {
        rootNode.detachChild(this.entity);
        shootables.detachChild(this.pickbox.get(0));
    }
    
    /**
     *
     * @return string version of object
     */
    @Override 
    public String toString() {
        return new StringBuffer(" Position: ").append(this.position).append(" Scale: ").append(this.scale).append(" PickBoxSize: ").append(this.pickboxSize).toString();
    }
}
