package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class SmallTree extends Entity {
    
    public SmallTree(Vector3f position, float scale, Vector3f hitboxSize) {
        super(position, scale, hitboxSize);
    }
    
    @Override
    public void onAction(Node rootNode, Node shootables) {
        this.despawn(rootNode, shootables);
    }
}
