import java.util.Set;
import java.util.HashMap;
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
 * @author  Michael Kölling and David J. Barnes and Yusuf Yacoobali
 * @version v2
 */

public class Room      
{   
    //initialise variables and collections
    private String description;
    private HashMap<String, Room> exits;        
    private HashMap<String, Item> inventory;        //inventory of this room object
    private ArrayList<Ostrich> ostrich;             //ostrich objects in this room object
    private ArrayList<Scientist> scientist;             
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        //variables and collections are decalred
        this.description = description;
        this.exits = new HashMap<>();
        this.inventory = new HashMap<>();
        this.ostrich = new ArrayList<>();
        this.scientist = new ArrayList<>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }
    
    /**
     * Places item in the room by creating them
     */
    public void setItems(String name, String description, int weight){
        Item item = new Item(name, description, weight);
        inventory.put(name, item);
    }
    
    /**
     * Adds ostrich objects to the rooms ostrich hashmap 
     */
    public void setOstrich(Ostrich newOstrich){
        ostrich.add(newOstrich);
    }
    
    /**
     * Places scientists in room's scientist hashmap
     */
    public void setScientist(Scientist newScientist){
        scientist.add(newScientist);
    }
    
    /**
     * Checks if an item is in the room or not
     */
    public boolean itemInRoom(String itemName){
        for(String name: inventory.keySet()){       
            if(itemName.equals(name)){
                return true;
            }
        }
        System.out.println("That object doesnt exist in this room");
        return false;
    }
    
    /**
     * Checks if an ostrich is in the room or not
     */
    public boolean hasOstrich(){
        if (ostrich.isEmpty()){
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Removes all ostrich in the rooms hashmap.
     * This is used to move the ostrich
     */
    public void removeAllOstrich(){
        ostrich.clear();
    }
    
    /**
     * Removes just one ostrich.
     * Used when player kills an ostrich
     */
    public void removeOneOstrich(){
        ostrich.remove(0);
    }
    
    /**
     * Remoces an item from this room
     * Used when player picks an item up
     */
    public void removeItem(Item item){
        inventory.remove(item.getName());
    }
    
    /**
     * Adds an item to the room
     * Used when player drops an item in the room
     */
    public void addItem(Item item){
        inventory.put(item.getName(), item);
    }
    
    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room
     * Displays description and exits of the room, 
     * Also displays if there are any items, ostrich or scientist in the room.
     * Displays only when objects are present
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + getItemString() + getOstrichString() + getScientistString() + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "\nExits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }
    
    /**
     * Returns a string of the items in the room if there are any.
     * eg: "Items in this room: key knife seeds"
     */
    private String getItemString(){
        String returnString = "";
        if(!inventory.isEmpty()){
            returnString = "\nItems in this room: ";
            for(String itemName: inventory.keySet()){
                returnString += itemName + " ";
            }
        }
        return returnString;
    }
    
    /**
     * Returns a string of the number of ostrich in the room if there are any.
     * eg: "Number of ostrich in this room: 2"
     */
    private String getOstrichString(){
        String returnString = "";
        if(!ostrich.isEmpty()){
            returnString = "\nNumber of ostrich in room: " + ostrich.size();
        }
        return returnString;
    }
    
    /**
     * Returns a string of that tells the user if there is a scientist in the room if there are any.
     * eg: "There is a scientist in this room. HINT: Inspect scientist"
     */
    private String getScientistString(){
        String returnString = "";
        if (!scientist.isEmpty()){
            returnString = "\nThere is a scientist in this room\nHINT: Inspect scientist";
        } 
        return returnString;
    }
    
    /**
     * Returns the first ostrich in the room's hashmap
     * Used to deal damage to that particular ostrich
     */
    public Ostrich getOstrich(){
        return ostrich.get(0);
    }
    
    /**
     * Returns the first scientist in the room's hashmap
     * Used to interact with the character
     */
    public Scientist getScientist(){
        return scientist.get(0);
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Returns an item that is in the room by using its key
     * Used to for player to pick up items in the room
     */
    public Item getItem(String itemName){
        Item item = inventory.get(itemName);  
        return item;
    }
    
    /**
     * Returns an arraylist of all ostrich in the room
     * Used to collect them so that they can be moved to other rooms
     */
    public ArrayList<Ostrich> getAllOstrich(){
        return ostrich;
    }
}