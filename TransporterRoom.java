import java.util.*;
/**
 * Class TransporterRoom models a room that can randomly transport the player 
 * into another room
 *
 * @author Hubert Dang
 * @version March 12, 2023
 */
public class TransporterRoom extends Room
{
    private final static String DESCRIPTION = "in a transporter room";
    
    private static Random generator = new Random();

    /**
     * Create a transporter room.
     */
    public TransporterRoom()
    {
        super(DESCRIPTION);
    }

    /**
     * Get an exit as a random room.
     * 
     * @return an exit that is random.
     */
    public Room getExit(String direction)
    {
        return findRandomRoom();
    }
    
    /**
     * Return a random room.
     * 
     * @return A room chosen at random.
     */
    private Room findRandomRoom()
    {
        return rooms.get(generator.nextInt(rooms.size()));
    }
}
