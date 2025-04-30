package main;

import javax.swing.JFrame;

/**
 * Main class to launch the game
 */
public class Main {
    
    /**
     * Main method, entry point for the game
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("RẠN - Act I: Khởi đầu");
        
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        
        window.pack();
        
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        // Setup the game after window is visible
        gamePanel.setupGame();
        gamePanel.startGameThread();
    }
}