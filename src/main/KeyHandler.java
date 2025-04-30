package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles keyboard input for the game
 */
public class KeyHandler implements KeyListener {

    GamePanel gp;
    
    // Key states
    public boolean upPressed, downPressed, leftPressed, rightPressed, 
                  interactPressed, inventoryPressed, 
                  pausePressed, enterPressed, backspacePressed;
    
    // Debug mode toggle
    boolean debugMode = false;
    
    /**
     * Constructor for KeyHandler
     * @param gp GamePanel reference
     */
    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        // Different handling based on game state
        if(gp.gameState == gp.titleState) {
            titleStateKeyInput(code);
        }
        else if(gp.gameState == gp.playState) {
            playStateKeyInput(code);
        }
        else if(gp.gameState == gp.pauseState) {
            pauseStateKeyInput(code);
        }
        else if(gp.gameState == gp.dialogueState) {
            dialogueStateKeyInput(code);
        }
        else if(gp.gameState == gp.inventoryState) {
            inventoryStateKeyInput(code);
        }
        else if(gp.gameState == gp.craftingState) {
            craftingStateKeyInput(code);
        }
        else if(gp.gameState == gp.cutsceneState) {
            cutsceneStateKeyInput(code);
        }
    }
    
    /**
     * Handles key inputs for the title state
     * @param code KeyEvent code
     */
    private void titleStateKeyInput(int code) {
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.setupGame();
            }
            if(gp.ui.commandNum == 1) {
                System.exit(0);
            }
        }
    }
    
    /**
     * Handles key inputs for the play state
     * @param code KeyEvent code
     */
    private void playStateKeyInput(int code) {
        // Movement keys
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        
        // Interaction keys
        if(code == KeyEvent.VK_E) {
            interactPressed = true;
        }
        if(code == KeyEvent.VK_I) {
            inventoryPressed = true;
            gp.gameState = gp.inventoryState;
        }
        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.craftingState;
        }
        if(code == KeyEvent.VK_P) {
            pausePressed = true;
            gp.gameState = gp.pauseState;
        }
        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.pauseState;
        }
        
        // Debug mode
        if(code == KeyEvent.VK_F3) {
            debugMode = !debugMode;
        }
    }
    
    /**
     * Handles key inputs for the pause state
     * @param code KeyEvent code
     */
    private void pauseStateKeyInput(int code) {
        if(code == KeyEvent.VK_P || code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
    }
    
    /**
     * Handles key inputs for the dialogue state
     * @param code KeyEvent code
     */
    private void dialogueStateKeyInput(int code) {
        if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE || code == KeyEvent.VK_E) {
            gp.gameState = gp.playState;
        }
    }
    
    /**
     * Handles key inputs for the inventory state
     * @param code KeyEvent code
     */
    private void inventoryStateKeyInput(int code) {
        if(code == KeyEvent.VK_I || code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if(gp.ui.slotRow > 0) {
                gp.ui.slotRow--;
            }
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            if(gp.ui.slotRow < 3) { // Assuming 4 rows of inventory
                gp.ui.slotRow++;
            }
        }
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if(gp.ui.slotCol > 0) {
                gp.ui.slotCol--;
            }
        }
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if(gp.ui.slotCol < 4) { // Assuming 5 columns of inventory
                gp.ui.slotCol++;
            }
        }
        if(code == KeyEvent.VK_E) {
            // Use item if usable
            int itemIndex = gp.ui.getItemIndexFromSlot();
            if(itemIndex < gp.player.inventory.size()) {
                if(gp.player.inventory.get(itemIndex) != null) {
                    if(gp.player.inventory.get(itemIndex).usable) {
                        gp.player.useItem(itemIndex);
                    }
                }
            }
        }
        if(code == KeyEvent.VK_C) {
            // Transfer to crafting system
            gp.gameState = gp.craftingState;
        }
    }
    
    /**
     * Handles key inputs for the crafting state
     * @param code KeyEvent code
     */
    private void craftingStateKeyInput(int code) {
        if(code == KeyEvent.VK_C || code == KeyEvent.VK_ESCAPE) {
            gp.crafting.returnItemsToInventory();
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_I) {
            // Switch to inventory to select items
            gp.gameState = gp.inventoryState;
        }
        if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_E) {
            // Finalize crafting if result exists
            if(gp.crafting.resultItem != null) {
                gp.crafting.finalizeCrafting();
            }
        }
    }
    
    /**
     * Handles key inputs for the cutscene state
     * @param code KeyEvent code
     */
    private void cutsceneStateKeyInput(int code) {
        // For some cutscenes, allow skipping with ENTER or SPACE
        if((code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) && gp.cutsceneManager.isPlaying) {
            // Only allow skipping for certain cutscenes or phases
            if(gp.cutsceneManager.events.get(gp.cutsceneManager.currentEventIndex).name.equals("radar_signal") && 
               gp.cutsceneManager.scenePhase > 1) {
                gp.cutsceneManager.scenePhase = 3; // Skip to end phase
                gp.cutsceneManager.sceneCounter = 0;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        if(code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if(code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if(code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if(code == KeyEvent.VK_E) {
            interactPressed = false;
        }
        if(code == KeyEvent.VK_P) {
            pausePressed = false;
        }
        if(code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
        if(code == KeyEvent.VK_BACK_SPACE) {
            backspacePressed = false;
        }
    }
}