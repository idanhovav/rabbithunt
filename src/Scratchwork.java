//TRIAL FOR TRACKING EACH LOCATION'S VALUE
    	
//    	
//    	
//    	//values based on escape routes in each location, 2D array declared up above
//    	
//    	int locvalue[][] = new int[rows][columns];
//    	
//    	//Initialize 2D array to all locations having 8 escape routes
//    	for(int i=0; i < rows; i++)
//    	{
//    		for(int j=0; j < columns; j++)
//    		{
//    			locvalue[i][j] = -1;
//    		}
//    	}
//    	
//    	
//    	//Only setting value if there has been no value assigned to this location
//    	if(locvalue[self.getRow()][self.getColumn()] != -1){
//    		
//    		//looping through different directions at location
//    		for(int i=0; i< Direction.NUM_DIRECTIONS; i++)
//        	{
//    			
//        		Direction dir = Direction.NORTH.rotate(i);
//        		
//        		//If there's a bush, then the bush's location is made a 0 value
//        		if(look(dir).isBush())
//        		{
//        			locvalue[self.getRow() + dir.getDeltaX()]
//        					[self.getColumn() + dir.getDeltaY()] = 0;
//        		}
//        		//if it is a possible escape route, value of rabbit's location is raised.
//        		else if(canStep(dir))
//        		{
//        			locvalue[self.getRow()][self.getColumn()] ++;
//        		}
//        		
//        	}
//    	}
//    	



//PRINT CHECKS OF BUSH LOCATIONS
    			//System.out.println(looking.toString());
    			//System.out.println("[" + self.getRow() + "," 
    					//+ self.getColumn() + "]");
    			//System.out.println(dist);
    			//Based on direction and distance, bush is recorded as 1 in 2D bushes array
    			//System.out.print("Row: " + self.getRow());
    			//System.out.println(" Row Distance: " + (dist) + 
    			//		" Vertical Change: " + dir.getDeltaY());
    			
    			//System.out.print("Column: " + self.getColumn() );
    			//System.out.println(" Column Distance: " + (dist) + 
    			//		" Horizontal Change: " + dir.getDeltaX());
    			
    			//System.out.println(" ");


//PART THING

//for(int i = 0; i < this.rows; i++)
//    	{
//    		for(int j =0; j < columns; j++)
//    		{
//    			//set perimeter to having bushes 
//    			bushes[0][i] = 1;
//    			locvalues[0][i] = -1;
//    			bushes[rows-1][i] = 1;
//    			locvalues[rows-1][i] = -1;
//    			
//    			bushes[i][0] = 1;
//    			locvalues[i][0] = -1;
//    			
//    			bushes[i][columns-1] = 1;
//    			locvalues[i][columns-1] = -1;
//    			
//    			//refresh the location of the rabbit
//    			if(bushes[i][j] == 2 && (i != self.getRow() || j != self.getColumn()))
//    			{
//    				bushes[i][j] = 0;
//    			}
//    		}
//    	}



//FIRST LOOP THROUGH DIRECTIONS TRIAL
/*
for(int i=0; i< Direction.NUM_DIRECTIONS; i++)
    	{
    		Direction dir = Direction.NORTH.rotate(i);
    		Entity looking = look(dir);
    		if(looking.isFox())
    		{
    			//System.out.println("Spotted da fox!");
    			gottaMove = true;
    			foxDirection = i;
    			foxDistance = self.distanceTo(looking);
    		}
    		if(looking.isBush())
    		{
    			int dist = self.distanceTo(looking);
    			bushes[(self.getRow() + dist*dir.getDeltaY() )]
    					[( self.getColumn() + (dist*dir.getDeltaX()) )] = 1;
    			bushes[self.getRow()][self.getColumn()] = 2;
    		}
    	}



*/






/*
FIRST FIRST FIRST FIRST EVER IMPLEMENTATION THIS TIME

    	
    	if(gottaMove)
    	{
    		//System.out.println("Yup gotta go");
    		if(canStep(Direction.NORTH.rotate(foxDirection+3)))
    		{
    			//System.out.println("Gotta get away!");
    			return Direction.NORTH.rotate(foxDirection+3);
    		}
    		else if(canStep(Direction.NORTH.rotate(foxDirection+5)))
    		{
    			//System.out.println("Gotta get away a different way!");
    			return Direction.NORTH.rotate(foxDirection+5);
    		}
    	}
    	


*/


//green cheese



/*


ONCE UPON A TIME, THIS WAS MY CODE


 * Represents the rabbit player.
 * 
 * @author ??
 * @version ??

public class Rabbit extends Player {
	//used for location values
    private Game game;
    
    private Entity self;
    
    //Self-created, to be used in decide method
	//2D Array for location of bushes.
    
    //stores the number of rows in the game. Includes perimeter bushes
	int rows;
	
	//stores the number of columns in the game. Includes perimeter bushes
	int columns;
	
	//A 2D array that stores the locations of bushes as 1's
	//in a grid-like fashion.
	int[][] bushes;
	
	 *
	 * A 2D array that has the value from 1-100 of each square, based on:
	 * 1 - proximity to bushes
	 * 2 - centrality
	 * 3 - proximity to fox
	 * 4 - relation to fox's directional movement
	 * 5 - fox's line of sight
	 * 
	 * Stores bushes as -1
	 * 
	 * TODO: Incorporate bushes into locvalues
	 * TODO: Decide on value of rabbit's location
	 *
	int[][] locvalues;
    
    *//**
     * Constructs the rabbit player.
     * 
     * @param myGame  the game in which this player plays.
     * @param mySelf  the entity that this player controls.
     *//*
    public Rabbit(Game myGame, Entity mySelf) {
        super();
        game = myGame;
        self = mySelf;
        
        //as stated, storing rows of game in rows and columns in columns
        rows = game.getRowCount();
    	columns = game.getColumnCount();
    	
    	//Since bushes and locvalues store info on every square, they cover entire game
    	bushes = new int[rows][columns];
    	locvalues = new int[rows][columns];

    	
    	
    	
    	
    	
    	 * Lines 68-100 initialize locvalues to the number of moves from that 
    	 * square to the center of the grid.
    	 
    		
    	for(int i = 0; i < this.rows; i++)
    	{
    		for(int j =0; j < columns; j++)
    		{
    	    	
    	    	 * Set perimeter of locvalues to having bushes since this is
    	    	 * the case for all games.
    	    	 
    			locvalues[0][j] = -1;
    			locvalues[rows-1][j] = -1;
    			
    			locvalues[i][0] = -1;
    			locvalues[i][columns-1] = -1;
    			
    	    	
    	    	//Enter values of squares based on centrality
    			
    			//check for not negative b/c fox and bushes are negative
    			if(locvalues[i][j] >= 0)
    			{
    				//columns/2 is the central column of the grid
    				//rows/2 is the central row of the grid
    				//i and j are row and column values of the square, respectively
    				//Math.abs takes the farther distance between i and rows/2 and 
    				//j and columns/2
    				//Distance from center is value 
    				locvalues[i][j] = (Math.max( Math.abs(i - (int)rows/2),
    						Math.abs(j - (int) columns/2) ) );
    			}
    		}
    	}
    	
    	
    	
    	
    }
    
    *//**
     * Decides which direction this rabbit should go in its next move.
     * 
     * @return the direction to go, or <code>null</code> to remain stationary.
     *//*
    public Direction decide() {
    	
    	 * Game Plan:
    	 * Filter out directions can move and directions can not move
    	 * check if fox can see
    	 * Move based on 3 priorities:
    	 * 1 - fox cannot see and move away from fox
    	 * 2 - Preference towards middle
    	 * 3 - bushes
    	 * Score field based on escape routes nearby. Prefer highest scores for movement
    	 * Manipulate scores based on location of fox (somehow)
    	 * give weights based on centrality
    	 * 
    	 * Other idea: based on distance to fox, we know distance and direction, so we
    	 * can figure out the fox's location. Calculate the fox's sight fields, and avoid.
    	 * 
    	 * 
    	 * TODO: Decide range of locvalues for valid locations
    	 * decide on weights and effects of influences on values.
    	 * Remember: effects must keep locvalues within range 
    	 * and close to fox > fox can see > centrality (in order of importance).
    	 * 
    	 
    	
    	int foxX = -1;
    	int foxY = -1;

    	
    	for(int i=0; i< Direction.NUM_DIRECTIONS; i++)
    	{
    		Direction dir = Direction.NORTH.rotate(i);
    		Entity looking = look(dir);
    		if(looking.isFox())
    		{
    			//-10 denotes a fox!!
    			int dist = self.distanceTo(looking);
    			foxX = (self.getRow() + dist*dir.getDeltaY() );
    			foxY = ( self.getColumn() + (dist*dir.getDeltaX()) );
    			locvalues[foxX][foxY] = -10;
    	    	
    			
    			//use 1 b/c no bushes
    			for(int a = 1; a < rows; a++)
    			{
    				for(int b = 1; b < columns; b++)
    				{
    					//no bushes
    					if(locvalues[a][b] >= 0){
   							
    						
    						 * This whole thing updates squares
    						 * that are close to fox
    						 * and then resets squares that aren't next to fox anymore
    						 
   							//newvalue calculated using update method
    						if(Math.max
    								( Math.abs(a - foxX), Math.abs(b - foxY) ) <= 5)
    						{
            					int newvalue = locvalues[a][b] - updateLocValueFoxDistance(a, b, foxX, foxY);
        						
            					//if value goes negative, set to zero
            					if(newvalue <= 0){
            						locvalues[a][b] = 0;
            					}
            					//else update locvalues to newvalue
            					else{
            						locvalues[a][b] = newvalue;
           						}
    						}
    						//resets squares to original value
    						//after they're no longer 5 away from fox
    						else{
    							resetSquareVal(a, b);
    						}
        					
        					//if the fox can see the square, then the value is halved
    						if(canFoxSeeSquare(a, b, foxX, foxY))
    						{
    							locvalues[a][b] = (int) (locvalues[a][b]/2);
    						}
    						
    						
    					}								
    				}
    			}
    		}
    		
    		if(looking.isBush())
    		{
    			//Update locvalue on new bushes found
    			//Location of bush is found b/c magnitude dist * direction .getDeltaX/Y
    			//is equal to a location when added to another location b/c physics 
    			//and forces and shit goddam vectors
    			
    			//Delta Y is change in rows, so multiplied by distance returns 
    			//distance from rabbit rows to bush rows
    			
    			//Same thing w/ Delta X and shit
    			int dist = self.distanceTo(looking);
    			locvalues[(self.getRow() + dist*dir.getDeltaY() )]
    					[( self.getColumn() + (dist*dir.getDeltaX()) )] = -1;
    			
    		}
    	}
    	
		int largest = -10;
		Direction largestDirection = Direction.NORTH;
    	for(int i = 0; i < Direction.NUM_DIRECTIONS; i++)
    	{
    		Direction direction = Direction.NORTH.rotate(i);
    		if(locvalues[self.getRow() + direction.getDeltaX()]
    				[self.getColumn() + direction.getDeltaY()] > largest)
    		{
    			largestDirection = direction;
    		}
    		
    	}


        return largestDirection;
    }

    //returns the value to subtract from the locvalues[a][b]
	private int updateLocValueFoxDistance(int a, int b, int foxX,
			int foxY) {
		
		int changer = 0;
		
		
		
		//Update values of spots that are 5 steps away from the fox
		//updated by subtracting steps away the spot is from fox

			//decrease value of those spots by distance to fox.
			changer = (  5 - Math.max
				( Math.abs(a - foxX), Math.abs(b - foxY) )  );
		return changer;
	}
	private void resetSquareVal(int a, int b){
		locvalues[a][b] = Math.max( Math.abs(a - (int)rows/2),
				Math.abs(b - (int) columns/2) ) ;
	}
	private boolean foxCanSeeSquare(int a, int b, int foxX, int foxY){
		
		
		//Updates square value based on if fox can see square.
		//3 parts: checks horizontally, checks vertically, and checks diagonally.
		
		//checks the fox's row
		if(a == foxX)
		{
			return true;
		}
		
		//checks the fox's column
		if(b == foxY)
		{
			return true;
		}
		
		//checks fox's diagonal
		//k goes through distances from fox location
		for(int k = 0; k < Math.max(rows, columns); k++)
		{
			if(a == foxX+k && b == foxY+k)
			{
				return true;
			}
		}
		
		return false;
	}
		
}*/






//		System.out.println("Adjacent values: ");
//		for(int i = 0; i < pref.length; i++)
//		{
//			System.out.print(pref[i] + " ");
//		}
//    	System.out.println(" ");








/*
 * I tried
 */

//    	//to avoid going out of range.
//    	if(!whenSpotted.isEmpty()){
//        	//if rabbit spotted fox last round, rabbit goes in the same direction again
//        	if(!notSpotted && turn - whenSpotted.get(whenSpotted.size() - 1) == 1)
//        	{
//        		//System.out.print(" a");
//        		
//        		//if the rabbit is getting chased, then keep moving
//        		if(whenSpotted.size() >= 2)
//        		{
//        			if(turn - whenSpotted.get(whenSpotted.size() - 2) == 2)
//        			{
//        				//System.out.println("boggie boogie");
//                		//in order to avoid getting cornered, prefer moving 2 off from previous direction
//                		if(pref[lastDirection] >= 1)
//                		{
//                			return Direction.NORTH.rotate(lastDirection);
//                		}
//                		
//                		//if the previous direction moved is valid, move in that direction
//                		if(pref[(lastDirection + 2) % 8] >= 1)
//                		{
//                			return Direction.NORTH.rotate(lastDirection + 2);
//                		}
//        			}
//        		}
//        		//but if the rabbit is only seen once, stay in the same place
//        		else
//        		{
//            		//STAY IN
//            		return null;
//        		}
//
//        	}
//    	}