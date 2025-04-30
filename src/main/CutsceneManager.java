package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Manages all cutscenes in the game, responsible for updating and rendering cutscene sequences
 */
public class CutsceneManager {
    GamePanel gp;
    Graphics2D g2;
    public ArrayList<CutsceneEvent> events;
    public int currentEventIndex = 0;
    public boolean isPlaying = false;
    public int scenePhase = 0;
    
    // Timer for scene transitions
    public int sceneCounter = 0;
    
    public CutsceneManager(GamePanel gp) {
        this.gp = gp;
        events = new ArrayList<>();
        setupEvents();
    }
    
    /**
     * Set up all cutscene events for Act I
     */
    public void setupEvents() {
        // Radar discovery cutscene
        events.add(new CutsceneEvent("radar_signal", 120) {
            @Override
            public void update() {
                switch(scenePhase) {
                    case 0: // Start cutscene
                        gp.gameState = gp.cutsceneState;
                        scenePhase++;
                        break;
                    case 1: // Show radar ping animation
                        sceneCounter++;
                        if(sceneCounter > duration/2) {
                            scenePhase++;
                            sceneCounter = 0;
                        }
                        break;
                    case 2: // Show text message
                        sceneCounter++;
                        if(sceneCounter > duration/2) {
                            scenePhase++;
                            sceneCounter = 0;
                        }
                        break;
                    case 3: // Return to game
                        gp.gameState = gp.playState;
                        gp.ui.currentDialogue = "I need to find the source of this signal.";
                        gp.gameState = gp.dialogueState;
                        isPlaying = false;
                        scenePhase = 0;
                        
                        // Enable radar UI after this cutscene
                        gp.ui.radarActive = true;
                        gp.ui.setObjective("Find the source of the signal");
                        break;
                }
            }
            
            @Override
            public void draw(Graphics2D g2) {
                g2.setColor(new Color(0, 0, 0, sceneCounter*4 > 200 ? 200 : sceneCounter*4));
                g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
                
                // Phase 1: Show radar ping animation
                if(scenePhase == 1) {
                    int size = 10 + sceneCounter*2;
                    int x = gp.screenWidth/2 - size/2;
                    int y = gp.screenHeight/2 - size/2;
                    
                    // Draw multiple expanding circles
                    for(int i = 0; i < 3; i++) {
                        int pingSize = size - (i * 20);
                        if(pingSize > 0) {
                            int pingX = gp.screenWidth/2 - pingSize/2;
                            int pingY = gp.screenHeight/2 - pingSize/2;
                            
                            float alpha = 1.0f - ((float)pingSize / (float)(size + 40));
                            if(alpha < 0) alpha = 0;
                            g2.setColor(new Color(0, 1.0f, 0, alpha));
                            g2.drawOval(pingX, pingY, pingSize, pingSize);
                        }
                    }
                }
                
                // Phase 2: Show text message
                if(scenePhase == 2) {
                    int alpha = (sceneCounter*10 > 255 ? 255 : sceneCounter*10);
                    g2.setColor(new Color(255, 255, 255, alpha));
                    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));
                    String text = "Signal detected: Abnormal temperature 1.2km away";
                    int x = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(text)/2;
                    int y = gp.screenHeight/2;
                    g2.drawString(text, x, y);
                }
            }
        });
        
        // Act I ending cutscene
        events.add(new CutsceneEvent("act1_ending", 180) {
            @Override
            public void update() {
                switch(scenePhase) {
                    case 0: // Start cutscene with door opening
                        gp.gameState = gp.cutsceneState;
                        scenePhase++;
                        break;
                    case 1: // Door opening animation
                        sceneCounter++;
                        if(sceneCounter > 60) {
                            scenePhase++;
                            sceneCounter = 0;
                        }
                        break;
                    case 2: // White flash
                        sceneCounter++;
                        if(sceneCounter > 60) {
                            scenePhase++;
                            sceneCounter = 0;
                        }
                        break;
                    case 3: // Show character with dog and backpack
                        sceneCounter++;
                        if(sceneCounter > 60) {
                            scenePhase++;
                            sceneCounter = 0;
                        }
                        break;
                    case 4: // Show title
                        sceneCounter++;
                        if(sceneCounter > 60) {
                            // End of Act I, transition to Act II or title screen
                            gp.gameState = gp.titleState;
                            isPlaying = false;
                            scenePhase = 0;
                        }
                        break;
                }
            }
            
            @Override
            public void draw(Graphics2D g2) {
                if(scenePhase == 1) {
                    // Door opening animation
                    // Draw the door slowly opening
                    int doorWidth = 200;
                    int doorHeight = 300;
                    int doorX = gp.screenWidth/2 - doorWidth/2;
                    int doorY = gp.screenHeight/2 - doorHeight/2;
                    
                    // Calculate opening amount
                    int openAmount = (int)(sceneCounter * 2.5f);
                    if(openAmount > doorWidth) openAmount = doorWidth;
                    
                    // Draw door frame
                    g2.setColor(Color.DARK_GRAY);
                    g2.fillRect(doorX - 20, doorY - 20, doorWidth + 40, doorHeight + 20);
                    
                    // Draw opening (black space)
                    g2.setColor(Color.BLACK);
                    g2.fillRect(doorX, doorY, openAmount, doorHeight);
                    
                    // Draw door (moving right)
                    g2.setColor(Color.GRAY);
                    g2.fillRect(doorX + openAmount, doorY, doorWidth - openAmount, doorHeight);
                }
                else if(scenePhase == 2) {
                    // White flash
                    float alpha = sceneCounter / 60f;
                    if(alpha > 1) alpha = 1;
                    
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                    g2.setColor(Color.WHITE);
                    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
                else if(scenePhase == 3) {
                    // White background
                    g2.setColor(Color.WHITE);
                    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
                    
                    // Draw character silhouette with dog and backpack
                    g2.setColor(Color.BLACK);
                    
                    // Draw character (simple silhouette)
                    int characterX = gp.screenWidth/2;
                    int characterY = gp.screenHeight/2 + 50;
                    int characterWidth = 40;
                    int characterHeight = 80;
                    
                    g2.fillRect(characterX - characterWidth/2, characterY - characterHeight, characterWidth, characterHeight);
                    
                    // Draw head
                    int headSize = 30;
                    g2.fillOval(characterX - headSize/2, characterY - characterHeight - headSize, headSize, headSize);
                    
                    // Draw dog silhouette
                    int dogX = characterX - 50;
                    int dogY = characterY - 20;
                    int dogWidth = 40;
                    int dogHeight = 20;
                    
                    g2.fillRect(dogX - dogWidth/2, dogY - dogHeight/2, dogWidth, dogHeight);
                    
                    // Draw dog head
                    int dogHeadSize = 15;
                    g2.fillOval(dogX - dogWidth/2 - dogHeadSize/2, dogY - dogHeadSize/2, dogHeadSize, dogHeadSize);
                    
                    // Draw backpack
                    int backpackWidth = 30;
                    int backpackHeight = 40;
                    g2.fillRect(characterX - backpackWidth/2, characterY - characterHeight + 10, backpackWidth, backpackHeight);
                    
                    // Fade in effect
                    float alpha = 1 - (sceneCounter / 60f);
                    if(alpha < 0) alpha = 0;
                    
                    g2.setColor(new Color(1f, 1f, 1f, alpha));
                    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
                }
                else if(scenePhase == 4) {
                    // White background
                    g2.setColor(Color.WHITE);
                    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
                    
                    // Draw character silhouette with dog and backpack (same as phase 3)
                    g2.setColor(Color.BLACK);
                    
                    // Draw character (simple silhouette)
                    int characterX = gp.screenWidth/2;
                    int characterY = gp.screenHeight/2 + 50;
                    int characterWidth = 40;
                    int characterHeight = 80;
                    
                    g2.fillRect(characterX - characterWidth/2, characterY - characterHeight, characterWidth, characterHeight);
                    
                    // Draw head
                    int headSize = 30;
                    g2.fillOval(characterX - headSize/2, characterY - characterHeight - headSize, headSize, headSize);
                    
                    // Draw dog silhouette
                    int dogX = characterX - 50;
                    int dogY = characterY - 20;
                    int dogWidth = 40;
                    int dogHeight = 20;
                    
                    g2.fillRect(dogX - dogWidth/2, dogY - dogHeight/2, dogWidth, dogHeight);
                    
                    // Draw dog head
                    int dogHeadSize = 15;
                    g2.fillOval(dogX - dogWidth/2 - dogHeadSize/2, dogY - dogHeadSize/2, dogHeadSize, dogHeadSize);
                    
                    // Draw backpack
                    int backpackWidth = 30;
                    int backpackHeight = 40;
                    g2.fillRect(characterX - backpackWidth/2, characterY - characterHeight + 10, backpackWidth, backpackHeight);
                    
                    // Draw title
                    float alpha = sceneCounter / 30f;
                    if(alpha > 1) alpha = 1;
                    
                    g2.setColor(new Color(0, 0, 0, alpha));
                    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
                    String text = "RẠN"; // Game title from document
                    int x = gp.screenWidth/2 - g2.getFontMetrics().stringWidth(text)/2;
                    int y = gp.screenHeight/4;
                    
                    g2.drawString(text, x, y);
                }
            }
        });
    }
    
    public void update() {
        if(isPlaying) {
            events.get(currentEventIndex).update();
        }
    }
    
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        if(isPlaying) {
            events.get(currentEventIndex).draw(g2);
        }
    }
    
    /**
     * Plays a cutscene by its name
     * @param eventName Name of the cutscene to play
     */
    public void playCutscene(String eventName) {
        for(int i = 0; i < events.size(); i++) {
            if(events.get(i).name.equals(eventName)) {
                currentEventIndex = i;
                isPlaying = true;
                scenePhase = 0;
                sceneCounter = 0;
                break;
            }
        }
    }
}

/**
 * Abstract class for cutscene events
 */
abstract class CutsceneEvent {
    public String name;
    public int duration;
    public int scenePhase;
    
    public CutsceneEvent(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }
    
    public abstract void update();
    public abstract void draw(Graphics2D g2);
}