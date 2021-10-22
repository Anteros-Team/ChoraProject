package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.game.chora.utils.ItemBillboard;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Mill is an extention of Entity.
 * Mill objects can only be purchased from the
 * game shop and increase the production of apples
 * from trees and water from wells for a short amount
 * of time. When this ends, there will be a countdown
 * until you can speed up the production again.
 * 
 * <p>
 * Every Mill cicle between three states:
 * <ul>
 * <li> 0 = waiting for the player to start
 *      the production increment.
 * <li> 1 = production is increased until timer ends.
 * <li> 2 = waiting for the production increment.
 * </ul>
 * Every Mill spawn with state = 0.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class Mill extends Entity {
    
    private int speed;
    private ItemBillboard popup;
    private float time;
    private int state;
    
    /**
     * class constructor.
     */
    public Mill() {
        super();
        this.speed = 1;
        this.time = 0;
        this.state = 0;
    }
    
    /**
     * class costructor with parameters.
     * @param position location in the 3D space
     * @param scale model size 
     * @param pickBoxSize size of the clickable area
     */
    public Mill(Vector3f position, float scale, Vector3f pickBoxSize) {
        super(position, scale, pickBoxSize);
        this.speed = 1;
    }
    
    /**
     *
     * @return time remaining
     */
    public float getTime() {
        return this.time;
    }
    
    /**
     *
     * update time remaining.
     * @param tpf time per frame
     */
    public void increaseTime(float tpf) {
        this.time += tpf;
    }
    
    /**
     * 
     * reset time to 0.
     */
    public void resetTime() {
        this.time = 0;
    }
    
    /**
     *
     * @return state of the mill.
     */
    public int getState() {
        return this.state;
    }
    
    /**
     *
     * @param state new state of the mill.
     */
    public void setState(int state) {
        this.state = state;
    }
    
    /**
     * create a popup window that appears when a mill
     * state is 0.
     * @param assetManager
     */
    public void createPopup(AssetManager assetManager) {
        popup = new ItemBillboard("popup", 50f, this.getPosition(), 150f, assetManager, "Interface/gui/vento.png");
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
     * @return speed of the mill wheel
     */
    public int getRotateSpeed() {
        return this.speed;
    }
    
    /**
     * set speed of the mill wheel.
     * @param speed 
     */
    public void setRotateSpeed(int speed) {
        this.speed = speed;
    }
    
    /**
     * rotate mill wheel based on the speed parameter.  
     * @param rootNode
     * @param tpf
     */
    public void rotateMill(Node rootNode, float tpf) {
        rootNode.getChildren().stream().filter(s -> ("Mill".equals(s.getName()))).forEachOrdered(s -> {
            ((Node) s).getChild("wheel").rotate(0, this.speed * tpf, 0);
        });
    }

}
