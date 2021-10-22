package com.game.chora.items.entities;

import com.game.chora.utils.Entity;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.io.Serializable;

/**
 * Trash is an extention of Entity.
 * Trash objects cover the entire map at
 * the start of the game. Trash only purpose
 * is to be removed.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class Trash extends Entity implements Serializable {
    
    /**
     * class constructor with parameters
     * @param position location in the 3D space
     * @param scale model size 
     * @param hitboxSize size of the clickable area
     */
    public Trash(Vector3f position, float scale, Vector3f hitboxSize) {
        super(position, scale, hitboxSize);
    }
    
    /**
     * when a trash is clicked, it disappear.
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
        return new StringBuffer(" Position: ").append(this.getPosition().toString()).append(" Scale: ").append(this.getScale()).append(" PickBoxSize: ").append(this.getPickboxSize().toString()).append(" Model: Models/trash/trash.j3o").toString();
    }
}
