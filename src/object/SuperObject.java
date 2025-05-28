package object;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

/**
 * Base class for all game objects
 */
public class SuperObject extends Entity {
    
    /**
     * Default constructor for SuperObject
     */
    public SuperObject() {
        // Set default collision area
        solidArea = new Rectangle(0, 0, 48, 48);
    }
    
    /**
     * Draws the object on screen
     * @param g2 Graphics2D object
     * @param gp GamePanel reference
     */
    public void draw(Graphics2D g2, GamePanel gp) {
        
        // Calculate screen position based on camera/player position
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        // Only draw if object is within screen bounds (optimization)
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
           worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
           worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
           worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            
            g2.drawImage(down1, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
} 