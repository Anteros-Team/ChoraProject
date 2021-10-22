package com.game.chora.gui;

import com.game.chora.Main;
import com.game.chora.Player;
import com.game.chora.utils.Entity;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
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
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gui is the class that provides a user interface for the game.
 * The graphic is implemented on the NiftyGUI library.
 * This is divided in three different screens:
 * <ul>
 * <li> screen "start" for game loading at start
 * <li> screen "startMenu" with play button, options and exit
 * <li> screen "game" that include all game related elements like
 *      numbers of apples, numbers of water, shop button and related
 *      panel, option button and related panel
 * </ul>
 *
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class Gui {
    
    private NiftyJmeDisplay niftyDisplay;
    private Nifty nifty;
    private boolean gameStarted;
    private Main app;
    private AssetManager assetManager;
    private Node rootNode;
    private Node shootables;
    private List<Entity> entities;
    private Player p;
    private boolean placeEntity;
    private String selectedEntityFromShop;
    private boolean guiOpened;
    private float time;
    private Map<String, Integer> shopProducts;    
    
    /**
     * class constructor with all parameters from Main class.
     * 
     * @param app
     * @param assetManager
     * @param inputManager
     * @param audioRenderer
     * @param guiViewPort
     * @param rootNode
     * @param shootables
     * @param entities
     * @param p
     */
    public Gui (Main app, AssetManager assetManager, InputManager inputManager, AudioRenderer audioRenderer, ViewPort guiViewPort, Node rootNode, Node shootables, List<Entity> entities, Player p) {
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        
        this.gameStarted = false;
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
        
        this.shopProducts = new HashMap<>();
        this.shopProducts.put("Sprout", 5);
        this.shopProducts.put("Well", 10);
        this.shopProducts.put("Mill", 20);
        
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        
        /**
         * screen "game".
         */
        Screen gameScreen = new ScreenBuilder("game") {{
            controller(new GuiScreenController());

            // Style layer
            layer(new LayerBuilder("baseLayer") {{
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
                        filename("Interface/gui/menuIcon.png");
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
                    marginRight("2%");
                    marginBottom("2%");
                
                    // Shop image
                    image(new ImageBuilder() {{
                       id("ShopImage");
                       filename("Interface/gui/shopIcon.png");
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
                        control(new LabelBuilder("Apple", "") {{
                            font("Interface/Fonts/SegoeUIBlack.fnt");
                            //color("#f00f");
                            width("30%");
                            height("100%");
                            valignCenter();
                        }});
                
                        // Water bucket text
                        control(new LabelBuilder("WaterBucket", "") {{
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
                    marginRight("2%");
                    marginBottom("2%");
                
                    // Shop button
                    control(new ControlBuilder("shopButton", "") {{
                        width("53%");
                        height("98%");
                        visibleToMouse(true);
                    }});
                }});
                
                // tutorial label
                panel(new PanelBuilder() {{                    
                    id("TutorialPanel");
                    width("100%");
                    height("20%");
                    alignCenter();
                    childLayoutCenter();
                    valignBottom();
                    
                    control(new LabelBuilder("tutorialLabel", "")  {{
                        text("");
                        font("Interface/Fonts/SegoeUIBlack.fnt");
                        color("#000");
                        height("100%");
                        width("100%");
                    }});
                }});
                
                // shop arrow (for tutorial)
                panel(new PanelBuilder() {{
                    childLayoutHorizontal();
                    alignRight();
                    valignBottom();
                    width("9%");
                    height("13%");
                    marginRight("5%");
                    marginBottom("18%");    
                    
                    // shop arrow image
                    image(new ImageBuilder() {{
                        id("arrowImage");
                        filename("Interface/gui/arrowGui.png");
                        width("100%");
                        height("100%");
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
                       id("shopPanelImage");
                       filename("Interface/gui/shopModal.png");
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
                            id("itemImageCloose");
                            filename("Interface/gui/closeModalButton.png");
                            width("6.5%");
                            height("11%");
                            alignRight();
                            valignTop();
                            marginRight("4%");
                            marginTop("7%");
                        }});
                        
                        image(new ImageBuilder() {{
                            id("itemImage_1");
                            filename("Interface/gui/shopItemModal.png");
                            width("25%");
                            height("40%");
                            alignLeft();
                            valignTop();
                            marginLeft("7.5%");
                            marginTop("30%");
                            
                        }});
                        
                        image(new ImageBuilder() {{
                            id("itemImage_2");
                            filename("Interface/gui/shopItemModal.png");
                            width("25%");
                            height("40%");
                            alignLeft();
                            valignTop();
                            marginLeft("37.5%");
                            marginTop("30%");
                        }});
                        
                        image(new ImageBuilder() {{
                            id("itemImage_3");
                            filename("Interface/gui/shopItemModal.png");
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
                    id("ShopModalPanelInteractive");
                    childLayoutCenter();
                    height("70%");
                    width("70%");
                    alignCenter();
                    valignCenter();
                    visibleToMouse(true);
                    
                    // Content Modal panel
                    panel(new PanelBuilder() {{
                        id("ShopContentPanelInteractive");
                        childLayoutCenter();
                        width("100%");
                        height("100%");
                        visibleToMouse(true);
                        
                        // close Button
                        control(new ControlBuilder("closeButton_1", "") {{
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
                            id("ItemShopContentPanel_1");
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
                            id("ItemShopContentPanel_2");
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
                            id("ItemShopContentPanel_3");
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
                       id("MenuImagePanel");
                       filename("Interface/gui/menuModal.png");
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
                            id("imageClosebotton");
                            filename("Interface/gui/closeModalButton.png");
                            width("13%");
                            height("12.5%");
                            alignRight();
                            valignTop();
                            marginRight("9%");
                            marginTop("7%");
                        }});
                        
                        image(new ImageBuilder() {{
                            id("buttonMenu_1");
                            filename("Interface/gui/menuButton.png");
                            width("50%");
                            height("15%");
                            alignCenter();
                            valignTop();
                            marginTop("25%");
                        }});
                        
                        image(new ImageBuilder() {{
                            id("buttonMenu_2");
                            filename("Interface/gui/menuButton.png");
                            width("50%");
                            height("15%");
                            alignCenter();
                            valignTop();
                            marginTop("45%");
                        }});
                        
                        image(new ImageBuilder() {{
                            id("buttonMenu_3");
                            filename("Interface/gui/menuButton.png");
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
                    id("MenuModalPanelInteractive");
                    childLayoutCenter();
                    height("65%");
                    width("35%");
                    alignCenter();
                    valignCenter();
                    visibleToMouse(true);
                    
                    // Content Modal panel
                    panel(new PanelBuilder() {{
                        id("MenuContentPanelInteractive");
                        childLayoutCenter();
                        width("100%");
                        height("100%");
                        visibleToMouse(true);
                        
                        // close Button
                        control(new ControlBuilder("closeButton_2", "") {{
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
                        
                            interactOnRelease("openOptions()");
                            
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
                    height("100%");
                    width("100%");
                    alignCenter();
                    valignCenter();
                    visibleToMouse(true);
                    
                    // Content Modal panel
                    panel(new PanelBuilder() {{
                        id("CreditsContentPanel");
                        childLayoutCenter();
                        width("100%");
                        height("100%");
                        visibleToMouse(true);                        
                        
                        image(new ImageBuilder() {{
                            id("creditImage");
                            filename("Interface/gui/closeModalButton.png");
                            width("9%");
                            height("12%");
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
                    id("CreditsPanelInteractive");
                    childLayoutCenter();
                    height("100%");
                    width("100%");
                    alignCenter();
                    valignCenter();
                    visibleToMouse(true);
                    
                    // close Button
                    control(new ControlBuilder("closeButton_3", "") {{
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
                        text("Developer\nAlessandro Pilleri, Giorgia Bertacchini\n\n"
                                + "Programming language\nJava\n\n"
                                + "Game engine\njMonkeyEngine\n\n"
                                + "Relevant Libraries\nSQLite, NiftyGUI\n\n"
                                + "Graphic Department\nAlessandro Pilleri, Giorgia Bertacchini\n\n"
                                + "Audio Department\nMusic provided by Genshin Impact by Mihoyo\n\n"
                                + "Project for\n'Object-oriented programming'\nDepartment of Engineering \"Enzo Ferrari\" Modena\n\n"
                                + "Teacher\nNicola Bicocchi");
                        font("Interface/Fonts/SegoeUIBlack.fnt");
                        color("#fff");
                        height("80%");
                        width("100%");
                        alignLeft();
                    }});                
                }});
            }});
            
            // Option layer
            layer(new LayerBuilder("optionsLayer") {{
                childLayoutCenter();
                
                // Modal panel
                panel(new PanelBuilder() {{
                    id("optionsPanel");
                    childLayoutCenter();
                    width("100%");
                    height("100%");
                    visibleToMouse(true);
                    
                    image(new ImageBuilder() {{
                        filename("Interface/gui/optionMenu.png");
                        width("100%");
                        height("100%");
                    }});
                    
                    // Ambient tick panel
                    panel(new PanelBuilder() {{
                        id("AmbientTick");
                        childLayoutCenter();
                        width("6%");
                        height("10%");
                        alignRight();
                        valignTop();
                        marginRight("29.5%");
                        marginTop("28%");
                        visibleToMouse(true); 

                        image(new ImageBuilder() {{
                            id("AmbientVolumeTick");
                            filename("Interface/gui/tick.png");
                            width("100%");
                            height("100%");
                        }});
                    }});
                    
                    // Music tick panel
                    panel(new PanelBuilder() {{
                        id("MusicTick");
                        childLayoutCenter();
                        width("6%");
                        height("10%");
                        alignRight();
                        valignTop();
                        marginRight("29.5%");
                        marginTop("47%");
                        visibleToMouse(true);   

                        image(new ImageBuilder() {{
                            id("MusicVolumeTick");
                            filename("Interface/gui/tick.png");
                            width("100%");
                            height("100%");
                        }});
                    }});
                }});
            }});
            
            // Interactive Option Layer
            layer(new LayerBuilder("interactiveOptionsLayer") {{
                childLayoutCenter();
                
                // Ambient text panel
                panel(new PanelBuilder() {{
                    id("AmbientText");
                    childLayoutCenter();
                    width("20%");
                    height("20%");
                    alignCenter();
                    valignTop();
                    marginTop("23%");
                    visibleToMouse(true);
                    
                    text(new TextBuilder() {{
                        text("Ambient Volume");
                        font("Interface/Fonts/SegoeUIBlack.fnt");
                        color("#000");
                        height("100%");
                        width("100%");
                    }});
                }});
                
                // Music text panel
                panel(new PanelBuilder() {{
                    id("MusicText");
                    childLayoutCenter();
                    width("20%");
                    height("20%");
                    alignCenter();
                    valignTop();
                    marginTop("42.5%");
                    visibleToMouse(true);
                    
                    text(new TextBuilder() {{
                        text("Music Volume");
                        font("Interface/Fonts/SegoeUIBlack.fnt");
                        color("#000");
                        height("100%");
                        width("100%");
                    }});
                }});
                
                // Ambient button panel
                panel(new PanelBuilder() {{
                    id("AmbientButton");
                    childLayoutCenter();
                    width("6%");
                    height("10%");
                    alignRight();
                    valignTop();
                    marginRight("29.5%");
                    marginTop("28%");
                    visibleToMouse(true);                    
                    
                    control(new ControlBuilder("AmbientVolume", "") {{
                        width("100%");
                        height("100%");
                        visibleToMouse(true);
                    }});
                }});
                
                // Music button panel
                panel(new PanelBuilder() {{
                    id("MusicButton");
                    childLayoutCenter();
                    width("6%");
                    height("10%");
                    alignRight();
                    valignTop();
                    marginRight("29.5%");
                    marginTop("47%");
                    visibleToMouse(true);
                    
                    control(new ControlBuilder("MusicVolume", "") {{
                        width("100%");
                        height("100%");
                        visibleToMouse(true);                            
                    }});
                }});
                
                // Close options panel
                panel(new PanelBuilder() {{
                    id("closeOptionsPanel");
                    childLayoutCenter();
                    width("19%");
                    height("13%");
                    alignCenter();
                    valignTop();
                    marginTop("68.5%");
                    visibleToMouse(true);
                    
                    control(new ControlBuilder("CloseOptions", "") {{
                        width("100%");
                        height("100%");
                        visibleToMouse(true);
                       
                        interactOnRelease("closeOptions()");
                        
                        text(new TextBuilder() {{
                            text("Close");
                            font("Interface/Fonts/SegoeUIBlack.fnt");
                            color("#000");
                            height("100%");
                            width("100%");
                        }});
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
                       filename("Interface/gui/closeGameModal.png");
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
                    id("CloseGamePanelInteractive");
                    childLayoutCenter();
                    height("45%");
                    width("45%");
                    alignCenter();
                    valignCenter();
                    visibleToMouse(true);
                    
                    // Text panel
                    panel(new PanelBuilder() {{
                        id("CloseTextPanel");
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
                        id("CloseButtonPanel");
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

        /**
         * screen "start".
         */
        Screen startScreen = new ScreenBuilder("start") {{
            controller(new GuiScreenController());
            
            layer(new LayerBuilder("startBaseLayer") {{
            backgroundColor("#003f");
            childLayoutCenter();
            
                panel(new PanelBuilder() {{
                    width("100%");
                    height("100%");
                    childLayoutCenter();
                    
                    image(new ImageBuilder() {{
                        filename("Interface/gui/choraLoadingScreen.jpg");
                        width("100%");
                        height("100%");
                    }});
                }});
            }});
        }}.build(nifty);
        
        /**
         * screen "startMenu".
         */
        Screen startMenuScreen = new ScreenBuilder("startMenu") {{
            controller(new GuiScreenController());
            
            layer(new LayerBuilder("startMenuBaseLayer") {{
                backgroundColor("#003f");
                childLayoutCenter();
            
                panel(new PanelBuilder() {{
                    width("100%");
                    height("100%");
                    childLayoutCenter();  
                    
                    image(new ImageBuilder() {{
                        filename("Interface/gui/startingMenu.png");
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
                    
                    // Play Button
                    control(new ControlBuilder("PlayButton", "") {{
                        width("29%");
                        height("21%");
                        alignCenter();
                        valignBottom();
                        marginBottom("34%");
                        visibleToMouse(true);                            
                        
                        text(new TextBuilder() {{
                            text("Play");
                            font("Interface/Fonts/SegoeUIBlack.fnt");
                            color("#000");
                            height("100%");
                            width("100%");
                        }});
                    }});
                    
                    // Option Button
                    control(new ControlBuilder("StartOptionButton", "") {{
                        width("19.5%");
                        height("13%");
                        valignBottom();
                        alignLeft();
                        marginBottom("15%");
                        marginLeft("21%");
                        visibleToMouse(true);                            
                        
                        interactOnRelease("openOptions()");
                        
                        text(new TextBuilder() {{
                            text("Options");
                            font("Interface/Fonts/SegoeUIBlack.fnt");
                            color("#000");
                            height("100%");
                            width("100%");
                        }});
                    }});
                    
                    // Exit Button
                    control(new ControlBuilder("StartExitButton", "") {{
                        width("19.5%");
                        height("13%");
                        valignBottom();
                        alignRight();
                        marginBottom("15%");
                        marginRight("21%");
                        visibleToMouse(true);                            
                        
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
            
            // Option layer
            layer(new LayerBuilder("optionsLayer") {{
                childLayoutCenter();
                
                // Modal panel
                panel(new PanelBuilder() {{
                    id("optionsPanel");
                    childLayoutCenter();
                    width("100%");
                    height("100%");
                    visibleToMouse(true);
                    
                    image(new ImageBuilder() {{
                        filename("Interface/gui/optionMenu.png");
                        width("100%");
                        height("100%");
                    }});
                    
                    // Ambient tick panel
                    panel(new PanelBuilder() {{
                        id("AmbientTick");
                        childLayoutCenter();
                        width("6%");
                        height("10%");
                        alignRight();
                        valignTop();
                        marginRight("29.5%");
                        marginTop("28%");
                        visibleToMouse(true); 

                        image(new ImageBuilder() {{
                            id("AmbientVolumeTick");
                            filename("Interface/gui/tick.png");
                            width("100%");
                            height("100%");
                        }});
                    }});
                    
                    // Music tick panel
                    panel(new PanelBuilder() {{
                        id("MusicTick");
                        childLayoutCenter();
                        width("6%");
                        height("10%");
                        alignRight();
                        valignTop();
                        marginRight("29.5%");
                        marginTop("47%");
                        visibleToMouse(true);   

                        image(new ImageBuilder() {{
                            id("MusicVolumeTick");
                            filename("Interface/gui/tick.png");
                            width("100%");
                            height("100%");
                        }});
                    }});
                }});
            }});
            
            // Interactive Option Layer
            layer(new LayerBuilder("interactiveOptionsLayer") {{
                childLayoutCenter();
                
                // Ambient text panel
                panel(new PanelBuilder() {{
                    id("AmbientText");
                    childLayoutCenter();
                    width("20%");
                    height("20%");
                    alignCenter();
                    valignTop();
                    marginTop("23%");
                    visibleToMouse(true);
                    
                    text(new TextBuilder() {{
                        text("Ambient Volume");
                        font("Interface/Fonts/SegoeUIBlack.fnt");
                        color("#000");
                        height("100%");
                        width("100%");
                    }});
                }});
                
                // Music text panel
                panel(new PanelBuilder() {{
                    id("MusicText");
                    childLayoutCenter();
                    width("20%");
                    height("20%");
                    alignCenter();
                    valignTop();
                    marginTop("42.5%");
                    visibleToMouse(true);
                    
                    text(new TextBuilder() {{
                        text("Music Volume");
                        font("Interface/Fonts/SegoeUIBlack.fnt");
                        color("#000");
                        height("100%");
                        width("100%");
                    }});
                }});
                
                // Ambient button panel
                panel(new PanelBuilder() {{
                    id("AmbientButton");
                    childLayoutCenter();
                    width("6%");
                    height("10%");
                    alignRight();
                    valignTop();
                    marginRight("29.5%");
                    marginTop("28%");
                    visibleToMouse(true);                    
                    
                    control(new ControlBuilder("AmbientVolume", "") {{
                        width("100%");
                        height("100%");
                        visibleToMouse(true);
                    }});
                }});
                
                // Music button panel
                panel(new PanelBuilder() {{
                    id("MusicButton");
                    childLayoutCenter();
                    width("6%");
                    height("10%");
                    alignRight();
                    valignTop();
                    marginRight("29.5%");
                    marginTop("47%");
                    visibleToMouse(true);
                    
                    control(new ControlBuilder("MusicVolume", "") {{
                        width("100%");
                        height("100%");
                        visibleToMouse(true);                            
                    }});
                }});
                
                // Close options panel
                panel(new PanelBuilder() {{
                    id("closeOptionsPanel");
                    childLayoutCenter();
                    width("19%");
                    height("13%");
                    alignCenter();
                    valignTop();
                    marginTop("68.5%");
                    visibleToMouse(true);
                    
                    control(new ControlBuilder("CloseOptions", "") {{
                        width("100%");
                        height("100%");
                        visibleToMouse(true);
                       
                        interactOnRelease("closeOptions()");
                        
                        text(new TextBuilder() {{
                            text("Close");
                            font("Interface/Fonts/SegoeUIBlack.fnt");
                            color("#000");
                            height("100%");
                            width("100%");
                        }});
                    }});
                }});
            }});
        }}.build(nifty);
        
        /**
         * all screens are taken from the nifty object that can
         * switch between them.
         */
        nifty.addScreen("start", startScreen);
        nifty.addScreen("startMenu", startMenuScreen);
        nifty.addScreen("game", gameScreen);
        nifty.gotoScreen("startMenu");
        
        /**
         * GUI setup
         * Operations are needed for some gui elements, for example:
         * <ul>
         * <li> set text when the argument may change (dynamic texts)
         * <li> set visible to false for all elements that should not be
         *      displayed at the start
         * <li> set invokers to all elements that cause any specific event
         * </ul>
         */
        nifty.getScreen("game").findElementById("Apple").getRenderer(TextRenderer.class).setText(""+p.getApple());
        nifty.getScreen("game").findElementById("WaterBucket").getRenderer(TextRenderer.class).setText(""+p.getWaterBucket());
        
        nifty.getScreen("game").findElementById("menuLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveMenuLayer").setVisible(false);

        nifty.getScreen("game").findElementById("shopLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveShopLayer").setVisible(false);

        nifty.getScreen("game").findElementById("shopButton").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "openShop()", this));
        nifty.getScreen("game").findElementById("arrowImage").setVisible(false);
        
        nifty.getScreen("game").findElementById("creditsLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveCreditsLayer").setVisible(false);

        nifty.getScreen("startMenu").findElementById("optionsLayer").setVisible(false);
        nifty.getScreen("startMenu").findElementById("interactiveOptionsLayer").setVisible(false);
        nifty.getScreen("game").findElementById("optionsLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveOptionsLayer").setVisible(false);
        
        nifty.getScreen("startMenu").findElementById("AmbientTick").setVisible(p.getAmbientVolume());
        nifty.getScreen("startMenu").findElementById("MusicTick").setVisible(p.getMusicVolume());
        nifty.getScreen("game").findElementById("AmbientTick").setVisible(p.getAmbientVolume());
        nifty.getScreen("game").findElementById("MusicTick").setVisible(p.getMusicVolume());
        
        nifty.getScreen("startMenu").findElementById("AmbientVolume").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "switchAmbientVolume()", this));
        nifty.getScreen("startMenu").findElementById("MusicVolume").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "switchMusicVolume()", this));
        nifty.getScreen("game").findElementById("AmbientVolume").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "switchAmbientVolume()", this));
        nifty.getScreen("game").findElementById("MusicVolume").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "switchMusicVolume()", this));
        
        nifty.getScreen("game").findElementById("closeGameLayer").setVisible(false);
        nifty.getScreen("game").findElementById("interactiveCloseGameLayer").setVisible(false);

        nifty.getScreen("game").findElementById("yesButton").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "closeGame()", this));
        
        nifty.getScreen("game").findElementById("Item1_Button").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "buyFromShop(Sprout)", this));
        nifty.getScreen("game").findElementById("Item2_Button").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "buyFromShop(Well)", this));
        nifty.getScreen("game").findElementById("Item3_Button").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "buyFromShop(Mill)", this));

        nifty.getScreen("game").findElementById("alertLayer").setVisible(true);
        nifty.getScreen("game").findElementById("errorLabel").setVisible(false);
        nifty.getScreen("game").findElementById("placingModePanel").setVisible(false);
        
        nifty.getScreen("startMenu").findElementById("PlayButton").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "playGame()", this));
        nifty.getScreen("startMenu").findElementById("StartExitButton").getElementInteraction().getPrimary().setOnReleaseMethod(new NiftyMethodInvoker(nifty, "closeGame()", this));
    }
    
    /**
     *
     * @return nifty object
     */
    public Nifty getNifty() {
        return this.nifty;
    }
    
    /**
     *
     * @return boolean value if game is started
     */
    public boolean isGameStarted() {
        return this.gameStarted;
    }
    
    /**
     *
     * @param name player name displayed on screen
     */
    public void setPlayerName(String name) {
        nifty.getCurrentScreen().findElementById("PlayerInfo").getRenderer(TextRenderer.class).setText(""+name+"");
    }
    
    /**
     *
     * @param apple quantity of player's apples
     */
    public void setApple(int apple) {
        nifty.getScreen("game").findElementById("Apple").getRenderer(TextRenderer.class).setText(""+apple+"");
    }
    
    /**
     *
     * @param waterBucket quantity of player's water buckets
     */
    public void setWaterBucket(int waterBucket) {
        nifty.getScreen("game").findElementById("WaterBucket").getRenderer(TextRenderer.class).setText(""+waterBucket+"");
    }
    
    /**
     * switch ambient volume
     */
    public void switchAmbientVolume() {
        p.setAmbientVolume(!p.getAmbientVolume());
        nifty.getScreen("startMenu").findElementById("AmbientTick").setVisible(p.getAmbientVolume());
        nifty.getScreen("game").findElementById("AmbientTick").setVisible(p.getAmbientVolume());
    }
    
    /**
     * switch music volume
     */
    public void switchMusicVolume() {
        p.setMusicVolume(!p.getMusicVolume());
        nifty.getScreen("startMenu").findElementById("MusicTick").setVisible(p.getMusicVolume());
        nifty.getScreen("game").findElementById("MusicTick").setVisible(p.getMusicVolume());
    }
    
    /**
     * start the game from the screen "startMenu"
     */
    public void playGame() {
        nifty.gotoScreen("game");
        this.gameStarted = true;
        this.updateTutorial();  
    }
    
    /**
     * update state of tutorial
     * @see Player
     */
    public void updateTutorial() {
        if (p.getTutorial() != 0) {
            if (p.getTutorial() == 1) {
                nifty.getScreen("game").findElementById("tutorialLabel").getRenderer(TextRenderer.class).setText("Welcome to Chora!\nStart by removing the trash from this world.");
            }
            if (p.getTutorial() == 2 || p.getTutorial() == 3 || p.getTutorial() == 4) {
                nifty.getScreen("game").findElementById("tutorialLabel").getRenderer(TextRenderer.class).setText("Wow! You found a sprout! Pick up some water from the pound to use it on the sprout.");
            }
            if (p.getTutorial() == 5) {
                nifty.getScreen("game").findElementById("tutorialLabel").getRenderer(TextRenderer.class).setText("Now the sprout is fully grown into a tree! Every tree drops apples that you can spend in the market.");
            }
            if (p.getTutorial() == 6) {
                nifty.getScreen("game").findElementById("tutorialLabel").getRenderer(TextRenderer.class).setText("Check out the market to see all the offerings.");
                nifty.getScreen("game").findElementById("arrowImage").setVisible(true);
            }
            nifty.getScreen("game").findElementById("tutorialLabel").setVisible(true);
        } else {
            nifty.getScreen("game").findElementById("tutorialLabel").setVisible(false);
            nifty.getScreen("game").findElementById("arrowImage").setVisible(false);
        }
    }
    
    /**
     * close game
     */
    public void closeGame() {
        app.stop(); 
    }
    
    /**
     * open shop panel
     */
    public void openShop() {
        nifty.getScreen("game").findElementById("shopLayer").setVisible(true);
        nifty.getScreen("game").findElementById("interactiveShopLayer").setVisible(true);
        p.setTutorial(0);
    }
    
    /**
     * check the selected item and start the placing mode.
     * @param selectedEntity item choosen from the shop
     */
    public void buyFromShop(String selectedEntity) {
        if (p.getApple() >= shopProducts.get(selectedEntity)) {  
            System.out.println(p.getMill());
            if(("Mill".equals(selectedEntity) && p.getMill() < 2) || ("Well".equals(selectedEntity) && p.getWell() < 5) || "Sprout".equals(selectedEntity)) {
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
                nifty.getCurrentScreen().findElementById("errorLabel").getRenderer(TextRenderer.class).setText("Maximum number of " + selectedEntity + " reached.");
                nifty.getCurrentScreen().findElementById("errorLabel").setVisible(true);
                time = 5;
            }
        }
        else {
            nifty.getCurrentScreen().findElementById("errorLabel").getRenderer(TextRenderer.class).setText("Not enough apples.");  
            nifty.getCurrentScreen().findElementById("errorLabel").setVisible(true);
            time = 5;
        }
    }
    
    /**
     *
     * @return item selected from the shop
     */
    public String getSelectedEntityFromShop() {
        return this.selectedEntityFromShop;
    }
    
    /**
     * 
     * @param selectedEntity
     */
    public void setSelectedEntityFromShop(String selectedEntity) {
        this.selectedEntityFromShop = selectedEntity;
    }
    
    /**
     *
     * @return boolean value if entity is placed
     */
    public boolean getPlaceEntity() {
        return this.placeEntity;
    }
    
    /**
     *
     * @param placeEntity
     */
    public void setPlaceEntity(boolean placeEntity) {
        this.placeEntity = placeEntity;
    }
    
    /**
     *
     * @return if gui is opened
     */
    public boolean getGuiOpened() {
        return this.guiOpened;
    }
    
    /**
     *
     * @param guiOpened
     */
    public void setGuiOpened(boolean guiOpened) {
        this.guiOpened = guiOpened;
    }
    
    /**
     *
     * @return time remaining for labels to disappear
     */
    public float getTime() {
        return this.time;
    }
    
    /**
     * update the time remaining for labels to disappear
     * @param tpf time per frame
     */
    public void decreaseTime(float tpf) {
        this.time -= tpf;
    }
}
