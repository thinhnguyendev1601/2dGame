package object;

import java.awt.Rectangle;
import main.GamePanel;

/**
 * Battery object that can be used to power devices
 */
public class OBJ_Battery extends SuperObject {
    
    GamePanel gp;
    
    /**
     * Constructor for Battery
     * @param gp GamePanel reference
     */
    public OBJ_Battery(GamePanel gp) {
        this.gp = gp;
        
        name = "Battery";
        description = "A rechargeable battery that can power electronic devices";
        collision = false;
        usable = true;
        
        // Set collision area
        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        
        // Load battery image
        down1 = setup("/object/battery", gp.tileSize, gp.tileSize);
    }
} 