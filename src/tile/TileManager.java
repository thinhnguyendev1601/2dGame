package tile;

import entity.Entity;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][][];  //[StoreMapNumber][col][row]
    boolean drawPath = true;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        // Initialize tile array to handle all possible tile numbers (0-92)
        tile = new Tile[93];

        // Read and setup tiles from tiledata.txt
        readAndSetupTiles();

        // Get map dimensions
        getMapDimensions();

        // Initialize map array
        mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

        // Load maps
        loadMap("/maps/bedroom.txt", 0);
        loadMap("/maps/livingroom.txt", 1);
        loadMap("/maps/stairs.txt", 2);
        loadMap("/maps/hall.txt", 3);
    }

    private void getMapDimensions() {
        try {
            InputStream is = getClass().getResourceAsStream("/maps/bedroom.txt");
            if (is == null) {
                System.err.println("bedroom.txt not found!");
                gp.maxWorldCol = 50;
                gp.maxWorldRow = 50;
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line2 = br.readLine();
            String maxTile[] = line2.split(" ");

            gp.maxWorldCol = maxTile.length;

            // Count rows
            int rowCount = 1;
            while (br.readLine() != null) {
                rowCount++;
            }
            gp.maxWorldRow = rowCount;

            br.close();
            System.out.println("Map dimensions: " + gp.maxWorldCol + "x" + gp.maxWorldRow);

        } catch (IOException e) {
            System.err.println("Error reading map dimensions: " + e.getMessage());
            gp.maxWorldCol = 50;
            gp.maxWorldRow = 50;
        }
    }

    private void readAndSetupTiles() {
        try {
            InputStream is = getClass().getResourceAsStream("/maps/tiledata.txt");
            if (is == null) {
                System.err.println("tiledata.txt not found! Using default tiles.");
                setupDefaultTiles();
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Map of which tile indices are defined in your tiledata.txt
            int[] tileIndices = {0, 1, 2, 4, 6, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 91, 92};
            int currentIndex = 0;

            String line;
            while ((line = br.readLine()) != null && currentIndex < tileIndices.length) {
                String fileName = line.trim();
                String collisionLine = br.readLine();

                if (collisionLine != null) {
                    boolean collision = collisionLine.trim().equals("true");
                    int tileNum = tileIndices[currentIndex];

                    // Setup the tile at the correct index
                    setup(tileNum, fileName, collision);

                    currentIndex++;
                }
            }

            br.close();

            // Fill any missing tiles with defaults
            for (int i = 0; i < tile.length; i++) {
                if (tile[i] == null) {
                    setupDefaultTile(i, false);
                }
            }

            setup(0, "000.png", false);
            setup(0, "004.png", false);
            setup(0, "006.png", true);

        } catch (Exception e) {
            System.err.println("Error reading tiledata.txt: " + e.getMessage());
            e.printStackTrace();
            setupDefaultTiles();
        }
    }

    private void setupDefaultTiles() {
        // Create default tiles for all indices
        for (int i = 0; i < tile.length; i++) {
            setupDefaultTile(i, false);
        }
    }

    private void setupDefaultTile(int index, boolean collision) {
        if (index >= 0 && index < tile.length) {
            tile[index] = new Tile();

            // Create a simple colored tile as placeholder
            BufferedImage placeholder = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = placeholder.createGraphics();

            // Different colors for different tile types
            if (index == 0) {
                g2.setColor(new Color(50, 50, 50)); // Dark gray
            } else if (index < 10) {
                g2.setColor(new Color(100, 100, 100)); // Gray
            } else {
                g2.setColor(new Color(150, 150, 150)); // Light gray
            }

            g2.fillRect(0, 0, gp.tileSize, gp.tileSize);

            // Add tile number for debugging
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString(String.valueOf(index), 5, 20);

            g2.dispose();

            tile[index].image = placeholder;
            tile[index].collision = collision;
        }
    }

    public void setup(int index, String imageName, boolean collision) {
        if (index < 0 || index >= tile.length) {
            System.err.println("Invalid tile index: " + index);
            return;
        }

        UtilityTool uTool = new UtilityTool();
        try {
            tile[index] = new Tile();

            // Try to load the image from /tiles1/ directory
            InputStream is = getClass().getResourceAsStream("/tiles/" + imageName);

            if (is == null) {
                // Try without the directory
                is = getClass().getResourceAsStream("/tiles/" + imageName);
            }

            if (is != null) {
                tile[index].image = ImageIO.read(is);
                tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
                tile[index].collision = collision;
                System.out.println("Successfully loaded tile " + index + ": " + imageName);
            } else {
                System.err.println("Could not find image: " + imageName + " for tile " + index);
                setupDefaultTile(index, collision);
            }

        } catch (IOException e) {
            System.err.println("Error loading tile " + index + " (" + imageName + "): " + e.getMessage());
            setupDefaultTile(index, collision);
        }
    }
    public void loadMap(String filePath, int map) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            if (is == null) {
                System.err.println("Map file not found: " + filePath);
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // Initialize the entire map area with a default tile (like tile 1)
            for(int c = 0; c < gp.maxWorldCol; c++) {
                for(int r = 0; r < gp.maxWorldRow; r++) {
                    mapTileNum[map][c][r] = 1; // Default floor tile
                }
            }

            int col = 0;
            int row = 0;

            String line;
            while ((line = br.readLine()) != null && row < gp.maxWorldRow) {
                String numbers[] = line.split(" ");
                col = 0;

                for(int i = 0; i < numbers.length && col < gp.maxWorldCol; i++) {
                    try {
                        int num = Integer.parseInt(numbers[i].trim());
                        mapTileNum[map][col][row] = num;
                        col++;
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number in " + filePath + " at " + col + "," + row);
                        mapTileNum[map][col][row] = 1; // Default to floor
                        col++;
                    }
                }
                row++;
            }
            br.close();

            System.out.println("Loaded map " + filePath + ": " + col + "x" + row + " (expected: " + gp.maxWorldCol + "x" + gp.maxWorldRow + ")");

        } catch (Exception e) {
            System.err.println("Error loading map " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        // Set rendering hints for better quality and no gaps
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        // Clear background with the dominant map color instead of black
        // This prevents black lines from showing through gaps
        g2.setColor(new Color(139, 69, 19)); // Brown color matching your floor
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;

            // Use Math.round to ensure integer pixel positions
            int screenX = Math.round(worldX - gp.player.worldX + gp.player.screenX);
            int screenY = Math.round(worldY - gp.player.worldY + gp.player.screenY);

            // Check if tile is visible on screen
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

                // Validate tile exists
                if(tile[tileNum] != null && tile[tileNum].image != null) {
                    // Draw tile with 1 pixel overlap to prevent seams
                    g2.drawImage(tile[tileNum].image, screenX, screenY,
                            gp.tileSize + 1, gp.tileSize + 1, null);
                }
            }

            worldCol++;

            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }

        // Draw black borders only at actual map edges
        drawMapBorders(g2);
    }

    private void drawMapBorders(Graphics2D g2) {
        g2.setColor(Color.BLACK);

        // Calculate actual map boundaries
        int mapPixelWidth = gp.maxWorldCol * gp.tileSize;
        int mapPixelHeight = gp.maxWorldRow * gp.tileSize;

        // Left edge
        if (gp.player.worldX - gp.player.screenX < 0) {
            int borderWidth = -(gp.player.worldX - gp.player.screenX);
            g2.fillRect(0, 0, borderWidth, gp.screenHeight);
        }

        // Right edge
        if (gp.player.worldX + gp.player.screenX > mapPixelWidth) {
            int borderStart = mapPixelWidth - gp.player.worldX + gp.player.screenX;
            g2.fillRect(borderStart, 0, gp.screenWidth - borderStart, gp.screenHeight);
        }

        // Top edge
        if (gp.player.worldY - gp.player.screenY < 0) {
            int borderHeight = -(gp.player.worldY - gp.player.screenY);
            g2.fillRect(0, 0, gp.screenWidth, borderHeight);
        }

        // Bottom edge
        if (gp.player.worldY + gp.player.screenY > mapPixelHeight) {
            int borderStart = mapPixelHeight - gp.player.worldY + gp.player.screenY;
            g2.fillRect(0, borderStart, gp.screenWidth, gp.screenHeight - borderStart);
        }
    }
}