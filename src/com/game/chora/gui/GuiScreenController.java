package com.game.chora.gui;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * GuiScreenController manage all events that only interact
 * with other gui elements with no impact on the game.
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class GuiScreenController implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    
    /**
     * set up the class with current nifty and screen objects.
     * @param nifty
     * @param screen
     */
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    /**
     * set all operations needed when starting the screen.
     */
    @Override
    public void onStartScreen() {
        
    }

    /**
     * set all opeartions needed when closing the screen.
     */
    @Override
    public void onEndScreen() {
        
    }
    
    /**
     * show menu layer.
     */
    public void openMenu() {
        nifty.getScreen("game").findElementById("menuLayer").setVisible(true);
        nifty.getScreen("game").findElementById("interactiveMenuLayer").setVisible(true);
    }
    
    /**
     * show credits layer.
     */
    public void openCredits() {
        nifty.getScreen("game").findElementById("menuLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveMenuLayer").setVisible(false);
        
        nifty.getScreen("game").findElementById("creditsLayer").setVisible(true);
        nifty.getScreen("game").findElementById("interactiveCreditsLayer").setVisible(true);
    }
    
    /**
     * show options layer.
     */
    public void openOptions() {
        nifty.getCurrentScreen().findElementById("optionsLayer").setVisible(true);
        nifty.getCurrentScreen().findElementById("interactiveOptionsLayer").setVisible(true);
    }
    
    /**
     * hide menu layer.
     */
    public void closeMenu() {
        nifty.getScreen("game").findElementById("menuLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveMenuLayer").setVisible(false);
    }
    
    /**
     * hide shop layer.
     */
    public void closeShop() {
        nifty.getScreen("game").findElementById("shopLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveShopLayer").setVisible(false);
    }
    
    /**
     * hide credits layer.
     */
    public void closeCredits() {
        nifty.getScreen("game").findElementById("creditsLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveCreditsLayer").setVisible(false);
    }
    
    /**
     * hide options layer.
     */
    public void closeOptions() {
        nifty.getCurrentScreen().findElementById("optionsLayer").setVisible(false);
        nifty.getCurrentScreen().findElementById("interactiveOptionsLayer").setVisible(false);
    }
    
    /**
     * open the close game layer.
     */
    public void exitGame() {
        nifty.getScreen("game").findElementById("menuLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveMenuLayer").setVisible(false);
        
        nifty.getScreen("game").findElementById("closeGameLayer").setVisible(true);
        nifty.getScreen("game").findElementById("interactiveCloseGameLayer").setVisible(true);
    }
    
    /**
     * hide the close game layer.
     */
    public void cancelExit() {
        nifty.getScreen("game").findElementById("closeGameLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveCloseGameLayer").setVisible(false);
    }
}
