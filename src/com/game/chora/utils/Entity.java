package com.game.chora.utils;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


public class Entity {
    
    static int id = 1000;
    protected Vector3f position;
    protected float scale;
    protected Vector3f hitboxSize;
    protected Spatial entity;
    //protected Material mat;
    protected BoxCollisionShape shape;
    protected CompoundCollisionShape hitbox;
    protected RigidBodyControl rbc;
    
    public Entity(Vector3f position, float scale, Vector3f hitboxSize) {
        this.id = id++;
        this.position = position;
        this.scale = scale;
        this.hitboxSize = hitboxSize;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Vector3f getPosition() {
        return this.position;
    }
    
    public float getScale() {
        return this.scale;
    }
    
    public Vector3f getHitboxSize() {
        return this.hitboxSize;
    }
    
    public Spatial getModel() {
        return this.entity;
    }
    
    public void setModel(AssetManager assetManager, Node rootNode, String pathModel) {
        this.entity = assetManager.loadModel(pathModel);
        this.entity.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        this.entity.move(this.position);
        this.entity.scale(this.scale);
        
        this.shape = new BoxCollisionShape(this.hitboxSize);
        this.hitbox = new CompoundCollisionShape();
        this.hitbox.addChildShape(shape, new Vector3f(shape.getScale().x, shape.getHalfExtents().y, shape.getScale().z));
    }
    
    /*
    public void setMaterial(AssetManager assetManager, String pathMaterial) {
        this.mat = new Material(assetManager, pathMaterial);
        this.entity.setMaterial(mat);
    }
    */
    
    public void setPhysics(BulletAppState bulletAppState) {
        this.rbc = new RigidBodyControl(this.hitbox);
        this.entity.addControl(rbc);
        
        bulletAppState.getPhysicsSpace().add(this.entity);
        bulletAppState.getPhysicsSpace().add(this.rbc);
    }
    
    public void Spawn(Node rootNode) {
        rootNode.attachChild(this.entity);
    }
}