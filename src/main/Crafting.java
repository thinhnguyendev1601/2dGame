package main;

import java.util.ArrayList;
import java.util.HashMap;

import entity.Entity;
import object.OBJ_Radar;
import object.OBJ_Battery;
import object.OBJ_Capacitor;
import object.OBJ_Sensor;

/**
 * Handles the crafting mechanics and recipes
 */
public class Crafting {
    
    GamePanel gp;
    
    // Crafting slots
    public Entity slot1Item;
    public Entity slot2Item;
    public Entity resultItem;
    
    // Recipe list
    private HashMap<String, Entity> recipes;
    
    /**
     * Constructor for Crafting system
     * @param gp GamePanel reference
     */
    public Crafting(GamePanel gp) {
        this.gp = gp;
        recipes = new HashMap<>();
        setupRecipes();
    }
    
    /**
     * Sets up all crafting recipes for the game
     */
    private void setupRecipes() {
        // Simple radar recipe: capacitor + sensor = radar
        addRecipe("OBJ_Capacitor", "OBJ_Sensor", new OBJ_Radar(gp));
        
        // If the radar also needs a battery
        addRecipe("OBJ_Radar", "OBJ_Battery", new OBJ_Radar(gp, true)); // Powered radar version
        
        // Can add more recipes here as needed for Act I
    }
    
    /**
     * Adds a recipe to the recipe list
     * @param item1Name First ingredient class name
     * @param item2Name Second ingredient class name
     * @param result Result item
     */
    private void addRecipe(String item1Name, String item2Name, Entity result) {
        // Create a unique key for the recipe
        String recipeKey = generateRecipeKey(item1Name, item2Name);
        recipes.put(recipeKey, result);
    }
    
    /**
     * Generates a unique key for a recipe
     * @param item1 First ingredient class name
     * @param item2 Second ingredient class name
     * @return Unique recipe key
     */
    private String generateRecipeKey(String item1, String item2) {
        // Sort items alphabetically to ensure consistent keys regardless of order
        if(item1.compareTo(item2) > 0) {
            String temp = item1;
            item1 = item2;
            item2 = temp;
        }
        return item1 + "+" + item2;
    }
    
    /**
     * Checks if the current items in slots can be crafted into something
     * @return true if craftable, false otherwise
     */
    public boolean checkCraftable() {
        if(slot1Item == null || slot2Item == null) {
            return false;
        }
        
        String key = generateRecipeKey(slot1Item.getClass().getSimpleName(), 
                                     slot2Item.getClass().getSimpleName());
        
        return recipes.containsKey(key);
    }
    
    /**
     * Performs the crafting operation
     * @return true if crafting was successful, false otherwise
     */
    public boolean craft() {
        if(!checkCraftable()) {
            return false;
        }
        
        String key = generateRecipeKey(slot1Item.getClass().getSimpleName(), 
                                     slot2Item.getClass().getSimpleName());
        
        resultItem = recipes.get(key);
        
        return true;
    }
    
    /**
     * Finalizes the crafting by adding the result to inventory and removing ingredients
     * @return true if successful, false otherwise
     */
    public boolean finalizeCrafting() {
        if(resultItem == null) {
            return false;
        }
        
        // Add result to inventory
        gp.player.inventory.add(resultItem);
        
        // Show crafting success message
        gp.ui.showMessage("Crafted: " + resultItem.name);
        
        // Check if this is the radar crafting - special case for Act I
        if(resultItem instanceof OBJ_Radar) {
            // Play radar discovery cutscene
            if(((OBJ_Radar)resultItem).isPowered()) {
                gp.cutsceneManager.playCutscene("radar_signal");
            }
        }
        
        // Reset crafting slots
        clearCraftingSlots();
        
        return true;
    }
    
    /**
     * Clears all crafting slots
     */
    public void clearCraftingSlots() {
        slot1Item = null;
        slot2Item = null;
        resultItem = null;
    }
    
    /**
     * Adds an item to the first available crafting slot
     * @param item Item to add
     * @return true if item was added, false if slots are full
     */
    public boolean addItemToSlot(Entity item) {
        if(slot1Item == null) {
            slot1Item = item;
            return true;
        } else if(slot2Item == null) {
            slot2Item = item;
            return true;
        }
        return false;
    }
    
    /**
     * Removes an item from the player's inventory and adds it to a crafting slot
     * @param itemIndex Index of the item in player's inventory
     * @return true if successful, false otherwise
     */
    public boolean addItemFromInventory(int itemIndex) {
        if(itemIndex < 0 || itemIndex >= gp.player.inventory.size()) {
            return false;
        }
        
        Entity item = gp.player.inventory.get(itemIndex);
        
        if(addItemToSlot(item)) {
            // Remove from inventory
            gp.player.inventory.remove(itemIndex);
            
            // Check if we can craft something
            if(slot1Item != null && slot2Item != null) {
                craft();
            }
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Returns all items from crafting slots to the player's inventory
     */
    public void returnItemsToInventory() {
        if(slot1Item != null) {
            gp.player.inventory.add(slot1Item);
        }
        
        if(slot2Item != null) {
            gp.player.inventory.add(slot2Item);
        }
        
        clearCraftingSlots();
    }
}