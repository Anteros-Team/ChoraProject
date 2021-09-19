/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.chora.utils;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;

public class ItemBillboard {
    private Geometry popupGeom;
    private BillboardControl billBoardControl;
    private Quad quad;
    private Node popup;
    private Node extraNode;
    protected Float scale;
    
    public ItemBillboard(String name, Float scale, Vector3f position, Float height, AssetManager assetManager, String texture){
        popup = new Node("Popup");
        this.scale = scale;
        
        Box b = new Box(1, 1, 1);
        Geometry boxGeom = new Geometry("Box", b);
        
        Material boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", ColorRGBA.Blue);
        boxGeom.setMaterial(boxMat);
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture(texture));
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.getAdditionalRenderState().setDepthWrite(false);
        
        quad = new Quad(this.scale, this.scale);
        popupGeom = new Geometry(name, quad);
        popupGeom.setMaterial(mat);
        popupGeom.setShadowMode(RenderQueue.ShadowMode.Off);
        popupGeom.setQueueBucket(RenderQueue.Bucket.Translucent);
        popupGeom.setCullHint(Spatial.CullHint.Never);
        
        extraNode = new Node("Extra Node");
        popup.attachChild(boxGeom);
        popup.attachChild(extraNode);
        extraNode.attachChild(popupGeom);
        extraNode.setLocalTranslation(position.x, position.y + height + 5f, position.z);
        popupGeom.move(-scale/2, 0 , scale/2);
        
        billBoardControl = new BillboardControl();
        extraNode.addControl(billBoardControl);
    }
    
    public Node getNode() {
        return extraNode;
    }
    
    public Float getScale() {
        return scale;
    }
    
    public void setRotation(Float rotation){
        setRotationEnabled();
        popupGeom.rotate(new Quaternion().fromAngles(0, 0, rotation));
    }
    
    protected void setRotationEnabled(){
        billBoardControl.setAlignment(BillboardControl.Alignment.AxialZ);
    }
    
    protected void setRotationDisabled(){
        billBoardControl.setAlignment(BillboardControl.Alignment.Screen);
    }
}
