package object;

import java.awt.Rectangle;
import main.GamePanel;

/**
 * Family Photo object that can be collected
 */
public class OBJ_FamilyPhoto extends SuperObject {
    
    GamePanel gp;
    
    /**
     * Constructor for Family Photo
     * @param gp GamePanel reference
     */
    public OBJ_FamilyPhoto(GamePanel gp) {
        this.gp = gp;
        
        name = "Family Photo";
        description = "A photo of your family. You need to find them.";
        collision = false;
        usable = true;
        
        // Set collision area
        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 48;
        solidArea.height = 48;
        
        // Load family photo image
        down1 = setup("/object/familyPhoto", gp.tileSize, gp.tileSize);
    }
} 