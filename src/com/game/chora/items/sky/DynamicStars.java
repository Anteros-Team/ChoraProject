package com.game.chora.items.sky;

import com.game.chora.utils.SkyBillboardItem;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;

/**
 * DynamicStars is a class that manage stars.
 * Stars spawn with random position, rotation and scale, then
 * they rotate on a sphere around the map.
 * Stars are not visible during daytime.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class DynamicStars extends Node{
    private ViewPort viewPort = null;
    private AssetManager assetManager = null;
    
    private SkyBillboardItem[] stars;
    
    private int stars_count = 1000;
    private Material mat;
    
    /**
     * class constructor with parameters.
     * @param assetManager
     * @param viewPort
     * @param scaling
     */
    public DynamicStars(AssetManager assetManager, ViewPort viewPort, Float scaling){
        this.assetManager = assetManager;
        this.viewPort = viewPort;
        
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/sky/star.png"));
        mat.getAdditionalRenderState().setDepthWrite(false);
        mat.setColor("Color", new ColorRGBA(1f,1f,1f, 0.4f));
        mat.getAdditionalRenderState().setBlendMode(BlendMode.AlphaAdditive);
        stars = new SkyBillboardItem[stars_count];                
        for(int i = 0; i < stars_count; i++){
            SkyBillboardItem item = new SkyBillboardItem("star_" + i, (float) Math.random() * scaling/5);
            stars[i] = item;
            item.setShadowMode(shadowMode.Off);
            
            item.setMaterial(mat);
            item.setLocalTranslation(getPointOnSphere().mult(scaling - 30f));
            item.removeBillboardController();
            item.lookAt(getRandomVector().mult(10), Vector3f.UNIT_Y);
            item.rotate(new Quaternion().fromAngles((float) Math.random() - 0.5f, (float) Math.random() - 0.5f, (float) Math.random() - 0.5f));
            attachChild(item);
        }
        
        setQueueBucket(Bucket.Sky);
        setCullHint(CullHint.Never);
    }
    
    /**
     * check time based on sun position, then hide stars
     * if it's daytime.
     * @param sunDir
     */
    public void update(Vector3f sunDir){
        mat.setColor("Color", new ColorRGBA(1f,1f,1f, 0));
        if(sunDir.y > 0 && sunDir.y < 1){
            mat.setColor("Color", new ColorRGBA(1f,1f,1f, 0.9f));
        }
    }
    
    /**
     *
     * @return random 3 float vector
     */
    protected Vector3f getRandomVector(){
        return new Vector3f(
                (float) Math.random() - 0.5f,
                (float) Math.random() - 0.5f,
                (float) Math.random() - 0.5f
        );
    }
    
    /**
     *
     * @return random point on a sphere
     */
    protected Vector3f getPointOnSphere(){
        Float x = (float) Math.random() - 0.5f;
        Float y = (float) Math.random() - 0.5f;
        Float z = (float) Math.random() - 0.5f;
        Float k = FastMath.sqrt(x * x + y * y + z * z);
        while(k < 0.2 || k > 0.3){
            x = (float) Math.random() - 0.5f;
            y = (float) Math.random() - 0.5f;
            z = (float) Math.random() - 0.5f;
            k = FastMath.sqrt(x * x + y * y + z * z);
        }
        
        return new Vector3f(
                x / k,
                y / k,
                z / k
        );
    }
}
