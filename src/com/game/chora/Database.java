package com.game.chora;

import com.game.chora.utils.Entity;
import com.game.chora.utils.EntitySerialization;
import com.jme3.math.Vector3f;
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


public class Database {
    
    protected String url;
    
    public Database() {
    }
    
    public void createNewDatabase(String pathname) {
        
        this.url = "jdbc:sqlite:" + pathname + "Chora.db";
       
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
    
    private Connection connect() {
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public void createTables() {
        
        String sqlPlayer = "CREATE TABLE IF NOT EXISTS Player ("
                + " name text PRIMARY KEY,"
                + " apple integer DEFAULT 0,"
                + " waterBucket integer DEFAULT 0,"
                + " well integer DEFAULT 0,"
                + " mill integer DEFAULT 0,"
                + " takePound integer DEFAULT 0,"
                + " ambientVolume boolean DEFAULT true,"
                + " musicVolume boolean true"
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
    
    public void insertPlayer(String name, int apple, int waterBucket, int well, int mill, int takePound, boolean ambientVolume, boolean musicVolume) {
        
        String sql = "INSERT INTO Player(name, apple, waterBucket, well, mill, takePound, ambientVolume, musicVolume) VALUES(?,?,?,?,?,?,?,?);";
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
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
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
                System.out.println(p.toString());
                return p;
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return null;
    }
    
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
