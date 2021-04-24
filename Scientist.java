import java.util.HashMap;
/**
 * Scientist class - respresents one scientist
 * 
 * A scientist is a character that essentially trades with the player. He is the key for the player getting out. 
 * The scientist does not move as of yet and does not do much too. 
 * I did want to do a lot more with this class but with the time limit it was difficult
 *
 * @author Yusuf Yacoobali
 * @version v1
 */
public class Scientist      
{
    private HashMap <String, Item> inventory;       
    private Room currentRoom;
    /**
     * Constructor for objects of class Scientist
     */
    public Scientist()
    {
        inventory = new HashMap<>();
        currentRoom = new Room("start");
    }
    
    /**
     * Prints out a message from the scientist for the player
     */
    public void interact(){
        System.out.println("\n\n'Hello there, I am a scientist working here.\nCould you help me in my research by finding and giving me some feathers\n\n");
        System.out.println("HINT: killing all 3 ostrich will give you feathers");
    }
    
    /**
     * Checks if the scientist has feathers in his inventory
     */
    public boolean hasFeathers(){
        if (inventory.containsKey("feathers")){     
            return true;
        } else {
            return false;
        }
    }
}