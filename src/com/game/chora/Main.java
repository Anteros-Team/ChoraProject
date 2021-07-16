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
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
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
    private Node shootables;
    private Geometry mark;    
    
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
        
        shootables = new Node("Shootables");
        rootNode.attachChild(shootables);
        inputManager.setCursorVisible(true);
        
        Box box = new Box(10, 10, 10);
        Geometry cube = new Geometry("PickBox", box);
        cube.setLocalTranslation(25, 15, 25);
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.White);
        cube.setMaterial(mat1);
        
        shootables.attachChild(cube);
        initKeys();       // load custom key mappings
        initMark();       // a red sphere to mark the hit

    }
    
    @Override
    public void simpleUpdate(float tpf) {
        view.updateLight(sky);
        sky.updateTime();
    }

    
    /** Declaring the "Shoot" action and mapping to its triggers. */
    private void initKeys() {
        inputManager.addMapping("Shoot",
            //new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
            new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
        inputManager.addListener(actionListener, "Shoot");
    }
    
    /** A red ball that marks the last spot that was "hit" by the "shot". */
  protected void initMark() {
    //Sphere sphere = new Sphere(30, 30, 0.2f);
    /*Sphere sphere = new Sphere(30, 30, 30);
    mark = new Geometry("BOOM!", sphere);
    Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mark_mat.setColor("Color", ColorRGBA.Red);
    mark.setMaterial(mark_mat);*/
    
    
  }
    
    /** Defining the "Shoot" action: Determine what was hit and how to respond. */
    private ActionListener actionListener = new ActionListener() {

        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
          if (name.equals("Shoot") && !keyPressed) {
            // 1. Reset results list.
            CollisionResults results = new CollisionResults();
            // 2. Aim the ray from cam loc to cam direction.
            Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
            Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
            direction.subtractLocal(origin).normalizeLocal();
            //Ray ray = new Ray(cam.getLocation(), cam.getDirection());
            Ray ray = new Ray(origin, direction);
            
            // 3. Collect intersections between Ray and Shootables in results list.
            // DO NOT check collision with the root node, or else ALL collisions will hit the
            // skybox! Always make a separate node for objects you want to collide with.
            shootables.collideWith(ray, results);
            // 4. Print the results
            System.out.println("----- Collisions? " + results.size() + "-----");
            String hit="";
            for (int i = 0; i < results.size(); i++) {
                // For each hit, we know distance, impact point, name of geometry.
                float dist = results.getCollision(i).getDistance();
                Vector3f pt = results.getCollision(i).getContactPoint();
                hit = results.getCollision(i).getGeometry().getName();
                System.out.println("* Collision #" + i);
                System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
            }            
            // 5. Use the results (we mark the hit object)
            if (results.size() > 0) {
                // The closest collision point is what was truly hit:
                CollisionResult closest = results.getClosestCollision();
                // Let's interact - we mark the hit with a red dot.
                
                /*mark.setLocalTranslation(closest.getContactPoint());
                rootNode.attachChild(mark);*/
                Spatial c = rootNode.getChild(hit);
                Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                material.setColor("Color", ColorRGBA.Red);
                c.setMaterial(material);
                
            } else {
                // No hits? Then remove the red mark.
                //rootNode.detachChild(mark);
                /*Material materialu = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                materialu.setColor("Color", ColorRGBA.White);
                c.setMaterial(materialu);*/
                
            }
          }
        }
  };
}
