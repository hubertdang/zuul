import java.util.Currency;
import java.util.Stack;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 * 
 * @author Lynn Marshall
 * @version October 21, 2012
 * 
 * @author Hubert Dang 
 * @version March 12, 2023
 */

public class Game 
{
    private Parser parser;
    
    private Room currentRoom;
    
    private Room previousRoom;
    
    private Stack<Room> roomStack;
    
    private Item playerItem;
    
    private int itemsPickedUp;
        
    /**
     * Create the Game and initialise its internal map.
     */
    public Game() 
    {
        // initializes currentRoom, previousRoom, and roomStack
        roomStack = new Stack<Room>();
        createRooms();
        parser = new Parser();
        playerItem = null;
        itemsPickedUp = 0;
    }

    /**
     * createRooms creates all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office;
        TransporterRoom transporter;
      
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        transporter = new TransporterRoom();

        // create items
        Item tree = new Item("tree", "a fir tree", 500.5);
        Item rock = new Item("rock", "a hard rock", 30.2);
        Item projector = new Item("projector", "a projector", 7.4);
        Item cider = new Item("cider", "a bottle of cider", 1.8);
        Item beer = new Item("beer", "a bottle of beer", 1.9);
        Item keyboard = new Item("keyboard", "a keyboard", 2.0);
        Item mouse = new Item("mouse", "a mouse", 1.1);
        Item chair = new Item("chair", "a chair", 23.4);
        Item pen = new Item("pen", "a blue ball-point pen", 0.5);
        Item cookie = new Item("cookie", "a hummy chocolate chip cookie", 0.2);
        Item beamer1 = new Beamer();
        Item beamer2 = new Beamer();

        // add items to rooms
        outside.addItem(tree);
        outside.addItem(rock);
        outside.addItem(cookie);
        theatre.addItem(projector);
        theatre.addItem(cookie);
        theatre.addItem(beamer1);
        pub.addItem(cider);
        pub.addItem(beer);
        pub.addItem(beamer2);
        lab.addItem(keyboard);
        lab.addItem(mouse);
        lab.addItem(chair);
        office.addItem(pen);
        office.addItem(cookie);
        transporter.addItem(cookie);
        
        // initialise room exits
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);
        pub.setExit("south", transporter);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);
        
        transporter.setExit("anywhere", null);

        currentRoom = outside;  // start game outside
        roomStack.push(currentRoom);
        previousRoom = null;
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        printCarrying();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackBack")) {
            stackBack(command);
        }
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        } 
        else if (commandWord.equals("charge")) {
            charge(command);
        } 
        else if (commandWord.equals("fire")) {
            fire(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom;
            currentRoom = nextRoom;
            roomStack.push(currentRoom);
            System.out.println(currentRoom.getLongDescription());
            printCarrying();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Print which room you are in, the room's exits, and its items.
     *
     * @param command The command to be processed.
     */
    private void look(Command command)
    {
        if (command.hasSecondWord()) {
            System.out.println("Look what?");
        } else {
            System.out.println(currentRoom.getLongDescription());
            printCarrying();
        }
    }
    
    /**
     * Print that the player is carrying a specific item, if the player is not
     * carrying anything, print that they are not carrying anything.
     */
    private void printCarrying()
    {
    if (playerItem == null) {
            System.out.println("You are not carrying anything.");              
        } else {
            System.out.println("You are carrying:");
            System.out.println("    " + playerItem.representItem());
        }
    }

    /**
     * Print a message to say that you have eaten and are no longer hungry.
     *
     * @param command The command to be processed.
     */

    private void eat(Command command)
    {
        if (command.hasSecondWord()) {
            System.out.println("Eat what?");
        } else if (playerItem == null || !(playerItem.getName().equals("cookie"))) {
            System.out.println("You don't have any food.");
        } else {
            itemsPickedUp = 0;
            playerItem = null;
            System.out.println("You have eaten and are no longer hungry.");
        }
    }

    /**
     * Try to go back to the previous room. If you are at the beginning of
     * the game, print an error message, otherwise, go back to the previous
     * room.
     *
     * @param command The command to be processed.
     */
    private void back(Command command)
    {
        if (command.hasSecondWord()) {
            System.out.println("Back what?");
        } else if (previousRoom == null) {
            System.out.println("No room to go back to.");
        } else {
            Room nextPreviousRoom = currentRoom;
            currentRoom = previousRoom;
            roomStack.push(currentRoom);
            previousRoom = nextPreviousRoom;
            System.out.println(currentRoom.getLongDescription());
            printCarrying();
        }
    }

    /**
     * Try to go back to the previous room ordered in a stack. If you are at
     * the beginning of the game, print an error message. Otherwise, go back
     * to the previous room in the stack.
     *
     * @param command The command to be processed.
     */
    private void stackBack(Command command)
    {
        if (command.hasSecondWord()) {
            System.out.println("stackBack what?");
        } else if (roomStack.size() == 1) {
            System.out.println("No room to go back to.");
        } else {
            previousRoom = roomStack.pop();
            currentRoom = roomStack.peek();
            System.out.println(currentRoom.getLongDescription());
            printCarrying();
        }
    }
    
    /**
     * Take an item from the current room. If the player is already holding an 
     * item, or the item is not in the roomn, print an error message. 
     * 
     * @param command The command to be processed.
     */
    private void take(Command command)
    {
        String itemName = command.getSecondWord();
        if (itemName == null) {
            System.out.println("take what?");
        } else if (playerItem != null) {
            System.out.println("You are already holding something.");
        } else if (!(itemName.equals("cookie")) && itemsPickedUp >= 5) {
            System.out.println("You must take and eat a cookie before taking anything else.");
        } else if (currentRoom.hasItem(itemName)) {
            playerItem = currentRoom.getItem(itemName);
            itemsPickedUp++;
            currentRoom.removeItem(itemName);
            System.out.println("You picked up " + itemName + ".");
        } else {
            System.out.println("That item is not in the room.");
        }
    }
    
    /**
     * Drop an item. If the player is not holding an item, print an error message.
     * 
     * @param command The command to be processed.
     */
    private void drop(Command command)
    {
        if (command.hasSecondWord()) {
            System.out.println("Drop what?");
        } else if (playerItem == null) {
            System.out.println("You have nothing to drop");
        } else {
            currentRoom.addItem(playerItem);
            System.out.println("You have dropped " + playerItem.getName());
            playerItem = null;
        } 
    }
    
    /**
     * Charge a beamer if the player is holding one and it is uncharged.
     * 
     * @param command The command to be processed.
     */
    private void charge(Command command)
    {
        if (command.hasSecondWord()) {
            System.out.println("Charge what?");
        } else if (playerItem.getName().equals("beamer")) {
            Beamer beamer = (Beamer) playerItem;
            if (beamer.isCharged()) {
                System.out.println("The beamer is already charged.");
            } else {
                beamer.charge(currentRoom);
                System.out.println("Beamer successfully charged.");
            }
        } else {
            System.out.println("You are not holding a beamer.");
        }
    }
    
    /**
     * Fire a beamer if the player is holding one and it is charged. Firing a 
     * beamer teleports the player to the room in which it was charged.
     * 
     * @param command The command to be processed.
     */
    private void fire(Command command)
    {
        if (command.hasSecondWord()) {
            System.out.println("Fire what?");
        } else if (playerItem.getName().equals("beamer")) {
            Beamer beamer = (Beamer) playerItem;
            if (beamer.isCharged()) {
                System.out.println("Beamer successfully fired.");
                currentRoom = beamer.fire();
                System.out.println(currentRoom.getLongDescription());
                printCarrying();
            } else {
                System.out.println("The beamer has not been charged.");
            }
        } else {
            System.out.println("You are not holding a beamer.");
        }
    }
    
}




