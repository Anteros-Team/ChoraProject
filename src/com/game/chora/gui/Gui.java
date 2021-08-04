/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.chora.gui;

import com.game.chora.items.entities.Sprout;
import com.game.chora.utils.Entity;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyMethodInvoker;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.imageselect.builder.ImageSelectBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author giorg
 */
public class Gui {
    
    protected NiftyJmeDisplay niftyDisplay;
    protected Nifty nifty;
    protected AssetManager assetManager;
    protected Node rootNode;
    protected Node shootables;
    protected List<Entity> entities;
    protected boolean placeEntity;
    protected String selectedEntityFromShop;
    protected boolean guiOpened;
    
    public Gui (AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer, ViewPort guiViewPort, Node rootNode, Node shootables, List<Entity> entities) {
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.shootables = shootables;
        this.entities = entities;
        this.placeEntity = false;
        this.selectedEntityFromShop = "";
        this.guiOpened = false;
        
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        
        // create a screen
        Screen gameScreen = new ScreenBuilder("game") {{
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
                    width("45%");
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
                            width("20%");
                            height("95%");
                            marginLeft("5%");
                        }});
                
                       // Water bucket image
                        image(new ImageBuilder() {{
                            id("WaterBucketImage");
                            filename("Interface/gui/waterBucket.png");
                            width("18%");
                            height("96%");
                            marginLeft("10%");
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
                        width("30%");
                        height("55%");
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
                       width("30%");
                       height("55%");
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
                    width("45%");
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
                            //font("Interface/Fonts/Default.fnt");
                            font("Interface/Fonts/YuGothicUISemibold.fnt");
                            width("40%");
                        }});
                        
                        // Apples text
                        control(new LabelBuilder("Apple", "0") {{
                            //font("Interface/Fonts/Default.fnt");
                            //font("aurulent-sans-16.fnt");
                            font("Interface/Fonts/YuGothicUISemibold.fnt");
                            //color("#f00f");
                            width("25%");
                        }});
                
                        // Water bucket text
                        control(new LabelBuilder("WaterBucket", "2") {{
                            //font("Interface/Fonts/Default.fnt");
                            font("Interface/Fonts/YuGothicUISemibold.fnt");
                            width("25%");
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
                        width("30%");
                        height("55%");
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
                        width("30%");
                        height("55%");
                        visibleToMouse(true);
                        
                        interactOnClick("openShop()");
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
                            width("13%");
                            height("12.5%");
                            alignRight();
                            valignTop();
                            marginRight("9%");
                            marginTop("7%");
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
                            width("13%");
                            height("12.5%");
                            alignRight();
                            valignTop();
                            marginRight("9%");
                            marginTop("7%");
                            visibleToMouse(true);                            
                        
                            interactOnRelease("closeMenu()");
                        }});
                        
                        // credits Button
                        control(new ControlBuilder("creditsButton", "") {{
                            width("50%");
                            height("15%");
                            alignCenter();
                            valignTop();
                            marginTop("25%");
                            visibleToMouse(true);     
                            font("Interface/Fonts/YuGothicUISemibold.fnt");                                                       
                        
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
            
            // Shop Layer
            layer(new LayerBuilder("shopLayer") {{
                childLayoutCenter();
                
                // Modal panel
                panel(new PanelBuilder() {{
                    id("ShopModalPanel");
                    childLayoutCenter();
                    backgroundColor("#f60f");
                    height("70%");
                    width("70%");
                    alignCenter();
                    valignCenter();
                    visibleToMouse(true);
                    
                    image(new ImageBuilder() {{
                       id("MenuImage");
                       filename("Interface/gui/ShopModal.png");
                       width("100%");
                       height("100%");
                    }});
                
                    // Content Modal panel
                    panel(new PanelBuilder() {{
                        id("ShopContentPanel");
                        childLayoutCenter();
                        width("100%");
                        height("100%");
                        visibleToMouse(true);
                        
                        image(new ImageBuilder() {{
                            id("MenuImage");
                            filename("Interface/gui/CloseModalButton.png");
                            width("6.5%");
                            height("11%");
                            alignRight();
                            valignTop();
                            marginRight("4%");
                            marginTop("7%");
                        }});
                        
                        image(new ImageBuilder() {{
                            id("MenuImage");
                            filename("Interface/gui/ShopItemModal.png");
                            width("20%");
                            height("35%");
                            alignLeft();
                            valignTop();                            
                            marginLeft("15%");
                            marginTop("20%");
                            
                        }});
                        
                        image(new ImageBuilder() {{
                            id("MenuImage");
                            filename("Interface/gui/ShopItemModal.png");
                            width("20%");
                            height("35%");
                            alignLeft();
                            valignTop();
                            marginLeft("40%");
                            marginTop("20%");
                        }});
                        
                        image(new ImageBuilder() {{
                            id("MenuImage");
                            filename("Interface/gui/ShopItemModal.png");
                            width("20%");
                            height("35%");
                            alignLeft();
                            valignTop();
                            marginLeft("65%");
                            marginTop("20%");
                        }});
                        
                    }});
                }});
            }});
            
            // interactive Shop Layer
            layer(new LayerBuilder("interactiveShopLayer") {{
                childLayoutCenter();
                
                // Modal panel
                panel(new PanelBuilder() {{
                    id("MenuModalPanel");
                    childLayoutCenter();
                    height("70%");
                    width("70%");
                    alignCenter();
                    valignCenter();
                    visibleToMouse(true);
                    
                    // Content Modal panel
                    panel(new PanelBuilder() {{
                        id("ShopContentPanel");
                        childLayoutCenter();
                        width("100%");
                        height("100%");
                        visibleToMouse(true);
                        
                        // close Button
                        control(new ControlBuilder("closeButton", "") {{
                            width("6.5%");
                            height("11%");
                            alignRight();
                            valignTop();
                            marginRight("4%");
                            marginTop("7%");
                            visibleToMouse(true);                            
                        
                            interactOnRelease("closeShop()");
                        }});
                        
                        // Content Item 1 panel
                        panel(new PanelBuilder() {{
                            id("ItemShopContentPanel");
                            childLayoutCenter();                            
                            width("20%");
                            height("35%");
                            alignLeft();
                            valignTop();                            
                            marginLeft("15%");
                            marginTop("20%");
                            visibleToMouse(true);
                       
                            image(new ImageBuilder() {{
                                filename("Interface/gui/sproutImage.png");
                                width("95%");
                                height("97%");
                                alignCenter();
                                valignTop();   
                            }});  
                            
                            // item 1 Button
                            control(new ControlBuilder("Item1_Button", "") {{
                                width("20%");
                                height("17%");
                                alignCenter();
                                valignTop();   
                                marginTop("82%"); 
                                visibleToMouse(true);

                                //interactOnClick("buyFromShop(\"sprout\", " + assetManager + ", " + rootNode + ", " + shootables + ")");

                                text(new TextBuilder("Price1_Text") {{
                                    text("30 Apples");
                                    font("Interface/Fonts/Default.fnt");
                                    color("#000");
                                    height("100%");
                                    width("100%");
                                }});
                            }});                        
                        }});
                        
                        // Content Item 2 panel
                        panel(new PanelBuilder() {{
                            id("ItemShopContentPanel");
                            childLayoutCenter();
                            width("20%");
                            height("35%");
                            alignLeft();
                            valignTop();
                            marginLeft("40%");
                            marginTop("20%");
                            visibleToMouse(true);
                            
                            image(new ImageBuilder() {{
                                filename("Interface/gui/wheelImage.png");
                                width("95%");
                                height("90%");
                                alignCenter();
                                valignTop();   
                            }}); 
                            
                            // item 2 Button
                            control(new ControlBuilder("Item2_Button", "") {{
                                width("20%");
                                height("17%");
                                alignCenter();
                                valignTop();   
                                marginTop("82%"); 
                                visibleToMouse(true);                           

                                interactOnClick("");

                                text(new TextBuilder() {{
                                    text("20 Apple");
                                    font("Interface/Fonts/Default.fnt");
                                    color("#000");
                                    height("100%");
                                    width("100%");
                                }});
                            }});
                        }});
                        
                        // Content Item 3 panel
                        panel(new PanelBuilder() {{
                            id("ItemShopContentPanel");
                            childLayoutCenter();
                            width("20%");
                            height("35%");
                            alignLeft();
                            valignTop();
                            marginLeft("65%");
                            marginTop("20%");
                            visibleToMouse(true);
                            
                            image(new ImageBuilder() {{
                                filename("Interface/gui/millImage.png");
                                width("92%");
                                height("80%");
                                alignCenter();
                                valignTop(); 
                                marginTop("3%");
                            }}); 
                        
                            // item 3 Button
                            control(new ControlBuilder("Item3_Button", "") {{
                                width("20%");
                                height("17%");
                                alignCenter();
                                valignTop();   
                                marginTop("82%"); 
                                visibleToMouse(true);                            

                                interactOnClick("");

                                text(new TextBuilder() {{
                                    text("50 Apple");
                                    font("Interface/Fonts/Default.fnt");
                                    color("#000");
                                    height("100%");
                                    width("100%");
                                }});
                            }});
                        }});                        
                    }});
                }});
            }});
            
            
        }}.build(nifty);

        Screen startScreen = new ScreenBuilder("start") {{
            controller(new GuiScreenController());
            
            layer(new LayerBuilder("baseLayer") {{
            backgroundColor("#003f");
            childLayoutCenter();
            
                panel(new PanelBuilder() {{
                    width("100%");
                    height("100%");
                    childLayoutCenter();
                    
                    image(new ImageBuilder() {{
                        filename("Interface/gui/ChoraLoadingScreen.jpg");
                        width("100%");
                        height("100%");
                    }});
                }});
            }});
        }}.build(nifty);
        
        Screen startMenuScreen = new ScreenBuilder("startMenu") {{
            controller(new GuiScreenController());
            
            layer(new LayerBuilder("baseLayer") {{
                backgroundColor("#003f");
                childLayoutCenter();
            
                panel(new PanelBuilder() {{
                    width("100%");
                    height("100%");
                    childLayoutCenter();
                    
                    image(new ImageBuilder() {{
                        filename("Interface/gui/StartingMenu.png");
                        width("100%");
                        height("100%");
                    }});
                }});
            }});
            
            layer(new LayerBuilder("interactiveBaseLayer") {{
                childLayoutCenter();
                
                panel(new PanelBuilder() {{
                    width("100%");
                    height("100%");
                    childLayoutCenter();
                    
                    control(new TextFieldBuilder("Name input", "Write your name here") {{
                        width("35%");
                        height("6%");
                        marginTop("0.6%");
                    }});
                    
                    control(new ImageSelectBuilder("someImageSelect") {{
                       width("5%");
                       height("10%");
                       //imageList("apple.png, waterBucket.png");
                    }});
                }});
            }});
        }}.build(nifty);
        
        
        nifty.addScreen("start", startScreen);
        nifty.addScreen("startMenu", startMenuScreen);
        nifty.addScreen("game", gameScreen);
        nifty.gotoScreen("game");
        
        /*ImageRenderer imageRenderer = nifty.getCurrentScreen().findElementById("someImageSelect").getRenderer(ImageRenderer.class);
        List<NiftyImage> images = new ArrayList<>();
        NiftyImage n = nifty.getRenderEngine().createImage(nifty.getCurrentScreen(), "Interface/gui/apple.png", false);
        imageRenderer.setImage(n);
        nifty.getCurrentScreen().findElementById("someImageSelect").setConstraintWidth(SizeValue.px(n.getWidth()));
        nifty.getCurrentScreen().findElementById("someImageSelect").setConstraintHeight(SizeValue.px(n.getHeight()));
        nifty.getCurrentScreen().findElementById("someImageSelect").layoutElements();
        nifty.getCurrentScreen().findElementById("someImageSelect").show();*/
        String tmp = nifty.getCurrentScreen().findElementById("Apple").getRenderer(TextRenderer.class).getWrappedText();
        nifty.getCurrentScreen().findElementById("Apple").getRenderer(TextRenderer.class).setText(tmp);
                    
        nifty.getCurrentScreen().findElementById("menuLayer").setVisible(false);
        nifty.getCurrentScreen().findElementById("interactiveMenuLayer").setVisible(false);
        
        nifty.getCurrentScreen().findElementById("shopLayer").setVisible(false);
        nifty.getCurrentScreen().findElementById("interactiveShopLayer").setVisible(false);
        
        nifty.getCurrentScreen().findElementById("Item1_Button").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "buyFromShop(sprout)", this));
        nifty.getCurrentScreen().findElementById("Item2_Button").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "buyFromShop(well)", this));
        nifty.getCurrentScreen().findElementById("Item3_Button").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "buyFromShop(mill)", this));
    }
    
    public Nifty getNifty() {
        return this.nifty;
    }
    
    public void setPlayerName(String name) {
        nifty.getCurrentScreen().findElementById("PlayerInfo").getRenderer(TextRenderer.class).setText(""+name+"");
    }
    
    public void setApple(int apple) {
        nifty.getCurrentScreen().findElementById("Apple").getRenderer(TextRenderer.class).setText(""+apple+"");
    }
    
    public void setWaterBucket(int waterBucket) {
        nifty.getCurrentScreen().findElementById("WaterBucket").getRenderer(TextRenderer.class).setText(""+waterBucket+"");
    }
    
    public void buyFromShop(String selectedEntity) {
        
        // TODO: check if have sufficient apples, remove apples
        
        
        nifty.getCurrentScreen().findElementById("shopLayer").setVisible(false);
        nifty.getCurrentScreen().findElementById("interactiveShopLayer").setVisible(false);
       
        this.setSelectedEntityFromShop(selectedEntity);
        this.setPlaceEntity(true);
        
        
        System.out.println("Selected " + selectedEntity + " from shop. " + this.getPlaceEntity());
        
        /*Entity e = new Sprout(new Vector3f(190, 0, 190), 0.6f, new Vector3f(10, 8, 5));
        e.setModel(this.assetManager, this.rootNode, "Models/sprout/sprout.j3o", this.shootables);
        e.spawn(this.rootNode, this.shootables);
        entities.add(e);*/
    }
    
    public String getSelectedEntityFromShop() {
        return this.selectedEntityFromShop;
    }
    
    public void setSelectedEntityFromShop(String selectedEntity) {
        this.selectedEntityFromShop = selectedEntity;
    }
    
    public boolean getPlaceEntity() {
        return this.placeEntity;
    }
    
    public void setPlaceEntity(boolean placeEntity) {
        this.placeEntity = placeEntity;
    }
    
    public boolean getGuiOpened() {
        return this.guiOpened;
    }
    
    public void setGuiOpened(boolean guiOpened) {
        this.guiOpened = guiOpened;
    }
}
