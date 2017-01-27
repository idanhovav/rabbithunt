/**
 * Represents an object that lies on the game's field. You can think
 * about the distinction between this class and <code>Player</code>
 * as the distinction between a chess piece and a chess player.
 * 
 * @author Carl Burch
 * @version 2005-07-22
 */
public class Entity {
    public static final String BUSH = "bush";
    public static final String RABBIT = "rabbit";
    public static final String FOX = "fox";
    
    private Model model;
    private String identity;
    private int row;
    private int column;
    
    /**
     * Constructs an entity at the given position.
     * 
     * @param myModel  the model in which the entity lives.
     * @param myIdentity  the type of entity that this is (<code>BUSH</code>,
     *                    <code>FOX</code>, <code>RABBIT</code>).
     */
    public Entity(Model myModel, String myIdentity) {
        model = myModel;
        identity = myIdentity;
        row = -1;
        column = -1;
    }
    
    /**
     * Returns <code>true</code> if this entity is a bush.
     * 
     * @return <code>true</code> if this entity is a bush.
     */
    public final boolean isBush() {
        return identity == BUSH;
    }
    
    /**
     * Returns <code>true</code> if this entity is a fox.
     * 
     * @return <code>true</code> if this entity is a fox.
     */
    public final boolean isFox() {
        return identity == FOX;
    }
    
    /**
     * Returns <code>true</code> if this entity is a rabbit.
     * 
     * @return <code>true</code> if this entity is a rabbit.
     */
    public final boolean isRabbit() {
        return identity == RABBIT;
    }
    
    /**
     * Returns the row that this entity currently occupies. Rows are
     * numbered increasing northward.
     * 
     * @return  the row that this entity currently occupies.
     */
    public final int getRow() {
        return row;
    }
    
    /**
     * Returns the column that this entity currently occupies.
     * 
     * @return  the column that this entity currently occupies.
     */
    public final int getColumn() {
        return column;
    }
    
    /**
     * Returns the distance from this entity to the given entity,
     * measured as the minimum number of steps it would take to
     * reach it assuming no intervening obstacles. That is, it is
     * the larger of the difference in rows and the difference in
     * columns.
     * 
     * @param other  the query entity.
     * 
     * @return  the minimum number of steps from this entity to the
     *        query entity assuming no obstacles in between.
     */
    public int distanceTo(Entity other) {
        return Math.max(Math.abs(this.row - other.row),
                Math.abs(this.column - other.column));
    }

    /**
     * Sets the location of this entity. The method should be called
     * only from within <code>Model</code>.
     * 
     * @param source  the model initiating the request. This is not really used
     *                within the method: It acts basically as a password to
     *                ensure the calling method has the right to alter the model.
     * @param newRow  the row where the entity should be placed.
     * @param newColumn  the column where the entity should be placed.
     */
    public final void setLocation(Model source, int newRow, int newColumn) {
        if(source != model) {
            throw new IllegalArgumentException("You cannot use the setLocation method.");
        }
        
        row = newRow;
        column = newColumn;
    }
    
    /**
     * Returns a descriptive string describing this entity.
     * 
     * @return the descriptive string for this entity.
     */
    public String toString() {
        return identity + "[" + row + "," + column + "]";
    }
}
