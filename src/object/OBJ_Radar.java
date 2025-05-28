package object;

import java.awt.Rectangle;
import main.GamePanel;

/**
 * Radar object that can be crafted and used to detect signals
 */
public class OBJ_Radar extends SuperObject {
    
    GamePanel gp;
    private boolean powered;
    
    /**
     * Constructor for basic Radar
     * @param gp GamePanel reference
     */
    public OBJ_Radar(GamePanel gp) {
        this(gp, false);
    }
    
    /**
     * Constructor for Radar with power state
     * @param gp GamePanel reference
     * @param powered Whether the radar is powered
     */
    public OBJ_Radar(GamePanel gp, boolean powered) {
        this.gp = gp;
        this.powered = powered;
        
        name = powered ? "Powered Radar" : "Radar";
        description = powered ? 
            "A fully functional radar that can detect signals" :
            "A basic radar that needs repairs and power";
        collision = false;
        usable = true;
        
        // Set collision area
        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        
        // Load radar image
        down1 = setup("/object/radar", gp.tileSize, gp.tileSize);
    }
    
    /**
     * Checks if radar is powered
     * @return true if radar is powered
     */
    public boolean isPowered() {
        return powered;
    }
} 