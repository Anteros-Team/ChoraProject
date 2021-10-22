package com.game.chora.utils;

import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;

/**
 * SkyBillboardItem is a class that display sun, moon and stars textures.
 * This also rotate texture based on camera.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class SkyBillboardItem extends Geometry{
    private BillboardControl billBoardControl = new BillboardControl();
    private Quad quad;
    
    /**
     * class constructor with parameters.
     * @param name geometry name
     * @param scale geometry scale
     */
    public SkyBillboardItem(String name, Float scale){
        super(name);
        
        quad = new Quad(scale, scale);
        
        setQueueBucket(Bucket.Transparent);
        setCullHint(CullHint.Never);
        
        setMesh(quad);
        
        addControl(billBoardControl);
    }
    
    /**
     * set geometry rotation.
     * @param rotation
     */
    public void setRotation(Float rotation){
        setRotationEnabled();
        this.rotate(new Quaternion().fromAngles(0, 0, rotation));
    }
    
    /**
     * remove billboard controller.
     */
    public void removeBillboardController(){
        removeControl(billBoardControl);
    }
    
    /**
     * enable rotation.
     */
    protected void setRotationEnabled(){
        billBoardControl.setAlignment(BillboardControl.Alignment.AxialZ);
    }
    
    /**
     * disable rotation.
     */
    protected void setRotationDisabled(){
        billBoardControl.setAlignment(BillboardControl.Alignment.Screen);
    }
}
