/**
 * Item class - represents one item
 * 
 * Each item has a name, description and weight. Items can be picked, dropped, given, used to kill or used to open.
 *
 * @author Yusuf Yacoobali
 * @version v1
 */
public class Item        
{
    private String name;
    private String description;     //initialise attributes of each item object
    private int weight;
    /**
     * Constructor for objects of class Item
     */
    public Item(String name, String description, int weight)
    {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }
    
    /**
     * Returns the name of the item
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * Returns the description of the item
     */
    public String getDescription(){
        return this.description;
    }
    
    /**
     * Returns the weight of the item
     */
    public int getWeight(){
        return this.weight;
    }
}