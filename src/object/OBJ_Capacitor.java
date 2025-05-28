package object;

import java.awt.Rectangle;
import main.GamePanel;

/**
 * Capacitor object that can be used in crafting
 */
public class OBJ_Capacitor extends SuperObject {
    
    GamePanel gp;
    
    /**
     * Constructor for Capacitor
     * @param gp GamePanel reference
     */
    public OBJ_Capacitor(GamePanel gp) {
        this.gp = gp;
        
        name = "Capacitor";
        description = "An electronic component that stores electrical energy";
        collision = false;
        usable = true;
        
        // Set collision area
        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        
        // Load capacitor image
        down1 = setup("/object/capacitor", gp.tileSize, gp.tileSize);
    }
} 