
/**
 * Class Beamer represents a beamer, a device that can be charged and fired. 
 * When the beamer is charged, it memorizes the current room. When the beamer
 * is fired, it transports the player 
 *
 * @author Hubert Dang 101229101
 * @version March 12, 2023
 */
public class Beamer extends Item
{
    private final static String NAME = "beamer";
    
    private final static String DESCRIPTION = "A wonderful beamer";
    
    private final static double WEIGHT = 1.0;

    private Room room;

    /**
     * Constructor for objects of class Beamer.
     */
    public Beamer()
    {
        super(NAME, DESCRIPTION, WEIGHT);
        Room room = null;
    }
    
    /**
     * Charge the beamer and memorize the room. 
     * 
     * @param room The room to be memorized by the beamer.
     */
    public void charge(Room room)
    {
        this.room = room;
    }
    
    /**
     * Return the beamer's memorized room.
     * 
     * @return The room memorized by the beamer.
     */
    public Room fire()
    {
        Room chargedRoom = room;
        room = null;
        return chargedRoom;
    }
    
    /**
     * Return true if the beamer is charged and false if it is not.
     * 
     * @return true if the beamer is charged, false otherwise.
     */
    public boolean isCharged()
    {
        if (room == null) {
            return false;
        }
        return true;
    }
}
