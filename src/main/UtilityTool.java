package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Utility class for common operations like image scaling
 */
public class UtilityTool {
    
    /**
     * Scales an image to the specified width and height
     * @param original Original image to scale
     * @param width Target width
     * @param height Target height
     * @return Scaled BufferedImage
     */
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();
        
        return scaledImage;
    }
}
