package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.io.Serializable;


public class Sprout extends Entity implements Serializable {
    
    public Sprout(Vector3f position, float scale, Vector3f hitboxSize) {
        super(position, scale, hitboxSize);
    }

    @Override
    public void onAction(Node rootNode, Node shootables) {
        this.despawn(rootNode, shootables);
    }
    
    @Override 
    public String toString() {
        return new StringBuffer(" Position: ").append(this.getPosition().toString()).append(" Scale: ").append(this.getScale()).append(" PickBoxSize: ").append(this.getPickboxSize().toString()).append(" Model: Models/sprout/sprout.j3o").toString();
    }
}
