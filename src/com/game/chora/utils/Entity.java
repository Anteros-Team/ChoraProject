package com.game.chora.utils;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Entity {
    
    protected Vector3f position;
    protected float scale;
    protected Vector3f pickboxSize;
    protected Spatial entity;
    //protected Material mat;
    protected List<Box> cube;
    protected List<Geometry> pickbox;
    protected Material matPickBox;
    
    public Entity() {
        this.position = new Vector3f(0, 0, 0);
        this.scale = 1;
        this.pickboxSize = new Vector3f(0, 0, 0);
    }
    
    public Entity(Vector3f position, float scale, Vector3f pickboxSize) {
        this.position = position;
        this.scale = scale;
        this.pickboxSize = pickboxSize;
    }
    
    public Vector3f getPosition() {
        return this.position;
    }
    
    public void setPosition(Vector3f position) { 
        this.position = position;
    }
    
    public float getScale() {
        return this.scale;
    }
    
    public void setScale(Float scale) {
        this.scale = scale;
    }
    
    public Vector3f getPickboxSize() {
        return this.pickboxSize;
    }
    
    public void setPickBoxSize(Vector3f pickboxSize) {
        this.pickboxSize = pickboxSize;
    }
    
    public Spatial getEntity() {
        return this.entity;
    }
    
    public List<Geometry> getPickBox() {
        return this.pickbox;
    }
    
    public void modifyPosition(Vector3f position) {
        this.position = position;
        this.entity.setLocalTranslation(this.position);
        this.pickbox.get(0).setLocalTranslation(position.add(new Vector3f(0, this.pickboxSize.y, 0)));
    }
    
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
        // no visible pickbox
        //this.pickbox.get(0).setCullHint(Spatial.CullHint.Always);
        
        this.matPickBox = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        this.matPickBox.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        this.pickbox.get(0).setQueueBucket(RenderQueue.Bucket.Transparent);
        this.matPickBox.setColor("Color", new ColorRGBA(0, 1, 0, 0.2f));  
        pickbox.get(0).setMaterial(matPickBox);
    }
    
    /*
    public void setMaterial(AssetManager assetManager, String pathMaterial) {
        this.mat = new Material(assetManager, pathMaterial);
        this.entity.setMaterial(mat);
    }
    */
    
    public void onAction(Node rootNode, Node shootables) {
        
    }
    
    public void spawn(Node rootNode, Node shootables) {
        rootNode.attachChild(this.entity);
        shootables.attachChild(this.pickbox.get(0));
    }
    
    public void despawn(Node rootNode, Node shootables) {
        rootNode.detachChild(this.entity);
        shootables.detachChild(this.pickbox.get(0));
    }
    
    @Override 
    public String toString() {
        return new StringBuffer(" Position: ").append(this.position).append(" Scale: ").append(this.scale).append(" PickBoxSize: ").append(this.pickboxSize).toString();
    }
}
