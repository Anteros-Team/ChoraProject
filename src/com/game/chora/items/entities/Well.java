package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.game.chora.utils.ItemBillboard;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.io.Serializable;

/**
 * Well is an extention of Entity.
 * Well object can be purchased from the shop.
 * Well produce water based on a specific timer.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class Well extends Entity implements Serializable {
    
    private float time;
    private int water;
    private ItemBillboard popup;
    
    /**
     * class constructor with parameters.
     * @param position location in the 3D space
     * @param scale model size 
     * @param hitboxSize size of the clickable area
     */
    public Well(Vector3f position, float scale, Vector3f hitboxSize) {
        super(position, scale, hitboxSize);
        this.time = 0;
        this.water = 0;
    }
    
    /**
     * 
     * @return time remaining
     */
    public float getTime() {
        return this.time;
    }
    
    /**
     * update time remaining.
     * @param tpf time per frame
     */
    public void increaseTime(float tpf) {
        this.time += tpf;
    }
    
    /**
     * reset time to 0.
     */
    public void resetTime() {
        this.time = 0;
    }
    
    /**
     * when timer ends, increase the amount of water
     * stored in the well.
     */
    public void increaseWater() {
        if (this.water < 5)
            this.water++;
    }
    
    /**
     * create a popup window that appears when there's water
     * stored in the well.
     * @param assetManager
     */
    public void createPopup(AssetManager assetManager) {
        popup = new ItemBillboard("popup", 50f, this.getPosition(), 25f, assetManager, "Interface/gui/acqua.png");
    }
    
    /**
     *
     * show the popup window.
     * @param rootNode
     */
    public void showPopup(Node rootNode) {
        rootNode.attachChild(popup.getNode());
    }
    
    /**
     *
     * hide the popup window.
     * @param rootNode
     */
    public void hidePopup(Node rootNode) {
        rootNode.detachChild(popup.getNode());
    }
    
    /**
     *
     * @param water
     */
    public void setWater(int water) {
        this.water = water;
    }
    
    /**
     *
     * @return water stored.
     */
    public int getWater() {
        return this.water;
    }
    
    /**
     *
     * @return string version of the object
     */
    @Override 
    public String toString() {
        return new StringBuffer(" Position: ").append(this.getPosition().toString()).append(" Scale: ").append(this.getScale()).append(" PickBoxSize: ").append(this.getPickboxSize().toString()).append(" Model: Models/well/well.j3o").toString();
    }
    
}
