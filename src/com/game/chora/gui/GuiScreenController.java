package com.game.chora.gui;

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
        
    }
    
    public void cancelExit() {
        
    }
    
    public void buyFromShop(String elementId) {
        
    }

}
