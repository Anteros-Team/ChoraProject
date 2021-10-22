package com.game.chora.utils;

import java.util.Date;
import java.util.logging.Logger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;


public class LunarSystem {
    private static final Logger logger = Logger.getLogger(LunarSystem.class.getName());
    private static float distScaleFactor = 900;
    double lambda;
    double beta;
    double r;
    double lambdaOffset;
    double betaOffset;
    double rOffset;

    Date currentDate;

    Vector3f moonPosition = new Vector3f();
    private boolean debug = false;
    private double xs;
    private double ys;
    private double JD;
    private float siteLat;
    private float siteLon;
    private float HR;
    private float i=0; 
    
    public LunarSystem(Date currentDate, double lambdaOffset, double betaOffset, double rOffset) {
        this.currentDate = currentDate;
        this.lambdaOffset = lambdaOffset;
        this.betaOffset = betaOffset;
        this.rOffset = rOffset;

        calculateCartesianCoords();
    }

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
        
        // Get long, lat
        xs = Math.atan2(moonPosition.z, -moonPosition.x);
        ys = Math.atan2(moonPosition.y, -moonPosition.x);

        // Scale distance
        moonPosition.multLocal(distScaleFactor);

        if (debug) {
            System.out.println(moonPosition);
        }
        return moonPosition;
    }

    public void enableDebug(boolean enable) {
        debug = true;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public double getLatitude() {
        return ys;
    }

    public double getLongitude() {
        return xs;
    }

    public Vector3f getPosition() {
        return moonPosition;
    }

    public Vector3f getDirection() {
        return moonPosition.normalize().mult(-1);
    }

    public float getScaleFactor() {
        return distScaleFactor;
    }
    
    public float getSiteLatitude() {
        return siteLat;
    }

    public float getSiteLongitude() {
        return siteLon;
    }

    public void setSiteLatitude(float siteLat) {
        this.siteLon = siteLat;
    }

    public void setSiteLongitude(float siteLon) {
        this.siteLon = siteLon;
    }

    public Vector3f updateSunPosition() {
        return calculateCartesianCoords();
    }
}
