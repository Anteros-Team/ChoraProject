package com.game.chora;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;
import com.jme3.terrain.geomipmap.TerrainQuad;
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
        
        this.setTextureScale(floor, new Vector2f(16, 16));
        
       
        bulletAppState.getPhysicsSpace().addAll(this.floor);
    }
    
    private void setTextureScale(Spatial spatial, Vector2f vector) {
        if (spatial instanceof Node) {
            Node findingnode = (Node) spatial;

            for (int i = 0; i < findingnode.getQuantity(); i++) {
                Spatial child = findingnode.getChild(i);
                setTextureScale(child, vector);
            }
        } else if (spatial instanceof Geometry) {
            ((Geometry) spatial).getMesh().scaleTextureCoordinates(vector);
        }
    } 
}
