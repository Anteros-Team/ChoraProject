package com.game.chora;

import com.game.chora.gui.Gui;
import com.game.chora.items.entities.*;
import com.game.chora.utils.Entity;
import com.game.chora.utils.EntitySerialization;
import com.game.chora.water.Ocean;
import com.game.chora.water.Pound;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
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
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
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
import com.jme3.niftygui.NiftyJmeDisplay;
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
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private List<EntitySerialization> es;
    private Gui gui;
    
    protected NiftyJmeDisplay niftyDisplay;
    protected Nifty nifty;
    
    protected Path currentRelativePath = Paths.get("");
    protected String filePath = currentRelativePath.toAbsolutePath().toString() + "\\assets\\GameData\\Entity.ser";
    
    private Database db;
    private boolean newGame = false;
    
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
        
        shootables = new Node("Shootables");
        rootNode.attachChild(shootables);
        inputManager.setCursorVisible(true);
        
        /*try {
            FileInputStream fileIn = new FileInputStream(playerFilePath);
            ObjectInputStream reader = new ObjectInputStream(fileIn);
            p = (Player) reader.readObject();
        }
        catch (Exception ex) {
            System.err.println("Failed to read " + playerFilePath + ", " + ex);
            newGame = true;
            p = new Player();
        }
        System.out.println(p);
        */
        db = new Database();
        db.createNewDatabase(currentRelativePath.toAbsolutePath().toString() + "\\assets\\GameData\\");
        db.createTables();
        
        
        p = new Player();
        entities = new ArrayList<>();
        es = new ArrayList<>();
        
        if (db.isPlayerEmpty() == false) {
            System.out.println("No player found. Creating player...");
            db.insertPlayer(p.getName(), p.getApple(), p.getWaterBucket());
            
            Entity e = new Trash(new Vector3f(0, 0, 0), 8, new Vector3f(25, 10, 25));
            e.setModel(assetManager, rootNode, "Models/trash/trash.j3o", shootables);
            //e.setPhysics(bulletAppState);
            e.spawn(rootNode, shootables);
            entities.add(e);

            e = new Trash(new Vector3f(50, 0, 50), 8, new Vector3f(25, 10, 25));
            e.setModel(assetManager, rootNode, "Models/trash/trash.j3o", shootables);
            //e.setPhysics(bulletAppState);
            e.spawn(rootNode, shootables);
            entities.add(e);

            e = new Sprout(new Vector3f(90, 0, 90), 0.6f, new Vector3f(10, 8, 5));
            e.setModel(assetManager, rootNode, "Models/sprout/sprout.j3o", shootables);
            //e.setPhysics(bulletAppState);
            e.spawn(rootNode, shootables);
            entities.add(e);

            e = new Sprout(new Vector3f(200, 0, 0), 0.6f, new Vector3f(10, 8, 5));
            e.setModel(assetManager, rootNode, "Models/sprout/sprout.j3o", shootables);
            //e.setPhysics(bulletAppState);
            e.spawn(rootNode, shootables);
            entities.add(e);

            e = new Mill(new Vector3f(-100, 0, -100), 200, new Vector3f(40, 77, 40));
            e.setModel(assetManager, rootNode, "Models/mill/mill.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);

            e = new Well(new Vector3f(-100, 0 , 100), 15, new Vector3f(20, 20, 20));
            e.setModel(assetManager, rootNode, "Models/well/well.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);
        } else {
            p = db.queryPlayer();
            es = db.queryEntity();
            
            for (EntitySerialization s: es) {
                Entity e = new Entity();
                if ("Trash".equals(s.getTypeOfEntity())) {
                    e = new Trash(s.getPosition(), s.getScale(), s.getPickboxSize());
                    e.setModel(assetManager, rootNode, "Models/trash/trash.j3o", shootables);
                }
                if ("Sprout".equals(s.getTypeOfEntity())) {
                    e = new Sprout(s.getPosition(), s.getScale(), s.getPickboxSize());
                    e.setModel(assetManager, rootNode, "Models/sprout/sprout.j3o", shootables);
                }
                if ("Mill".equals(s.getTypeOfEntity())) {
                    e = new Mill(s.getPosition(), s.getScale(), s.getPickboxSize());
                    e.setModel(assetManager, rootNode, "Models/mill/mill.j3o", shootables);
                }
                if ("Well".equals(s.getTypeOfEntity())) {
                    e = new Well(s.getPosition(), s.getScale(), s.getPickboxSize());
                    e.setModel(assetManager, rootNode, "Models/well/well.j3o", shootables);
                }
                e.spawn(rootNode, shootables);
                entities.add(e);
            }
        }
            //try {
                /*FileInputStream fileInApple = new FileInputStream(appleFilePath);
                FileInputStream fileInMill = new FileInputStream(millFilePath);
                FileInputStream fileInSmallTree = new FileInputStream(smallTreeFilePath);
                FileInputStream fileInSprout = new FileInputStream(sproutFilePath);
                FileInputStream fileInTrash = new FileInputStream(trashFilePath);
                FileInputStream fileInTree = new FileInputStream(treeFilePath);
                FileInputStream fileInWell = new FileInputStream(wellFilePath);

                ObjectInputStream readerApple = new ObjectInputStream(fileInApple);
                ObjectInputStream readerMill = new ObjectInputStream(fileInMill);
                ObjectInputStream readerSmallTree = new ObjectInputStream(fileInSmallTree);
                ObjectInputStream readerSprout = new ObjectInputStream(fileInSprout);
                ObjectInputStream readerTrash = new ObjectInputStream(fileInTrash);
                ObjectInputStream readerTree = new ObjectInputStream(fileInTree);
                ObjectInputStream readerWell = new ObjectInputStream(fileInWell);*/

               /* FileInputStream fileIn = new FileInputStream(filePath);
                ObjectInputStream reader = new ObjectInputStream(fileIn);
                
                while (true) {
                    try {
                        EntitySerialization s = (EntitySerialization) reader.readObject();
                        System.out.println(reader.readObject());
                        es.add(s);
                    } catch (Exception ex) {
                        System.out.println("end of reader file ");
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            for (EntitySerialization s: es) {
                Entity e = new Entity();
                if ("Trash".equals(s.getTypeOfEntity())) {
                    e = new Trash(s.getPosition(), s.getScale(), s.getPickboxSize());
                    e.setModel(assetManager, rootNode, "Models/trash/trash.j3o", shootables);
                }
                if ("Sprout".equals(s.getTypeOfEntity())) {
                    e = new Sprout(s.getPosition(), s.getScale(), s.getPickboxSize());
                    e.setModel(assetManager, rootNode, "Models/sprout/sprout.j3o", shootables);
                }
                if ("Mill".equals(s.getTypeOfEntity())) {
                    e = new Mill(s.getPosition(), s.getScale(), s.getPickboxSize());
                    e.setModel(assetManager, rootNode, "Models/mill/mill.j3o", shootables);
                }
                if ("Well".equals(s.getTypeOfEntity())) {
                    e = new Well(s.getPosition(), s.getScale(), s.getPickboxSize());
                    e.setModel(assetManager, rootNode, "Models/well/well.j3o", shootables);
                }
                e.spawn(rootNode, shootables);
                entities.add(e);
            }
            
        }*/
        
        
        
        gui = new Gui(assetManager, inputManager, audioRenderer, guiViewPort);
   
        // Create scene and terrain
        
        scene = new Scene(assetManager, rootNode);

        // Create Sky, Sun and Moon and Ambient Light
        
        sky = new DynamicSky(assetManager, viewPort, rootNode);
        rootNode.attachChild(sky);
        
        view = new View(assetManager, rootNode);
        
        
        // Add water pound
        
        pound = new Pound(assetManager, rootNode, viewPort, shootables, new Vector3f(60, 5, 60));
        
        
        // Add ocean
        
        ocean = new Ocean(rootNode, viewPort, view.getFilterPostProcessor(), sky);
       
        
        initKeys();       // load custom key mappings
    }
    
    @Override
    public void stop() {
        
        /*try {      
            
            FileOutputStream fileOut = new FileOutputStream(playerFilePath);
            ObjectOutputStream writer = new ObjectOutputStream(fileOut);
            writer.writeObject(p);
            writer.close();
            fileOut.close();
            
            System.out.println("The Player  was succesfully written to a file");
            System.out.println(p.toString());
            System.out.println(playerFilePath);
 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        es = new ArrayList<>();
        
        try {*/
            /*FileOutputStream fileOutApple = new FileOutputStream(appleFilePath);
            FileOutputStream fileOutMill = new FileOutputStream(millFilePath);
            FileOutputStream fileOutSmallTree = new FileOutputStream(smallTreeFilePath);
            FileOutputStream fileOutSprout = new FileOutputStream(sproutFilePath);
            FileOutputStream fileOutTrash = new FileOutputStream(trashFilePath);
            FileOutputStream fileOutTree = new FileOutputStream(treeFilePath);
            FileOutputStream fileOutWell = new FileOutputStream(wellFilePath);
            
            ObjectOutputStream writerApple = new ObjectOutputStream(fileOutApple);
            ObjectOutputStream writerMill = new ObjectOutputStream(fileOutMill);
            ObjectOutputStream writerSmallTree = new ObjectOutputStream(fileOutSmallTree);
            ObjectOutputStream writerSprout = new ObjectOutputStream(fileOutSprout);
            ObjectOutputStream writerTrash = new ObjectOutputStream(fileOutTrash);
            ObjectOutputStream writerTree = new ObjectOutputStream(fileOutTree);
            ObjectOutputStream writerWell = new ObjectOutputStream(fileOutWell);*/
           /* FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream writer = new ObjectOutputStream(fileOut);
            
            for (Entity e: entities) {
                EntitySerialization s = new EntitySerialization();
                s.setPosition(e.getPosition());
                s.setScale(e.getScale());
                s.setPickboxSize(e.getPickboxSize());
                
                if (e instanceof Trash) {
                    System.out.println("Trash: ");
                    s.setTypeOfEntity("Trash");
                }
                if (e instanceof Sprout) {
                    System.out.println("Sprout: ");
                    s.setTypeOfEntity("Sprout");
                }
                if (e instanceof Mill) {
                    System.out.println("Mill: ");
                    s.setTypeOfEntity("Mill");
                }
                if (e instanceof Well) {
                    System.out.println("Well: ");
                    s.setTypeOfEntity("Well");
                }
                /*if (e instanceof SmallTree) {
                   writerSmallTree.writeObject(e);
                }
                if (e instanceof Sprout) {
                   writerSprout.writeObject(e);
                }
                if (e instanceof Trash) {
                   writerTrash.writeObject(e);
                }
                if (e instanceof Tree) {
                   writerTree.writeObject(e);
                   for (Apple a: ((Tree) e).getApples()) {
                       writerApple.writeObject(a);
                   }
                }
                if (e instanceof Well) {
                   writerWell.writeObject(e);
                }*/
               /* es.add(s);
            }
            
            for (EntitySerialization s: es) {
                writer.writeObject(s);
                System.out.println(s);
            }
            
            writer.close();
            fileOut.close();
            
            /*writerApple.close();
            writerMill.close();
            writerSmallTree.close();
            writerSprout.close();
            writerTrash.close();
            writerTree.close();
            writerWell.close();
            
            fileOutApple.close();
            fileOutMill.close();
            fileOutSmallTree.close();
            fileOutSprout.close();
            fileOutTrash.close();
            fileOutTree.close();
            fileOutWell.close();
            
            FileInputStream f = new FileInputStream(millFilePath);
            ObjectInputStream o = new ObjectInputStream(f);
            System.out.println(o.readObject());*/
        /*} catch (Exception ex) {
            ex.printStackTrace();
        }
        
        System.out.println(p.getWaterBucket());*/
        db.dropTables();     
        db.createTables();
        db.insertPlayer(p.getName(), p.getApple(), p.getWaterBucket());
        for(Entity e: entities) {
            EntitySerialization s = new EntitySerialization();
            s.setPosition(e.getPosition());
            s.setScale(e.getScale());
            s.setPickboxSize(e.getPickboxSize());
                
            if (e instanceof Trash) {
                s.setTypeOfEntity("Trash");
            }
            if (e instanceof Sprout) {
                s.setTypeOfEntity("Sprout");
            }
            if (e instanceof Mill) {
                s.setTypeOfEntity("Mill");
            }
            if (e instanceof Well) {
                s.setTypeOfEntity("Well");
            }
            es.add(s);
        }
        
        for (EntitySerialization s: es) {
                db.insertEntity(s.getPosition(), s.getScale(), s.getPickboxSize(), s.getTypeOfEntity());
                System.out.println(s);
            }
        
        super.stop(); // continue quitting the game
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
                        
                        a.modifyPosition(position);
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
                    if (pound.getWaterLocation().y <= -3.5f) {
                        pound.despawn(rootNode, shootables);
                    }
                } else {
                    for (ListIterator<Entity> li = entities.listIterator(); li.hasNext();) {
                        Entity e = li.next();
                        
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
                                    entities.remove(e);
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
                                    Entity st = new SmallTree(e.getPosition(), 3.5f, new Vector3f(30, 40, 21));
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
