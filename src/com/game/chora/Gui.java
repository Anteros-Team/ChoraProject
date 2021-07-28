/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.chora;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;

/**
 *
 * @author giorg
 */
public class Gui {
    
    protected NiftyJmeDisplay niftyDisplay;
    protected Nifty nifty;
    
    public Gui (AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer, ViewPort guiViewPort) {
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        
        // create a screen
        Screen screen = new ScreenBuilder("start") {{
            controller(new GuiScreenController());

            // Style layer
            layer(new LayerBuilder("baseLayer") {{
            //backgroundColor("#003f");
            childLayoutCenter();

                // Top left panel
                panel(new PanelBuilder() {{
                    id("topLeftPanel");
                    childLayoutCenter();
                    height("15%");
                    width("40%");
                    alignLeft();
                    valignTop();
                    backgroundColor("#f60f");
                    visibleToMouse();
                    //interactOnClick("quit()");
                    marginLeft("2%");
                    marginTop("2%");

                    // Content
                    panel(new PanelBuilder() {{
                        id("topLeftContentPanel");
                        childLayoutHorizontal();
                        alignCenter();
                        valignCenter();
                        width("100%");
                
                        // Player info
                        image(new ImageBuilder() {{
                            id("PlayerInfoImage");
                            filename("Interface/gui/playerInfo.png");
                            width("40%");
                            height("100%");
                        }});
                
                        // Apple image
                        image(new ImageBuilder() {{
                            id("AppleImage");
                            filename("Interface/gui/apple.png");
                            width("30%");
                            height("100%");
                        }});
                
                       // Water bucket image
                        image(new ImageBuilder() {{
                            id("WaterBucketImage");
                            filename("Interface/gui/waterBucket.png");
                            width("30%");
                            height("100%");
                        }});
                    }});
                }});
            
                // Top right panel
                panel(new PanelBuilder() {{
                    id("TopRightPanel");
                    childLayoutCenter();
                    height("15%");
                    width("15%");
                    alignRight();
                    valignTop();
                    backgroundColor("#f60f");
                    visibleToMouse();
                    interactOnClick("openMenu()");
                    marginRight("2%");
                    marginTop("2%");
                
                    // Menu image
                    image(new ImageBuilder() {{
                        id("MenuImage");
                        filename("Interface/gui/menu_icon.png");
                        width("25%");
                        height("50%");
                    }});
                }});
            
                // Buttom right panel
                panel(new PanelBuilder() {{
                    id("ButtomRightPanel");
                    childLayoutCenter();
                    height("15%");
                    width("15%");
                    alignRight();
                    valignBottom();
                    backgroundColor("#f60f");
                    visibleToMouse();
                    //interactOnClick("quit()");
                    marginRight("2%");
                    marginBottom("2%");
                
                    // Shop image
                    image(new ImageBuilder() {{
                       id("ShopImage");
                       filename("Interface/gui/shop_icon.png");
                       width("25%");
                       height("50%");
                    }});
                }});
            }});
            
            
            // Interactive layerinteractiveBaseLayer
            layer(new LayerBuilder("interactiveBaseLayer") {{
                childLayoutCenter();
                
                // Top left panel
                panel(new PanelBuilder() {{
                    childLayoutCenter();
                    height("15%");
                    width("40%");
                    alignLeft();
                    valignTop();
                    //backgroundColor("#f60f");
                    visibleToMouse();
                    //interactOnClick("quit()");
                    marginLeft("2%");
                    marginTop("2%");
                    
                    // Content
                    panel(new PanelBuilder() {{
                        childLayoutHorizontal();
                        alignCenter();
                        valignCenter();
                        width("100%");
                
                        // Player info texts
                        control(new LabelBuilder("PlayerInfo", "") {{
                            font("Interface/Fonts/Default.fnt");
                            width("40%");
                        }});
                        
                        // Apples text
                        control(new LabelBuilder("Apple", "0") {{
                            font("Interface/Fonts/Default.fnt");
                            width("30%");
                        }});
                
                        // Water bucket text
                        control(new LabelBuilder("WaterBucket", "0") {{
                            font("Interface/Fonts/Default.fnt");
                            width("30%");
                        }});
                    }});
                    
                }});
                
                // Top right panel
                panel(new PanelBuilder() {{
                    childLayoutCenter();
                    height("15%");
                    width("15%");
                    alignRight();
                    valignTop();
                    //backgroundColor("#f60f");
                    visibleToMouse();
                    //interactOnClick("quit()");
                    marginRight("2%");
                    marginTop("2%");
                
                    // Menu button
                    control(new ControlBuilder("menuButton", "") {{
                        width("25%");
                        height("50%");
                        visibleToMouse(true);
                        
                        interactOnClick("openMenu()");
                    }});
                }});
            
                // Buttom right panel
                panel(new PanelBuilder() {{
                    childLayoutCenter();
                    height("15%");
                    width("15%");
                    alignRight();
                    valignBottom();
                    //backgroundColor("#f60f");
                    visibleToMouse();
                    //interactOnClick("quit()");
                    marginRight("2%");
                    marginBottom("2%");
                
                    // Shop button
                    control(new ControlBuilder("shopButton", "") {{
                        width("25%");
                        height("50%");
                        visibleToMouse(true);
                        
                        interactOnClick("");
                    }});
                }});
                
            }});
            
            // Menu Layer
            layer(new LayerBuilder("menuLayer") {{
                childLayoutCenter();
                
                // Modal panel
                panel(new PanelBuilder() {{
                    id("MenuModalPanel");
                    childLayoutCenter();
                    backgroundColor("#f60f");
                    height("65%");
                    width("35%");
                    alignCenter();
                    valignCenter();
                    visibleToMouse(true);
                    
                    image(new ImageBuilder() {{
                       id("MenuImage");
                       filename("Interface/gui/MenuModal.png");
                       width("100%");
                       height("100%");
                    }});
                
                    // Content Modal panel
                    panel(new PanelBuilder() {{
                        id("MenuContentPanel");
                        childLayoutCenter();
                        width("100%");
                        height("100%");
                        visibleToMouse(true);
                        
                        image(new ImageBuilder() {{
                            id("MenuImage");
                            filename("Interface/gui/CloseModalButton.png");
                            width("15%");
                            height("15%");
                            alignRight();
                            valignTop();
                            marginRight("8%");
                            marginTop("6%");
                        }});
                        
                        image(new ImageBuilder() {{
                            id("MenuImage");
                            filename("Interface/gui/MenuButton.png");
                            width("50%");
                            height("15%");
                            alignCenter();
                            valignTop();
                            marginTop("25%");
                        }});
                        
                        image(new ImageBuilder() {{
                            id("MenuImage");
                            filename("Interface/gui/MenuButton.png");
                            width("50%");
                            height("15%");
                            alignCenter();
                            valignTop();
                            marginTop("45%");
                        }});
                        
                        image(new ImageBuilder() {{
                            id("MenuImage");
                            filename("Interface/gui/MenuButton.png");
                            width("50%");
                            height("15%");
                            alignCenter();
                            valignTop();
                            marginTop("65%");
                        }});
                        
                    }});
                }});
            }});
            
            // interactive Menu Layer
            layer(new LayerBuilder("interactiveMenuLayer") {{
                childLayoutCenter();
                
                // Modal panel
                panel(new PanelBuilder() {{
                    id("MenuModalPanel");
                    childLayoutCenter();
                    height("65%");
                    width("35%");
                    alignCenter();
                    valignCenter();
                    visibleToMouse(true);
                    
                    // Content Modal panel
                    panel(new PanelBuilder() {{
                        id("MenuContentPanel");
                        childLayoutCenter();
                        width("100%");
                        height("100%");
                        visibleToMouse(true);
                        
                        // close Button
                        control(new ControlBuilder("closeButton", "") {{
                            width("15%");
                            height("15%");
                            alignRight();
                            valignTop();
                            marginRight("8%");
                            marginTop("6%");
                            visibleToMouse(true);                            
                        
                            interactOnClick("");
                        }});
                        
                        // credits Button
                        control(new ControlBuilder("creditsButton", "") {{
                            width("50%");
                            height("15%");
                            alignCenter();
                            valignTop();
                            marginTop("25%");
                            visibleToMouse(true);                            
                        
                            interactOnClick("");
                            
                            text(new TextBuilder() {{
                                text("Credits");
                                font("Interface/Fonts/Default.fnt");
                                color("#000");
                                height("100%");
                                width("100%");
                            }});
                        }});
                        
                        // option Button
                        control(new ControlBuilder("optionButton", "") {{
                            width("50%");
                            height("15%");
                            alignCenter();
                            valignTop();
                            marginTop("45%");
                            visibleToMouse(true);                            
                        
                            interactOnClick("");
                            
                            text(new TextBuilder() {{
                                text("Option");
                                font("Interface/Fonts/Default.fnt");
                                color("#000");
                                height("100%");
                                width("100%");
                            }});
                        }});
                        
                        // exit Button
                        control(new ControlBuilder("exitButton", "") {{
                            width("50%");
                            height("15%");
                            alignCenter();
                            valignTop();
                            marginTop("65%");
                            visibleToMouse(true);                            
                        
                            interactOnClick("");
                            
                            text(new TextBuilder() {{
                                text("Exit");
                                font("Interface/Fonts/Default.fnt");
                                color("#000");
                                height("100%");
                                width("100%");
                            }});
                        }});
                        
                    }});
                }});
            }});
            
        }}.build(nifty);

        nifty.addScreen("start", screen);
        nifty.gotoScreen("start");
        
        String tmp = nifty.getCurrentScreen().findElementById("Apple").getRenderer(TextRenderer.class).getWrappedText();
        nifty.getCurrentScreen().findElementById("Apple").getRenderer(TextRenderer.class).setText(tmp);
        
        nifty.getCurrentScreen().findElementById("menuLayer").setVisible(false);
        nifty.getCurrentScreen().findElementById("interactiveMenuLayer").setVisible(false);
    }
}
