/**
 * Represents one of the eight directions.
 * 
 * @author Carl Burch
 * @version 2005-07-22
 *
 */public class Direction {
    /** The number of directions available (8). */
    public static final int NUM_DIRECTIONS = 8;
    
    /** The north direction. */
    public static final Direction NORTH = new Direction("north", 0, 1);
    
    /** The northeast direction. */
    public static final Direction NORTHEAST = new Direction("northeast", 1, 1);
    
    /** The east direction. */
    public static final Direction EAST = new Direction("east", 1, 0);
    
    /** The southeast direction. */
    public static final Direction SOUTHEAST = new Direction("southeast", 1, -1);
    
    /** The south direction. */
    public static final Direction SOUTH = new Direction("south", 0, -1);
    
    /** The southwest direction. */
    public static final Direction SOUTHWEST = new Direction("southwest", -1, -1);
    
    /** The west direction. */
    public static final Direction WEST = new Direction("west", -1, 0);
    
    /** The northwest direction. */
    public static final Direction NORTHWEST = new Direction("northwest", -1, 1);
    
    /** An array of all directions listed in clockwise order. */
    private static final Direction[] VALUES = {
            NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
    };

    //
    // instance variables
    //
    /** The name of this direction. */
    private String name;
    
    /** The change in x for this direction. */
    private int dx;
    
    /** The change in y for this direction. */
    private int dy;
    
    /** The index of this direction within its array. */
    private int index;
    
    /** Other classes cannot create new directions. They must use one of
     * the eight defined constants.
     * 
     * @param myName  the name to associate with the direction.
     * @param myDx  the change in x to associate with the direction.
     * @param myDy  the change in y to associate with the direction.
     */
    private Direction(String myName, int myDx, int myDy) {
        name = myName;
        dx = myDx;
        dy = myDy;
        index = -1;
    }
    
    /**
     * Returns the number of columns changed with each step in this
     * direction. An eastward direction will return 1, a westward 
     * direction will return -1, and north/south will return 0.
     * 
     * @return the number of columns for each step in this direction.
     */
    public int getDeltaX() {
        return dx;
    }
    
    /**
     * Returns the number of rows changed with each step in this
     * direction. A northward direction will return 1, a southward 
     * direction will return -1, and east/west will return 0.
     * 
     * @return the number of rows for each step in this direction.
     */
    public int getDeltaY() {
        return dy;
    }
    
    /**
     * Returns the direction that is clockwise from this direction
     * on the compass by the specified number of 45-degree steps.
     * A negative parameter will lead to going counterclockwise.
     * 
     * @param distance  the number of 45-degree steps clockwise.
     * @return  the computed direction.
     */
    public Direction rotate(int distance) {
        if(index < 0) {
            for(int i = 0; i < VALUES.length; i++) {
                if(VALUES[i] == this) { index = i; break; }
            }
        }
        int retIndex = (index + distance) % VALUES.length;
        if(retIndex < 0) {
            // negative numerator leads to negative result
            // for example: -9 % 8 == -1
            retIndex = VALUES.length + retIndex;
        }
        return VALUES[retIndex];
    }
    
    /**
     * Returns a descriptive name for this direction.
     * 
     * @return the descriptive name.
     */
    public String toString() {
        return name;
    }
}
