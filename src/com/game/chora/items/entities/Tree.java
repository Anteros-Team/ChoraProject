package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
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
import java.util.Timer;


public class Tree extends Entity {
    
    protected List<Apple> apples;
    protected float time;
    
    public Tree(Vector3f position, float scale, Vector3f hitboxSize) {
        super(position, scale, hitboxSize);
        this.apples = new ArrayList<>();
        this.time = 0;
    }
    
    public List<Apple> getApples() {
        return this.apples;
    }
    
    @Override
    public void setModel(AssetManager assetManager, Node rootNode, String pathModel, Node shootables) {
        this.entity = assetManager.loadModel(pathModel);
        this.entity.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        this.entity.move(this.position);
        this.entity.scale(this.scale);
        
        this.cube = new ArrayList<>();
        this.cube.add(new Box(this.pickboxSize.x / 8, this.pickboxSize.y, this.pickboxSize.z / 8)); // truck
        this.cube.add(new Box(this.pickboxSize.x, this.pickboxSize.y / 1.5f, this.pickboxSize.z)); // leaves
        
        this.pickbox = new ArrayList<>();
        this.pickbox.add(new Geometry("PickBox0" + this.hashCode(), cube.get(0)));
        this.pickbox.add(new Geometry("PickBox1" + this.hashCode(), cube.get(1)));
        
        this.pickbox.get(0).setShadowMode(RenderQueue.ShadowMode.Off);
        this.pickbox.get(0).setLocalTranslation(this.position.add(new Vector3f(-10, this.pickboxSize.y, -10)));
        //this.pickbox.get(0).setCullHint(Spatial.CullHint.Always);
        
        this.pickbox.get(1).setShadowMode(RenderQueue.ShadowMode.Off);
        this.pickbox.get(1).setLocalTranslation(this.position.add(new Vector3f(0, this.pickboxSize.y * 2f, 0)));
        
        this.matPickBox = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        this.matPickBox.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        this.matPickBox.setColor("Color", new ColorRGBA(0, 1, 0, 0.2f));
        this.pickbox.get(0).setQueueBucket(RenderQueue.Bucket.Transparent);
        this.pickbox.get(1).setQueueBucket(RenderQueue.Bucket.Transparent);
        this.pickbox.get(0).setMaterial(matPickBox);
        this.pickbox.get(1).setMaterial(matPickBox);
    }
    
    @Override
    public void spawn(Node rootNode, Node shootables) {
        rootNode.attachChild(this.entity);
        shootables.attachChild(this.pickbox.get(0));
        shootables.attachChild(this.pickbox.get(1));
    }
    
    @Override
    public void despawn(Node rootNode, Node shootables) {
        rootNode.detachChild(this.entity);
        shootables.detachChild(this.pickbox.get(0));
        shootables.detachChild(this.pickbox.get(1));
    }
    
    public float getTime() {
        return this.time;
    }
    
    public void increaseTime(float tpf) {
        this.time += tpf;
        //System.out.println(this.time);
    }
    
    public void resetTime() {
        this.time = 0;
    }
    
    public void newApple(AssetManager assetManager, Node rootNode, Node shootables) {
        Apple a = new Apple(this.getPosition(), 11, new Vector3f(5, 5, 5));
        a.setModel(assetManager, rootNode, "Models/apple01/apple01.j3o", shootables);

        Vector3f reset = new Vector3f(-this.getPickboxSize().x, 0, -this.getPickboxSize().z);
        a.setPosition(a.getPosition().add(reset));
        
        float x = (float) Math.random();
        float z = (float) Math.random();
        
        
        Vector3f offset = new Vector3f(x * this.getPickboxSize().x * 2, this.getPickboxSize().y / 1.2f, z * this.getPickboxSize().z * 2);
        
        a.setPosition(a.getPosition().add(offset));
        a.spawn(rootNode, shootables);
        this.apples.add(a);
    }
    
}
