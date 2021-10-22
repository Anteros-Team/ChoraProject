package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.game.chora.utils.ItemBillboard;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.io.Serializable;


public class Well extends Entity implements Serializable {
    
    protected float time;
    protected int water;
    protected ItemBillboard popup;
    
    public Well(Vector3f position, float scale, Vector3f hitboxSize) {
        super(position, scale, hitboxSize);
        this.time = 0;
        this.water = 0;
    }
    
   public float getTime() {
        return this.time;
    }
    
    public void increaseTime(float tpf) {
        this.time += tpf;
    }
    
    public void resetTime() {
        this.time = 0;
    }
    
    public void increaseWater() {
        if (this.water < 5)
            this.water++;
    }
    
    public void createPopup(AssetManager assetManager) {
        popup = new ItemBillboard("popup", 50f, position, 25f, assetManager, "Interface/gui/acqua.png");
    }
    
    public void showPopup(Node rootNode) {
        rootNode.attachChild(popup.getNode());
    }
    
    public void hidePopup(Node rootNode) {
        rootNode.detachChild(popup.getNode());
    }
    
    public void setWater(int water) {
        this.water = water;
    }
    
    public int getWater() {
        return this.water;
    }
    
    @Override 
    public String toString() {
        return new StringBuffer(" Position: ").append(this.getPosition().toString()).append(" Scale: ").append(this.getScale()).append(" PickBoxSize: ").append(this.getPickboxSize().toString()).append(" Model: Models/well/well.j3o").toString();
    }
    
}
