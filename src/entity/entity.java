package entity;

import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Base class for all game entities (player, NPCs, etc.)
 */
public class Entity {

    // Position on world map
    public int worldX, worldY;
    public int speed;
    
    // Image sprites for different directions
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;
    
    // Animation counter
    public int spriteCounter = 0;
    public int spriteNum = 1;
    
    // Collision
    public Rectangle solidArea;
    public boolean collisionOn = false;
    
    // Entity stats
    public int maxLife;
    public int life;
    
    // Item properties
    public String name;
    public boolean collision = false;
    public boolean usable = false;
    public String description = "";
    public BufferedImage setup(String imagePath, int width, int height)
    {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try
        {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image,width,height);   //it scales to tilesize , will fix for player attack(16px x 32px) by adding width and height
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }
    
    /**
     * Updates entity state
     * Override in subclasses
     */
    public void update() {
        // Base update logic
    }
    
    /**
     * Draws the entity
     * @param g2 Graphics2D object
     */
    public void draw(Graphics2D g2) {
        // Base drawing logic
    }
}