package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

/**
 * Main game panel that handles rendering and game loop
 */
public class GamePanel extends JPanel implements Runnable {
    
    // Screen settings
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3; 
    
    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    
    // World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    
    // FPS
    int FPS = 60;
    
    // System
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound sound = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    public CutsceneManager cutsceneManager = new CutsceneManager(this);
    public Crafting crafting = new Crafting(this);
    Thread gameThread;
    
    // Entity and Object
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[20];
    
    // Game state
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int inventoryState = 4;
    public final int craftingState = 5;
    public final int cutsceneState = 6;
    
    // Act I specific fields
    private boolean actOneCompleted = false;
    
    // Current location/room
    private String currentLocation = "Bedroom";
    
    /**
     * Constructor for GamePanel
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }
    
    /**
     * Sets up the initial game state
     */
    public void setupGame() {
        aSetter.setObject();
        
        // Start with title screen
        gameState = titleState;
        
        // PlayState start position will be the bedroom
        currentLocation = "Bedroom";
        
        // Set first objective
        ui.setObjective("Explore the bedroom");
    }
    
    /**
     * Starts the game thread
     */
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    /**
     * Game loop implementation
     */
    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;
        
        while(gameThread != null) {
            // Update character and game state
            update();
            
            // Draw the screen with updated information
            repaint();
            
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                
                if(remainingTime < 0) {
                    remainingTime = 0;
                }
                
                Thread.sleep((long) remainingTime);
                
                nextDrawTime += drawInterval;
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Updates game state
     */
    public void update() {
        if(gameState == playState) {
            // Player
            player.update();
            
            // Check for location changes
            checkLocationTransition();
        }
        
        // Update UI
        ui.update();
        
        // Update cutscene if playing
        if(gameState == cutsceneState) {
            cutsceneManager.update();
        }
    }
    
    /**
     * Checks if player has moved to a new location/room
     */
    private void checkLocationTransition() {
        // Check for location boundaries
        // This is a simplified example - you'll need to implement proper logic
        // based on your map and locations
        
        // Example: Check if player reached specific coordinates for location transition
        if(currentLocation.equals("Bedroom") && player.worldX > 1000 && player.worldY > 500) {
            changeLocation("TechnicalTunnel");
        }
        else if(currentLocation.equals("TechnicalTunnel") && player.worldX > 2000) {
            changeLocation("Stairs");
        }
        else if(currentLocation.equals("Stairs") && player.worldY < 200) {
            changeLocation("LivingRoom");
        }
    }
    
    /**
     * Changes the current location and updates objectives
     * @param newLocation New location name
     */
    public void changeLocation(String newLocation) {
        // Start transition effect
        ui.startTransition(false);
        
        // Change location
        currentLocation = newLocation;
        
        // Update objectives based on new location
        switch(newLocation) {
            case "TechnicalTunnel":
                ui.setObjective("Explore the technical tunnel");
                break;
            case "Stairs":
                ui.setObjective("Fix the broken stair step");
                break;
            case "LivingRoom":
                ui.setObjective("Find family photo and pack essentials");
                break;
        }
        
        // Reset player position for the new location
        // This is simplified - you'd need proper position logic
        player.worldX = tileSize * 10;
        player.worldY = tileSize * 10;
        
        // Play location transition sound
        sound.play(1); // Assuming sound index 1 is door/transition sound
    }
    
    /**
     * Renders the game
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        // Debug start time
        long drawStart = 0;
        if(keyH.debugMode) {
            drawStart = System.nanoTime();
        }
        
        // Title Screen
        if(gameState == titleState) {
            ui.draw(g2);
        }
        // Game screens
        else {
            // Draw tiles
            tileM.draw(g2);
            
            // Draw objects
            for(int i = 0; i < obj.length; i++) {
                if(obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }
            
            // Draw player
            player.draw(g2);
            
            // Draw UI
            ui.draw(g2);
            
            // Draw cutscene if active
            if(gameState == cutsceneState) {
                cutsceneManager.draw(g2);
            }
        }
        
        // Debug info
        if(keyH.debugMode) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10, 400);
            g2.drawString("Player X: " + player.worldX, 10, 420);
            g2.drawString("Player Y: " + player.worldY, 10, 440);
            g2.drawString("Col: " + (player.worldX + player.solidArea.x)/tileSize, 10, 460);
            g2.drawString("Row: " + (player.worldY + player.solidArea.y)/tileSize, 10, 480);
            g2.drawString("Location: " + currentLocation, 10, 500);
        }
        
        g2.dispose();
    }
    
    /**
     * Triggers the Act I ending cutscene when all objectives are complete
     */
    public void triggerActOneEnding() {
        if(!actOneCompleted) {
            actOneCompleted = true;
            gameState = cutsceneState;
            cutsceneManager.playCutscene("act1_ending");
        }
    }
}