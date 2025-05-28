package object;

import java.awt.Rectangle;
import main.GamePanel;

/**
 * Sensor object that can be used in crafting
 */
public class OBJ_Sensor extends SuperObject {
    
    GamePanel gp;
    
    /**
     * Constructor for Sensor
     * @param gp GamePanel reference
     */
    public OBJ_Sensor(GamePanel gp) {
        this.gp = gp;
        
        name = "Sensor";
        description = "A sensitive electronic sensor that can detect signals";
        collision = false;
        usable = true;
        
        // Set collision area
        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        
        // Load sensor image
        down1 = setup("/object/sensor", gp.tileSize, gp.tileSize);
    }
} 