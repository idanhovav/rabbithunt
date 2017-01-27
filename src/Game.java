import java.util.Random;

/**
 * Represents the basic information surrounding the game being
 * played. This is something like a weaker version of the
 * <code>Model</code> class: The difference is that players
 * have access to the <code>Game</code> object and its methods,
 * but no direct access to the underlying <code>Model</code>.
 *  
 * @author Carl Burch
 * @version 2005-07-22
 */
public class Game {
    /** The threshold number of steps that the rabbit must make successfully
     * to have escaped, winning the game. */
    public static final int MAX_RABBIT_STEPS = 100;

    /** The number of rows in the field. */
    private int rowCount;
    
    /** The number of columns in the field. */
    private int columnCount;
    
    /** The generator to use for random numbers. */
    private Random generator;
    
    /**
     * Constructs an object holding basic information about the hunt.
     * 
     * @param myRowCount     the number of rows in the grid.
     * @param myColumnCount  the number of columns in the grid.
     * @param myGenerator    the random number generator to use.
     */
    public Game(int myRowCount, int myColumnCount, Random myGenerator) {
        rowCount = myRowCount;
        columnCount = myColumnCount;
        generator = myGenerator;
    }
    
    /**
     * Returns the number of rows in the grid.
     * 
     * @return  the number of rows in the grid.
     */
    public int getRowCount() {
        return rowCount;
    }
    
    /**
     * Returns the number of columns in the grid.
     * 
     * @return  the number of columns in the grid.
     */
    public int getColumnCount() {
        return columnCount;
    }
    
    /**
     * Returns a randomly chosen direction of the 8 available. All
     * simulation components should use this method to generate random
     * directions so that replaying correctly retraces previous steps.
     * 
     * @return a randomly chosen direction.
     */
    public Direction randomDirection() {
        int choice = randomInt(0, Direction.NUM_DIRECTIONS - 1);
        return Direction.NORTH.rotate(choice);
    }

    /**
     * Returns a randomly chosen integer from min to max, inclusive. All
     * simulation components should use this method to generate random
     * numbers so that replaying correctly retraces previous steps.
     * 
     * @param min  the smallest number to be returned
     * @param max  the largest number to be returned
     * @return a random number N, where min &lt;= N &lt;= max
     */
    public int randomInt(int min, int max) {
        return min + generator.nextInt(max - min + 1);
    }
}
