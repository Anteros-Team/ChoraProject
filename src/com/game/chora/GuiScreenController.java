package com.game.chora;

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
    

}
