import java.util.HashMap;
/**
 * Write a description of class Player here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Player
{
    private HashMap<String, Item> inventory;        //player inventory
                
    private int maxWeight;                          //player current weight and limit
    private int currentWeight;
            
    private Room currentRoom;                       //players current and previous room
    private Room previousRoom;
        
    private int health;                             //players health and damage stats
    private int damage;
    /**
     * Constructor for objects of class Player
     */
    public Player()
    {
        inventory = new HashMap<>();
        maxWeight = 10;
        currentWeight = 0;
        currentRoom = new Room("start");
        previousRoom = new Room("previous");
        health = 60;
        damage = 0;
    }
    
    /**
     * Sets the current room for the player
     */
    public void setCurrentRoom(Room room){
        previousRoom = currentRoom;
        currentRoom = room;
    }
    
    /**
     * Creates and adds an item to the players inventory 
     * Used for the creation of items such as key and feathers
     */
    public void setItems(String name, String description, int weight){
        Item item = new Item(name, description, weight);
        inventory.put(name, item);
    }
    
    /**
     * Sets the health of the player
     * Used when player gets a health boost by using items like medicine
     */
    public void setHealth(int num){
        this.health += num;
    }
    
    /**
     * Sets the damage stat of the player
     */
    public void setDamage(){
        if(itemInInventory("knife")){       //if player has a knife then the players damage is 5, else its 0
            this.damage = 5;
        } else {
            this.damage = 0;
        }
    }
    
    /**
     * This method deals with giving a health boost to the player 
     * Depends on what item the player used
     * That item is then discarded to make the game realistic
     */
    public void healthBoost(String object){
        int boost = 0;                          //Holds the value of how much of a health boost the player will get depending on what they use
        if (itemInInventory("seeds")){
            boost = 10;
        } else if (itemInInventory("medicine")){
             boost = 15;
        } 
        setHealth(boost);       //sets player health
        removeItem(object);     //removes item from inventory after use
        System.out.println("Your health has been restored by: +" +  boost );
    }
    
    /**
     * Checks if the player can carry an item considering the weight restriction
     */
    public boolean underWeight(Item item){
        if(item.getWeight() + this.currentWeight > this.maxWeight){
            System.out.println(item.getName() + " is too heavy for you, it has a weight of: " + item.getWeight());
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * Rewards the player with the feathers if all 3 ostrich have been killed
     */
    public void rewardFeathers(){
        System.out.println("Wow, you truly are brutal. You killed all the ostrich\n\nYou have now been given feathers, inspect your inventory.");
        setItems("feathers", "a scientist in another lab wants them", 0);
    }
    
    /**
     * Checks if the player has an item in their inventory or not
     */
    public boolean itemInInventory(String itemName){
        for(String name: inventory.keySet()){
            if(itemName.equals(name)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds items to players inventory
     * Used when collecting things from rooms
     */
    public void addItem(Item item){
        inventory.put(item.getName(), item);
        currentWeight += item.getWeight();
        System.out.println("You have picked up " + item.getName());
    }
    
    /**
     * Removes items from players inventory
     * Used when the player drops an item
     */
    public void removeItem(String itemName){
        Item item = inventory.get(itemName);
        currentWeight -= item.getWeight();
        inventory.remove(item.getName());
    }
    
    /**
     * Returns the current room of the player
     */
    public Room getCurrentRoom(){
        return this.currentRoom;
    }
    
    /**
     * Returns the previous room of the player
     */
    public Room getPreviousRoom(){
        return this.previousRoom;
    }
    
    /**
     * Returns an item object that the player has
     * Used to reference items to drop them
     */
    public Item getItem(String itemName){
        Item item = inventory.get(itemName);    
        return item;
    }
    
    /**
     * Returns the health of the player
     */
    public int getHealth(){
        return this.health;
    }
    
    /**
     * Returns the damage stat of the player
     */
    public int getDamage(){
        return this.damage;
    }
    
    /**
     * Deals damage to the player
     * The parameter is for the total damage caused by all enemies in the room
     */
    public void getAttacked(int damage){
        this.health -= damage;
    }  
    
    /**
     * Returns a string of the entire inventory of the player
     * Includes the description and weight of each item carried along with the maximum limit the player can carry
     */
    public String getInventory(){
        String playerItems = "You have no items in your inventory";
        String weightLimit = "\nYour weight limit is: " + maxWeight;
        String carrying = "\n\nRight now you are carrying a weight of: " + currentWeight;
        
        //playerItems string is changed if player does have items in the inventory
        if(!inventory.isEmpty()){                             
            playerItems = "Items in your inventory: ";
            for(Item item: inventory.values()){
                playerItems = playerItems + "\n-" + (item.getName() + ", " + item.getDescription() + ", Weight: " + item.getWeight());
            }
        }
        return playerItems + carrying + weightLimit;
    }
}
