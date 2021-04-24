import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
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
 * @author  Michael Kölling and David J. Barnes and Yusuf Yacoobali
 * @version v2
 */

public class Game 
{   
    //initialise all variables and collections       
    private Parser parser;
    
    private Room outside, lab1, savanna, lab2, transporter, gate;   //decided to initialise rooms and others here to call them directly further on in the class
    private Player player;    
    private Scientist scientist1;
    private Ostrich ostrich1, ostrich2, ostrich3;
    
    private ArrayList <Room> rooms;
    
    private int ostrichNum;         //to keep track of how many ostrich are present, if it falls below 0 then the player is rewarded
    private boolean finished;       
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {   
        //decalre all variables and collections
        parser = new Parser();
        player = new Player();
        scientist1 = new Scientist();
        ostrich1 = new Ostrich();
        ostrich2 = new Ostrich();
        ostrich3 = new Ostrich();
        rooms = new ArrayList<>();
        createRooms();
        fillRooms();
        ostrichNum = 3;
        finished = false;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        // create the rooms
        outside = new Room("outside the lab you were tied in");
        lab1 = new Room("in a lab");
        savanna = new Room("in a savanna");
        lab2 = new Room("in another lab");
        transporter = new Room("in the magic transporter room");
        gate = new Room("next to the gate of the savanna");
        
        // initialise room exits, magic room has no exit
        outside.setExit("right", lab2);
        outside.setExit("left", transporter);
        outside.setExit("forward", savanna);
        outside.setExit("backwards", lab1);
        lab1.setExit("forward", outside);
        lab2.setExit("left", outside);
        savanna.setExit("backwards", outside);
        savanna.setExit("right", gate);        
        gate.setExit("left", savanna);
        
        //set items in rooms
        outside.setItems("knife", "used to brutally murder birds", 6);
        outside.setItems("medicine", "used to restore health", 3);
        outside.setItems("gun", "used to destroy everything in its path", 100);
        lab2.setItems("seeds", "used to feed birds", 2);
        
        //set scientist in room
        lab2.setScientist(scientist1);
                 
        //set ostrich in starting room so player gets a taste of the enenmy
        lab1.setOstrich(ostrich1);
        lab1.setOstrich(ostrich2);
        lab1.setOstrich(ostrich3);
        
        //set players starting room
        player.setCurrentRoom(lab1);
    }
    
    /**
     * Adds all rooms to arraylist 
     * Used to get a random room to move characters in
     */
    private void fillRooms(){    
        rooms.add(outside);
        rooms.add(lab1);
        rooms.add(savanna);
        rooms.add(lab2);
        rooms.add(transporter);
        rooms.add(gate);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command); 
            takeDamage();                           //every time the player gives input there is a chance he will be attacked
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("You are playing Birds' Beater\nYou finally regain consciousness and find yourself tied up in what seems to be a lab");
        System.out.println("With your mighty strength you break the restraints and have a look around.\n\nTheres only one thought on your mind: ESCAPE");
        System.out.println("The enemies here are ostrich and they will attack\n\nHINT: To escape, try to find and inspect the scientist\nType 'help' if you need help.");
        System.out.println("To move, try something like 'go forward'\n ");
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;
        
        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }
        CommandWord commandWord = command.getCommandWord();
        
        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
            
            case PICK:
                pickItem(command);
                break;
                
            case DROP:
                dropItem(command);
                break;
                
            case USE:
                useItem(command);
                break;
            
            case INSPECT:
                makeInspection(command);
                break;
                
            case BACK:
                goBack(command);
                break;
                
            case KILL:
                killOstrich(command);
                break;
                
            case GIVE:
                giveItem(command);
                break;
         }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:
    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            player.setCurrentRoom(nextRoom);
            moveOstrich();
            if (player.getCurrentRoom() != transporter){
                System.out.println(player.getCurrentRoom().getLongDescription());
            } else {
                transportPlayer();
            }            
        }
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
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
     * This method lets the player pick items up from a room
     */
    private void pickItem(Command command){
        if(!command.hasSecondWord()) {                 //check if the input has a second word, if not then return
            System.out.println("Pick what?");
            return;
        }
        String item = command.getSecondWord();  //get second word as string
        
        if(player.getCurrentRoom().itemInRoom(item)){   
            Item roomItem = player.getCurrentRoom().getItem(item);      
            if(player.underWeight(roomItem)){                       //if the item is in the room then check if the player is able to carry it
                player.addItem(roomItem);                                                
                player.getCurrentRoom().removeItem(roomItem);       //remove the item from the room when the player picks it up
                player.setDamage();                                 //sees if player picked up knife, if so then damage is increased to 5
            }
        } 
    }
    
    /**
     * Lets the player drop items in a room
     */
    private void dropItem(Command command){
        if(!command.hasSecondWord()) {
            System.out.println("drop what?");
            return;
        }
        String object = command.getSecondWord();
        
        if(player.itemInInventory(object)){         //if player has that object, then drop it in the room
            Item item = player.getItem(object);     
            player.removeItem(item.getName());
            player.getCurrentRoom().addItem(item);
            player.setDamage();                                 //if item dropped = knife, then damage goes back down to 0
            System.out.println("You have dropped " + object);
        } else {
            System.out.println("You dont have that");
        }
    }
    
    /**
     * Lets the player use an item that they have in their inventory
     * Used to escape or give a health boost
     */
    private void useItem(Command command){      
        if(!command.hasSecondWord()) {
            System.out.println("use what?");
            return;
        }        
        String object = command.getSecondWord();
                                                     
        if (player.itemInInventory(object)){                                //player can only use item if they have it
            if (object.equals("key") && player.getCurrentRoom() == gate){
                winGame();            
            } else if (object.equals("seeds") || object.equals("medicine")) {       //these items give player a health boost depening on what item they use
                player.healthBoost(object);
            } else {
                System.out.println("Maybe you cant use the item here or it cant be used at all");
            }
        } else {
            System.out.println("You dont have that item");
        }                                                    
    }
    
    /**
     * Lets the player inspect something
     */
    private void makeInspection(Command command){
        if(!command.hasSecondWord()) {
            System.out.println("Inspect what?");
            return;
        }
        String object = command.getSecondWord();

        if(object.equals("inventory")){                 //player can inspect 3 different things
            System.out.println(player.getInventory());
        } else if(object.equals("room")){                //inspect the current room
            System.out.println(player.getCurrentRoom().getLongDescription());        
        } else if(object.equals("scientist") && player.getCurrentRoom() == lab2 ){      //interact with the scientist in lab2
            lab2.getScientist().interact();                          
        } else {
            System.out.println("The only items you can inspect are: inventory, room, scientist");   //in the future could make all inspectable objects enums
        }
    }
    
    /**
     * Lets the player go to the previous room they were in
     */
    private void goBack(Command command){
        if(command.hasSecondWord()) {
            System.out.println("You can only go back to the previous room\nOnly type 'back'");
            return;
        }
        
        if((player.getPreviousRoom().getShortDescription()).equals("start")){       //easiest way to see if player has not moved at all therefore he cant go back.
            System.out.println("You cant go back from the start");
            return;
        } else if (player.getPreviousRoom() == transporter) {       //player cant go back to the teleporation room if he was teleported = more realistic
            System.out.println("You cant teleport");
        } else{
            moveOstrich();                                                      //moves the characters randomly
            player.setCurrentRoom(player.getPreviousRoom());                    //sets current room to the previous room
            System.out.println(player.getCurrentRoom().getLongDescription());
        }   
    }
    
    /**
     * Deals damage to one ostrich in the room if there is one
     */
    private void killOstrich(Command command) {    
        if(!command.hasSecondWord()) {                
            System.out.println("kill what?");
            return;
        }
        
        String object = command.getSecondWord();
        if (object.equals("ostrich") && player.itemInInventory("knife") && player.getCurrentRoom().hasOstrich() ){  //requirements to kill an ostrich
            Ostrich enemy = player.getCurrentRoom().getOstrich();           //an ostrich object is collected and dealt damage to
            enemy.getAttacked(player.getDamage());
            System.out.println("You attacked an ostrich");
            if (enemy.getHealth() <= 0) {                                   //checks health of ostrich, if its below 0 then it is removed permenantly
                    player.getCurrentRoom().removeOneOstrich();
                    ostrichNum -= 1;
                    System.out.println("You killed the ostrich");
                    if (ostrichNum <= 0){                               //if all 3 are killed then the player is rewarded feathers
                        player.rewardFeathers();
                    }
            }    
        } else {
                System.out.println("You need a knife, if you have one then you can only kill an ostrich and thats only when they are in the room.\n");
        }        
    }
    
    /**
     * Lets player give an item to the scientist
     */
    private void giveItem(Command command){
        if(!command.hasSecondWord()) {
            System.out.println("Give where?");
            return;
        }
        String object = command.getSecondWord();
        String word3 = command.getThirdWord();
        
        if(!command.hasThirdWord()) {                                   //this method requires user to enter 3 words so this if statments checks for thirdword
            System.out.println("Give " + object+ " to who?");
            System.out.println("\n\nHINT: 'give feathers scientist'");
            return;
        }
        
        //a lot of requirements to give the feathers to the scientist
        if (player.getCurrentRoom() == lab2 && scientist1.hasFeathers() == false && object.equals("feathers") && word3.equals("scientist") && player.itemInInventory("feathers")){
            System.out.println("Thank you so much for that. I shall repay you by giving you the key to escape. Good luck.");
            player.setItems("key", "used in the exit room to escape", 0);                                                        //key is rewarded to player
            player.removeItem("feathers");                                                                                       //feathers is removed from player
            System.out.println("\nYou have been given a key");
        } else {
            System.out.println("Perhaps give feathers to the scientist when you have them");
        }
    }
        
    /**
     * Transports player to a random room after they enter the magic room
     */
    private void transportPlayer(){
        while (player.getCurrentRoom() == transporter){
                int b = getRandomNumber();
                while(rooms.get(b) != transporter){     //if new room isnt the magic room again then player is transported
                    player.setCurrentRoom(rooms.get(b));
                    System.out.println("You entered the magic transporting room and have been transported.\n" + player.getCurrentRoom().getLongDescription());
                    break;
                }
                b = getRandomNumber();  //if current room was set to magic room then while loop carries on until player set in magic room anymore
        }
    }
    
    /**
     * Moves all characters to random rooms
     * Does this by collecting all ostrich then setting them somewhere else
     */
    private void moveOstrich(){                
        ArrayList<Ostrich> allOstrich = new ArrayList<>();
        for (Room room: rooms){              
            if (room.hasOstrich()){              
                ArrayList<Ostrich> ostrich = room.getAllOstrich();      //gets all ostrich from one room
                for(Ostrich x: ostrich){                                //for each ostrich in the room, add it to the list
                    allOstrich.add(x);
                }
                room.removeAllOstrich();                                //after all ostrich has been collected, then remove all from current room
            }
        }
        
        //sets all collected ostrich to new rooms
        for (Ostrich x: allOstrich){
            int b = getRandomNumber();
            rooms.get(b).setOstrich(x);
        }
    }
    
    /**
     * Deals damage to the player if an ostrich is in the same room
     */
    private void takeDamage(){
        if (player.getCurrentRoom().hasOstrich()){
            ArrayList<Ostrich> ostrich = player.getCurrentRoom().getAllOstrich();  //gets all ostrich from one room
            int totalDamage = 0;
            
            for (Ostrich x: ostrich){                              //collects damage stat from each ostrich and adds it to a total
                totalDamage += x.getDamage();                      //i did this so in the future different ostrich can have different damage stats
            }
            
            //inflicts damage to player and displays the numbers associated with it
            player.getAttacked(totalDamage);            
            System.out.println("You just took damage: -" + totalDamage);
            System.out.println("Your current health: " + player.getHealth());            
            
            if (player.getHealth() <= 0){                   //checks if player has 0 health, if so then game over for them
                gameLost();
                return;
            }
            System.out.println("You better move or kill them or YOU will end up dying");
        }
    }
    
    /**
     * Returns a random number
     * Used to assign random rooms to characters
     */
    private int getRandomNumber(){
        Random rand = new Random();
        int x = rand.nextInt(rooms.size());
        return x;
    }
    
    /**
     * Stops the game loop and prints a 'you win' message
     */
    private void winGame(){
        System.out.println("\n\nWell done! You have escaped...and came out into a zoo\nYou were just in the zoo\n\nAt least you won the game\n");
        finished = true;
    }
    
    /**
     * Stops the game loop and prints a 'you lose' message
     */
    private void gameLost(){
        System.out.println("\nWow you actually died...BY OSTRICH\nPlay again when your ready for the challenge");
        finished = true;
    }
}