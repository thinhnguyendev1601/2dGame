package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

/**
 * Represents the player character
 */
public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    
    // Character position on screen
    public final int screenX;
    public final int screenY;
    
    // Inventory
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
    
    // Nearby object for interaction
    public Entity nearbyObject = null;
    protected int idelcounter=0;
    private int aniIndex=0;
    protected BufferedImage idelImage;
    protected BufferedImage[]idelanimation;
    private int waittime=0;
    private int onceTimeimport=0;
    private Boolean idelIsfull=false;

    /**
     * Constructor for Player
     * @param gp GamePanel reference
     * @param keyH KeyHandler reference
     */
    public Player(GamePanel gp, KeyHandler keyH) {
        
        this.gp = gp;
        this.keyH = keyH;
        
        // Center the player on screen
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);
        
        // Set collision area
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;
        
        setDefaultValues();
        getPlayerImage();
        if(onceTimeimport<1) {//once time import only
            importidleImg();
            loadidelAnimation();
            onceTimeimport++;
        }
    }
    
    /**
     * Set default values for the player
     */
    public void setDefaultValues() {
        
        // Starting position
        worldX = gp.tileSize * 10;
        worldY = gp.tileSize * 10;
        speed = 4;
        direction = "down";
        
        // Player status
        maxLife = 6;
        life = maxLife;
    }
    
    /**
     * Load player character sprites
     */
    private void loadidelAnimation(){
        idelanimation = new BufferedImage[5];
        for (int j = 0; j < idelanimation.length; j++)
            idelanimation[j] = idelImage.getSubimage(j * 64, 0, 64, 40);

    }
    private void importidleImg() {
        InputStream is = getClass().getResourceAsStream("/player/player_sprites.png");
        try {
            idelImage = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean isIdel(){
        if(keyH.downPressed==false && keyH.upPressed==false &&
                keyH.leftPressed== false && keyH.rightPressed==false){
            waittime++;
        }
        if(waittime<=180){
            return idelIsfull=false;
        }
        return idelIsfull=true;
    }
    public void getPlayerImage() {
        
       // try {
            // Load all player sprites
            // Front view (down)
           /* down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));

            // Back view (up)
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));

            // Side views
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));

        } catch(IOException e) {
            e.printStackTrace();
        }*/
        idel=setup("/player/mCidel",gp.tileSize,gp.tileSize);
        up1 = setup("/player/mCup1",gp.tileSize*2,gp.tileSize*2);
        up2 = setup("/player/mCup2",gp.tileSize*2,gp.tileSize*2);
        down1 = setup("/player/mCdown1",gp.tileSize*2,gp.tileSize*2);
        down2 = setup("/player/mCdown2",gp.tileSize*2,gp.tileSize*2);
        left1 = setup("/player/mCleft1",gp.tileSize*2,gp.tileSize*2);
        left2 = setup("/player/mCleft2",gp.tileSize*2,gp.tileSize*2);
        right1 = setup("/player/mCright1",gp.tileSize*2,gp.tileSize*2);
        right2 = setup("/player/mCright2",gp.tileSize*2,gp.tileSize*2);

    }
    
    /**
     * Updates player state
     */
    private void updateIdelanimation(){
        idelcounter++;
        if (idelcounter >= 10 && aniIndex <= idelanimation.length) {
            aniIndex++;
            idelcounter = 0;
        }
        if (aniIndex >= idelanimation.length) {
            aniIndex = 0;
        }
    }
    public void update() {
        if(idelIsfull==true) {
            updateIdelanimation();
        }
        // Check for player movement
        if(keyH.upPressed || keyH.downPressed || 
           keyH.leftPressed || keyH.rightPressed) {
            idelcounter=0;
            
            if(keyH.upPressed) {
                direction = "up";
            }
            else if(keyH.downPressed) {
                direction = "down";
            }
            else if(keyH.leftPressed) {
                direction = "left";
            }
            else if(keyH.rightPressed) {
                direction = "right";
            }
            
            // Check collision
            collisionOn = false;
            gp.cChecker.checkTile(this);
            
            // Check object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);
            
            // Check NPC collision if needed
            
            // If collision is false, player can move
            if(collisionOn == false) {
                
                switch(direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
                }
            }
            
            // Animate sprites
            spriteCounter++;
            if(spriteCounter > 10) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                }
                else if(spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
        
        // Check interact key
        if(keyH.interactPressed) {
            interactWithObject();
               keyH.interactPressed = false;
        }
        
        // Check for nearby objects for interaction
        checkNearbyObjects();
    }
    
    /**
     * Checks for objects near the player for interaction
     */
    private void checkNearbyObjects() {
        nearbyObject = null;
        
        // Search object array
        for(int i = 0; i < gp.obj.length; i++) {
            if(gp.obj[i] != null) {
                // Get entity's solid area position
                int entityLeftX = worldX + solidArea.x;
                int entityRightX = worldX + solidArea.x + solidArea.width;
                int entityTopY = worldY + solidArea.y;
                int entityBottomY = worldY + solidArea.y + solidArea.height;
                
                // Extend search range slightly beyond collision
                int extendedRange = gp.tileSize;
                
                // Get the object's solid area position
                int objLeftX = gp.obj[i].worldX + gp.obj[i].solidArea.x - extendedRange;
                int objRightX = gp.obj[i].worldX + gp.obj[i].solidArea.x + gp.obj[i].solidArea.width + extendedRange;
                int objTopY = gp.obj[i].worldY + gp.obj[i].solidArea.y - extendedRange;
                int objBottomY = gp.obj[i].worldY + gp.obj[i].solidArea.y + gp.obj[i].solidArea.height + extendedRange;
                
                // Check if player is near object
                if(entityRightX > objLeftX && entityLeftX < objRightX &&
                   entityBottomY > objTopY && entityTopY < objBottomY) {
                    nearbyObject = gp.obj[i];
                    break;
                }
            }
        }
    }
    
    /**
     * Interacts with nearby object
     */
    private void interactWithObject() {
        if(nearbyObject != null) {
            // Different interactions based on object type
            if(nearbyObject.name.equals("Flashlight")) {
                gp.ui.showMessage("You found a flashlight!");
                gp.ui.setObjective("Find your way to the technical tunnel");
                
                // Add to inventory
                addToInventory(nearbyObject);
                
                // Remove from world
                for(int i = 0; i < gp.obj.length; i++) {
                    if(gp.obj[i] == nearbyObject) {
                        gp.obj[i] = null;
                        break;
                    }
                }
                
                nearbyObject = null;
            }
            else if(nearbyObject.name.equals("Wall Tally")) {
                gp.ui.currentDialogue = "Tally marks on the wall... I've been keeping track of days?";
                gp.gameState = gp.dialogueState;
            }
            else if(nearbyObject.name.equals("Washbasin")) {
                gp.ui.currentDialogue = "The water still works. But it's so cold...";
                gp.gameState = gp.dialogueState;
            }
            else if(nearbyObject.name.equals("Bookshelf")) {
                gp.ui.currentDialogue = "Old books about engineering. Some notes about a shelter.";
                gp.gameState = gp.dialogueState;
            }
            else if(nearbyObject.name.equals("Broken Step")) {
                if(hasItem("OBJ_RepairKit")) {
                    gp.ui.currentDialogue = "I've fixed the broken step. Now I can continue upstairs.";
                    gp.gameState = gp.dialogueState;
                    
                    // Remove repair kit from inventory
                    removeItemByName("OBJ_RepairKit");
                    
                    // Update objective
                    gp.ui.setObjective("Continue to the living room");
                    
                    // Remove broken step
                    for(int i = 0; i < gp.obj.length; i++) {
                        if(gp.obj[i] == nearbyObject) {
                            gp.obj[i] = null;
                            break;
                        }
                    }
                } else {
                    gp.ui.currentDialogue = "This step is broken. I need something to fix it.";
                    gp.gameState = gp.dialogueState;
                }
            }
            else if(nearbyObject.name.equals("Steel Door")) {
                if(hasItem("OBJ_Radar") && hasItem("OBJ_FamilyPhoto") && hasItem("OBJ_Mask") && hasItem("OBJ_Notebook")) {
                    // Player has all required items, trigger Act I ending
                    gp.triggerActOneEnding();
                } else {
                    gp.ui.currentDialogue = "I need to collect essential items before I leave...";
                    gp.gameState = gp.dialogueState;
                }
            }
            else {
                // Generic collectible items
                gp.ui.showMessage("You found " + nearbyObject.name + "!");
                
                // Add to inventory
                addToInventory(nearbyObject);
                
                // Remove from world
                for(int i = 0; i < gp.obj.length; i++) {
                    if(gp.obj[i] == nearbyObject) {
                        gp.obj[i] = null;
                        break;
                    }
                }
                
                nearbyObject = null;
            }
        }
    }
    
    /**
     * Picks up an object and adds it to inventory
     * @param index Index of the object in the obj array
     */
    private void pickUpObject(int index) {
        if(index != 999) {
            // Auto-pickup items
            String objName = gp.obj[index].name;
            
            if(objName.equals("Flashlight") || 
               objName.equals("Capacitor") ||
               objName.equals("Sensor") ||
               objName.equals("Battery") ||
               objName.equals("RepairKit") ||
               objName.equals("FamilyPhoto") ||
               objName.equals("Mask") ||
               objName.equals("Notebook")) {
                
                gp.ui.showMessage("You picked up " + objName + "!");
                
                // Add to inventory
                addToInventory(gp.obj[index]);
                
                // Remove from world
                gp.obj[index] = null;
            }
        }
    }
    
    /**
     * Adds an item to the player's inventory
     * @param item Item to add
     * @return true if added successfully, false if inventory is full
     */
    public boolean addToInventory(Entity item) {
        if(inventory.size() < maxInventorySize) {
            inventory.add(item);
            return true;
        }
        return false;
    }
    
    /**
     * Checks if player has a specific item in inventory
     * @param itemName Name of the item to check
     * @return true if item is in inventory, false otherwise
     */
    public boolean hasItem(String itemName) {
        for(Entity item : inventory) {
            if(item.getClass().getSimpleName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Removes an item from inventory by name
     * @param itemName Name of the item to remove
     * @return true if removed, false if not found
     */
    public boolean removeItemByName(String itemName) {
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i).getClass().getSimpleName().equals(itemName)) {
                inventory.remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Uses an item from inventory
     * @param index Index of the item in inventory
     */
    public void useItem(int index) {
        if(index < inventory.size()) {
            Entity item = inventory.get(index);
            
            // Different behavior based on item type
            if(item.getClass().getSimpleName().equals("OBJ_Flashlight")) {
                gp.ui.showMessage("You turned on the flashlight.");
                // Implement flashlight functionality
            }
            else if(item.getClass().getSimpleName().equals("OBJ_FamilyPhoto")) {
                gp.ui.currentDialogue = "My family... I need to find them.";
                gp.gameState = gp.dialogueState;
            }
            else if(item.getClass().getSimpleName().equals("OBJ_Notebook")) {
                gp.ui.currentDialogue = "Notes about shelter locations and survival tips.";
                gp.gameState = gp.dialogueState;
            }
            
            // Some items stay in inventory after use, others are consumed
            // Add consumption logic here if needed
        }
    }
    
    /**
     * Draws the player character
     * @param g2 Graphics2D object
     */
    public void draw(Graphics2D g2) {
        
        BufferedImage image = null;
        
        switch(direction) {
        case "up":
            if(spriteNum == 1) {
                image = up1;
            }
            if(spriteNum == 2) {
                image = up2;
            }
            break;
        case "down":
            if(spriteNum == 1) {
                image = down1;
            }
            if(spriteNum == 2) {
                image = down2;
            }
            break;
        case "left":
            if(spriteNum == 1) {
                image = left1;
            }
            if(spriteNum == 2) {
                image = left2;
            }
            break;
        case "right":
            if(spriteNum == 1) {
                image = right1;
            }
            if(spriteNum == 2) {
                image = right2;
            }
            break;
        }
        if(idelIsfull==true){
            image=idelanimation[aniIndex];
        }
        
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}