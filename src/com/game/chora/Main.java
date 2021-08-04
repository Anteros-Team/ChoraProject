package com.game.chora;

import com.game.chora.gui.Gui;
import com.game.chora.items.entities.*;
import com.game.chora.utils.Entity;
import com.game.chora.utils.EntitySerialization;
import com.game.chora.water.Ocean;
import com.game.chora.water.Pound;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.image.ImageRaster;
import de.lessvoid.nifty.Nifty;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class Main extends SimpleApplication {
    
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

        
        gui = new Gui(assetManager, inputManager, audioRenderer, guiViewPort, rootNode, shootables, entities);
   
        gui.setApple(p.getApple());
        gui.setWaterBucket(p.getWaterBucket());
        gui.setPlayerName(p.getName());
        
        
        // Create scene and terrain
        
        scene = new Scene(assetManager, rootNode, shootables);

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
        
        db.clearTables();     
        db.createTables();
        Player pp = db.queryPlayer();
        System.out.println("Risposta: " + pp.getName() + "," + pp.getApple() + "," + pp.getWaterBucket());
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
                System.out.println(e);
                ((Mill) e).rotateMill(rootNode, tpf);
            }
        }
        
        if (gui.getPlaceEntity() == true) {
            // show placing mode
            System.out.println("Placing mode active.");
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
            
            if (name.equals("Shoot") && !keyPressed && gui.getNifty().getCurrentScreen().getScreenId().equals("game")) {
                if (gui.getNifty().getCurrentScreen().findElementById("menuLayer").isVisible() == false && gui.getNifty().getCurrentScreen().findElementById("shopLayer").isVisible() == false) {
                    CollisionResults results = new CollisionResults();

                    Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
                    Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
                    direction.subtractLocal(origin).normalizeLocal();

                    Ray ray = new Ray(origin, direction);

                    shootables.collideWith(ray, results);

                    if (gui.getPlaceEntity() == true) {
                        // placing mode
                        System.out.println(results.size());
                        if (results.size() > 0) {
                            Vector3f pt = results.getClosestCollision().getContactPoint();
                            addEntity(gui.getSelectedEntityFromShop(), pt);
                        }
                        

                    } else {
                        // picking mode

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
                                gui.setWaterBucket(p.getWaterBucket());
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
                                                    gui.setApple(p.getApple());
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
                                                gui.setWaterBucket(p.getWaterBucket());

                                                ImageRaster imageRaster = ImageRaster.create(scene.getAlphaTexture().getImage());

                                                System.out.println(t.getPosition().x - t.getPickboxSize().x);
                                                System.out.println(t.getPosition().x + t.getPickboxSize().x);

                                                for (float i = 512 + t.getPosition().x - 55; i < 512 + t.getPosition().x + 65; i++) {
                                                    for (float j = 512 - t.getPosition().z - 65 ; j < + 512 - t.getPosition().z + 75; j++) {
                                                        if (imageRaster.getPixel((int) i, (int) j).r > 0.7) {
                                                            imageRaster.setPixel((int) i, (int) j, ColorRGBA.Green);
                                                        }
                                                        
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
                                                gui.setWaterBucket(p.getWaterBucket());
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // no hit
            }
        }
    };
    
    public void addEntity(String selectedEntity, Vector3f position) {
        System.out.println("Entity = " + selectedEntity);
        System.out.println("Position = " + position);
        
        if ("sprout".equals(selectedEntity)) {
            boolean notAllowedPlace = false;
            for (float i = 512 + position.x - 55; i < 512 + position.x + 65; i++) {
                for (float j = 512 - position.z - 65 ; j < 512 - position.z + 75; j++) {
                    if (i >= 0 && i <= 1024 && j >= 0 && j <= 1024) {
                        if (scene.getCoveredArea()[(int) i][(int) j] == true) {
                            notAllowedPlace = true;
                        }
                    }
                }
            }
            if (scene.getPlaceableArea()[512 + (int) position.x][512 - (int) position.z] == false) {
                notAllowedPlace = true;
            }
                            
            if (notAllowedPlace == false) {
                System.out.println("Placing entity...");
                System.out.println("Sprout spawning.");
                Entity e = new Sprout(position, 0.6f, new Vector3f(10, 8, 5));
                e.setModel(assetManager, rootNode, "Models/sprout/sprout.j3o", this.shootables);
                e.spawn(this.rootNode, this.shootables);
                entities.add(e);
                gui.setPlaceEntity(false);
            
                for (float i = 512 + position.x - 55; i < 512 + position.x + 65; i++) {
                    for (float j = 512 - position.z - 65 ; j < + 512 - position.z + 75; j++) {
                        scene.getCoveredArea()[(int) i][(int) j] = true;
                    }
                }
            }
            
        }
        if ("mill".equals(selectedEntity)) {
            boolean notAllowedPlace = false;
            for (float i = 512 + position.x - 55; i < 512 + position.x + 65; i++) {
                for (float j = 512 - position.z - 65 ; j < + 512 - position.z + 75; j++) {
                    if (i >= 0 && i <= 1024 && j >= 0 && j <= 1024) {
                        if (scene.getPlaceableArea()[(int) i][(int) j] == false || scene.getCoveredArea()[(int) i][(int) j] == true) {
                            notAllowedPlace = true;
                        }
                    }
                }
            }
            
            if (notAllowedPlace == false) {
                System.out.println("Placing entity...");
                System.out.println("Mill spawning.");
                Entity e = new Mill(position, 200, new Vector3f(40, 77, 40));
                e.setModel(assetManager, rootNode, "Models/mill/mill.j3o", shootables);
                e.spawn(rootNode, shootables);
                entities.add(e);
                gui.setPlaceEntity(false);
                
                for (float i = 512 + position.x - 55; i < 512 + position.x + 65; i++) {
                    for (float j = 512 - position.z - 65 ; j < + 512 - position.z + 75; j++) {
                        scene.getCoveredArea()[(int) i][(int) j] = true;
                    }
                }
            }
            
        }
        if ("well".equals(selectedEntity)) {
            boolean notAllowedPlace = false;
            for (float i = 512 + position.x - 55; i < 512 + position.x + 65; i++) {
                for (float j = 512 - position.z - 65 ; j < + 512 - position.z + 75; j++) {
                    if (i >= 0 && i <= 1024 && j >= 0 && j <= 1024) {
                        if (scene.getPlaceableArea()[(int) i][(int) j] == false || scene.getCoveredArea()[(int) i][(int) j] == true) {
                            notAllowedPlace = true;
                        }
                    }
                }
            }
            
            if (notAllowedPlace == false) {
                Entity e = new Well(position, 15, new Vector3f(20, 20, 20));
                e.setModel(assetManager, rootNode, "Models/well/well.j3o", shootables);
                e.spawn(rootNode, shootables);
                entities.add(e);
                gui.setPlaceEntity(false);
                
                for (float i = 512 + position.x - 55; i < 512 + position.x + 65; i++) {
                    for (float j = 512 - position.z - 65 ; j < + 512 - position.z + 75; j++) {
                        scene.getCoveredArea()[(int) i][(int) j] = true;
                    }
                }
            }
            
        } 
    }
}
