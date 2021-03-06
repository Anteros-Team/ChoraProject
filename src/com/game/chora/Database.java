package com.game.chora;

import com.game.chora.utils.EntitySerialization;
import com.jme3.math.Vector3f;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database is a class that create and manage game database.
 * Game data is saved as the game close and loaded at the start.
 * 
 * <p>
 * The database is created the first time you open the game. 
 * All tables are overrided every time as the game is closing with
 * the game information in that moment.
 * Information is divided in two tables:
 * 
 * Player table, that contains:
 * <ul>
 * <li> Player name
 * <li> Player apples
 * <li> Player water buckets
 * <li> Number of wells
 * <li> Number of mills
 * <li> Quantity of water taken from the Pound
 * <li> Ambient Volume on/off
 * <li> Music Volume on/off
 * <li> Tutorial state
 * </ul>
 * 
 * Entity table, that contains:
 * <ul>
 * <li> Autogenerated entity id 
 * <li> Entity position X, Y, Z
 * <li> Entity scale
 * <li> Entity pickbox size X, Y, Z
 * <li> Type of entity
 * </ul>
 * 
 * @author Giorgia Bertacchini
 * @author Alessandro Pilleri
 */
public class Database {
    
    private String url;
    
    /**
     * class constructor.
     */
    public Database() {
        
    }
    
    /**
     * create new database.
     * @param pathname 
     */
    public void createNewDatabase(String pathname) {
        
        this.url = "jdbc:sqlite:" + pathname + File.separator + "Chora.db";
       
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * create database connection.
     * @return connection
     */
    private Connection connect() {
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    /**
     * create Player and Entity tables.
     */
    public void createTables() {
        
        String sqlPlayer = "CREATE TABLE IF NOT EXISTS Player ("
                + " name text PRIMARY KEY,"
                + " apple integer DEFAULT 0,"
                + " waterBucket integer DEFAULT 0,"
                + " well integer DEFAULT 0,"
                + " mill integer DEFAULT 0,"
                + " takePound integer DEFAULT 0,"
                + " ambientVolume boolean DEFAULT true,"
                + " musicVolume boolean true,"
                + " tutorial integer DEFAULT 1"
                + ");";
        System.out.println(sqlPlayer);
        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            
            stmt.execute(sqlPlayer);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        String sqlEntity = "CREATE TABLE IF NOT EXISTS Entity ("
                + " id integer PRIMARY KEY AUTOINCREMENT,"
                + " positionX float NOT NULL,"
                + " positionY float NOT NULL,"
                + " positionZ float NOT NULL,"
                + " scale float DEFAULT 1,"
                + " pickboxSizeX float DEFAULT 0,"
                + " pickboxSizeY float DEFAULT 0,"
                + " pickboxSizeZ float DEFAULT 0,"
                + " typeOfEntity text NOT NULL"
                + ");";
        System.out.println(sqlEntity);
        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            
            stmt.execute(sqlEntity);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    /**
     * clear Player table.
     */
    public void clearTablePlayer() {
        Connection conn = this.connect();
        PreparedStatement ps = null;
        
        try {
            String sql = "DELETE FROM Player;";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.out.append(e.toString());
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * clear Entity table.
     */
    public void clearTableEntity() {
        Connection conn = this.connect();
        PreparedStatement ps = null;
        
        try {
            String sql = "DELETE FROM Entity;";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            
        } catch (Exception e) {
            System.out.append(e.toString());
        } finally {
            try {
                ps.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * drop Player and Entity tables.
     */
    public void dropTables() {
        
        String sqlPlayer = "DROP TABLE IF EXISTS 'Chora.Player' ";
        String sqlEntity = "DROP TABLE IF EXISTS 'Chora.Entity' ";
        
        System.out.println(sqlPlayer);
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sqlPlayer);
            stmt.close();
            conn.commit();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        System.out.println(sqlEntity);
        try (Connection conn = this.connect();
                Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sqlEntity);
            stmt.close();
            conn.commit();
            conn.close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            
            stmt.execute("VACUUM");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     *
     * insert row into Player table.
     * @param name
     * @param apple
     * @param waterBucket
     * @param well
     * @param mill
     * @param takePound
     * @param ambientVolume
     * @param musicVolume
     * @param tutorial
     */
    public void insertPlayer(String name, int apple, int waterBucket, int well, int mill, int takePound, boolean ambientVolume, boolean musicVolume, int tutorial) {
        
        String sql = "INSERT INTO Player(name, apple, waterBucket, well, mill, takePound, ambientVolume, musicVolume, tutorial) VALUES(?,?,?,?,?,?,?,?,?);";
        System.out.println(sql);
        
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setInt(2, apple);
            pstmt.setInt(3, waterBucket);
            pstmt.setInt(4, well);
            pstmt.setInt(5, mill);
            pstmt.setInt(6, takePound);
            pstmt.setBoolean(7, ambientVolume);
            pstmt.setBoolean(8, musicVolume);
            pstmt.setInt(9, tutorial);
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * 
     * insert row into Entity table.
     * @param position
     * @param scale
     * @param pickboxSize
     * @param typeOfEntity
     */
    public void insertEntity(Vector3f position, float scale, Vector3f pickboxSize, String typeOfEntity) {
        
        String sql = "INSERT INTO Entity(positionX, positionY, positionZ, scale, pickboxSizeX, pickboxSizeY, pickboxSizeZ, typeOfEntity) VALUES(?,?,?,?,?,?,?,?)";
        System.out.println(sql);
        
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setFloat(1, position.x);
            pstmt.setFloat(2, position.y);
            pstmt.setFloat(3, position.z);
            pstmt.setFloat(4, scale);
            pstmt.setFloat(5, pickboxSize.x);
            pstmt.setFloat(6, pickboxSize.y);
            pstmt.setFloat(7, pickboxSize.z);
            pstmt.setString(8, typeOfEntity);
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }
    
    /**
     * check if Player table is empty.
     * @return boolean
     */
    public boolean isPlayerEmpty() {
        
        String sql = "SELECT * FROM Player";
        System.out.println(sql);
        
         try (Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
             
            if (rs.next() == false) {
                System.out.println("Select is empty.");
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Row founded.");
        return false;
    }
    
    /**
     * perform a query to Player table.
     * @return query results
     */
    public Player queryPlayer() {
        
        String sql = "SELECT * FROM Player";
        Player p = new Player();
        System.out.println(sql);
        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                p.setName(rs.getString("name"));
                p.setApple(rs.getInt("apple"));
                p.setWaterBucket(rs.getInt("waterBucket"));
                p.setWell(rs.getInt("well"));
                p.setMill(rs.getInt("mill"));
                p.setTakePound(rs.getInt("takePound"));
                p.setAmbientVolume(rs.getBoolean("ambientVolume"));
                p.setMusicVolume(rs.getBoolean("musicVolume"));
                p.setTutorial(rs.getInt("tutorial"));
                System.out.println(p.toString());
                return p;
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return null;
    }
    
    /**
     * perform a query to Entity table.
     * @return query results
     */
    public List<EntitySerialization> queryEntity() {
        
        String sql = "SELECT * FROM Entity";
        List<EntitySerialization> entities;
        entities = new ArrayList<>();
        System.out.println(sql);
        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
        
            while (rs.next()) {
                EntitySerialization en = new EntitySerialization();
                en.setPosition(new Vector3f(rs.getFloat("positionX"), rs.getFloat("positionY"), rs.getFloat("positionZ")));
                en.setScale(rs.getFloat("scale"));
                en.setPickboxSize(new Vector3f(rs.getFloat("pickboxSizeX"), rs.getFloat("pickboxSizeY"), rs.getFloat("pickboxSizeZ")));
                en.setTypeOfEntity(rs.getString("typeOfEntity"));
                entities.add(en);
                System.out.println(en.toString());
            }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return entities;
    }
    
}
