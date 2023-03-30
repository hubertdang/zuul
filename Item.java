/**
 * Class Item - an item in an adventure game.
 *
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *
 * An "Item" represents one item in a room of the game. Each item
 * has a name, description, and a weight in kg.
 *
 * @author Hubert Dang 
 * @version March 12, 2023
 */

public class Item
{
    private String description;
    
    private double weight;
    
    private String name;
    

    /**
     * Create an item object with a description and a weight.
     *
     * @param name The name of the item.
     * @param description The description of the item.
     * @param weight The weight of the item.
     */
    public Item(String name, String description, double weight)
    {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    /**
     * Represent the item as a string, showing the description and weight.
     *
     * @return String representation of the item.
     */
    public String representItem()
    {
        return name + ": " + description + " that weighs " + Double.toString(weight) + "kg.";
    }
    
    /**
     * Return the name of the item.
     * 
     * @return String value of the item's name.
     */
    public String getName()
    {
        return name;
    }
}
