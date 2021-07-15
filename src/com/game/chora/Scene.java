package com.game.chora;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;


public class Scene {
    
    protected Spatial scene;
    protected Spatial floor;
    protected Material matTerrain;
    protected Texture texTerrain;
    
    public Scene(AssetManager assetManager, Node rootNode, BulletAppState bulletAppState) {
        setScene(assetManager, rootNode);
        setFloor(assetManager, rootNode, bulletAppState);
    }
    
    private void setScene(AssetManager assetManager, Node rootNode) {
        this.scene = assetManager.loadModel("Scenes/map.j3o");
        this.scene.setShadowMode(RenderQueue.ShadowMode.Receive);
        rootNode.attachChild(this.scene);
    }
    
    private void setFloor(AssetManager assetManager, Node rootNode, BulletAppState bulletAppState) {
        this.matTerrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        
        this.texTerrain = assetManager.loadTexture("Textures/Terrain/dirt.jpg");
        this.texTerrain.setWrap(Texture.WrapMode.Repeat);
        this.matTerrain.setTexture("Tex1", this.texTerrain);
        
        this.floor = rootNode.getChild("terrain-map");
        this.floor.addControl(new RigidBodyControl(0));
        this.floor.setMaterial(this.matTerrain);
        
        bulletAppState.getPhysicsSpace().addAll(this.floor);
    }
    
}