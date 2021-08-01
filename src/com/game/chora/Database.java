package com.game.chora;

import com.game.chora.utils.Entity;
import com.game.chora.utils.EntitySerialization;
import com.jme3.math.Vector3f;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


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
    
    public void createTables() {
        
        String sqlPlayer = "CREATE TABLE IF NOT EXISTS Player ("
                + "             name text PRIMARY KEY,"
                + "             apple integer DEFAULT 0,"
                + "             waterBucket integer DEFAULT 0"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sqlPlayer);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        String sqlEntity = "CREATE TABLE IF NOT EXISTS Entity ("
                + "             id integer PRIMARY KEY AUTOINCREMENT,"
                + "             positionX float NOT NULL,"
                + "             positionY float NOT NULL,"
                + "             positionZ float NOT NULL,"
                + "             scale float DEFAULT 1,"
                + "             pickboxSizeX float DEFAULT 0,"
                + "             pickboxSizeY float DEFAULT 0,"
                + "             pickboxSizeZ float DEFAULT 0,"
                + "             typeOfEntity text NOT NULL"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            
            stmt.execute(sqlEntity);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    public void dropTables() {
        
        String sqlPlayer = "TRUNCATE TABLE Player";
        String sqlEntity = "TRUNCATE TABLE Entity";
        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            
            stmt.execute(sqlPlayer);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            
            stmt.execute(sqlEntity);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
    }
    
    public void insertPlayer(String name, int apple, int waterBucket) {
        
        String sql = "INSERT INTO Player VALUES(\"" + name + "\"," + apple + "," + waterBucket + ")";
        System.out.println(sql);
        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            
        } catch (SQLException e) {
            System.out.println("Player insert error: " + e.getMessage());
        }
    }
    
    public void insertEntity(Vector3f position, float scale, Vector3f pickboxSize, String typeOfEntity) {
        
        String sql = "INSERT INTO Entity(positionX, positionY, positionZ, scale, pickboxSizeX, pickboxSizeY, pickboxSizeZ, typeOfEntity) VALUES(" + position.x + "," + position.y + "," + position.z + "," +  scale + "," + pickboxSize.x + "," + pickboxSize.y + "," + pickboxSize.z + ",\"" + typeOfEntity + "\")";
        System.out.println(sql);
        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            
        } catch (SQLException e) {
            System.out.println("Entity insert error: " + e.getMessage());
        }
    }
    
    public boolean isPlayerEmpty() {
        
        String sql = "SELECT * FROM Player";
        
         try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return false;
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    
    public Player queryPlayer() {
        
        String sql = "SELECT * FROM Player";
        Player p = new Player();
        
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                p.setName(rs.getString("name"));
                p.setApple(rs.getInt("apple"));
                p.setWaterBucket(rs.getInt("waterBucket"));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return p;
    }
    
    public List<EntitySerialization> queryEntity() {
        String sql = "SELECT * FROM Entity";
        List<EntitySerialization> entities;
        entities = new ArrayList<>();
        
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
            }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return entities;
    }
    
}
