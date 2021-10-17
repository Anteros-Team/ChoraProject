package com.game.chora.gui;

import com.game.chora.items.entities.Sprout;
import com.game.chora.utils.Entity;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.elements.render.TextRenderer;
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
        nifty.getScreen("game").findElementById("menuLayer").setVisible(true);
        nifty.getScreen("game").findElementById("interactiveMenuLayer").setVisible(true);
    }
    
    public void openCredits() {
        nifty.getScreen("game").findElementById("menuLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveMenuLayer").setVisible(false);
        
        nifty.getScreen("game").findElementById("creditsLayer").setVisible(true);
        nifty.getScreen("game").findElementById("interactiveCreditsLayer").setVisible(true);
    }
    
    public void openOptions() {
        nifty.getCurrentScreen().findElementById("optionsLayer").setVisible(true);
        nifty.getCurrentScreen().findElementById("interactiveOptionsLayer").setVisible(true);
    }
    
    public void closeMenu() {
        nifty.getScreen("game").findElementById("menuLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveMenuLayer").setVisible(false);
    }
    
    public void closeShop() {
        nifty.getScreen("game").findElementById("shopLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveShopLayer").setVisible(false);
    }
    
    public void closeCredits() {
        nifty.getScreen("game").findElementById("creditsLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveCreditsLayer").setVisible(false);
    }
    
    public void closeOptions() {
        nifty.getCurrentScreen().findElementById("optionsLayer").setVisible(false);
        nifty.getCurrentScreen().findElementById("interactiveOptionsLayer").setVisible(false);
    }
    
    public void exitGame() {
        nifty.getScreen("game").findElementById("menuLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveMenuLayer").setVisible(false);
        
        nifty.getScreen("game").findElementById("closeGameLayer").setVisible(true);
        nifty.getScreen("game").findElementById("interactiveCloseGameLayer").setVisible(true);
    }
    
    public void cancelExit() {
        nifty.getScreen("game").findElementById("closeGameLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveCloseGameLayer").setVisible(false);
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
