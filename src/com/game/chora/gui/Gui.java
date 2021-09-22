/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.chora.gui;

import com.game.chora.Main;
import com.game.chora.Player;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giorg
 */
public class Gui {
    
    protected NiftyJmeDisplay niftyDisplay;
    protected Nifty nifty;
    protected Main app;
    protected AssetManager assetManager;
    protected Node rootNode;
    protected Node shootables;
    protected List<Entity> entities;
    protected Player p;
    protected boolean placeEntity;
    protected String selectedEntityFromShop;
    protected boolean guiOpened;
    protected float time;
    
    protected Map<String, Integer> shopProducts;    
    
    public Gui (Main app, AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer, ViewPort guiViewPort, Node rootNode, Node shootables, List<Entity> entities, Player p) {
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        
        this.app = app;
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.shootables = shootables;
        this.entities = entities;
        this.p = p;
        this.placeEntity = false;
        this.selectedEntityFromShop = "";
        this.guiOpened = false;
        this.time = 0;
        
        this.shopProducts = new HashMap<String, Integer>();
        this.shopProducts.put("Sprout", 10);
        this.shopProducts.put("Well", 20);
        this.shopProducts.put("Mill", 50);
        
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
                    visibleToMouse();
                    interactOnClick("openMenu()");
                    marginRight("2%");
                    marginTop("2%");
                
                    // Menu image
                    image(new ImageBuilder() {{
                        id("MenuImage");
                        filename("Interface/gui/menu_icon.png");
                        width("60%");
                        height("100%");
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
                    visibleToMouse();
                    //interactOnClick("quit()");
                    marginRight("2%");
                    marginBottom("2%");
                
                    // Shop image
                    image(new ImageBuilder() {{
                       id("ShopImage");
                       filename("Interface/gui/shop_icon.png");
                       width("60%");
                       height("100%");
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
                        
                        // Player avatar image
                        image(new ImageBuilder() {{
                            id("playerImage");
                            width("10%");
                            height("100%");
                        }});
                        
                        // Player info texts
                        control(new LabelBuilder("PlayerInfo", "") {{
                            font("Interface/Fonts/SegoeUIBlack.fnt");
                            color("#000f");
                            width("30%");
                            height("20%");
                            marginTop("23%");
                        }});
                        
                        // Apples text
                        control(new LabelBuilder("Apple", "0") {{
                            font("Interface/Fonts/SegoeUIBlack.fnt");
                            //color("#f00f");
                            width("30%");
                            height("100%");
                            valignCenter();
                        }});
                
                        // Water bucket text
                        control(new LabelBuilder("WaterBucket", "2") {{
                            font("Interface/Fonts/SegoeUIBlack.fnt");
                            width("30%");                            
                            height("100%");
                            valignCenter();
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
                    visibleToMouse();
                    //interactOnClick("quit()");
                    marginRight("2%");
                    marginTop("2%");
                
                    // Menu button
                    control(new ControlBuilder("menuButton", "") {{
                        width("53%");
                        height("98%");
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
                    visibleToMouse();
                    //interactOnClick("quit()");
                    marginRight("2%");
                    marginBottom("2%");
                
                    // Shop button
                    control(new ControlBuilder("shopButton", "") {{
                        width("53%");
                        height("98%");
                        visibleToMouse(true);
                        
                        interactOnClick("openShop()");
                    }});
                }});
                
            }});
            
            // Shop Layer
            layer(new LayerBuilder("shopLayer") {{
                childLayoutCenter();
                
                // Items Modal panel
                panel(new PanelBuilder() {{
                    id("ShopModalPanel");
                    childLayoutCenter();
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
                        
                        text(new TextBuilder() {{
                            text("SHOP");
                            font("Interface/Fonts/SegoeUIBlack.fnt");
                            color("#000f");
                            height("10%");
                            width("100%");
                            alignRight();
                            valignTop();
                            marginTop("8%");
                        }});
                        
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
                            width("25%");
                            height("40%");
                            alignLeft();
                            valignTop();
                            marginLeft("7.5%");
                            marginTop("30%");
                            
                        }});
                        
                        image(new ImageBuilder() {{
                            id("MenuImage");
                            filename("Interface/gui/ShopItemModal.png");
                            width("25%");
                            height("40%");
                            alignLeft();
                            valignTop();
                            marginLeft("37.5%");
                            marginTop("30%");
                        }});
                        
                        image(new ImageBuilder() {{
                            id("MenuImage");
                            filename("Interface/gui/ShopItemModal.png");
                            width("25%");
                            height("40%");
                            alignLeft();
                            valignTop();
                            marginLeft("67.5%");
                            marginTop("30%");
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
                            width("25%");
                            height("40%");
                            alignLeft();
                            valignTop();                            
                            marginLeft("7.5%");
                            marginTop("30%");
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
                                width("25%");
                                height("17%");
                                alignCenter();
                                valignTop();   
                                marginTop("82%"); 
                                visibleToMouse(true);

                                //interactOnClick("buyFromShop(\"sprout\", " + assetManager + ", " + rootNode + ", " + shootables + ")");

                                text(new TextBuilder("Price1_Text") {{
                                    text(shopProducts.get("Sprout") + " Apples");
                                    font("Interface/Fonts/SegoeUIBlack.fnt");
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
                            width("25%");
                            height("40%");
                            alignLeft();
                            valignTop();
                            marginLeft("37.5%");
                            marginTop("30%");
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
                                width("25%");
                                height("17%");
                                alignCenter();
                                valignTop();   
                                marginTop("82%"); 
                                visibleToMouse(true);                           

                                interactOnClick("");

                                text(new TextBuilder() {{
                                    text(shopProducts.get("Well") + " Apple");
                                    font("Interface/Fonts/SegoeUIBlack.fnt");
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
                            width("25%");
                            height("40%");
                            alignLeft();
                            valignTop();
                            marginLeft("67.5%");
                            marginTop("30%");
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
                                width("25%");
                                height("17%");
                                alignCenter();
                                valignTop();   
                                marginTop("82%"); 
                                visibleToMouse(true);                            

                                interactOnClick("");

                                text(new TextBuilder() {{
                                    text(shopProducts.get("Mill") + " Apple");
                                    font("Interface/Fonts/SegoeUIBlack.fnt");
                                    color("#000");
                                    height("100%");
                                    width("100%");
                                }});
                            }});
                        }});                        
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
                            font("Interface/Fonts/SegoeUIBlack.fnt");                                                       
                        
                            interactOnClick("openCredits()");
                            
                            text(new TextBuilder() {{
                                text("Credits");
                                font("Interface/Fonts/SegoeUIBlack.fnt");
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
                                font("Interface/Fonts/SegoeUIBlack.fnt");
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
                        
                            interactOnClick("exitGame()");
                            
                            text(new TextBuilder() {{
                                text("Exit");
                                font("Interface/Fonts/SegoeUIBlack.fnt");
                                color("#000");
                                height("100%");
                                width("100%");
                            }});
                        }});
                        
                    }});
                }});
            }});
            
            // Credits game layer
            layer(new LayerBuilder("creditsLayer") {{
                childLayoutCenter();
                backgroundColor("#000f");
                
                // Modal panel
                panel(new PanelBuilder() {{
                    id("CreditsPanel");
                    childLayoutCenter();
                    height("85%");
                    width("60%");
                    alignCenter();
                    valignCenter();
                    visibleToMouse(true);
                    //backgroundColor("#00f");
                    
                    // Content Modal panel
                    panel(new PanelBuilder() {{
                        id("CreditsContentPanel");
                        childLayoutCenter();
                        width("100%");
                        height("100%");
                        visibleToMouse(true);                        
                        
                        image(new ImageBuilder() {{
                            id("MenuImage");
                            filename("Interface/gui/CloseModalButton.png");
                            width("9%");
                            height("10%");
                            alignRight();
                            valignTop();
                            marginRight("9%");
                            marginTop("7%");
                        }});
                    }});
                }});
            }});
            
            // Interactive credits layer
            layer(new LayerBuilder("interactiveCreditsLayer") {{
                childLayoutCenter();                
                
                // Modal panel
                panel(new PanelBuilder() {{
                    id("CreditsPanel");
                    childLayoutCenter();
                    height("85%");
                    width("60%");
                    alignCenter();
                    valignCenter();
                    visibleToMouse(true);
                    
                    // close Button
                    control(new ControlBuilder("closeButton", "") {{
                        width("9%");
                        height("10%");
                        alignRight();
                        valignTop();
                        marginRight("9%");
                        marginTop("7%");
                        visibleToMouse(true);                            
                        
                        interactOnRelease("closeCredits()");
                    }});
                    
                    text(new TextBuilder() {{
                        text("Developer: Alessandro Pilleri, Giorgia Bertacchini\n"
                                + "Programming language: Java \n"
                                + "Game engine: jMonkeyEngine.\n"
                                + "Project for: 'Object-oriented programming' course of engineering UNIMORE of Modena\n"
                                + "Teacher: Nicola Bicocchi");
                        font("Interface/Fonts/SegoeUIBlack.fnt");
                        color("#fff");
                        height("80%");
                        width("100%");
                        alignLeft();
                    }});                
                }});
            }});
            
            // Close game layer
            layer(new LayerBuilder("closeGameLayer") {{
                childLayoutCenter();
                
                // Modal panel
                panel(new PanelBuilder() {{
                    id("CloseGamePanel");
                    childLayoutCenter();
                    height("45%");
                    width("45%");
                    alignCenter();
                    valignCenter();
                    visibleToMouse(true);
                    
                    image(new ImageBuilder() {{
                       id("CloseGameImage");
                       filename("Interface/gui/CloseGameModal.png");
                       width("100%");
                       height("100%");
                    }});
                }});
            }});
            
            // Interactive close game layer
            layer(new LayerBuilder("interactiveCloseGameLayer") {{
                childLayoutCenter();
                
                // Modal panel
                panel(new PanelBuilder() {{
                    id("CloseGamePanel");
                    childLayoutCenter();
                    height("45%");
                    width("45%");
                    alignCenter();
                    valignCenter();
                    visibleToMouse(true);
                    
                    // Text panel
                    panel(new PanelBuilder() {{
                        id("MenuModalPanel");
                        childLayoutCenter();
                        height("50%");
                        width("100%");
                        alignCenter();
                        valignTop();
                        visibleToMouse(true);

                        text(new TextBuilder() {{
                            text("Quit game?");
                            font("Interface/Fonts/SegoeUIBlack.fnt");
                            color("#000");
                            height("100%");
                            width("100%");
                        }});
                    }});

                    // Buttons panel
                    panel(new PanelBuilder() {{
                        id("MenuModalPanel");
                        childLayoutCenter();
                        height("50%");
                        width("100%");
                        alignCenter();
                        valignBottom();
                        visibleToMouse(true);
                        marginBottom("25%");

                        control(new ControlBuilder("yesButton", "") {{
                            width("28%");
                            height("34%");
                            alignLeft();
                            valignCenter();
                            marginLeft("14%");
                            marginTop("20%");
                            visibleToMouse(true);     
                            font("Interface/Fonts/SegoeUIBlack.fnt");                                                       

                            interactOnClick("");

                            text(new TextBuilder() {{
                                text("Yes");
                                font("Interface/Fonts/SegoeUIBlack.fnt");
                                color("#000");
                                height("100%");
                                width("100%");
                            }});
                        }});

                        control(new ControlBuilder("noButton", "") {{
                            width("28%");
                            height("34%");
                            alignRight();
                            valignCenter();
                            marginRight("14%");
                            marginTop("20%");
                            visibleToMouse(true);     
                            font("Interface/Fonts/SegoeUIBlack.fnt");                                                       

                            interactOnClick("cancelExit()");

                            text(new TextBuilder() {{
                                text("No");
                                font("Interface/Fonts/SegoeUIBlack.fnt");
                                color("#000");
                                height("100%");
                                width("100%");
                            }});
                        }});                    
                    }});
                }});
            }});
            
            layer(new LayerBuilder("alertLayer") {{
                childLayoutCenter();
                
                panel(new PanelBuilder() {{                    
                    id("ErrorPanel");
                    width("100%");
                    height("65%");
                    alignCenter();
                    childLayoutCenter();
                    valignTop();
                    
                    control(new LabelBuilder("errorLabel", "")  {{
                        text("");
                        font("Interface/Fonts/SegoeUIBlack.fnt");
                        color("#000");
                        height("100%");
                        width("100%");
                    }});
                }});
                
                panel(new PanelBuilder() {{
                    id("placingModePanel");
                    width("100%");
                    height("10%");
                    alignCenter();
                    childLayoutCenter();
                    valignBottom();                    
                    
                    text(new TextBuilder() {{
                        text("Placing mode");
                        font("Interface/Fonts/SegoeUIBlack.fnt");
                        color("#000");
                        height("100%");
                        width("100%");
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
        
        nifty.getCurrentScreen().findElementById("creditsLayer").setVisible(false);
        nifty.getCurrentScreen().findElementById("interactiveCreditsLayer").setVisible(false);
        
        nifty.getCurrentScreen().findElementById("closeGameLayer").setVisible(false);
        nifty.getCurrentScreen().findElementById("interactiveCloseGameLayer").setVisible(false);
        
        nifty.getCurrentScreen().findElementById("yesButton").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "closeGame()", this));
        
        nifty.getCurrentScreen().findElementById("Item1_Button").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "buyFromShop(Sprout)", this));
        nifty.getCurrentScreen().findElementById("Item2_Button").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "buyFromShop(Well)", this));
        nifty.getCurrentScreen().findElementById("Item3_Button").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "buyFromShop(Mill)", this));
        
        nifty.getCurrentScreen().findElementById("alertLayer").setVisible(true);
        nifty.getCurrentScreen().findElementById("errorLabel").setVisible(false);
        nifty.getCurrentScreen().findElementById("placingModePanel").setVisible(false);    
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
    
    public void closeGame() {
        app.stop(false);
    }
    
    public void buyFromShop(String selectedEntity) {
        if (p.getApple() >= shopProducts.get(selectedEntity)) {  
            System.out.println(p.getMill());
            if(("Mill".equals(selectedEntity) && p.getMill() < 2) || ("Well".equals(selectedEntity) && p.getWell() < 5)) {
                p.setApple(p.getApple() - shopProducts.get(selectedEntity));
                this.setApple(p.getApple());
            
                nifty.getCurrentScreen().findElementById("shopLayer").setVisible(false);
                nifty.getCurrentScreen().findElementById("interactiveShopLayer").setVisible(false);
       
                this.setSelectedEntityFromShop(selectedEntity);
                this.setPlaceEntity(true);
        
                System.out.println("Selected " + selectedEntity + " from shop. " + this.getPlaceEntity());
                nifty.getCurrentScreen().findElementById("placingModePanel").setVisible(true);
            } 
            else {
                //TODO errore hai raggiunto il limite di mill/well
                nifty.getCurrentScreen().findElementById("errorLabel").getRenderer(TextRenderer.class).setText("Maximum number of " + selectedEntity + " reached.");
                nifty.getCurrentScreen().findElementById("errorLabel").setVisible(true);
                time = 5;
            }
        }
        else {
            //TODO errore non hai abbastanza mele
            nifty.getCurrentScreen().findElementById("errorLabel").getRenderer(TextRenderer.class).setText("Not enough apples.");  
            nifty.getCurrentScreen().findElementById("errorLabel").setVisible(true);
            time = 5;
        }
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
    
    public float getTime() {
        return this.time;
    }
    
    public void decreaseTime(float tpf) {
        this.time -= tpf;
    }
}
