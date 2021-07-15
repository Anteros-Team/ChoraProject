package com.game.chora;

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
    BasicShadowRenderer bsr = null;
    DirectionalLightShadowFilter dlsfSun;
    DirectionalLightShadowFilter dlsfMoon;
    final int SHADOWMAP_SIZE = 512;
    private BulletAppState bulletAppState;
    
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
        
        Spatial scene = assetManager.loadModel("Scenes/map.j3o");
        rootNode.attachChild(scene);
        
        Material matTerrain = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");        
        
        Texture dirt = assetManager.loadTexture("Textures/Terrain/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        matTerrain.setTexture("Tex1", dirt);
        
        scene.setMaterial(matTerrain);
        
        /*Spatial floor = rootNode.getChild("terrain-map");
        floor.addControl(new RigidBodyControl(0));
        //bulletAppState.getPhysicsSpace().add(floor.getControl(RigidBodyControl.class));
        bulletAppState.getPhysicsSpace().add(floor);*/
        
        Spatial floor = rootNode.getChild("terrain-map");
        floor.addControl(new RigidBodyControl(0));
        
        bulletAppState.getPhysicsSpace().addAll(floor);
        
        
        
        // Create Sky, Sun and Moon
        
        sky = new DynamicSky(assetManager, viewPort, rootNode);
        rootNode.attachChild(sky);
        
        dlsfMoon = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        //dlsfm.setLight(sky.getMoonLight());
        dlsfMoon.setLambda(0.2f);
        dlsfMoon.setShadowIntensity(0.3f);
        dlsfMoon.setRenderBackFacesShadows(true);
        dlsfMoon.setEnabledStabilization(true);
        dlsfMoon.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);        
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        //shadow distance and fade
        dlsfMoon.setShadowZExtend(650);
        dlsfMoon.setShadowZFadeLength(150);
        fpp.addFilter(dlsfMoon);
        
        dlsfSun = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        //dlsf.setLight(sky.getSunLight());
        dlsfSun.setLambda(0.2f);
        dlsfSun.setShadowIntensity(0.5f);
        dlsfSun.setRenderBackFacesShadows(true);
        dlsfSun.setEnabledStabilization(true);
        //dlsfSun.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        dlsfSun.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);
        
        //shadow distance and fade
        dlsfSun.setShadowZExtend(650);
        dlsfSun.setShadowZFadeLength(150);
        fpp.addFilter(dlsfSun);
        
        
        // Add ambient light
        
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(.3f));        
        rootNode.addLight(al);
        
        
        // Add water pound
        
        SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(rootNode);
        waterProcessor.setDebug(false);
        waterProcessor.setLightPosition(sky.getSunDirection());
        waterProcessor.setRefractionClippingOffset(1.0f);
        
        Vector3f waterLocation=new Vector3f(0,-20,0);
        waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));       
        waterProcessor.setWaterColor(ColorRGBA.Brown);
        waterProcessor.setDebug(false);
        
        Quad quad = new Quad(400,400);
        
        quad.scaleTextureCoordinates(new Vector2f(6f,6f));

        Geometry water=new Geometry("water", quad);
        water.setShadowMode(RenderQueue.ShadowMode.Receive);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setMaterial(waterProcessor.getMaterial());
        water.scale(0.5f);
        water.setLocalTranslation(150, -10, 300);
       
        rootNode.attachChild(water);

        viewPort.addProcessor(waterProcessor);
        
        // Add ocean
        
        WaterFilter waterPP = new WaterFilter(rootNode, sky.getSunDirection()); // LightDir
        waterPP.setWaterHeight(-40);
        fpp.addFilter(waterPP);
        viewPort.addProcessor(fpp);
        
        // Set shadow mode        
        scene.setShadowMode(RenderQueue.ShadowMode.Receive);
        
        //BoundingVolume bvScene = scene.getWorldBound();
        
        // Level 1
        Spatial trash = assetManager.loadModel("Models/trash2/trash2.j3o");                
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
        */        
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
        rootNode.attachChild(trash);
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        dlsfSun.setLight(sky.getSunLight());
        dlsfMoon.setLight(sky.getMoonLight());
        
        sky.updateTime();
    }
}
