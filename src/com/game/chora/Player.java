package com.game.chora;

/**
 * Player is a class that manage all player and some
 * game state information.
 * Player class parameters are:
 * <ul>
 * <li> Player name
 * <li> Player apples
 * <li> Player water buckets
 * <li> Numbers of wells (max number = 5)
 * <li> Numbers of mills (max number = 2)
 * <li> Quantity of water taken from the pound
 * <li> Ambient Volume on/off
 * <li> Music Volume on/off
 * <li> Tutorial state ( 0 = no tutorial,
 *                       1 = first step,
 *                       2 = second step,
 *                       3 = third step,
 *                       4 = fourth step,
 *                       5 = fifth step,
 *                       6 = sixth step )
 * </ul>
 *
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class Player {
    
    private String name;
    private int waterBucket;
    private int apple;
    private int well;
    private int mill;
    private int takePound;
    private boolean ambientVolume;
    private boolean musicVolume;
    private int tutorial;
    
    /**
     * class constructor.
     */
    public Player() {
        this.name = "Player";
        this.waterBucket = 0;
        this.apple = 0;
        this.well = 0;
        this.mill = 0;
        this.takePound = 0;
        this.ambientVolume = true;
        this.musicVolume = true;
        this.tutorial = 1;
    }
    
    /**
     *
     * @return player name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     *
     * @return player water bucket
     */
    public int getWaterBucket() {
        return this.waterBucket;
    }
    
    /**
     *
     * @return player apples
     */
    public int getApple() {
        return this.apple;
    }
    
    /**
     *
     * @return number of wells
     */
    public int getWell() {
        return this.well;
    }
    
    /**
     *
     * @return number of mills
     */
    public int getMill() {
        return this.mill;
    }
    
    /**
     *
     * @return quantity of water taken from pound
     */
    public int getTakePound() {
        return this.takePound;
    }
    
    /**
     *
     * @return ambient volume state
     */
    public boolean getAmbientVolume() {
        return this.ambientVolume;
    }
    
    /**
     *
     * @return music volume state
     */
    public boolean getMusicVolume() {
        return this.musicVolume;
    }
    
    /**
     *
     * @return tutorial state
     */
    public int getTutorial() {
        return this.tutorial;
    }
    
    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     *
     * @param waterBucket
     */
    public void setWaterBucket(int waterBucket) {
        this.waterBucket = waterBucket;
    }
    
    /**
     *
     * @param apple
     */
    public void setApple(int apple) {
        this.apple = apple;
    }
    
    /**
     *
     * @param well
     */
    public void setWell(int well) {
        this.well = well;
    }
    
    /**
     *
     * @param mill
     */
    public void setMill(int mill) {
        this.mill = mill;
    }
    
    /**
     *
     * @param takePound
     */
    public void setTakePound(int takePound) {
        this.takePound = takePound;
    }
    
    /**
     *
     * @param av
     */
    public void setAmbientVolume(boolean av) {
        this.ambientVolume = av;
    }
    
    /**
     *
     * @param mv
     */
    public void setMusicVolume(boolean mv) {
        this.musicVolume = mv;
    }

    /**
     *
     * @param tutorial
     */
    public void setTutorial(int tutorial) {
        this.tutorial = tutorial;
    }
}
