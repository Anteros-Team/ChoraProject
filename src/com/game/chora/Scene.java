package com.game.chora;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.image.ImageRaster;


public class Scene {
    
    protected Spatial scene;
    protected Material matTerrain;
    protected TerrainQuad terrain;
    protected Texture AlphaTexture;
    protected ImageRaster imageRaster;
    protected boolean[][] placeableArea;
    protected boolean[][] coveredArea;
    
    public Scene(AssetManager assetManager, Node rootNode, Node shootables) {
        this.placeableArea = new boolean[1024][1024];
        this.coveredArea = new boolean[1024][1024];
        setFloor(assetManager, rootNode, shootables);
    }
    
    public TerrainQuad getTerrain() {
        return this.terrain;
    }
    
    public Texture getAlphaTexture() {
        return this.AlphaTexture;
    }
    
    public boolean[][] getPlaceableArea() {
        return this.placeableArea;
    }
    
    public boolean[][] getCoveredArea() {
        return this.coveredArea;
    }
    
    private void setFloor(AssetManager assetManager, Node rootNode, Node shootables) {
        this.matTerrain = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
        this.matTerrain.setBoolean("useTriPlanarMapping", false);
        this.matTerrain.setBoolean("WardIso", true);
        this.matTerrain.setFloat("Shininess", 10);
        
        this.AlphaTexture = assetManager.loadTexture("Textures/Terrain/alphamap.png");
        this.matTerrain.setTexture("AlphaMap", this.AlphaTexture);
        
        Texture dirt = assetManager.loadTexture("Textures/Terrain/dirt.png");
        dirt.setWrap(Texture.WrapMode.Repeat);
        this.matTerrain.setTexture("DiffuseMap", dirt);
        this.matTerrain.setFloat("DiffuseMap_0_scale", 16);
        
        Texture grass = assetManager.loadTexture("Textures/Terrain/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        this.matTerrain.setTexture("DiffuseMap_1", grass);
        this.matTerrain.setFloat("DiffuseMap_1_scale", 16);
        
        Texture sand = assetManager.loadTexture("Textures/Terrain/sand.jpg");
        sand.setWrap(Texture.WrapMode.Repeat);
        this.matTerrain.setTexture("DiffuseMap_2", sand);
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

        terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
        terrain.setMaterial(matTerrain);
        terrain.setLocalTranslation(0, -23, 0);
        terrain.setLocalScale(2f, 0.5f, 2f);
        rootNode.attachChild(terrain);
        shootables.attachChild(terrain);
        
        this.imageRaster = ImageRaster.create(this.getAlphaTexture().getImage());
        for(int i = 0; i < 1024; i++) {
            for (int j = 0; j < 1024; j++) {
                this.coveredArea[i][j] = false;
                if (this.imageRaster.getPixel(i, j).r > 0.7) {
                    this.placeableArea[i][j] = true;
                } else {
                    this.placeableArea[i][j] = false;
                }
            }
        }        
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
