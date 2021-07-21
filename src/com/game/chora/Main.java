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
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
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
import com.jme3.scene.BatchNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.image.ImageRaster;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import jme3tools.optimize.GeometryBatchFactory;


public class Main extends SimpleApplication{
    
    private Player p;
    private DynamicSky sky = null;
    //private BulletAppState bulletAppState;
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
        
        //bulletAppState = new BulletAppState();
        //stateManager.attach(bulletAppState);
        //bulletAppState.setDebugEnabled(true);
        //bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        
        shootables = new Node("Shootables");
        rootNode.attachChild(shootables);
        inputManager.setCursorVisible(true);
        
        
        // Create player
        
        p = new Player("Chicco");
        
        
        // Create scene and terrain
        
        scene = new Scene(assetManager, rootNode);
        /*TerrainQuad terrain;
        Material matRock;
        
        matRock = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/heightmap.png");
        Texture dirt = assetManager.loadTexture("Textures/Terrain/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        matRock.setTexture("Tex1", dirt);
        matRock.setFloat("Tex1Scale", 64f);
        
        AbstractHeightMap heightmap = null;
        try {
            heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.25f);
            heightmap.load();

        } catch (Exception e) {
        }
        
        terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
        TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
        control.setLodCalculator( new DistanceLodCalculator(65, 2.7f) ); // patch size, and a multiplier
        terrain.addControl(control);
        terrain.setMaterial(matRock);
        terrain.setLocalScale(new Vector3f(2, 2, 2));
        terrain.setLocked(false); // unlock it so we can edit the height
        rootNode.attachChild(terrain);

        // if set to false, only the first collision is returned and collision is slightly faster.
        terrain.setSupportMultipleCollisions(true);
        
        terrain.addControl(new RigidBodyControl(0));
        bulletAppState.getPhysicsSpace().addAll(terrain);*/
        

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
        
        Entity e = new Trash(new Vector3f(0, 0, 0), 8, new Vector3f(20, 10, 20));
        e.setModel(assetManager, rootNode, "Models/trash2/trash2.j3o", shootables);
        //e.setPhysics(bulletAppState);
        e.spawn(rootNode, shootables);
        entities.add(e);
        
        e = new Trash(new Vector3f(50, 0, 50), 8, new Vector3f(20, 10, 20));
        e.setModel(assetManager, rootNode, "Models/trash2/trash2.j3o", shootables);
        //e.setPhysics(bulletAppState);
        e.spawn(rootNode, shootables);
        entities.add(e);
        
        e = new Sprout(new Vector3f(90, 0, 90), 0.6f, new Vector3f(10, 8, 10));
        e.setModel(assetManager, rootNode, "Models/sprout/sprout.j3o", shootables);
        //e.setPhysics(bulletAppState);
        e.spawn(rootNode, shootables);
        entities.add(e);
        
        e = new Sprout(new Vector3f(200, 0, 0), 0.6f, new Vector3f(10, 8, 10));
        e.setModel(assetManager, rootNode, "Models/sprout/sprout.j3o", shootables);
        //e.setPhysics(bulletAppState);
        e.spawn(rootNode, shootables);
        entities.add(e);
        
        e = new Mill(new Vector3f(-100, 0, -100), 200, new Vector3f(40, 80, 40));
        e.setModel(assetManager, rootNode, "Models/mill/mill.j3o", shootables);
        e.spawn(rootNode, shootables);
        entities.add(e);
        
        
        
        initKeys();       // load custom key mappings
        
        /*Material matWire = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matWire.getAdditionalRenderState().setWireframe(true);
        matWire.setColor("Color", ColorRGBA.Green);
        
        Geometry sphere = new Geometry("esempio", new Sphere(10, 10, 5));
        sphere.setMaterial(matWire);
        sphere.setLocalTranslation(0, 100, 0);
        sphere.addControl(new RigidBodyControl(new SphereCollisionShape(5), 2));
        rootNode.attachChild(sphere);
        bulletAppState.getPhysicsSpace().add(sphere);*/
        
        
        /*Box b = new Box(20, 50, 20);
        Box b1 = new Box(50, 20, 50);
        Geometry g = new Geometry("primo", b);
        Geometry g1 = new Geometry("secondo", b1);   
        Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        g.setMaterial(m);
        g1.setMaterial(m);
        g.setShadowMode(RenderQueue.ShadowMode.Off);
        g.setLocalTranslation(new Vector3f(100, 0, 80));
        g1.setShadowMode(RenderQueue.ShadowMode.Off);
        g1.setLocalTranslation(new Vector3f(100, 50, 80));
        Node n = new Node("Pickbox");
        n.attachChild(g);
        n.attachChild(g1);
        
        //Spatial gg = GeometryBatchFactory.optimize(n);
        rootNode.attachChild(n);
        */
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
                    ((Tree) e).newApple(assetManager, rootNode, shootables);
                }
                for (Apple a: ((Tree)e).getApples()) {
                    if (a.getPosition().y > 0) {
                        Vector3f position = new Vector3f();
                        position.x = a.getPosition().x;
                        position.y = a.getPosition().y - (100 * tpf);
                        position.z = a.getPosition().z;
                        
                        if (position.y < 0) {
                            position.y = 0;
                        }
                        
                        a.setPosition(position);
                    }
                }
            }
            if (e instanceof Mill) {
                ((Mill) e).rotateMill(rootNode, tpf);
            }
        }
    }
    
    private Geometry getGeometry(Spatial spatial, String name) {

        if (spatial instanceof Node) {
            
            Node node = (Node) spatial;
            for (int i = 0; i < node.getQuantity(); i++) {

            Spatial child = node.getChild(i);
            getGeometry(child, name);
            }
        } else if (spatial instanceof Geometry) {

            String na = spatial.getParent().getName();
            if (na.contains(name)) {

                Geometry geo = (Geometry) spatial;
                return geo;
            }
        }
        return null;
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
                            
                            if (e instanceof Tree) {
                                for (Apple a: ((Tree) e).getApples()) {
                                    if (a.getPickBox().get(0).getName().equals(closest)) {
                                        a.onAction(rootNode, shootables);
                                        p.setApple(p.getApple() + 1);
                                        break;
                                    }
                                }
                            }
                            
                            if (e.getPickBox().get(0).getName().equals(closest)) {
                                
                                // Entity clicked is Trash
                                if (e instanceof Trash) {
                                    e.onAction(rootNode, shootables);
                                    break;
                                }

                                // Entity clicked is SmallTree
                                if (e instanceof SmallTree && p.getWaterBucket() > 0) {
                                    Entity t = new Tree(e.getPosition(), 4, new Vector3f(65, 50, 75));
                                    t.setModel(assetManager, rootNode, "Models/tree/tree.j3o", shootables);
                                    e.onAction(rootNode, shootables);
                                    t.spawn(rootNode, shootables);
                                    entities.add(t);
                                    entities.remove(e);
                                    p.setWaterBucket(p.getWaterBucket() - 1);
                                    
                                    ImageRaster imageRaster = ImageRaster.create(scene.getAlphaTexture().getImage());
                                    
                                    System.out.println(t.getPosition().x - t.getPickboxSize().x);
                                    System.out.println(t.getPosition().x + t.getPickboxSize().x);

                                    /*for (float i = 512 + t.getPosition().x - t.getPickboxSize().x; i < 512 + t.getPosition().x + t.getPickboxSize().x; i++) {
                                        for (float j = 512 - t.getPosition().z - t.getPickboxSize().z; j < 512 - t.getPosition().z + t.getPickboxSize().z; j++) {
                                            imageRaster.setPixel((int) i, (int) j, ColorRGBA.Green);
                                        }
                                    }*/
                                    for (float i = 512 + t.getPosition().x - 55; i < 512 + t.getPosition().x + 65; i++) {
                                        for (float j = 512 - t.getPosition().z - 65 ; j < + 512 - t.getPosition().z + 75; j++) {
                                            imageRaster.setPixel((int) i, (int) j, ColorRGBA.Green);
                                        }
                                    }
                                    scene.getTerrain().updateModelBound();
                                    break;
                                }

                                // Entity clicked is Sprout
                                if (e instanceof Sprout && p.getWaterBucket() > 0) {
                                    Entity st = new SmallTree(e.getPosition(), 5, new Vector3f(35, 40, 35));
                                    st.setModel(assetManager, rootNode, "Models/small_tree/small_tree.j3o", shootables);
                                    e.onAction(rootNode, shootables);
                                    st.spawn(rootNode, shootables);
                                    entities.add(st);
                                    entities.remove(e);
                                    p.setWaterBucket(p.getWaterBucket() - 1);
                                    break;
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
