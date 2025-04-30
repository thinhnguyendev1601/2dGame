package main;

import object.OBJ_Battery;
import object.OBJ_Capacitor;
import object.OBJ_FamilyPhoto;
import object.OBJ_Flashlight;
import object.OBJ_Mask;
import object.OBJ_Notebook;
import object.OBJ_RepairKit;
import object.OBJ_Sensor;
import object.SuperObject;

/**
 * Places objects in the game world for Act I
 */
public class AssetSetter {
    
    GamePanel gp;
    
    /**
     * Constructor for AssetSetter
     * @param gp GamePanel reference
     */
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    
    /**
     * Sets up objects in the game world
     * Organized by location for Act I
     */
    public void setObject() {
        // Clear existing objects
        for(int i = 0; i < gp.obj.length; i++) {
            gp.obj[i] = null;
        }
        
        // BEDROOM OBJECTS
        setupBedroomObjects();
        
        // TECHNICAL TUNNEL OBJECTS
        setupTunnelObjects();
        
        // STAIRS OBJECTS
        setupStairsObjects();
        
        // LIVING ROOM OBJECTS
        setupLivingRoomObjects();
    }
    
    /**
     * Sets up objects in the bedroom area
     */
    private void setupBedroomObjects() {
        // Flashlight in bedroom
        gp.obj[0] = new OBJ_Flashlight(gp);
        gp.obj[0].worldX = gp.tileSize * 7;
        gp.obj[0].worldY = gp.tileSize * 5;
        
        // Wall Tally Marks (created as a custom interaction object)
        // This would be a visual element in the tilemap, but we need interaction
        gp.obj[1] = new SuperObject();
        gp.obj[1].name = "Wall Tally";
        gp.obj[1].down1 = gp.tileM.tile[12].image; // Using a specific tile image
        gp.obj[1].collision = true;
        gp.obj[1].worldX = gp.tileSize * 10;
        gp.obj[1].worldY = gp.tileSize * 3;
        
        // Washbasin
        gp.obj[2] = new SuperObject();
        gp.obj[2].name = "Washbasin";
        gp.obj[2].down1 = gp.tileM.tile[13].image; // Using a specific tile image
        gp.obj[2].collision = true;
        gp.obj[2].worldX = gp.tileSize * 12;
        gp.obj[2].worldY = gp.tileSize * 3;
        
        // Bookshelf
        gp.obj[3] = new SuperObject();
        gp.obj[3].name = "Bookshelf";
        gp.obj[3].down1 = gp.tileM.tile[14].image; // Using a specific tile image
        gp.obj[3].collision = true;
        gp.obj[3].worldX = gp.tileSize * 5;
        gp.obj[3].worldY = gp.tileSize * 3;
    }
    
    /**
     * Sets up objects in the technical tunnel area
     */
    private void setupTunnelObjects() {
        // These objects will only be visible when in the tunnel area
        // Capacitor for radar
        gp.obj[4] = new OBJ_Capacitor(gp);
        gp.obj[4].worldX = gp.tileSize * 22;
        gp.obj[4].worldY = gp.tileSize * 6;
        
        // Sensor for radar
        gp.obj[5] = new OBJ_Sensor(gp);
        gp.obj[5].worldX = gp.tileSize * 26;
        gp.obj[5].worldY = gp.tileSize * 8;
        
        // Battery for radar
        gp.obj[6] = new OBJ_Battery(gp);
        gp.obj[6].worldX = gp.tileSize * 30;
        gp.obj[6].worldY = gp.tileSize * 7;
    }
    
    /**
     * Sets up objects in the stairs area
     */
    private void setupStairsObjects() {
        // Broken Step
        gp.obj[7] = new SuperObject();
        gp.obj[7].name = "Broken Step";
        gp.obj[7].down1 = gp.tileM.tile[15].image; // Using a specific tile image
        gp.obj[7].collision = true;
        gp.obj[7].worldX = gp.tileSize * 35;
        gp.obj[7].worldY = gp.tileSize * 8;
        
        // Repair materials
        gp.obj[8] = new OBJ_RepairKit(gp);
        gp.obj[8].worldX = gp.tileSize * 33;
        gp.obj[8].worldY = gp.tileSize * 10;
    }
    
    /**
     * Sets up objects in the living room area
     */
    private void setupLivingRoomObjects() {
        // Family Photo
        gp.obj[9] = new OBJ_FamilyPhoto(gp);
        gp.obj[9].worldX = gp.tileSize * 42;
        gp.obj[9].worldY = gp.tileSize * 6;
        
        // Mask
        gp.obj[10] = new OBJ_Mask(gp);
        gp.obj[10].worldX = gp.tileSize * 45;
        gp.obj[10].worldY = gp.tileSize * 8;
        
        // Notebook
        gp.obj[11] = new OBJ_Notebook(gp);
        gp.obj[11].worldX = gp.tileSize * 40;
        gp.obj[11].worldY = gp.tileSize * 9;
        
        // Steel Door (exit to Act II)
        gp.obj[12] = new SuperObject();
        gp.obj[12].name = "Steel Door";
        gp.obj[12].down1 = gp.tileM.tile[16].image; // Using a specific tile image
        gp.obj[12].collision = true;
        gp.obj[12].worldX = gp.tileSize * 48;
        gp.obj[12].worldY = gp.tileSize * 7;
    }
    
    /**
     * Resets objects for a new game
     */
    public void resetObjects() {
        setObject();
    }
    
    /**
     * Updates object visibility based on current location
     * @param currentLocation Current location name
     */
    public void updateObjectVisibility(String currentLocation) {
        // Hide/show objects based on current location
        // Bedroom objects (index 0-3)
        boolean inBedroom = currentLocation.equals("Bedroom");
        for(int i = 0; i <= 3; i++) {
            if(gp.obj[i] != null) {
                // Set to be outside screen if not in right location
                if(!inBedroom) {
                    gp.obj[i].worldX = -1000;
                    gp.obj[i].worldY = -1000;
                } else {
                    resetObjectPosition(i);
                }
            }
        }
        
        // Technical Tunnel objects (index 4-6)
        boolean inTunnel = currentLocation.equals("TechnicalTunnel");
        for(int i = 4; i <= 6; i++) {
            if(gp.obj[i] != null) {
                if(!inTunnel) {
                    gp.obj[i].worldX = -1000;
                    gp.obj[i].worldY = -1000;
                } else {
                    resetObjectPosition(i);
                }
            }
        }
        
        // Stairs objects (index 7-8)
        boolean inStairs = currentLocation.equals("Stairs");
        for(int i = 7; i <= 8; i++) {
            if(gp.obj[i] != null) {
                if(!inStairs) {
                    gp.obj[i].worldX = -1000;
                    gp.obj[i].worldY = -1000;
                } else {
                    resetObjectPosition(i);
                }
            }
        }
        
        // Living Room objects (index 9-12)
        boolean inLivingRoom = currentLocation.equals("LivingRoom");
        for(int i = 9; i <= 12; i++) {
            if(gp.obj[i] != null) {
                if(!inLivingRoom) {
                    gp.obj[i].worldX = -1000;
                    gp.obj[i].worldY = -1000;
                } else {
                    resetObjectPosition(i);
                }
            }
        }
    }
    
    /**
     * Resets an object's position to its original coordinates
     * @param index Object index
     */
    private void resetObjectPosition(int index) {
        // This would restore the object to its original position
        // In a full implementation, you'd store original positions
        // For this example, we'll use the setObject method again
        setObject();
    }
}