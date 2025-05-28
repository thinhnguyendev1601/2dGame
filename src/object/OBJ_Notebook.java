package object;

import java.awt.Rectangle;
import main.GamePanel;

/**
 * Notebook object that can be collected
 */
public class OBJ_Notebook extends SuperObject {
    
    GamePanel gp;
    
    /**
     * Constructor for Notebook
     * @param gp GamePanel reference
     */
    public OBJ_Notebook(GamePanel gp) {
        this.gp = gp;
        
        name = "Notebook";
        description = "A notebook containing important survival information.";
        collision = false;
        usable = true;
        
        // Set collision area
        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        
        // Load notebook image
        down1 = setup("/object/notebook", gp.tileSize, gp.tileSize);
    }
} 