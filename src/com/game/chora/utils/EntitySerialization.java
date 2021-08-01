
package com.game.chora.utils;

import com.jme3.math.Vector3f;
import java.io.Serializable;


public class EntitySerialization {
    
    protected Vector3f position;
    protected float scale;
    protected Vector3f pickboxSize;
    protected String typeOfEntity;

    public EntitySerialization() {
        this.position = new Vector3f(0, 0, 0);
        this.scale = 1;
        this.pickboxSize = new Vector3f(0, 0, 0);
        this.typeOfEntity = "";
    }
    
    public Vector3f getPosition() {
        return this.position;
    }
    
    public float getScale() {
        return this.scale;
    }
    
    public Vector3f getPickboxSize() {
        return this.pickboxSize;
    }
    
    public String getTypeOfEntity() {
        return this.typeOfEntity;
    }
    
    public void setPosition(Vector3f position) {
        this.position = position;
    }
    
    public void setScale(float scale) {
        this.scale = scale;
    }
    
    public void setPickboxSize(Vector3f pickboxSize) {
        this.pickboxSize = pickboxSize;
    }
    
    public void setTypeOfEntity(String typeOfEntity) {
        this.typeOfEntity = typeOfEntity;
    }
}
