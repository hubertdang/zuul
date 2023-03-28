import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 * 
 * @author Lynn Marshall
 * @version October 21, 2012
 * 
 * @author Hubert Dang 101229101
 * @version March 12, 2023
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private ArrayList<Item> items;
    protected static ArrayList<Room> rooms = new ArrayList<Room>();

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * 
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        items = new ArrayList<Item>();
        rooms.add(this);
    }

    /**
     * Define an exit from this room.
     * 
     * @param direction The direction of the exit
     * @param neighbour The room to which the exit leads
     */
    public void setExit(String direction, Room neighbour) 
    {
        exits.put(direction, neighbour);
    }

    /**
     * Returns a short description of the room, i.e. the one that
     * was defined in the constructor
     * 
     * @return The short description of the room
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a long description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     *     Items:
     *         a pot that weights 200.0kg.
     *     
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        String longDescription = "You are " + description + ".\n" + getExitString() + "\nItems:";
        for (Item item: items) {
            longDescription += "\n    " + item.representItem();
        }
        
        return longDescription;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * 
     * @return Details of the room's exits
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * 
     * @param direction The exit's direction
     * @return The room in the given direction
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

    /**
     * Add an item to the room.
     *
     * @param item The item object to be added to the room.
     */
    public void addItem(Item item)
    {
        items.add(item);
    }
    
    /**
     * Check if the room contains an item.
     * 
     * @param itemName The item's name.
     * 
     * @return true if the item is in the room, false otherwise.
     */
    public boolean hasItem(String itemName)
    {
        for (Item i: items) {
            if (i.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Remove an item with a specific name from the room.
     * 
     * @param itemName The name of the item.
     */
    public void removeItem(String itemName) 
    {
        Iterator<Item> iter = items.iterator();
        
        while (iter.hasNext()) {
            if (iter.next().getName().equals(itemName)) {
                iter.remove();
                return;
            }
        }
    }
    
    /**
     * Return the first instance of an item with a specific name from the room.
     * 
     * @param itemName The name of the item.
     * 
     * @return The first item that has the specified name, null if the item is 
     * not in the room.
     */
    public Item getItem(String itemName)
    {
        for (Item item: items) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }
    
    /**
     * Return rooms.
     * 
     * @return ArrayList rooms.
     */
    public static ArrayList<Room> getRooms()
    {
        return rooms;
    }
}

