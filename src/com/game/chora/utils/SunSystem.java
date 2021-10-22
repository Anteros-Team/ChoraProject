package com.game.chora.utils;

import java.util.Date;
import java.util.logging.Logger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;

/**
 * SunSystem is a class that manage sun rotation.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class SunSystem {
    
    private static final Logger logger = Logger.getLogger(LunarSystem.class.getName());
    private static float distScaleFactor = 900;

    private Vector3f sunPosition = new Vector3f();
    private boolean debug = false;
    private float siteLat;
    private float siteLon;
    private float i=0; 
    
    /**
     * class constructor.
     */
    public SunSystem() {
        calculateCartesianCoords();
    }

    /**
     * setup sun position
     * @return sun position
     */
    private Vector3f calculateCartesianCoords() {
        sunPosition = new Vector3f(0f, 1f, 0.5f);

        // time
        Matrix3f matRx = new Matrix3f();
        Matrix3f matRy = new Matrix3f();
        Matrix3f matRz = new Matrix3f();

        matRx.fromAngleNormalAxis(0, new Vector3f(1, 0, 0));
        matRy.fromAngleNormalAxis(0, new Vector3f(0, 1, 0));
        
        matRz.fromAngleNormalAxis(i, new Vector3f(0, 0, 1));
        sunPosition = matRz.mult(matRx.mult(matRy.mult(sunPosition)));
        i-=0.0005;

        // Scale distance
        sunPosition.multLocal(distScaleFactor);
        
        if (debug) {
            System.out.println(sunPosition);       
        }   
        return sunPosition;
    }

    /**
     * enable/disable code debug.
     * @param enable
     */
    public void enableDebug(boolean enable) {
        debug = enable;
    }

    /**
     * 
     * @return sun position
     */
    public Vector3f getPosition() {
        return sunPosition;
    }
    
    /**
     *
     * @return sun direction
     */
    public Vector3f getDirection() {
        return sunPosition.normalize().mult(-1);
    }

    /**
     *
     * @return sun scale factor
     */
    public float getScaleFactor() {
        return distScaleFactor;
    }

    /**
     *
     * @return sun site latitude
     */
    public float getSiteLatitude() {
        return siteLat;
    }

    /**
     *
     * @return sun site longitude
     */
    public float getSiteLongitude() {
        return siteLon;
    }

    /**
     *
     * set sun site latitude.
     * @param siteLat
     */
    public void setSiteLatitude(float siteLat) {
        this.siteLon = siteLat;
    }

    /**
     *
     * set sun site longitude.
     * @param siteLon
     */
    public void setSiteLongitude(float siteLon) {
        this.siteLon = siteLon;
    }

    /**
     * 
     * update sun position.
     * @return sun position
     */
    public Vector3f updateSunPosition() {
        return calculateCartesianCoords();
    }
}
