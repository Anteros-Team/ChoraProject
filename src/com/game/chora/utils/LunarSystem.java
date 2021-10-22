package com.game.chora.utils;

import java.util.Date;
import java.util.logging.Logger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;

/**
 * LunarSystem is a class that manage moon rotation.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */ 
public class LunarSystem {
   
    private static final Logger logger = Logger.getLogger(LunarSystem.class.getName());
    private static float distScaleFactor = 900;

    private Vector3f moonPosition = new Vector3f();
    private boolean debug = false;
    private float siteLat;
    private float siteLon;
    private float i=0; 
    
    /**
     * class constructor.
     */
    public LunarSystem() {
        calculateCartesianCoords();
    }
    
    /**
     * setup moon position
     * @return moon position
     */
    private Vector3f calculateCartesianCoords() {
        moonPosition = new Vector3f(0f, -1f, 0.5f);

        // time
        Matrix3f matRx = new Matrix3f();
        Matrix3f matRy = new Matrix3f();
        Matrix3f matRz = new Matrix3f();

        matRx.fromAngleNormalAxis(0, new Vector3f(1, 0, 0));
        matRy.fromAngleNormalAxis(0, new Vector3f(0, 1, 0));
        matRz.fromAngleNormalAxis(i, new Vector3f(0, 0, 1));
        moonPosition = matRz.mult(matRx.mult(matRy.mult(moonPosition)));
        i-=0.0005;

        // Scale distance
        moonPosition.multLocal(distScaleFactor);

        if (debug) {
            System.out.println(moonPosition);
        }
        return moonPosition;
    }

    /**
     * enable/disable code debug
     * @param enable
     */
    public void enableDebug(boolean enable) {
        debug = enable;
    }
    /**
     *
     * @return moon position
     */
    public Vector3f getPosition() {
        return moonPosition;
    }

    /**
     *
     * @return moon direction
     */
    public Vector3f getDirection() {
        return moonPosition.normalize().mult(-1);
    }

    /**
     *
     * @return moon scale factor
     */
    public float getScaleFactor() {
        return distScaleFactor;
    }
    
    /**
     *
     * @return moon site latitude
     */
    public float getSiteLatitude() {
        return siteLat;
    }

    /**
     *
     * @return moon site longitude
     */
    public float getSiteLongitude() {
        return siteLon;
    }

    /**
     *
     * set moon site latutide.
     * @param siteLat
     */
    public void setSiteLatitude(float siteLat) {
        this.siteLon = siteLat;
    }

    /**
     * 
     * set moon site longitude.
     * @param siteLon
     */
    public void setSiteLongitude(float siteLon) {
        this.siteLon = siteLon;
    }

    /**
     *
     * update moon position.
     * @return moon position
     */
    public Vector3f updateSunPosition() {
        return calculateCartesianCoords();
    }
}
