package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class Mill extends Entity {
    
    private int speed;
    
    public Mill(Vector3f position, float scale, Vector3f hitboxSize) {
        super(position, scale, hitboxSize);
        this.speed = 1;
    }
    
    public int getRotateSpeed() {
        return this.speed;
    }
    
    public void setRotateSpeed(int speed) {
        this.speed = speed;
    }
    
    public void rotateMill(Node rootNode, float tpf) {
        rootNode.getChild("wheel").rotate(0, this.speed *tpf, 0);
    }
}
