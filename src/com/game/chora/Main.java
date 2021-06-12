package com.game.chora;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.ChaseCamera;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.material.TechniqueDef;
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
import com.jme3.shadow.BasicShadowRenderer;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import com.jme3.texture.Texture.WrapMode;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    DynamicSky sky = null;
    BasicShadowRenderer bsr = null;
    private Node shootables;
    DirectionalLightShadowFilter dlsf;
    
    public static void main(String[] args){
        Main app = new Main();
        app.start(); // start the game
    }
    
    @Override
    public void simpleInitApp() {
        
        sky = new DynamicSky(assetManager, viewPort, rootNode);
        rootNode.attachChild(sky);
        
        //rootNode.attachChild(SkyFactory.createSky(getAssetManager(), "Textures/SkyOld/BrightSky.dds", false ));
        //stateManager.attach(new BaseLevel(this));
        flyCam.setDragToRotate(true);
        
        
        ChaseCamera chaseCam = new ChaseCamera(cam, rootNode, inputManager);
        chaseCam.setMinDistance(500);
        
        shootables = new Node("Shootables");
        rootNode.attachChild(shootables);
        
        cam.setFrustumFar(100000f);
                
        Spatial scene = assetManager.loadModel("Scenes/map.j3o");
//        scene.setShadowMode(ShadowMode.CastAndReceive);
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
        
        /*Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setFloat("Shininess", 12);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient",  new ColorRGBA(42, 117, 33, 1));
        mat.setColor("Diffuse",  new ColorRGBA(17, 83, 158, 1));
        mat.setColor("Specular",  new ColorRGBA(42, 117, 33, 1));*/
        
               
        Material mat_terrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        
        Texture grass = assetManager.loadTexture("Textures/Terrain/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex1Scale", 64f);
        
        
        Spatial floor = rootNode.getChild("terrain-map");
        shootables.attachChild(floor);
        floor.setShadowMode(ShadowMode.Receive);
        //floor.setMaterial(mat_terrain);  
        
        
        Material matTrunk = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        //matTrunk.setColor("Color", new ColorRGBA(.67f, .41f, .10f, 1.0f));        
        
        Material matLeaves = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        matLeaves.setColor("Color", new ColorRGBA(.25f, .72f, .18f, 1.0f));   
        
        
        Spatial model = assetManager.loadModel("Models/cartoon_lowpoly_trees_blend/cartoon_lowpoly_trees_blend.j3o");
        model.setShadowMode(ShadowMode.CastAndReceive);        
        renderManager.setPreferredLightMode(TechniqueDef.LightMode.SinglePass);
        
        matLeaves.selectTechnique(INPUT_MAPPING_EXIT, renderManager);
       
       // model.setMaterial(mat_t);
  
        rootNode.attachChild(model);
        
        Spatial trunk = rootNode.getChild("Tree_2_1");
        //trunk.setMaterial(matTrunk);
        
        Spatial leaves = rootNode.getChild("Tree_2_0");
        //leaves.setMaterial(matLeaves);
        
        
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
        
        
        /*
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(2f));
        Vector3f lightPos = new Vector3f(-1,-1,-1).normalizeLocal();
        sun.setDirection(lightPos);
        rootNode.addLight(sun);
        */
        
        final int SHADOWMAP_SIZE=512;   
       
        
        /*DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        
        dlsr.setLambda(0.55f);
        dlsr.setShadowIntensity(0.8f);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.Nearest);
        viewPort.addProcessor(dlsr);    */    
        
        
        dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        //dlsf.setLight(sky.getSunLight());
        dlsf.setLambda(0.55f);
        dlsf.setShadowIntensity(0.5f);
        dlsf.setRenderBackFacesShadows(true);
        dlsf.setEnabledStabilization(true);
        //dlsf.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        dlsf.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);
        
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        
        //viewPort.addProcessor(fpp);
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(.3f));        
        rootNode.addLight(al);
        
       
        
        
        //FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        //SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 43.92f, 0.33f, 0.61f);
        //fpp.addFilter(ssaoFilter);
        //viewPort.addProcessor(fpp);  
        
        
        SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(rootNode);
        waterProcessor.setDebug(false);
        waterProcessor.setLightPosition(sky.getSunDirection());
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
        water.scale(0.5f);
        water.setLocalTranslation(150, -10, 300);
       
        rootNode.attachChild(water);

        viewPort.addProcessor(waterProcessor);
        
        
        WaterFilter waterPP = new WaterFilter(rootNode, sky.getSunDirection()); // LightDir
        waterPP.setWaterHeight(-40);
        fpp.addFilter(waterPP);
        viewPort.addProcessor(fpp);
        
        
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
        
        rootNode.setShadowMode(ShadowMode.Off);
        bsr = new BasicShadowRenderer(assetManager, 1024);
        bsr.setDirection(new Vector3f(-1, -1, -1).normalizeLocal());
        viewPort.addProcessor(bsr);
       
    }
    
    @Override
     public void simpleUpdate(float tpf){
        dlsf.setLight(sky.getSunLight());
        System.out.println("SKY_LIGHT: "+sky.getSunLight());
        if(sky.getSunLight().getDirection().y < 0.05){
            dlsf.setLight(sky.getSunLight());
        }
        else
            dlsf.setLight(sky.getMoonLight());
        sky.updateTime();
        bsr.setDirection(sky.getSunDirection().normalize().mult(-1));
        Vector3f origin = cam.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
        System.out.println(origin);
     }
    
     
     
    public void addSprout(Vector3f coordinate){
        Spatial newmodel = assetManager.loadModel("Models/hoe/hoe.j3o");
        newmodel.setShadowMode(ShadowMode.CastAndReceive);
        newmodel.scale(50f);
        //model.setMaterial(mat);
        newmodel.setLocalTranslation(coordinate);
        
        rootNode.attachChild(newmodel);
    }

    private Object getAdditionalRenderState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
