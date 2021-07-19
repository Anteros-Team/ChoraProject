package com.game.chora;

import com.game.chora.items.entities.*;
import com.game.chora.utils.Entity;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Main extends SimpleApplication{
    
    private Player p;
    private DynamicSky sky = null;
    private BulletAppState bulletAppState;
    private Scene scene;
    private View view;
    private Pound pound;
    private Ocean ocean;
    private Node shootables;
    private Geometry mark;  
    private List<Entity> entities;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start(); // start the game
    }
    
    @Override
    public void simpleInitApp() {
        
        //ChaseCamera chaseCam = new ChaseCamera(cam, rootNode, inputManager);
        //chaseCam.setMinDistance(500);
        flyCam.setMoveSpeed(500);
        flyCam.setDragToRotate(true);
        cam.setFrustumFar(100000f);
        
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        
        shootables = new Node("Shootables");
        rootNode.attachChild(shootables);
        inputManager.setCursorVisible(true);
        
        
        // Create player
        
        p = new Player("Chicco");
        
        
        // Create scene and terrain
        
        scene = new Scene(assetManager, rootNode, bulletAppState);
                
        
        // Create Sky, Sun and Moon and Ambient Light
        
        sky = new DynamicSky(assetManager, viewPort, rootNode);
        rootNode.attachChild(sky);
        
        view = new View(assetManager, rootNode);
        
        
        // Add water pound
        
        pound = new Pound(assetManager, rootNode, viewPort, shootables, new Vector3f(60, 5, 40));
        
        
        // Add ocean
        
        ocean = new Ocean(rootNode, viewPort, view.getFilterPostProcessor(), sky);
        
        
        // Initial setup
        
        entities = new ArrayList<>();
        
        Entity e = new Trash(new Vector3f(0, 0, 0), 10, new Vector3f(25, 25, 25));
        e.setModel(assetManager, rootNode, "Models/trash2/trash2.j3o", shootables);
        e.setPhysics(bulletAppState);
        e.spawn(rootNode, shootables);
        entities.add(e);
        
        e = new Trash(new Vector3f(50, 0, 50), 10, new Vector3f(25, 25, 25));
        e.setModel(assetManager, rootNode, "Models/trash2/trash2.j3o", shootables);
        e.setPhysics(bulletAppState);
        e.spawn(rootNode, shootables);
        entities.add(e);
        
        e = new Sprout(new Vector3f(90, 0, 90), 0.5f, new Vector3f(5, 15, 5));
        e.setModel(assetManager, rootNode, "Models/sprout/sprout.j3o", shootables);
        e.setPhysics(bulletAppState);
        e.spawn(rootNode, shootables);
        entities.add(e);
        
        initKeys();       // load custom key mappings

    }
    
    @Override
    public void simpleUpdate(float tpf) {
        view.updateLight(sky);
        sky.updateTime();
        for (Entity e: entities) {
            if (e instanceof Tree) {
                ((Tree) e).increaseTime(tpf);
                if (((Tree) e).getTime() >= 10) {
                    ((Tree) e).resetTime();
                    
                    Apple a = new Apple(e.getPosition(), 10, new Vector3f(5, 5, 5));
                    a.setModel(assetManager, rootNode, "Models/apple01/apple01.j3o", shootables);
                    a.setPhysics(bulletAppState);
                    a.spawn(rootNode, shootables);
                    ((Tree) e).newApple(a);
                }
            }
        }
    }

    
    private void initKeys() {
        inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "Shoot");
    }

   
    private final ActionListener actionListener = new ActionListener() {

        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
          if (name.equals("Shoot") && !keyPressed) {
            CollisionResults results = new CollisionResults();
            
            Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
            Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
            direction.subtractLocal(origin).normalizeLocal();
            
            Ray ray = new Ray(origin, direction);
            
            shootables.collideWith(ray, results);
            
            String closest;
            for (int i = 0; i < results.size(); i++) {
                float dist = results.getCollision(i).getDistance();
                Vector3f pt = results.getCollision(i).getContactPoint();
                String hit = results.getCollision(i).getGeometry().getName();
                System.out.println("* Collision #" + i);
                System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
            }
            
            if (results.size() > 0) {
                closest = results.getClosestCollision().getGeometry().getName();
                
                Spatial c = rootNode.getChild(closest);
                
                // Pound clicked
                if (pound.getPickBox().getName().equals(closest)) {
                    pound.takeWater();
                    p.setWaterBucket(p.getWaterBucket() + 1);
                    if (pound.getWaterLocation().y <= -15) {
                        pound.despawn(rootNode, shootables);
                    }
                } else {
                    for (Entity e: entities) {
                    if (e != null) {
                        if (e.getPickBox().getName().equals(closest)) {
                            e.onAction(rootNode, shootables);
                            
                            // Entity clicked is SmallTree
                            if (e instanceof SmallTree) {
                                Entity t = new Tree(e.getPosition(), 5, new Vector3f(20, 20, 20));
                                t.setModel(assetManager, rootNode, "Models/tree/tree.j3o", shootables);
                                t.setPhysics(bulletAppState);
                                t.spawn(rootNode, shootables);
                                entities.add(t);
                                e.onAction(rootNode, shootables);
                                entities.remove(e);
                            }
                            
                            // Entity clicked is Sprout
                            if (e instanceof Sprout) {
                                Entity st = new SmallTree(e.getPosition(), 5, new Vector3f(20, 20, 20));
                                st.setModel(assetManager, rootNode, "Models/small_tree/small_tree.j3o", shootables);
                                st.setPhysics(bulletAppState);
                                st.spawn(rootNode, shootables);
                                entities.add(st);
                                e.onAction(rootNode, shootables);
                                entities.remove(e);
                            }
                        }
                    }
                }
                }
            } else {
                // no hit
            }
          }
        }
  };
}
