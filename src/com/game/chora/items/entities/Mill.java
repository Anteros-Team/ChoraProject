package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.game.chora.utils.ItemBillboard;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.Serializable;

public class Mill extends Entity {
    
    private int speed;
    protected ItemBillboard popup;
    protected float time;
    protected int state;
    
    public Mill() {
        super();
        this.speed = 1;
        this.time = 0;
        this.state = 0;
    }
    
    public Mill(Vector3f position, float scale, Vector3f pickBoxSize) {
        super(position, scale, pickBoxSize);
        this.speed = 1;
    }
    
    public float getTime() {
        return this.time;
    }
    
    public void increaseTime(float tpf) {
        this.time += tpf;
        //System.out.println(this.time);
    }
    
    public void resetTime() {
        this.time = 0;
    }
    
    public int getState() {
        return this.state;
    }
    
    public void setState(int state) {
        this.state = state;
    }
    
    public void createPopup(AssetManager assetManager) {
        popup = new ItemBillboard("popup", 50f, position, 150f, assetManager, "Interface/gui/vento.png");
    }
    
    public void showPopup(Node rootNode) {
        rootNode.attachChild(popup.getNode());
    }
    
    public void hidePopup(Node rootNode) {
        rootNode.detachChild(popup.getNode());
    }
    
    public int getRotateSpeed() {
        return this.speed;
    }
    
    public void setRotateSpeed(int speed) {
        this.speed = speed;
    }
    
    public void rotateMill(Node rootNode, float tpf) {
        for(Spatial s : rootNode.getChildren()) {
            //System.out.println("Nome mill: " + s.getName());
            if ("Mill".equals(s.getName())) {
                ((Node) s).getChild("wheel").rotate(0, this.speed * tpf, 0);
            }
        }
    }

}
