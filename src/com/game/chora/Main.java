package com.game.chora;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args){
        Main app = new Main();
        app.start(); // start the game
    }

    private Node shootables;
    
    @Override
    public void simpleInitApp() {
        rootNode.attachChild(SkyFactory.createSky(getAssetManager(), "Textures/Sky/BrightSky.dds", false ));
        //stateManager.attach(new BaseLevel(this));
        flyCam.setMoveSpeed(10);
        flyCam.setRotationSpeed(3);
        flyCam.setDragToRotate(true);
        
        shootables = new Node("Shootables");
        rootNode.attachChild(shootables);
        
        
                
        Spatial scene = assetManager.loadModel("Scenes/map.j3o");
        scene.setShadowMode(ShadowMode.CastAndReceive);
        rootNode.attachChild(scene);
        
        /*
        Texture west = getAssetManager().loadTexture("Textures/Sky/west_q.png");
        Texture east = getAssetManager().loadTexture("Textures/Sky/east_q.png");
        Texture north = getAssetManager().loadTexture("Textures/Sky/front_q.png");
        Texture south = getAssetManager().loadTexture("Textures/Sky/back_q.png");
        Texture up = getAssetManager().loadTexture("Textures/Sky/top_q.png");
        Texture down = getAssetManager().loadTexture("Textures/Sky/bottom_q.png");
        rootNode.attachChild(SkyFactory.createSky(getAssetManager(), west, east, north, south, up, down));
        */
        
        Spatial floor = rootNode.getChild("terrain-map");
        shootables.attachChild(floor);
        
        Material matTrunk = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        matTrunk.setColor("Color", new ColorRGBA(.67f, .41f, .10f, 1.0f));
        
        Material matLeaves = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        matLeaves.setColor("Color", new ColorRGBA(.25f, .72f, .18f, 1.0f));   
        
        Spatial model = assetManager.loadModel("Models/cartoon_lowpoly_trees_blend/cartoon_lowpoly_trees_blend.j3o");
        model.setShadowMode(ShadowMode.CastAndReceive);
        //model.setMaterial(mat);
  
        rootNode.attachChild(model);
        
        Spatial trunk = rootNode.getChild("Tree_2_1");
        trunk.setMaterial(matTrunk);
        
        Spatial leaves = rootNode.getChild("Tree_2_0");
        leaves.setMaterial(matLeaves);
        
        Spatial albelello = assetManager.loadModel("Models/small_tree/small_tree.j3o");
        albelello.setShadowMode(ShadowMode.CastAndReceive);
        albelello.move(-20, 0, 0);
        rootNode.attachChild(albelello);
        
        Spatial mela = assetManager.loadModel("Models/apple01/apple01.j3o");
        mela.setShadowMode(ShadowMode.CastAndReceive);
        mela.move(5, 0, 0);
        mela.scale(3f);
        rootNode.attachChild(mela);
        
        Spatial pozzo = assetManager.loadModel("Models/pozzo/pozzo.j3o");
        pozzo.setShadowMode(ShadowMode.CastAndReceive);
        pozzo.move(50, 0, 50);
        rootNode.attachChild(pozzo);
        
        Spatial germoglio = assetManager.loadModel("Models/sprout/sprout.j3o");
        germoglio.setShadowMode(ShadowMode.CastAndReceive);
        germoglio.move(-10, 0, 0);
        germoglio.scale(0.2f);
        rootNode.attachChild(germoglio);
        
        /*Spatial model_1 = assetManager.loadModel("Models/cartoon_lowpoly_trees_blend/Tree_2_0.cartoon_lowpoly_trees_blend.j3o");
        model_1.setShadowMode(ShadowMode.CastAndReceive);
        model_1.setMaterial(mat);
        model_1.setLocalTranslation(10,10,10);*/
        
        
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(2f));
        Vector3f lightPos = new Vector3f(-1,-1,-1).normalizeLocal();
        sun.setDirection(lightPos);
        rootNode.addLight(sun);
        
        final int SHADOWMAP_SIZE=2048;
        
               
       
        /*
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        dlsr.setLambda(0.55f);
        dlsr.setShadowIntensity(0.8f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.Nearest);
        viewPort.addProcessor(dlsr);
        */
        
        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        dlsf.setLight(sun);
        dlsf.setLambda(0.55f);
        dlsf.setShadowIntensity(0.8f);
        dlsf.setRenderBackFacesShadows(true);
        dlsf.setEnabledStabilization(true);
        dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        
        viewPort.addProcessor(fpp);
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(.1f));
        rootNode.addLight(al);
        
        //FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        //SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 43.92f, 0.33f, 0.61f);
        //fpp.addFilter(ssaoFilter);
        //viewPort.addProcessor(fpp);  
        
        
        
        SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(rootNode);
        waterProcessor.setDebug(false);
        waterProcessor.setLightPosition(lightPos);
        waterProcessor.setRefractionClippingOffset(1.0f);


        //setting the water plane
        Vector3f waterLocation=new Vector3f(0,-20,0);
        waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));       
        waterProcessor.setWaterColor(ColorRGBA.Brown);
        waterProcessor.setDebug(false);
        
        Quad quad = new Quad(400,400);

        //the texture coordinates define the general size of the waves
        quad.scaleTextureCoordinates(new Vector2f(6f,6f));

        Geometry water=new Geometry("water", quad);
        water.setShadowMode(ShadowMode.Receive);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setMaterial(waterProcessor.getMaterial());
        water.setLocalTranslation(-200, -20, 250);
        
        fpp = new FilterPostProcessor(assetManager);
        WaterFilter waterPP = new WaterFilter(rootNode, lightPos); // LightDir
        waterPP.setWaterHeight(-40);
        fpp.addFilter(waterPP);
        viewPort.addProcessor(fpp);

        rootNode.attachChild(water);

        viewPort.addProcessor(waterProcessor);
        
       
        
        
        
        // add action listener for mouse click 
        // to all easier custom mapping
        inputManager.addMapping("mouseClick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(new ActionListener() {
            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if(isPressed){
                    Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
                    Vector3f direction = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
                    direction.subtractLocal(origin).normalizeLocal();

                    Ray ray = new Ray(origin, direction);
                    CollisionResults results = new CollisionResults();
                    shootables.collideWith(ray, results);
                    if (results.size() > 0) {
                        addSprout(results.getClosestCollision().getContactPoint());
                    }
                   
                }
            }
        }, "mouseClick");
       
    }
    
    @Override
     public void simpleUpdate(float tpf){
        Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
        System.out.println(origin);
     }
    
     
     
    public void addSprout(Vector3f coordinate){
        Spatial newmodel = assetManager.loadModel("Models/mill/mill.j3o");
        newmodel.setShadowMode(ShadowMode.CastAndReceive);
        newmodel.scale(50f);
        //model.setMaterial(mat);
        newmodel.setLocalTranslation(coordinate);
        
        rootNode.attachChild(newmodel);
    }
}