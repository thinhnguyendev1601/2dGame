package object;

import java.awt.Rectangle;
import main.GamePanel;

/**
 * Mask object that can be collected
 */
public class OBJ_Mask extends SuperObject {
    
    GamePanel gp;
    
    /**
     * Constructor for Mask
     * @param gp GamePanel reference
     */
    public OBJ_Mask(GamePanel gp) {
        this.gp = gp;
        
        name = "Mask";
        description = "A protective mask. Essential for survival outside.";
        collision = false;
        usable = true;
        
        // Set collision area
        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        
        // Load mask image
        down1 = setup("/object/mask", gp.tileSize, gp.tileSize);
    }
} 