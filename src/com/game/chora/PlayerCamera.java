package com.game.chora;

import com.jme3.input.FlyByCamera;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;


public class PlayerCamera {
    
    private final float X_MIN = -1000;
    private final float X_MAX = 1000;
    private final float Y_MIN = 50;
    private final float Y_MAX = 600;
    private final float Z_MIN = -1000;
    private final float Z_MAX = 1000;
    private final float MAX_ANGLE = (float) Math.PI/2;
    
    public PlayerCamera(Camera cam, FlyByCamera flyCam) {
        flyCam.setMoveSpeed(500);
        flyCam.setDragToRotate(true);
        cam.setFrustumFar(100000f);
        cam.setLocation(new Vector3f(-700, 300, 700));
    }
    
    public void lookAtWorld(Camera cam, Scene scene) {
        cam.lookAt(scene.getTerrain().getLocalTranslation(), Vector3f.ZERO);
    }
    
    public void checkCameraLimits(Camera cam) {
        if (cam.getLocation().x < X_MIN) {
            cam.setLocation(new Vector3f(X_MIN, cam.getLocation().y, cam.getLocation().z));
        }
        if (cam.getLocation().x > X_MAX) {
            cam.setLocation(new Vector3f(X_MAX, cam.getLocation().y, cam.getLocation().z));
        }
        if (cam.getLocation().y < Y_MIN) {
            cam.setLocation(new Vector3f(cam.getLocation().x, Y_MIN, cam.getLocation().z));
        }
        if (cam.getLocation().y > Y_MAX) {
            cam.setLocation(new Vector3f(cam.getLocation().x, Y_MAX, cam.getLocation().z));
        }
        if (cam.getLocation().z < Z_MIN) {
            cam.setLocation(new Vector3f(cam.getLocation().x, cam.getLocation().y, Z_MIN));
        }
        if (cam.getLocation().z > Z_MAX) {
            cam.setLocation(new Vector3f(cam.getLocation().x,cam.getLocation().y, Z_MAX));
        }
        
        Quaternion tmpQuat = new Quaternion();
        float[] angles = new float[3];
        cam.getRotation().toAngles(angles);
        
        if(angles[0] > MAX_ANGLE) {
            
            angles[0] = MAX_ANGLE;
            cam.setRotation(tmpQuat.fromAngles(angles));
            
        } else if(angles[0] < -MAX_ANGLE) {
            
            angles[0]= -MAX_ANGLE;
            cam.setRotation(tmpQuat.fromAngles(angles));
        }
    }
    
}
