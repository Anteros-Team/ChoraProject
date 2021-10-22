package com.game.chora.utils;

import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;


public class SkyBillboardItem extends Geometry{
    private BillboardControl billBoardControl = new BillboardControl();
    private Quad quad;
    
    public SkyBillboardItem(String name, Float scale){
        super(name);
        
        quad = new Quad(scale, scale);
        
        setQueueBucket(Bucket.Transparent);
        setCullHint(CullHint.Never);
        
        setMesh(quad);
        
        addControl(billBoardControl);
    }
    
    public void setRotation(Float rotation){
        setRotationEnabled();
        this.rotate(new Quaternion().fromAngles(0, 0, rotation));
    }
    
    public void removeBillboardController(){
        removeControl(billBoardControl);
    }
    
    protected void setRotationEnabled(){
        billBoardControl.setAlignment(BillboardControl.Alignment.AxialZ);
    }
    
    protected void setRotationDisabled(){
        billBoardControl.setAlignment(BillboardControl.Alignment.Screen);
    }
}
