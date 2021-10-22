package com.game.chora.utils;

import com.jme3.math.Vector3f;

/**
 * EntitySerialization is a class that collect and setup
 * all the entities for the database.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class EntitySerialization {
    
    private Vector3f position;
    private float scale;
    private Vector3f pickboxSize;
    private String typeOfEntity;

    /**
     * class constructor.
     */
    public EntitySerialization() {
        this.position = new Vector3f(0, 0, 0);
        this.scale = 1;
        this.pickboxSize = new Vector3f(0, 0, 0);
        this.typeOfEntity = "";
    }
    
    /**
     *
     * @return entity position
     */
    public Vector3f getPosition() {
        return this.position;
    }
    
    /**
     *
     * @return entity scale
     */
    public float getScale() {
        return this.scale;
    }
    
    /**
     *
     * @return entity pickbox size
     */
    public Vector3f getPickboxSize() {
        return this.pickboxSize;
    }
    
    /**
     *
     * @return type of entity
     */
    public String getTypeOfEntity() {
        return this.typeOfEntity;
    }
    
    /**
     * set entity position.
     * @param position
     */
    public void setPosition(Vector3f position) {
        this.position = position;
    }
    
    /**
     * set entity scale.
     * @param scale
     */
    public void setScale(float scale) {
        this.scale = scale;
    }
    
    /**
     * set entity pickbox size.
     * @param pickboxSize
     */
    public void setPickboxSize(Vector3f pickboxSize) {
        this.pickboxSize = pickboxSize;
    }
    
    /**
     * set type of entity.
     * @param typeOfEntity
     */
    public void setTypeOfEntity(String typeOfEntity) {
        this.typeOfEntity = typeOfEntity;
    }
}
