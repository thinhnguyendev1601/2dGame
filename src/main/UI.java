package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Heart;
import object.OBJ_Key;
import object.OBJ_ManaPortion;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font arial_20, arial_30, arial_40, arial_80B;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int slotCol = 0;
    public int slotRow = 0;
    
    // INVENTORY UI IMAGES
    public BufferedImage inventoryWindow, craftingWindow, dialogueWindow;
    
    // For Act I specific mechanics
    public boolean tutorialActive = true;
    public String currentObjective = "Explore the bedroom";
    public boolean radarActive = false;
    public int signalStrength = 0;
    
    // For cutscene transitions
    public int transitionCounter = 0;
    public boolean isTransitioning = false;
    public boolean fadeToWhite = false;
    
    public UI(GamePanel gp) {
        this.gp = gp;
        
        arial_20 = new Font("Arial", Font.PLAIN, 20);
        arial_30 = new Font("Arial", Font.PLAIN, 30);
        arial_40 = new Font("Arial", Font.PLAIN, 40);
        arial_80B = new Font("Arial", Font.BOLD, 80);
        
        // CREATE HUD OBJECTS
        Entity heart = new OBJ_Heart(gp);
        Entity manaPotion = new OBJ_ManaPortion(gp);
        Entity key = new OBJ_Key(gp);
        
        // LOAD UI IMAGES
        try {
            inventoryWindow = ImageIO.read(getClass().getResourceAsStream("/ui/inventory_window.png"));
            craftingWindow = ImageIO.read(getClass().getResourceAsStream("/ui/crafting_window.png"));
            dialogueWindow = ImageIO.read(getClass().getResourceAsStream("/ui/dialogue_window.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showMessage(String text) {
        message = text;
        messageOn = true;
        messageCounter = 0;
    }
    
    public void setObjective(String objective) {
        currentObjective = objective;
        showMessage("New objective: " + objective);
    }
    
    public void startTransition(boolean toWhite) {
        isTransitioning = true;
        fadeToWhite = toWhite;
        transitionCounter = 0;
    }
    
    public void update() {
        // Message fade out
        if(messageOn) {
            messageCounter++;
            if(messageCounter > 120) {
                messageOn = false;
            }
        }
        
        // Transition effect
        if(isTransitioning) {
            transitionCounter++;
            if(transitionCounter > 60) {
                isTransitioning = false;
                transitionCounter = 0;
                
                // Trigger any post-transition effects here
            }
        }
        
        // Radar pulse effect if active
        if(radarActive) {
            signalStrength = (signalStrength + 1) % 60; // Pulse every second
        }
    }
    
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        
        g2.setFont(arial_40);
        g2.setColor(Color.white);
        
        // TITLE STATE
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        
        // PLAY STATE
        if(gp.gameState == gp.playState) {
            // Draw game elements
            drawPlayerLife();
            drawMessage();
            drawObjective();
            
            // Draw radar UI if active
            if(radarActive) {
                drawRadar();
            }
            
            // Draw interaction prompt if needed
            if(gp.player.nearbyObject != null) {
                drawInteractionPrompt();
            }
        }
        
        // PAUSE STATE
        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
        
        // DIALOGUE STATE
        if(gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }
        
        // INVENTORY STATE
        if(gp.gameState == gp.inventoryState) {
            drawInventoryScreen();
        }
        
        // CRAFTING STATE
        if(gp.gameState == gp.craftingState) {
            drawCraftingScreen();
        }
        
        // CUTSCENE STATE
        if(gp.gameState == gp.cutsceneState) {
            // Cutscene manager handles most drawing
            // But we can add transition effects here
        }
        
        // Draw screen transition if active
        if(isTransitioning) {
            drawTransition();
        }
    }
    
    public void drawTitleScreen() {
        // Title screen implementation
        g2.setColor(new Color(70, 120, 80));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "RẠN"; // Game title from document
        int x = getXForCenteredText(text);
        int y = gp.tileSize * 3;
        
        // Shadow
        g2.setColor(Color.black);
        g2.drawString(text, x+5, y+5);
        
        // Title
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
        
        // Menu options
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        
        text = "NEW GAME";
        x = getXForCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawString(">", x-gp.tileSize, y);
        }
        
        text = "QUIT";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-gp.tileSize, y);
        }
    }
    
    public void drawPlayerLife() {
        // Display player health if needed
    }
    
    public void drawMessage() {
        if(messageOn) {
            g2.setFont(g2.getFont().deriveFont(20F));
            g2.setColor(Color.white);
            
            // Calculate message fade effect
            float alpha = 1.0f;
            if(messageCounter > 90) {
                alpha = (float)(120 - messageCounter) / 30f;
            }
            g2.setColor(new Color(1f, 1f, 1f, alpha));
            
            g2.drawString(message, gp.tileSize/2, gp.tileSize);
        }
    }
    
    public void drawObjective() {
        // Draw current objective in top corner
        g2.setFont(arial_20);
        g2.setColor(new Color(0, 0, 0, 150)); // Semi-transparent background
        
        String text = "Objective: " + currentObjective;
        int width = g2.getFontMetrics().stringWidth(text) + 20;
        
        g2.fillRoundRect(10, 40, width, 30, 10, 10);
        g2.setColor(Color.white);
        g2.drawString(text, 20, 60);
    }
    
    public void drawRadar() {
        // Radar UI in corner when active
        int size = gp.tileSize * 2;
        int x = gp.screenWidth - size - 20;
        int y = 20;
        
        // Draw radar background
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillOval(x, y, size, size);
        
        // Draw radar circle
        g2.setColor(new Color(0, 200, 0));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x, y, size, size);
        
        // Draw radar ping animation
        int pingSize = signalStrength * size / 60;
        int pingX = x + (size - pingSize)/2;
        int pingY = y + (size - pingSize)/2;
        
        float pingAlpha = 1.0f - ((float)signalStrength / 60f);
        g2.setColor(new Color(0, 1.0f, 0, pingAlpha));
        g2.drawOval(pingX, pingY, pingSize, pingSize);
        
        // Draw signal text
        g2.setFont(arial_20);
        g2.setColor(Color.white);
        g2.drawString("1.2km", x + size/2 - 25, y + size + 20);
    }
    
    public void drawInteractionPrompt() {
        g2.setFont(arial_20);
        g2.setColor(Color.white);
        
        String text = "Press E to interact";
        int x = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(text)/2;
        int y = gp.screenHeight - 50;
        
        // Background
        g2.setColor(new Color(0, 0, 0, 150));
        int width = g2.getFontMetrics().stringWidth(text) + 20;
        g2.fillRoundRect(x - 10, y - 20, width, 30, 10, 10);
        
        // Text
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }
    
    public void drawDialogueScreen() {
        // Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;
        
        drawSubWindow(x, y, width, height);
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += gp.tileSize;
        y += gp.tileSize;
        
        // Break the dialogue into multiple lines if needed
        for(String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }
    
    public void drawInventoryScreen() {
        // Frame
        final int frameX = gp.tileSize * 2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 12;
        final int frameHeight = gp.tileSize * 10;
        
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        // Slot
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;
        
        // Draw player's items
        for(int i = 0; i < gp.player.inventory.size(); i++) {
            
            // Draw slot
            g2.setColor(Color.white);
            g2.fillRoundRect(slotX, slotY, slotSize, slotSize, 10, 10);
            
            // Draw item icon
            if(gp.player.inventory.get(i) != null) {
                g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
            }
            
            // Move to next slot position
            slotX += slotSize + 10;
            
            // Next row
            if(i % 5 == 4) {
                slotX = slotXStart;
                slotY += slotSize + 10;
            }
        }
        
        // Cursor
        int cursorX = slotXStart + (slotSize + 10) * slotCol;
        int cursorY = slotYStart + (slotSize + 10) * slotRow;
        
        // Draw cursor
        g2.setColor(Color.yellow);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, slotSize, slotSize, 10, 10);
        
        // Description frame
        int descFrameX = frameX;
        int descFrameY = frameY + frameHeight + 20;
        int descFrameWidth = frameWidth;
        int descFrameHeight = gp.tileSize * 3;
        
        // Draw description
        drawSubWindow(descFrameX, descFrameY, descFrameWidth, descFrameHeight);
        
        // Description text
        int textX = descFrameX + 20;
        int textY = descFrameY + 30;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24));
        
        int itemIndex = getItemIndexFromSlot();
        if(itemIndex < gp.player.inventory.size()) {
            if(gp.player.inventory.get(itemIndex) != null) {
                g2.drawString(gp.player.inventory.get(itemIndex).description, textX, textY);
                
                // Draw "press E to use" text if the item is usable
                if(gp.player.inventory.get(itemIndex).usable) {
                    textY += 30;
                    g2.drawString("Press E to use", textX, textY);
                }
            }
        }
    }
    
    public void drawCraftingScreen() {
        // Frame
        int frameX = gp.screenWidth/2 - 250;
        int frameY = gp.screenHeight/2 - 150;
        int frameWidth = 500;
        int frameHeight = 300;
        
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        // Title
        g2.setFont(arial_40);
        g2.setColor(Color.WHITE);
        String text = "Crafting";
        int textX = getXForCenteredText(text);
        int textY = frameY + 50;
        g2.drawString(text, textX, textY);
        
        // Input slots
        int slotSize = 60;
        int slot1X = frameX + 100;
        int slot2X = frameX + 200;
        int slotY = frameY + 100;
        
        // Draw slots
        g2.setColor(Color.GRAY);
        g2.fillRect(slot1X, slotY, slotSize, slotSize);
        g2.fillRect(slot2X, slotY, slotSize, slotSize);
        
        // Draw "+" symbol
        g2.setFont(arial_40);
        g2.setColor(Color.WHITE);
        g2.drawString("+", slot1X + slotSize + 10, slotY + 40);
        
        // Output slot
        int outputX = frameX + 320;
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(outputX, slotY, slotSize, slotSize);
        
        // Draw "=" symbol
        g2.drawString("=", outputX - 40, slotY + 40);
        
        // Draw recipe button
        int buttonX = frameX + frameWidth/2 - 75;
        int buttonY = frameY + frameHeight - 80;
        int buttonWidth = 150;
        int buttonHeight = 50;
        
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(buttonX, buttonY, buttonWidth, buttonHeight);
        
        g2.setFont(arial_20);
        g2.setColor(Color.WHITE);
        text = "Craft";
        textX = buttonX + buttonWidth/2 - g2.getFontMetrics().stringWidth(text)/2;
        textY = buttonY + 30;
        g2.drawString(text, textX, textY);
        
        // Draw currently selected items
        if(gp.crafting.slot1Item != null) {
            g2.drawImage(gp.crafting.slot1Item.down1, slot1X, slotY, slotSize, slotSize, null);
        }
        if(gp.crafting.slot2Item != null) {
            g2.drawImage(gp.crafting.slot2Item.down1, slot2X, slotY, slotSize, slotSize, null);
        }
        if(gp.crafting.resultItem != null) {
            g2.drawImage(gp.crafting.resultItem.down1, outputX, slotY, slotSize, slotSize, null);
        }
    }
    
    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight/2;
        
        g2.drawString(text, x, y);
    }
    
    public void drawTransition() {
        int alpha = 0;
        if(fadeToWhite) {
            alpha = (int)(255 * ((float)transitionCounter / 60f));
            g2.setColor(new Color(255, 255, 255, alpha));
        } else {
            alpha = (int)(255 * ((float)transitionCounter / 60f));
            g2.setColor(new Color(0, 0, 0, alpha));
        }
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    }
    
    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }
    
    public int getItemIndexFromSlot() {
        return slotCol + (slotRow * 5);
    }
    
    public int getXForCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWidth/2 - length/2;
    }
}