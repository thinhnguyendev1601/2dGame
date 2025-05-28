package object;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.AlphaComposite;
import java.awt.RadialGradientPaint;

import main.GamePanel;

/**
 * Flashlight object that provides lighting functionality
 */
public class OBJ_Flashlight extends SuperObject {
    
    GamePanel gp;
    public boolean isActive = false;
    public int lightRadius = 150; // Radius of the light circle
    public Color lightColor = new Color(255, 255, 200, 100); // Warm light color with transparency
    
    /**
     * Constructor for OBJ_Flashlight
     * @param gp GamePanel reference
     */
    public OBJ_Flashlight(GamePanel gp) {
        this.gp = gp;
        
        name = "Flashlight";
        description = "A flashlight that can illuminate dark areas";
        collision = false;
        usable = true;
        
        // Set collision area
        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        
        // Load flashlight image
        down1 = setup("/object/flashlight", gp.tileSize, gp.tileSize);
    }
    
    /**
     * Toggles the flashlight on/off
     */
    public void toggle() {
        isActive = !isActive;
        if (isActive) {
            System.out.println("Flashlight turned ON - lighting radius: " + lightRadius);
        } else {
            System.out.println("Flashlight turned OFF");
        }
    }
    
    /**
     * Sets the flashlight state
     * @param active Whether the flashlight should be active
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    /**
     * Checks if flashlight is currently active
     * @return true if flashlight is on, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Draws the lighting effect around the player when flashlight is active
     * This method should be called from GamePanel's paintComponent method
     * @param g2 Graphics2D object
     */
    public void drawLightingEffect(Graphics2D g2) {
        if (!isActive) {
            return;
        }
        
        // Save original composite
        Composite originalComposite = g2.getComposite();
        
        // Create a dark overlay for the entire screen
        g2.setColor(new Color(0, 0, 0, 180)); // Dark overlay
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // Create circular light area around player
        int playerCenterX = gp.player.screenX + gp.tileSize/2;
        int playerCenterY = gp.player.screenY + gp.tileSize/2;
        
        // Create the light circle
        Ellipse2D lightCircle = new Ellipse2D.Double(
            playerCenterX - lightRadius, 
            playerCenterY - lightRadius, 
            lightRadius * 2, 
            lightRadius * 2
        );
        
        // Create radial gradient for smooth light falloff
        Point2D center = new Point2D.Float(playerCenterX, playerCenterY);
        float[] dist = {0.0f, 0.7f, 1.0f};
        Color[] colors = {
            new Color(255, 255, 200, 0),    // Bright center (transparent)
            new Color(255, 255, 200, 100),  // Medium light
            new Color(0, 0, 0, 180)         // Dark edge
        };
        
        try {
            RadialGradientPaint rgp = new RadialGradientPaint(center, lightRadius, dist, colors);
            g2.setPaint(rgp);
            
            // Use XOR composite to "subtract" the light area from the dark overlay
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));
            g2.fill(lightCircle);
            
            // Draw a subtle light glow
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2.setColor(lightColor);
            g2.fill(lightCircle);
            
        } catch (Exception e) {
            // Fallback simple circular light if gradient fails
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));
            g2.setColor(Color.WHITE);
            g2.fill(lightCircle);
        }
        
        // Restore original composite
        g2.setComposite(originalComposite);
    }
    
    /**
     * Alternative lighting method using Area subtraction
     * @param g2 Graphics2D object
     */
    public void drawLightingEffectAlternative(Graphics2D g2) {
        if (!isActive) {
            return;
        }
        
        // Create the dark overlay area
        Area darkArea = new Area(new Rectangle2D.Double(0, 0, gp.screenWidth, gp.screenHeight));
        
        // Create the light circle around player
        int playerCenterX = gp.player.screenX + gp.tileSize/2;
        int playerCenterY = gp.player.screenY + gp.tileSize/2;
        
        Ellipse2D lightCircle = new Ellipse2D.Double(
            playerCenterX - lightRadius, 
            playerCenterY - lightRadius, 
            lightRadius * 2, 
            lightRadius * 2
        );
        
        // Subtract the light circle from the dark area
        darkArea.subtract(new Area(lightCircle));
        
        // Draw the dark area
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fill(darkArea);
        
        // Add a subtle glow effect
        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        g2.setColor(lightColor);
        g2.fill(lightCircle);
        g2.setComposite(originalComposite);
    }
    
    /**
     * Updates the flashlight (can be used for battery drain, flickering effects, etc.)
     */
    @Override
    public void update() {
        // Future enhancements: battery drain, flickering, etc.
        super.update();
    }
    
    /**
     * Draws the flashlight object when it's on the ground
     * @param g2 Graphics2D object
     * @param gp GamePanel reference
     */
    public void draw(Graphics2D g2, GamePanel gp) {
        // Calculate screen position
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        // Only draw if object is within screen bounds
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
           worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
           worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
           worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            
            g2.drawImage(down1, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}
