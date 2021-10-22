package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.io.Serializable;

/**
 * Apple is an extention of Entity.
 * Apple objects are items that spawn
 * from trees and can be picked up and collected.
 * Apples can be spent in the shop to buy every
 * listed item.
 *
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class Apple extends Entity implements Serializable {
    
    /**
     * class costructor. 
     * 
     * @param position location in the 3D space
     * @param scale model size 
     * @param hitboxSize size of the clickable area
     */
    public Apple(Vector3f position, float scale, Vector3f hitboxSize) {
        super(position, scale, hitboxSize);
    }
    
    /**
     * when an apple is clicked, it disappear.
     * Player class is responsable to increment the number
     * of total apples after the pickup.
     * @see Player
     * 
     * @param rootNode
     * @param shootables
     */
    @Override
    public void onAction(Node rootNode, Node shootables) {
        this.despawn(rootNode, shootables);
    }
    
    /**
     * 
     * @return string version of the object
     */
    @Override 
    public String toString() {
        return new StringBuffer(" Position: ").append(this.getPosition().toString()).append(" Scale: ").append(this.getScale()).append(" PickBoxSize: ").append(this.getPickboxSize().toString()).append(" Model: Models/apple01/apple01.j3o").toString();
    }
}
