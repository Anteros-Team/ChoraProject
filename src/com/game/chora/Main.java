package com.game.chora;

import com.game.chora.items.entities.*;
import com.game.chora.water.Ocean;
import com.game.chora.water.Pound;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.texture.Texture;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import java.util.HashSet;


public class Main extends SimpleApplication{
    
    DynamicSky sky = null;
    private BulletAppState bulletAppState;
    private Scene scene;
    private View view;
    private Pound pound;
    private Ocean ocean;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start(); // start the game
    }
    
    @Override
    public void simpleInitApp() {
        
        ChaseCamera chaseCam = new ChaseCamera(cam, rootNode, inputManager);
        chaseCam.setMinDistance(500);
        cam.setFrustumFar(100000f);
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        
        
        // Create scene and terrain
        
        scene = new Scene(assetManager, rootNode, bulletAppState);
        
        
        // Create Sky, Sun and Moon and Ambient Light
        
        sky = new DynamicSky(assetManager, viewPort, rootNode);
        rootNode.attachChild(sky);
        
        view = new View(assetManager, rootNode);
        
        
        // Add water pound
        
        pound = new Pound(assetManager, rootNode, viewPort);
        
        
        // Add ocean
        
        ocean = new Ocean(rootNode, viewPort, view.getFilterPostProcessor(), sky);
        
        
        // Initial setup
        
        /*Spatial trash = assetManager.loadModel("Models/trash2/trash2.j3o");                
        trash.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        trash.scale(10f);
        trash.setLocalTranslation(100, 0, 50);
                
        //CollisionShape shapet = CollisionShapeFactory.createBoxShape(trash); 
        BoxCollisionShape shapet = new BoxCollisionShape(new Vector3f(25,10,25));
        CompoundCollisionShape shapect = new CompoundCollisionShape();
        shapect.addChildShape(shapet, new Vector3f(shapet.getScale().x, shapet.getHalfExtents().y, shapet.getScale().z));
        /*
        Box cube = new Box(shape.getScale().x, shape.getScale().y, shape.getScale().z);
        Geometry box = new Geometry("trash", cube);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/ShowNormals.j3md");
        box.setMaterial(mat);
        rootNode.attachChild(box);
             
        RigidBodyControl rigidBodyControlt = new RigidBodyControl(shapect);
        trash.addControl(rigidBodyControlt);
        
        bulletAppState.getPhysicsSpace().add(trash);
        
        bulletAppState.getPhysicsSpace().add(rigidBodyControlt);
        //bulletAppState.setDebugEnabled(true);
        
        Spatial sprout = assetManager.loadModel("Models/sprout/sprout.j3o");
        sprout.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        //sprout.addControl(new RigidBodyControl(1));
        //bulletAppState.getPhysicsSpace().add(sprout);        
        BoxCollisionShape shapes = new BoxCollisionShape(new Vector3f(10, 10 ,10));
        CompoundCollisionShape shapecs = new CompoundCollisionShape();
        shapecs.addChildShape(shapes, new Vector3f(shapes.getScale().x, shapes.getHalfExtents().y, shapes.getScale().z));
        RigidBodyControl rigidBodyControlts = new RigidBodyControl(shapecs);
        sprout.addControl(rigidBodyControlts);        
        bulletAppState.getPhysicsSpace().add(sprout);
        bulletAppState.getPhysicsSpace().add(rigidBodyControlts);
        
        rootNode.attachChild(sprout);
        rootNode.attachChild(trash);*/
        
        Trash t = new Trash(new Vector3f(0, 0, 0), 10, new Vector3f(25, 10, 25));
        t.setModel(assetManager, rootNode, "Models/trash2/trash2.j3o");
        t.setPhysics(bulletAppState);
        t.Spawn(rootNode);
        
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        view.updateLight(sky);
        sky.updateTime();
    }
}
