package com.game.chora;

import com.game.chora.gui.Gui;
import com.game.chora.items.entities.*;
import com.game.chora.utils.Entity;
import com.game.chora.utils.EntitySerialization;
import com.game.chora.water.Ocean;
import com.game.chora.water.Pound;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
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
    
    static private Main app;
    private Player p;
    private DynamicSky sky = null;
    private PlayerCamera pc;
    private Scene scene;
    private View view;
    private Pound pound;
    private Ocean ocean;
    private Node shootables;
    private Geometry mark;  
    private List<Entity> entities;
    private List<EntitySerialization> es;
    private Gui gui;
    private AudioNode menuAudio;
    private AudioNode dayAudio;
    private AudioNode nightAudio;
    private AudioNode clickAudio;
    
    private int speed = 10;
    
    protected NiftyJmeDisplay niftyDisplay;
    protected Nifty nifty;
    
    protected Path currentRelativePath = Paths.get("");
    protected String filePath = currentRelativePath.toAbsolutePath().toString() + "\\assets\\GameData\\Entity.ser";
    
    private Database db;
    private boolean newGame = false;
    
    public static void main(String[] args) {
        app = new Main();
        app.start(); // start the game
    }
    
    @Override
    public void simpleInitApp() {
        
        //inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        
        pc = new PlayerCamera(cam, flyCam);
        
        shootables = new Node("Shootables");
        rootNode.attachChild(shootables);
        inputManager.setCursorVisible(true);
        
        db = new Database();
        db.createNewDatabase(currentRelativePath.toAbsolutePath().toString() + "\\assets\\GameData\\");
        db.createTables();
        
        p = new Player();
        entities = new ArrayList<>();
        es = new ArrayList<>();
        
                scene = new Scene(assetManager, rootNode, shootables);
        pc.lookAtWorld(cam, scene);
        
        if (db.isPlayerEmpty() == true) {
            System.out.println("No player found. Creating player...");
            db.insertPlayer(p.getName(), p.getApple(), p.getWaterBucket(), p.getWell(), p.getMill(), p.getTakePound(), p.getMusicVolume(), p.getAmbientVolume());
            
            Entity e = new Trash(new Vector3f(0, 0, 0), 8, new Vector3f(25, 10, 25));
            e.setModel(assetManager, rootNode, "Models/trash/trash.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);

            e = new Trash(new Vector3f(50, 0, 50), 8, new Vector3f(25, 10, 25));
            e.setModel(assetManager, rootNode, "Models/trash/trash.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);

            e = new Sprout(new Vector3f(90, 0, 90), 0.6f, new Vector3f(10, 8, 5));
            e.setModel(assetManager, rootNode, "Models/sprout/sprout.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);

            e = new Sprout(new Vector3f(200, 0, 0), 0.6f, new Vector3f(10, 8, 5));
            e.setModel(assetManager, rootNode, "Models/sprout/sprout.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);

            e = new Mill(new Vector3f(-100, 0, -100), 200, new Vector3f(40, 77, 40));
            e.setModel(assetManager, rootNode, "Models/mill/mill.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);
            p.setMill(p.getMill() + 1);
            ((Mill) e).createPopup(assetManager);
            ((Mill) e).showPopup(rootNode);
            
            e = new Well(new Vector3f(-100, 0 , 100), 15, new Vector3f(20, 20, 20));
            e.setModel(assetManager, rootNode, "Models/well/well.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);
            p.setWell(p.getWell() + 1);
            ((Well) e).createPopup(assetManager);
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
                if ("SmallTree".equals(s.getTypeOfEntity())) {
                    e = new SmallTree(s.getPosition(), s.getScale(), s.getPickboxSize());
                    e.setModel(assetManager, rootNode, "Models/small_tree/small_tree.j3o", shootables);
                }
                if ("Tree".equals(s.getTypeOfEntity())) {
                    e = new Tree(s.getPosition(), s.getScale(), s.getPickboxSize());
                    e.setModel(assetManager, rootNode, "Models/tree/tree.j3o", shootables);
                    
                    ImageRaster imageRaster = ImageRaster.create(scene.getAlphaTexture().getImage());

                    for (float i = 512 + s.getPosition().x - 55; i < 512 + s.getPosition().x + 65; i++) {
                        for (float j = 512 - s.getPosition().z - 65 ; j < + 512 - s.getPosition().z + 75; j++) {
                            if (imageRaster.getPixel((int) i, (int) j).r > 0.7) {
                                imageRaster.setPixel((int) i, (int) j, ColorRGBA.Green);
                            }
                        }
                    }
                    scene.getTerrain().updateModelBound();
                }
                if ("Mill".equals(s.getTypeOfEntity())) {
                    e = new Mill(s.getPosition(), s.getScale(), s.getPickboxSize());
                    e.setModel(assetManager, rootNode, "Models/mill/mill.j3o", shootables);
                    p.setMill(p.getMill() + 1);
                    ((Mill) e).createPopup(assetManager);
                    ((Mill) e).showPopup(rootNode);
                }
                if ("Well".equals(s.getTypeOfEntity())) {
                    e = new Well(s.getPosition(), s.getScale(), s.getPickboxSize());
                    e.setModel(assetManager, rootNode, "Models/well/well.j3o", shootables);
                    p.setWell(p.getWell() + 1);
                    ((Well) e).createPopup(assetManager);
                }
                e.spawn(rootNode, shootables);
                entities.add(e);
            }
        }
        
        gui = new Gui(app, assetManager, inputManager, audioRenderer, guiViewPort, rootNode, shootables, entities, p);

        // Create Sky, Sun and Moon and Ambient Light
        
        sky = new DynamicSky(assetManager, viewPort, rootNode);
        rootNode.attachChild(sky);
        
        view = new View(assetManager, rootNode);
        
        // Add audio
        
        menuAudio = new AudioNode(assetManager, "Sounds/menu.wav");
        menuAudio.setPositional(false);
        menuAudio.setLooping(true);
        
        dayAudio = new AudioNode(assetManager, "Sounds/day.wav");
        dayAudio.setPositional(false);
        dayAudio.setLooping(true);
        
        nightAudio = new AudioNode(assetManager, "Sounds/night.wav");
        nightAudio.setPositional(false);
        nightAudio.setLooping(true);
        
        clickAudio = new AudioNode(assetManager, "Sounds/click.wav");
        clickAudio.setPositional(false);
        clickAudio.setLooping(false);
        
        // Add water pound
        
        if(p.getTakePound() < 5 ) {
            pound = new Pound(assetManager, rootNode, viewPort, shootables, new Vector3f(60, 5, 60), p.getTakePound());
        }
        
        
        // Add ocean
        
        ocean = new Ocean(rootNode, viewPort, view.getFilterPostProcessor(), sky);
       
        
        initKeys();       // load custom key mappings
    }
    
    @Override
    public void stop() {
        
        db.clearTablePlayer(); 
        db.clearTableEntity();
        //db.createTables();
        //Player pp = db.queryPlayer();
        es.clear();
        System.out.println("Empty : " + es.isEmpty());
        System.out.println("Entity table empty: " + db.queryEntity().isEmpty());
        
        //System.out.println("Player table empty: " + db.queryPlayer().waterBucket);
        db.insertPlayer(p.getName(), p.getApple(), p.getWaterBucket(), p.getWell(), p.getMill(), p.getTakePound(), p.getAmbientVolume(), p.getMusicVolume());
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
            if (e instanceof SmallTree) {
                s.setTypeOfEntity("SmallTree");
            }
            if (e instanceof Tree) {
                s.setTypeOfEntity("Tree");
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
        
        if (gui.isGameStarted()) {
            pc.checkCameraLimits(cam);

            menuAudio.stop();
            if (sky.isDayTime()) {
                nightAudio.stop();
                dayAudio.play();
                if (p.getMusicVolume()) {
                    dayAudio.setVolume(sky.getDayChanging());
                } else {
                    dayAudio.setVolume(0);
                }
            } else {
                dayAudio.stop();
                nightAudio.play();
                nightAudio.setVolume(sky.getDayChanging());
                if (p.getMusicVolume()) {
                    nightAudio.setVolume(sky.getDayChanging());
                } else {
                    nightAudio.setVolume(0);
                }
            }

            if (!p.getAmbientVolume()) {
                clickAudio.setVolume(0);
            } else {
                clickAudio.setVolume(1);
            }

            view.updateLight(sky);
            sky.updateTime();

            for (Entity e: entities) {
                if (e instanceof Tree) {
                    ((Tree) e).increaseTime(tpf);
                    if (((Tree) e).getTime() >= speed) {
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
                    if (((Mill) e).getState() == 0) {
                        ((Mill) e).rotateMill(rootNode, tpf * 0.3f);
                    }
                    if (((Mill) e).getState() == 1) {
                        ((Mill) e).rotateMill(rootNode, tpf * 3f);
                        ((Mill) e).increaseTime(tpf);
                        if (((Mill) e).getTime() >= 30) {
                            ((Mill) e).resetTime();
                            ((Mill) e).setState(2);
                            speed *= 2;
                        }
                    }
                    if (((Mill) e).getState() == 2) {
                        ((Mill) e).rotateMill(rootNode, tpf * 0.3f);
                        ((Mill) e).increaseTime(tpf);
                        if (((Mill) e).getTime() >= 10) {
                            ((Mill) e).resetTime();
                            ((Mill) e).setState(0);
                            ((Mill) e).showPopup(rootNode);
                        }
                    }
                }
                if (e instanceof Well) {
                    if (((Well) e).getWater() < 5) {
                        ((Well) e).increaseTime(tpf);
                        if (((Well) e).getTime() >= speed) {
                            ((Well) e).resetTime();                        
                            ((Well) e).increaseWater();
                            if (((Well) e).getWater() == 1) {
                                ((Well) e).showPopup(rootNode);
                            }
                        }                    
                    }
                }
            }

            if(gui.getTime() > 0) {
                gui.decreaseTime(tpf);
                if(gui.getTime() <= 0) {
                    gui.getNifty().getCurrentScreen().findElementById("errorLabel").setVisible(false);
                }
            }


            if (gui.getPlaceEntity() == true) {
                // show placing mode
                System.out.println("Placing mode active."); 
            }
        } else {
            menuAudio.play();
            if (p.getMusicVolume()) {
                menuAudio.setVolume(1);
            } else {
                menuAudio.setVolume(0);
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
                            clickAudio.playInstance();
                            Vector3f pt = results.getClosestCollision().getContactPoint();
                            addEntity(gui.getSelectedEntityFromShop(), pt);
                            //gui.getNifty().getCurrentScreen().findElementById("alertLayer").setVisible(false);
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
                            if (pound instanceof Pound) {
                                if (pound.getPickBox().getName().equals(closest)) {
                                    clickAudio.playInstance();
                                    pound.takeWater();
                                    p.setWaterBucket(p.getWaterBucket() + 1);
                                    gui.setWaterBucket(p.getWaterBucket());
                                    p.setTakePound(p.getTakePound() + 1);
                                    if (pound.getWaterLocation().y <= -3.5f) {
                                        pound.despawn(rootNode, shootables);
                                    }
                                }
                            } else {
                                for (ListIterator<Entity> li = entities.listIterator(); li.hasNext();) {
                                    Entity e = li.next();

                                    if (e != null) {                                

                                        // Entity clicked is Apple
                                        if (e instanceof Tree) {
                                            for (Apple a: ((Tree) e).getApples()) {
                                                if (a.getPickBox().get(0).getName().equals(closest)) {
                                                    clickAudio.playInstance();
                                                    a.onAction(rootNode, shootables);
                                                    p.setApple(p.getApple() + 1);
                                                    gui.setApple(p.getApple());
                                                    break;
                                                }
                                            }
                                        }

                                        if (e.getPickBox().get(0).getName().equals(closest)) {

                                            clickAudio.playInstance();
                                            
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

                                                //System.out.println(t.getPosition().x - t.getPickboxSize().x);
                                                //System.out.println(t.getPosition().x + t.getPickboxSize().x);

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
                                            
                                            // Entity clicked is Well
                                            if (e instanceof Well) {
                                                p.setWaterBucket( p.getWaterBucket() + ((Well) e).getWater());
                                                ((Well) e).hidePopup(rootNode);
                                                ((Well) e).setWater(0);
                                                gui.setWaterBucket(p.getWaterBucket());
                                                break;
                                            }
                                            
                                            // Entity clicked is Mill
                                            if (e instanceof Mill) {
                                                if (((Mill) e).getState() == 0) {
                                                    ((Mill) e).setState(1);
                                                    speed /= 2;
                                                    ((Mill) e).hidePopup(rootNode);
                                                }
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
        
        if ("Sprout".equals(selectedEntity)) {
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
                gui.getNifty().getCurrentScreen().findElementById("placingModePanel").setVisible(false);
            
                for (float i = 512 + position.x - 55; i < 512 + position.x + 65; i++) {
                    for (float j = 512 - position.z - 65 ; j < + 512 - position.z + 75; j++) {
                        scene.getCoveredArea()[(int) i][(int) j] = true;                        
                    }
                }
            }
            
        }
        if ("Mill".equals(selectedEntity)) {
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
                p.setMill(p.getMill() + 1);
                ((Mill) e).createPopup(assetManager);
                // serve la riga dopo? 
                gui.setPlaceEntity(false);
                
                gui.getNifty().getCurrentScreen().findElementById("placingModePanel").setVisible(false);
                
                for (float i = 512 + position.x - 55; i < 512 + position.x + 65; i++) {
                    for (float j = 512 - position.z - 65 ; j < + 512 - position.z + 75; j++) {
                        scene.getCoveredArea()[(int) i][(int) j] = true;
                    }
                }
            }
            
        }
        if ("Well".equals(selectedEntity)) {
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
                p.setWell(p.getWell() + 1);
                ((Well) e).createPopup(assetManager);
                gui.setPlaceEntity(false);
                gui.getNifty().getCurrentScreen().findElementById("placingModePanel").setVisible(false);
                
                for (float i = 512 + position.x - 55; i < 512 + position.x + 65; i++) {
                    for (float j = 512 - position.z - 65 ; j < + 512 - position.z + 75; j++) {
                        scene.getCoveredArea()[(int) i][(int) j] = true;
                    }
                }
            }
            
        } 
    }
}
