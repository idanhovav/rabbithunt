import java.util.Observable;
import java.util.Random;

/**
 * Creates and maintains a "field" for the rabbit and fox, and sets up a number
 * of methods to be used by animals. This class, rather than the Animal class,
 * is in charge of letting animals move and look around--this makes it harder
 * for an animal to "cheat" by knowing things it shouldn't know, or making moves
 * it shouldn't be allowed to make.
 * 
 * @author David Matuszek
 * @version October 26, 2001
 * 
 * @author Carl Burch
 * @version 2005-07-22
 */
public class Model extends Observable {
    //
    // constants
    //
    public static final Object LOOK_ACTION = "look";
    public static final Object STEP_ACTION = "step";
    public static final Object RESET_ACTION = "reset";
    public static final Object REPLAY_ACTION = "replay";
    
    //
    // controlled from outside class
    //
    /** The Game object containing model methods accessible by players. */
    private Game game;
    
    /** The number of milliseconds to pause after each call to <code>look</code>. */
    private int lookDelay;

    // only changed between games

    /** A grid allowing easy lookup of bushes. */
    private Entity[][] field;
    
    /** An array of all entities to place (i.e., those not on the boundary). */
    private Entity[] entities;

    /** The rabbit player in the game. */
    private Rabbit rabbit;

    /** The fox player in the game. */
    private Fox fox;
    
    /** A copy of the fox entity, returned by the <code>look<code> method so that
     * the rabbit can't track the real fox. */
    private Entity foxCopy;

    /** The seed used for the current simulation. */
    private long curSeed;

    /** The random number generator used. */
    private static Random generator = new Random();

    /** Tracks whether the game is being reset, so that methods can
     * behave appropriately. */
    private boolean populateInProgress;

    //
    // instance variables changed within game
    //
    /** Tracks whose turn it currently is. */
    private boolean isRabbitsTurn;
    
    /** The direction in which an animal is now looking, or <code>null</code>
     * if no animal is looking. */
    private Direction lookingDirection;
    
    /** The entity who is looking. */
    private Entity lookingEntity;

    /**
     * Constructs a model on a grid of the given size.
     * 
     * @param rowCount  the number of rows in the grid.
     * @param columnCount  the number of columns in the grid.
     */
    public Model(int rowCount, int columnCount) {
        generator = new Random();
        game = new Game(rowCount, columnCount, generator);
        lookDelay = 0;
        field = new Entity[rowCount][columnCount];

        // Put bushes around boundary
        for(int i = 0; i < rowCount; i++) {
            field[i][0] = new Entity(this, Entity.BUSH);
            
            
            //This one's the only one that isn't flipped goddam
            field[i][0].setLocation(this, i, 0);
            
            
            field[i][columnCount - 1] = new Entity(this, Entity.BUSH);
            
            
            //ME IDAN:
            //CHANGING THIS CUZ I THINK ITS FLIPPED
            //field[i][columnCount - 1].setLocation(this, columnCount - 1, i);
            field[i][columnCount - 1].setLocation(this, i, columnCount - 1);
            
            
        }
        for(int i = 0; i < columnCount; i++) {
            field[0][i] = new Entity(this, Entity.BUSH);
            
            
            //changed because also flipped fuck
            //field[0][i].setLocation(this, i, 0);
            field[0][i].setLocation(this, 0, i);
            
            
            
            field[rowCount - 1][i] = new Entity(this, Entity.BUSH);
            //THIS ONE TOO
            //CHANGING GAHHH
            //field[rowCount - 1][i].setLocation(this, i, rowCount - 1);
            field[rowCount - 1][i].setLocation(this, rowCount - 1, i);
        }
        
        // Create other entities all at once. We do this to
        // reduce memory allocation, particularly during grading. 
        foxCopy = new Entity(this, Entity.FOX);
        entities = new Entity[2 + (rowCount - 2) * (columnCount - 2) / 20];
        for(int i = 0; i < entities.length; i++) {
            String identity = i < 2 ? (i == 0 ? Entity.RABBIT : Entity.FOX)
                    : Entity.BUSH;
            entities[i] = new Entity(this, identity);
        }

        // Now set up a new simulation.
        reset();
    }
    
    /**
     * Sets the number of milliseconds that should be delayed each time
     * an animal looks in a direction. Use 0 to turn off all delays; this
     * is the default behavior.
     * 
     * @param millis  the number of millisecansd to delay each time an animal
     *               looks in a direction.
     */
    public void setLookDelay(int millis) {
        lookDelay = millis;
    }

    /**
     * Sets up a new hunt.
     */
    public void reset() {
        if(populateInProgress) return;
        setChanged();
        curSeed = generator.nextLong();
        newSimulation();
        notifyObservers(RESET_ACTION);
    }

    /**
     * Sets up the same hunt all over again, using old random seed.
     */
    public void replay() {
        if(populateInProgress) return;
        setChanged();
        newSimulation();
        notifyObservers(REPLAY_ACTION);
    }
    
    /**
     * Initializes the simulation, moving all the players.
     */
    private void newSimulation() {
        populateInProgress = true;
        generator.setSeed(curSeed);

        // remove all internal entities
        for(int i = 0; i < entities.length; i++) {
            Entity ent = entities[i];
            int row = ent.getRow();
            int col = ent.getColumn();
            if(row >= 0 && col >= 0) field[row][col] = null;
        }
        
        // place the internal entities
        int curEntity = 0;
        int triesLeft = 20 * entities.length;
        int rowCount = game.getRowCount();
        int columnCount = game.getColumnCount();
        int minFoxDist = Math.min(rowCount, columnCount) / 4;
        while(curEntity < entities.length && triesLeft > 0) {
            int row = game.randomInt(1, rowCount - 2);
            int col = game.randomInt(1, columnCount - 2);
            if(field[row][col] == null) {
                Entity ent = entities[curEntity];
                ent.setLocation(this, row, col);
                if(ent.isFox() && ent.distanceTo(entities[0]) < minFoxDist) {
                    ; // don't commit the fox because it is too close
                } else {
                    field[row][col] = ent;
                    curEntity++;
                }
            }

            --triesLeft;
        }
        if(triesLeft <= 0) {
            throw new RuntimeException("could only place " + curEntity
                    + " of " + entities.length + " entities");
        }

        // create the players
        rabbit = new Rabbit(game, entities[0]);
        rabbit.setModel(this, entities[0]);
        
        fox = new Fox(game, entities[1]);
        fox.setModel(this, entities[1]);

        // finish
        isRabbitsTurn = true;
        populateInProgress = false;
    }
    
    /**
     * Returns the number of rows in the grid. This will include the
     * border that is full of bushes.
     * 
     * @return the number of rows in the grid.
     */
    public int getRowCount() {
        return game.getRowCount();
    }
    
    /**
     * Returns the number of columns in the grid. This will include the
     * border that is full of bushes.
     * 
     * @return the number of columns in the grid.
     */
    public int getColumnCount() {
        return game.getColumnCount();
    }
        
    /**
     * Returns <code>true</code> if the hunt is over. This will be true
     * if either the fox has caught the rabbit or the number of steps taken
     * by the rabbit exceeds a threshold.
     * 
     * @return <code>true</code> if the hunt is over.
     */
    public boolean isGameOver() {
        if(populateInProgress) return false;
        return isRabbitCaught() || rabbit.getStepCount() >= Game.MAX_RABBIT_STEPS;
    }
    
    /**
     * Returns <code>true</code> if the fox has caught the rabbit.
     * 
     * @return <code>true</code> if the fox has caught the rabbit.
     */
    public boolean isRabbitCaught() {
        if(populateInProgress) return false;
        return entities[0].distanceTo(entities[1]) == 0;
    }
    
    /**
     * Returns the number of steps the rabbit has made so far.
     * 
     * @return the number of steps the rabbit has made so far.
     */
    public int getStepCount() {
        return rabbit.getStepCount();
    }

    /**
     * Returns the rabbit entity in this simulation.
     * 
     * @return this simulation's rabbit object.
     */
    public Entity getRabbit() {
        return entities[0];
    }
    
    /**
     * Returns the entity at the given location, or <code>null</code> if it
     * is empty.
     * 
     * @param row  the row of the location queried.
     * @param col  the column of the location queried.
     * @return  the entity at the query location, or <code>null</code> if
     *   nothing is there.
     */
    public Entity get(int row, int col) {
        return field[row][col];
    }
    
    /**
     * Returns <code>true</code> if a player is currently looking and the
     * query location is within its current line of view.
     * 
     * @param queryRow  the row of the location in question.
     * @param queryCol  the column of the location in question.
     * @return  <code>true</code> if a player is currently looking and the
     *   query location is within its current line of view.
     */
    public boolean isSeen(int queryRow, int queryCol) {
        Entity entity = lookingEntity;
        Direction dir = lookingDirection;
        if(entity == null || dir == null) return false;

        int triesLeft = 2 + Math.max(game.getRowCount(), game.getColumnCount());
        int row = lookingEntity.getRow();
        int col = lookingEntity.getColumn();
        do {
            row += dir.getDeltaY();
            col += dir.getDeltaX();
            if(row == queryRow && col == queryCol) return true;
            triesLeft--;
        } while(get(row, col) == null && triesLeft > 0);
        return false;
    }
    
    /**
     * Returns the first entity that the given entity will see in the
     * direction. If the look delay has been changed to be positive,
     * this method will block for some time.
     * 
     * @param entity  the entity that is looking.  
     * @param dir  the direction the entity is looking.
     * @return  the first entity found looking in that direction.
     */
    public Entity look(Entity entity, Direction dir) {
        if(dir == null) return null;
        
        lookingEntity = entity;
        lookingDirection = dir;
        
        Entity ret;
        int row = entity.getRow();
        int col = entity.getColumn();
        int triesLeft = 2 + Math.max(game.getRowCount(), game.getColumnCount());
        do {
            row += dir.getDeltaY();
            col += dir.getDeltaX();
            ret = get(row, col);
            triesLeft--;
        } while(ret == null && triesLeft > 0);
        
        if(triesLeft <= 0) {
            throw new RuntimeException("infinite loop in look");
        }
        
        if(lookDelay > 0) {
            setChanged();
            notifyObservers(LOOK_ACTION);
            try { Thread.sleep(lookDelay); }
            catch(InterruptedException ex) { }
        }
        lookingEntity = null;
        lookingDirection = null;

        // don't return the fox directly, since that would allow
        // the rabbit to track it. Return a copy instead.
        if(ret.isFox()) {
            foxCopy.setLocation(this, ret.getRow(), ret.getColumn());
            return foxCopy;
        } else {
            return ret;
        }
    }
    
    /**
     * Steps this simulation forward one turn. Note that the count returned
     * by <code>setStepCount</code> will be roughly 1/2 the number of times
     * this method is called, since the rabbit steps only once every other
     * call to this method.
     */
    public void step() {
        // make sure it's legal move
        if(populateInProgress || isGameOver()) return;

        // decide whose turn it is now
        Entity moved;
        Player player;
        if(isRabbitsTurn) {
            moved = entities[0];
            player = rabbit;
        } else { // fox's turn
            moved = entities[1];
            player = fox;
        }
        
        // clear the looking information
        lookingEntity = null;
        lookingDirection = null;
        
        // make the move
        int oldRow = moved.getRow();
        int oldCol = moved.getColumn();
        player.step(this);
        if(field[oldRow][oldCol] == moved) field[oldRow][oldCol] = null;
        int newRow = moved.getRow();
        int newCol = moved.getColumn();
        if(moved.isFox() || field[newRow][newCol] == null) {
            field[newRow][newCol] = moved;
        }
        setChanged();
        notifyObservers(STEP_ACTION);
        
        // flip whose turn it is
        isRabbitsTurn = !isRabbitsTurn;
    }
}
