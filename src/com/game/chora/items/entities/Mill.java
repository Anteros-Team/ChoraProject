package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.Serializable;

public class Mill extends Entity {
    
    private int speed;
    
    public Mill() {
        super();
        this.speed = 1;
    }
    
    public Mill(Vector3f position, float scale, Vector3f pickBoxSize) {
        super(position, scale, pickBoxSize);
        this.speed = 1;
    }
    
    public int getRotateSpeed() {
        return this.speed;
    }
    
    public void setRotateSpeed(int speed) {
        this.speed = speed;
    }
    
    public void rotateMill(Node rootNode, float tpf) {
        /*for(Spatial s : rootNode.getChildren()) {
            System.out.println(s.getName());
            if ( ((Node)s).getChild("wheel") instanceof Node ) {
                ((Node)s).getChild("wheel").rotate(0, this.speed * tpf, 0);
            }
        }*/
        rootNode.getChild("wheel").rotate(0, this.speed * tpf, 0);
    }

}
