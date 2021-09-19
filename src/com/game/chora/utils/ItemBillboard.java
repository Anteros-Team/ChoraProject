/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.chora.utils;

import com.jme3.math.Quaternion;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Quad;

public class ItemBillboard extends Geometry{
    private BillboardControl billBoardControl = new BillboardControl();
    private Quad quad;
    
    public ItemBillboard(String name, Float scale){
        super(name);
        
        quad = new Quad(scale, scale);
        
        setQueueBucket(RenderQueue.Bucket.Translucent);
        setCullHint(Spatial.CullHint.Never);
        
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
