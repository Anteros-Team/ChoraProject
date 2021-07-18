package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;


public class Apple extends Entity {
    
    public Apple(Vector3f position, float scale, Vector3f hitboxSize) {
        super(position, scale, hitboxSize);
    }
    
    @Override
    public void onAction(Node rootNode, Node shootables) {
        this.despawn(rootNode, shootables);
    }
    
}
