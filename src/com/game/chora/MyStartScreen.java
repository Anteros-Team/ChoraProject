package com.game.chora;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;

public class MyStartScreen extends BaseAppState {
    
    protected NiftyJmeDisplay niftyDisplay;
    protected Nifty nifty;
    
    @Override
    protected void initialize(Application arg0) {
        //It is technically safe to do all initialization and cleanup in the
        //onEnable()/onDisable() methods. Choosing to use initialize() and
        //cleanup() for this is a matter of performance specifics for the
        //implementor.
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    protected void cleanup(Application app) {
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
    }

    //onEnable()/onDisable() can be used for managing things that should
    //only exist while the state is enabled. Prime examples would be scene
    //graph attachment or input listener attachment.
    @Override
    protected void onEnable() {
        niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
                getApplication().getAssetManager(),
                getApplication().getInputManager(),
                getApplication().getAudioRenderer(),
                getApplication().getGuiViewPort());      

        //nifty = niftyDisplay.getNifty();
        //getApplication().getGuiViewPort().addProcessor(niftyDisplay);
        //((SimpleApplication) getApplication()).getFlyByCamera().setDragToRotate(true);
        
       

        /*nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        
        Screen screen = new ScreenBuilder("start") {
        {
        layer(new LayerBuilder("baseLayer") {{
            childLayoutCenter();
            panel(new PanelBuilder() {{
                height("50px");
                backgroundColor("#f00f");
                }});
            }});
        }
        }.build(nifty);*/
        
         

        // <screen>
        /*nifty.addScreen("Screen_ID", new ScreenBuilder("Hello Nifty Screen"){{
            controller(new DefaultScreenController()); // Screen properties

            // <layer>
            layer(new LayerBuilder("Layer_ID") {{
                childLayoutVertical(); // layer properties, add more...

                // <panel>
                panel(new PanelBuilder("Panel_ID") {{
                   childLayoutCenter(); // panel properties, add more...

                    // GUI elements
                    control(new ButtonBuilder("Button_ID", "Hello Nifty"){{
                        alignCenter();
                        valignCenter();
                        height("5%");
                        width("15%");
                    }});

                    //.. add more GUI elements here

                }});
                // </panel>
              }});
            // </layer>
          }}.build(nifty));
        // </screen>*/

        //nifty.gotoScreen("Screen_ID"); // start the screen
        //getApplication().getGuiViewPort().addProcessor(niftyDisplay);
    }
    
    private static Screen createIntroScreen(final Nifty nifty) {
    Screen screen = new ScreenBuilder("start") {
      {
        layer(new LayerBuilder("baseLayer") {{
            childLayoutCenter();
            panel(new PanelBuilder() {{
            height("8px");
            backgroundColor("#f00f");
            }});
        }});
      }
    }.build(nifty);
    return screen;
  }

    @Override
    protected void onDisable() {
        //Called when the state was previously enabled but is now disabled
        //either because setEnabled(false) was called or the state is being
        //cleaned up.
    }

    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }
}
