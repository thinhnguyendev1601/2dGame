package object;

import java.awt.Rectangle;
import main.GamePanel;

/**
 * Repair Kit object that can be used to craft items
 */
public class OBJ_RepairKit extends SuperObject {
    
    GamePanel gp;
    
    /**
     * Constructor for Repair Kit
     * @param gp GamePanel reference
     */
    public OBJ_RepairKit(GamePanel gp) {
        this.gp = gp;
        
        name = "Repair Kit";
        description = "A toolkit containing various tools and parts for repairs";
        collision = false;
        usable = true;
        
        // Set collision area
        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        
        // Load repair kit image
        down1 = setup("/object/repairKit", gp.tileSize, gp.tileSize);
    }
} 