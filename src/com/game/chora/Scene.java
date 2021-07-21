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
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;


public class Scene {
    
    protected Spatial scene;
    //protected Spatial floor;
    protected Material matTerrain;
    //protected Texture texTerrain;
    protected TerrainQuad terrain;
    protected Texture AlphaTexture;
    
    public Scene(AssetManager assetManager, Node rootNode) {
        setScene(assetManager, rootNode);
        setFloor(assetManager, rootNode);
    }
    
    public TerrainQuad getTerrain() {
        return this.terrain;
    }
    
    public Texture getAlphaTexture() {
        return this.AlphaTexture;
    }
    
    private void setScene(AssetManager assetManager, Node rootNode) {
        //this.scene = assetManager.loadModel("Scenes/map.j3o");
        //this.scene.setShadowMode(RenderQueue.ShadowMode.Receive);
        //rootNode.attachChild(this.scene);
    }
    
    private void setFloor(AssetManager assetManager, Node rootNode) {
        this.matTerrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        this.matTerrain.setBoolean("useTriPlanarMapping", false);
        this.matTerrain.setBoolean("WardIso", true);
        this.matTerrain.setFloat("Shininess", 0);
        
        this.AlphaTexture = assetManager.loadTexture("Textures/Terrain/alphamap.png");
        this.matTerrain.setTexture("AlphaMap", this.AlphaTexture);
        
        Texture sand = assetManager.loadTexture("Textures/Terrain/aa.jpg");
        sand.setWrap(Texture.WrapMode.Repeat);
        this.matTerrain.setTexture("DiffuseMap", sand);
        this.matTerrain.setFloat("DiffuseMap_0_scale", 16);
        
        Texture grass = assetManager.loadTexture("Textures/Terrain/erba.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        this.matTerrain.setTexture("DiffuseMap_1", grass);
        this.matTerrain.setFloat("DiffuseMap_1_scale", 16);
        
        Texture dirt = assetManager.loadTexture("Textures/Terrain/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        this.matTerrain.setTexture("DiffuseMap_2", dirt);
        this.matTerrain.setFloat("DiffuseMap_2_scale", 16);
        
        Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/heightmap.png");
        AbstractHeightMap heightmap = null;
        try {
            heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.5f);
            heightmap.load();
            heightmap.smooth(0.9f, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // CREATE THE TERRAIN
        terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
        //TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
        //control.setLodCalculator( new DistanceLodCalculator(65, 2.7f) ); // patch size, and a multiplier
        //terrain.addControl(control);
        terrain.setMaterial(matTerrain);
        terrain.setLocalTranslation(0, -63, 0);
        terrain.setLocalScale(2f, 0.5f, 2f);
        rootNode.attachChild(terrain);

        
        //this.floor = rootNode.getChild("terrain-map");
        //this.floor.addControl(new RigidBodyControl(0));
        //this.floor.setMaterial(this.matTerrain);
        
        
        
        //this.setTextureScale(floor, new Vector2f(16, 16));
        
       
        //bulletAppState.getPhysicsSpace().addAll(this.floor);
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
