package com.game.chora.gui;

import com.game.chora.items.entities.Sprout;
import com.game.chora.utils.Entity;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class GuiScreenController implements ScreenController {

    protected Nifty nifty;
    protected Screen screen;
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        
    }

    @Override
    public void onEndScreen() {
        
    }
    
    public void openMenu() {
        nifty.getCurrentScreen().findElementById("menuLayer").setVisible(true);
        nifty.getCurrentScreen().findElementById("interactiveMenuLayer").setVisible(true);
        
    }
    
    public void openShop() {
        nifty.getCurrentScreen().findElementById("shopLayer").setVisible(true);
        nifty.getCurrentScreen().findElementById("interactiveShopLayer").setVisible(true);
    }
    
    public void openCredits() {
        
    }
    
    public void openOptions() {
        
    }
    
    public void openExit() {
        
    }
    
    public void closeMenu() {
        nifty.getCurrentScreen().findElementById("menuLayer").setVisible(false);
        nifty.getCurrentScreen().findElementById("interactiveMenuLayer").setVisible(false);
    }
    
    public void closeShop() {
        nifty.getCurrentScreen().findElementById("shopLayer").setVisible(false);
        nifty.getCurrentScreen().findElementById("interactiveShopLayer").setVisible(false);
    }
    
    public void closeCredits() {
        
    }
    
    public void closeOptions() {
        
    }
    
    public void exitGame() {
        nifty.getCurrentScreen().findElementById("menuLayer").setVisible(false);
        nifty.getCurrentScreen().findElementById("interactiveMenuLayer").setVisible(false);
        
        nifty.getCurrentScreen().findElementById("closeGameLayer").setVisible(true);
        nifty.getCurrentScreen().findElementById("interactiveCloseGameLayer").setVisible(true);
    }
    
    public void cancelExit() {
        nifty.getCurrentScreen().findElementById("closeGameLayer").setVisible(false);
        nifty.getCurrentScreen().findElementById("interactiveCloseGameLayer").setVisible(false);
    }
    
    public void buyFromShop(String elementId, AssetManager assetManager, Node rootNode, Node shootables) {
        
        // TODO: check if have sufficient apples, remove apples
        
        this.closeShop();
        Entity e = new Sprout(new Vector3f(190, 0, 190), 0.6f, new Vector3f(10, 8, 5));
        e.setModel(assetManager, rootNode, "Models/sprout/sprout.j3o", shootables);
        e.spawn(rootNode, shootables);
        //entities.add(e);
    }

}
