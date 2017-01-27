import java.util.ArrayList;

/**
 * Represents the rabbit player.
 * 
 * @author Idan Hovav
 * @version 2
 */
public class Rabbit extends Player {
	//used for location values
    private Game game;
    
    private Entity self;
    
    //stores the number of rows in the game. Includes perimeter bushes
	int rows;
	
	//stores the number of columns in the game. Includes perimeter bushes
	int columns;
	
	//what turn is it
	int turn = 0;
	
	//on what turns the fox has been spotted
	ArrayList<Integer> timesSpotted = new ArrayList<Integer>();
	
	//the last direction the rabbit went
	int lastDirection;
    
	//options for final decision
	ArrayList<Integer> options = new ArrayList<Integer>();
	
	
    /**
     * Constructs the rabbit player.
     * 
     * @param myGame  the game in which this player plays.
     * @param mySelf  the entity that this player controls.
     */
    public Rabbit(Game myGame, Entity mySelf) {
        super();
        game = myGame;
        self = mySelf;
        
        //as stated, storing rows of game in rows and columns in columns
        rows = game.getRowCount();
    	columns = game.getColumnCount();
    	
    }
    
    /**
     * Decides which direction this rabbit should go in its next move.
     * 
     * @return the direction to go, or <code>null</code> to remain stationary.
     */
    public Direction decide() {
    	
    	//location of fox
    	int foxX = -1;
    	int foxY = -1;
    	int foxDist = -1;
    	
    	//maximum square nearby
    	int max = -2;
    	int maxDirection = -1;
    	
    	//boolean to skip 2nd if statement if fox is already spotted
    	boolean notSpotted = true;
    	
    	//array that stores values of preferences to each square
    	int[] pref = new int[9];
    	
    	for(int i = 0; i < pref.length; i++)
    	{
    		pref[i] = 1;
    	}
    	
    	
    	//minus 1 for pref because 9 represents not moving
    	/*
    	 * This for loop does the following:
    	 * updates adjacent values as bushes or fox by assigning value -1
    	 * updates location of fox if it is spotted
    	 * 
    	*/ 
    	for(int i = 0; i < pref.length - 1; i++)
    	{
    		//checks all 8 directions
    		Direction direction = Direction.NORTH.rotate(i);
    		
    		//whatever is seen put here
    		Entity entity = look(direction);
    		
    		//if the entity is adjacent, then update as bush
    		if(self.distanceTo(entity) == 1)
    		{
        		if(entity.isBush())
        		{
        			pref[i] = -1;
        		}
    		}
    		
    		if(entity.isFox() && notSpotted)
    		{		
    			//make that direction the wurst
    			pref[i] = -2;
    			
    			//Updates fox location
    			//some tricky shit goin on with X and Y
    			foxDist = self.distanceTo(entity);
    			foxX = self.getColumn() + ( foxDist * direction.getDeltaX() );
    			foxY = self.getRow() + ( foxDist * direction.getDeltaY() );				
    			notSpotted = false;
    			timesSpotted.add(turn);
    		}					
    	}
    	
    	
    	
    	 //this for loop checks all directions to see if the fox can see them
    	if(!notSpotted)
    	{
        	for(int i = 0; i < pref.length - 1; i++)
        	{
        		Direction direction = Direction.NORTH.rotate(i);
        		
        		//doesn't change bushes
        		if(pref[i] != -1)
        		{
            		//the location of the adjacent square in that direction.
            		int row = self.getRow() + direction.getDeltaY();
            		int col = self.getColumn() + direction.getDeltaX();
            		
            		//value of 0 denotes that fox can see square, but can still move there 
            		if( foxCanSeeSquare(row, col, foxY, foxX) )
            		{
            			pref[i] = 0;
            		}
        		} 		
        	}
    	}
    			

    	
    	//gotta add a statement to check the present square since fox could see it.
    	//can't put in for loop b/c not a direction
    	if(!notSpotted){
    		if(foxCanSeeSquare(self.getRow(), self.getColumn(), foxY, foxX) )			
    		{
    			pref[8] = 0;
    		}
    	}
    	
    	//at this point, there are two routes we need to split into
    	//the first is if there is at least 1 viable direction to take
    	//the second is if there are 0 viable options to take
    	
    	
    		//this for loop changes values of viable squares
    		//based on their distance to the center
    		
    	for(int i = 0; i < pref.length - 1; i++)
    	{
    		if(pref[i] > 0)
    		{
    			Direction direction = Direction.NORTH.rotate(i);
    			
    			int row = self.getRow() + direction.getDeltaY();
           		int col = self.getColumn() + direction.getDeltaX();
           		
    			pref[i] = 25 - Math.max( Math.abs(row - ((int)rows/2)),
    					Math.abs(col - ((int) columns/2)) );
    		}
   		}
    		
    	//this is for null option of staying in same place
    	if(pref[8] > 0)
    	{
        	pref[8] = 25 - Math.max( Math.abs( self.getRow() - ((int)rows/2) ),
    				Math.abs(self.getColumn() - ((int) columns/2)) );
    	}
    	max = getMaxVal(pref);
    	maxDirection = getMaxIndex(pref);
    	
    	//make options out of directions that have equal values
    	options = createArrayListOfOptions(pref, max);
    	
    	/*
    	 * Make algorithm for choosing from options if more than one
    	 * if fox spotted, choose one that makes farther away from fox
    	 */
    	
    	turn++;
    	if (notSpotted && max >= 5) {
    		return null; // keeps rabbit from moving around in the middle
    	}
    	if (maxDirection == 9) {
    		return null;
    	}
    	lastDirection = maxDirection;
    	return Direction.NORTH.rotate(maxDirection);
    }
    
    /*
     * Returns if the fox can see a certain square.
     * This is dependant on the expected square the fox is on.
     * Does not take bushes into account.
     * Checks if square and fox are in line horizontally, vertically, and then diagonally.
     */
	
    private boolean foxCanSeeSquare(int row, int col, int foxY, int foxX) {
		if (row == foxY || col == foxX) {
			return true;
		}
		//checks fox's diagonal
		for (int dist = 0; dist < Math.max(rows, columns); dist++) {
			if (col == foxX + dist && row == foxY + dist ||
				col == foxX + dist && row == foxY - dist ||
				col == foxX - dist && row == foxY + dist ||
				col == foxX - dist && row == foxY - dist)
				return true;
		}
		return false;
	}

    private int getMaxVal(int[] arr) {
    	int max = Integer.MIN_VALUE;
    	for (int index = 0; index < arr.length; index++) {
    		int val = arr[index];
    		if (val > max) {
    			max = val;
    		}
    	}
    	return max;
    }
    
    /*
     * Crashes for arrays of length 0 because of array index out of bounds exception.
     * This should never happen since array being passed in is the directions.
     */
    private int getMaxIndex(int[] arr) {
    	int max = Integer.MIN_VALUE;
    	int index = 0;
    	for (int i = 0; i < arr.length; i++) {
    		int val = arr[i];
    		if (val > max) {
    			max = val;
    			index = i;
    		}
    	}
    	return index;
    }
    
    private ArrayList<Integer> createArrayListOfOptions(int[] pref, int max) {
    	ArrayList<Integer> options = new ArrayList<Integer>();
    	for (int index = 0; index < pref.length; index++) {
    		if (pref[index] == max) {
    			options.add(index);
    		}
    	}
    	return options;
    }
}
