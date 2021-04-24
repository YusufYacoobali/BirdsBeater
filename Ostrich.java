/**
 * Ostrich class - represents one ostrich object
 * 
 * Ostrich are seen as the enemy in the game. They have health but can also deal damage to the player
 * 
 * Once the player kills 3 ostrich, he gets feathers
 * Feathers can be given to the scientist in exchange for keys
 *
 * @author Yusuf Yacoobali
 * @version v1
 */
public class Ostrich        
{
    private Room currentRoom;
    private Room previousRoom;    
    private int health;
    private int damage;
    /**
     * Constructor for objects of class Ostrich
     */
    public Ostrich()
    {
        currentRoom = new Room("start");
        previousRoom = new Room("previous");
        this.health = 7;                        //health and damage stats are declared
        this.damage = 5;
    }
    
    /**
     * Returns the health of the ostrich
     */
    public int getHealth(){
        return this.health;
    }
    
    /**
     * Returns the damage stat of the ostrich
     */
    public int getDamage(){
        return this.damage;
    }
    
    /**
     * Decreases the health of the object by the players damage stat
     */
    public void getAttacked(int damage){
        this.health -= damage;
    }  
}