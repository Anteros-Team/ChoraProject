package com.game.chora;

import com.game.chora.gui.Gui;
import com.game.chora.items.entities.*;
import com.game.chora.utils.Entity;
import com.game.chora.utils.EntitySerialization;
import com.game.chora.utils.ItemBillboard;
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
import com.jme3.system.AppSettings;
import com.jme3.texture.image.ImageRaster;
import de.lessvoid.nifty.Nifty;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
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
    private ItemBillboard arrow;
    private int speed = 10;
    private NiftyJmeDisplay niftyDisplay;
    private Nifty nifty;
    private Path relativePath = Paths.get("");
    
    private Database db;
    private boolean newGame = false;
    
    /**
     * main constructor.
     * @param args
     */
    public static void main(String[] args) {
        app = new Main();
        
        AppSettings a = new AppSettings(true);
        //a.setResolution(1920, 1080);
        //a.setFullscreen(true);
        a.setVSync(true);
        a.setTitle("Chora");
        
        app.setSettings(a);
        app.setShowSettings(false);
        
        app.start(); // start the game
    }
    
    /**
     * app initialization.
     */
    @Override
    public void simpleInitApp() {
        
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        shootables = new Node("Shootables");
        rootNode.attachChild(shootables);
        inputManager.setCursorVisible(true);
        
        /**
         * Player camera initialization.
         * 
         * @see PlayerCamera
         */
        pc = new PlayerCamera(cam, flyCam);
        
        /**
         * Database object initialization.
         * 
         * @see Database
         */
        db = new Database();
        db.createNewDatabase(relativePath.toAbsolutePath().toString());
        db.createTables();
        
        p = new Player();
        entities = new ArrayList<>();
        es = new ArrayList<>();
        
        /**
         * Scene object initialization.
         * 
         * @see Scene
         */
        scene = new Scene(assetManager, rootNode, shootables);
        pc.lookAtWorld(cam, scene);
        
        /**
         * Database first interactions.
         * If no player is found, the game was opened for the
         * first time, so player starts with default data and initial 
         * entities.
         * If player is found, the game load player data and spawn all
         * saved entities.
         * 
         * @see Database
         */
        if (db.isPlayerEmpty() == true) {
            System.out.println("No player found. Creating player...");
            db.insertPlayer(p.getName(), p.getApple(), p.getWaterBucket(), p.getWell(), p.getMill(), p.getTakePound(), p.getMusicVolume(), p.getAmbientVolume(), p.getTutorial());
            
            Entity e = new Trash(new Vector3f(0, 0, 0), 8, new Vector3f(25, 10, 25));
            e.setModel(assetManager, rootNode, "Models/trash/trash.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);
            
            e = new Trash(new Vector3f(-50, 0, -50), 8, new Vector3f(25, 10, 25));
            e.setModel(assetManager, rootNode, "Models/trash/trash.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);
            
            e = new Trash(new Vector3f(-80, 0, 50), 8, new Vector3f(25, 10, 25));
            e.setModel(assetManager, rootNode, "Models/trash/trash.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);
            
            e = new Trash(new Vector3f(-50, 0, 100), 8, new Vector3f(25, 10, 25));
            e.setModel(assetManager, rootNode, "Models/trash/trash.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);

            e = new Trash(new Vector3f(90, 0, 90), 8, new Vector3f(25, 10, 25));
            e.setModel(assetManager, rootNode, "Models/trash/trash.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);

            e = new Sprout(new Vector3f(90, 0, 90), 0.6f, new Vector3f(10, 8, 5));
            e.setModel(assetManager, rootNode, "Models/sprout/sprout.j3o", shootables);
            e.spawn(rootNode, shootables);
            entities.add(e);
            
            arrow = new ItemBillboard("arrow", 50f, new Vector3f(0, 0, 0), 15f, assetManager, "Interface/gui/arrowGui.png");
            rootNode.attachChild(arrow.getNode());
            
        } else {
            p = db.queryPlayer();
            es = db.queryEntity();
            
            if (p.getTutorial() != 0) {
                if (p.getTutorial() == 1) {
                    arrow = new ItemBillboard("arrow", 50f, new Vector3f(0, 0, 0), 15f, assetManager, "Interface/gui/arrowGui.png");
                }
                if (p.getTutorial() == 2) {
                    arrow = new ItemBillboard("arrow", 50f, new Vector3f(210, -1, 300), 15f, assetManager, "Interface/gui/arrowGui.png");
                }
                if (p.getTutorial() == 3) {
                    arrow = new ItemBillboard("arrow", 50f, new Vector3f(90, 0, 90), 15f, assetManager, "Interface/gui/arrowGui.png");
                }
                if (p.getTutorial() == 4) {
                    arrow = new ItemBillboard("arrow", 50f, new Vector3f(90, 0, 90), 65f, assetManager, "Interface/gui/arrowGui.png");
                }
                if (p.getTutorial() == 5) {
                    arrow = new ItemBillboard("arrow", 50f, new Vector3f(90, 0, 90), 85f, assetManager, "Interface/gui/arrowGui.png");
                }
                rootNode.attachChild(arrow.getNode());
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
        
        /**
         * Gui initialization.
         * 
         * @see Gui
         */
        gui = new Gui(app, assetManager, inputManager, audioRenderer, guiViewPort, rootNode, shootables, entities, p);

        /**
         * Sky, Sun, Moon and stars initliazation.
         * 
         * @see DynamicSky
         */
        sky = new DynamicSky(assetManager, viewPort, rootNode);
        rootNode.attachChild(sky);
        
        /**
         * Light, shadows and post processor filters initialization.
         * 
         * @see View
         */
        view = new View(assetManager, rootNode, viewPort, sky);
        
        /**
         * Audio initialization.
         */
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
        
        
        /**
         * Water Pound initialization.
         * 
         * @see Pound
         */
        if(p.getTakePound() < 5 ) {
            pound = new Pound(assetManager, rootNode, viewPort, shootables, new Vector3f(60, 5, 60), p.getTakePound());
        }
        
        /**
         * Ocean initialization.
         * 
         * @see Ocean
         */
        ocean = new Ocean(rootNode, viewPort, view.getFilterPostProcessor(), sky);
       
        /**
         * load custom key mappings.
         */
        initKeys();
    }
    
    /**
     * operations before game closes.
     */
    @Override
    public void stop() {
        
        /**
         * Saving game state on Database.
         * Tables are cleared and new data is stored.
         * 
         * @see Database
         */
        db.clearTablePlayer(); 
        db.clearTableEntity();
        es.clear();
        System.out.println("Empty : " + es.isEmpty());
        System.out.println("Entity table empty: " + db.queryEntity().isEmpty());

        db.insertPlayer(p.getName(), p.getApple(), p.getWaterBucket(), p.getWell(), p.getMill(), p.getTakePound(), p.getAmbientVolume(), p.getMusicVolume(), p.getTutorial());
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
        
        /**
         * continue quitting the game.
         */
        super.stop();
    }
    
    /**
     * game update.
     * @param tpf time per frame
     */
    @Override
    public void simpleUpdate(float tpf) {

        if (gui.isGameStarted()) {
            /**
             * Keep camera inside limits of distance and inclination.
             * 
             * @see PlayerCamera
             */
            pc.checkCameraLimits(cam);

            /**
             * Play audio based on current game state and player will.
             * 
             * If player doesn't turn off music from 
             * option menu, it will be divided in:
             * <ul>
             * <li> menuAudio in game menu
             * <li> dayAudio for in-game day time
             * <li> nightAudio for in-game night time
             * </ul>
             * 
             * If player doesn't turn off ambient audio
             * from option menu:
             *  clickAudio is played on mouse click
             * 
             */
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

            /**
             * Sky, Sun, Moon, Stars and all lights and shadows are
             * updated based on in-game time.
             */
            view.updateLight(sky);
            sky.updateTime();

            /**
             * Update entities section.
             */
            for (Entity e: entities) {
                
                /**
                 * Tree apple spawning update.
                 * 
                 * @see Tree
                 */
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
                
                /**
                 * Mill wheel rotation update.
                 * 
                 * @see Mill
                 */
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
                
                /**
                 * Well water production update.
                 * 
                 * @see Well
                 */
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

            /**
             * Error label timer update.
             * 
             * @see Gui
             */
            if(gui.getTime() > 0) {
                gui.decreaseTime(tpf);
                if(gui.getTime() <= 0) {
                    gui.getNifty().getCurrentScreen().findElementById("errorLabel").setVisible(false);
                }
            }

            /**
             * Placing mode active update.
             * 
             * @see Gui
             */
            if (gui.getPlaceEntity() == true) {
                System.out.println("Placing mode active."); 
            }
            
            /**
             * Gui tutorial update.
             * 
             * @see Gui
             */
            gui.updateTutorial();
        } else {
            /**
             * Play menuAudio if still in menu.
             */
            menuAudio.play();
            if (p.getMusicVolume()) {
                menuAudio.setVolume(1);
            } else {
                menuAudio.setVolume(0);
            }
        }
    }
    
    /**
     * bind keys with specific actions.
     */
    private void initKeys() {
        inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "Shoot");
    }

    /**
     * action listener implementation.
     */
    private final ActionListener actionListener = new ActionListener() {

        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            
            /**
             * Before any game interaction, check if menus are closed to prevent
             * unwanted actions.
             */
            if (name.equals("Shoot") && !keyPressed && gui.getNifty().getCurrentScreen().getScreenId().equals("game")) {
                if (gui.getNifty().getCurrentScreen().findElementById("menuLayer").isVisible() == false && gui.getNifty().getCurrentScreen().findElementById("shopLayer").isVisible() == false) {
                    
                    /**
                     * Collect collisions by founding intersection of 
                     * in-game elements with the ray generated by cursor position. 
                     */
                    CollisionResults results = new CollisionResults();

                    Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
                    Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
                    direction.subtractLocal(origin).normalizeLocal();

                    Ray ray = new Ray(origin, direction);

                    shootables.collideWith(ray, results);

                    if (gui.getPlaceEntity() == true) {
                        /**
                         * If placing mode is active, selected entity is spawned
                         * in the world collided position.
                         */                        
                        System.out.println(results.size());
                        if (results.size() > 0) {
                            clickAudio.playInstance();
                            Vector3f pt = results.getClosestCollision().getContactPoint();
                            addEntity(gui.getSelectedEntityFromShop(), pt);
                        }
                        

                    } else {
                        /**
                         * If placing mode is not active, check the closest element
                         * in the CollisionResults array if not empty.
                         */
                        String closest;
                        for (int i = 0; i < results.size(); i++) {
                            float dist = results.getCollision(i).getDistance();
                            Vector3f pt = results.getCollision(i).getContactPoint();
                            String hit = results.getCollision(i).getGeometry().getName();
                            System.out.println("* Collision #" + i);
                            System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
                        }

                        if (results.size() > 0) {
                            
                            /**
                             * If CollisionResults is not empty, check what kind of
                             * element was clicked.
                             */
                            closest = results.getClosestCollision().getGeometry().getName();

                            Spatial c = rootNode.getChild(closest);                            
                            boolean isPound = false;

                            /**
                             * If the element is an instance of Pound, water is
                             * collected from it and water level is lowered.
                             */
                            if (pound instanceof Pound) {
                                if (pound.getPickBox().getName().equals(closest)) {
                                    isPound = true;
                                    clickAudio.playInstance();
                                    pound.takeWater();
                                    p.setWaterBucket(p.getWaterBucket() + 1);
                                    gui.setWaterBucket(p.getWaterBucket());
                                    p.setTakePound(p.getTakePound() + 1);
                                    if (pound.getWaterLocation().y <= -3.5f) {
                                        pound.despawn(rootNode, shootables);
                                    }
                                    if (p.getTutorial() == 2) {
                                        p.setTutorial(3);
                                        rootNode.detachChild(arrow.getNode());
                                        arrow = new ItemBillboard("arrow", 50f, new Vector3f(90, 0, 90), 15f, assetManager, "Interface/gui/arrowGui.png");
                                        rootNode.attachChild(arrow.getNode());
                                    }

                                }
                            } 
                            if (isPound == false) {
                                for (ListIterator<Entity> li = entities.listIterator(); li.hasNext();) {
                                    Entity e = li.next();

                                    if (e != null) {                                

                                        /**
                                         * If the element is an instance of Apple, an apple
                                         * is collected and the entity despawns.
                                         */
                                        if (e instanceof Tree) {
                                            for (Apple a: ((Tree) e).getApples()) {
                                                if (a.getPickBox().get(0).getName().equals(closest)) {
                                                    clickAudio.playInstance();
                                                    a.onAction(rootNode, shootables);
                                                    p.setApple(p.getApple() + 1);
                                                    gui.setApple(p.getApple());
                                                    
                                                    if (p.getTutorial() == 5) {
                                                        p.setTutorial(6);
                                                        rootNode.detachChild(arrow.getNode());
                                                    }
                                                    break;
                                                }
                                            }
                                        }

                                        if (e.getPickBox().get(0).getName().equals(closest)) {

                                            clickAudio.playInstance();
                                            
                                            /**
                                             * If the element is an instance of Trash, the
                                             * entity despawns.
                                             */
                                            if (e instanceof Trash) {
                                                e.onAction(rootNode, shootables);
                                                entities.remove(e);
                                                if (p.getTutorial() == 1) {
                                                    if (e.getPosition().x == 0 && e.getPosition().z == 0) {
                                                        rootNode.detachChild(arrow.getNode());
                                                    }
                                                    if (e.getPosition().x == 90 && e.getPosition().z == 90) {
                                                        p.setTutorial(2);
                                                        arrow = new ItemBillboard("arrow", 50f, new Vector3f(210, -1, 300), 15f, assetManager, "Interface/gui/arrowGui.png");
                                                        rootNode.attachChild(arrow.getNode());
                                                    }
                                                }
                                                break;
                                            }

                                            /**
                                             * If the element is an instance of SmallTree, it grows
                                             * into a Tree if player water is sufficient.
                                             */
                                            if (e instanceof SmallTree && p.getWaterBucket() > 0) {
                                                Entity t = new Tree(e.getPosition(), 4, new Vector3f(65, 50, 75));
                                                t.setModel(assetManager, rootNode, "Models/tree/tree.j3o", shootables);
                                                e.onAction(rootNode, shootables);
                                                t.spawn(rootNode, shootables);
                                                entities.add(t);
                                                entities.remove(e);
                                                p.setWaterBucket(p.getWaterBucket() - 1);
                                                gui.setWaterBucket(p.getWaterBucket());

                                                /**
                                                 * Terrain around the tree is covered with grass.
                                                 */
                                                ImageRaster imageRaster = ImageRaster.create(scene.getAlphaTexture().getImage());
                                                
                                                for (float i = 512 + t.getPosition().x - 55; i < 512 + t.getPosition().x + 65; i++) {
                                                    for (float j = 512 - t.getPosition().z - 65 ; j < + 512 - t.getPosition().z + 75; j++) {
                                                        if (imageRaster.getPixel((int) i, (int) j).r > 0.7) {
                                                            imageRaster.setPixel((int) i, (int) j, ColorRGBA.Green);
                                                        }
                                                        
                                                    }
                                                }
                                                scene.getTerrain().updateModelBound();
                                                
                                                if (p.getTutorial() == 4) {
                                                    p.setTutorial(5);
                                                    rootNode.detachChild(arrow.getNode());
                                                    arrow = new ItemBillboard("arrow", 50f, new Vector3f(90, 0, 90), 85f, assetManager, "Interface/gui/arrowGui.png");
                                                    rootNode.attachChild(arrow.getNode());
                                                }
                                                break;
                                            }

                                            /**
                                             * If the element is an instance of Sprout, it grows
                                             * into a SmallTree if player water is sufficient.
                                             */
                                            if (e instanceof Sprout && p.getWaterBucket() > 0) {
                                                Entity st = new SmallTree(e.getPosition(), 3.5f, new Vector3f(30, 40, 21));
                                                st.setModel(assetManager, rootNode, "Models/small_tree/small_tree.j3o", shootables);
                                                e.onAction(rootNode, shootables);
                                                st.spawn(rootNode, shootables);
                                                entities.add(st);
                                                entities.remove(e);
                                                p.setWaterBucket(p.getWaterBucket() - 1);
                                                gui.setWaterBucket(p.getWaterBucket());
                                                
                                                if (p.getTutorial() == 3) {
                                                    p.setTutorial(4);
                                                    rootNode.detachChild(arrow.getNode());
                                                    arrow = new ItemBillboard("arrow", 50f, new Vector3f(90, 0, 90), 65f, assetManager, "Interface/gui/arrowGui.png");
                                                    rootNode.attachChild(arrow.getNode());
                                                }
                                                break;
                                            }
                                            
                                            /**
                                             * If the element is an instance of Well, water
                                             * stored is taken.
                                             * 
                                             * @see Well
                                             */
                                            if (e instanceof Well) {
                                                p.setWaterBucket( p.getWaterBucket() + ((Well) e).getWater());
                                                ((Well) e).hidePopup(rootNode);
                                                ((Well) e).setWater(0);
                                                gui.setWaterBucket(p.getWaterBucket());
                                                break;
                                            }
                                            
                                            /**
                                             * If the element is an instance of Mill and if
                                             * Mill state is 0, turn to state 1.
                                             * 
                                             * @see Mill
                                             */
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
                /**
                 * If CollisionResults array is empty, the player
                 * clicked nothing.
                 */
            }
        }
    };
    
    /**
     * Place new entity in a selected position.
     * Also check if the area selected is available.
     * @param selectedEntity
     * @param position
     */
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
