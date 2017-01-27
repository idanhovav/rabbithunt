/**
 * Represents a player of the game, providing some methods that
 * represent actions the player can take on the board. You can
 * think about the distinction between this class and <code>Entity</code>
 * as analogous to a chess player and a chess piece.
 * 
 * @author Carl Burch
 * @version 2005-07-22
 */
public abstract class Player {
    private Model model;
    private Entity self;
    private int stepCount;
    
    /**
     * Constructs a player corresponding to the entity.
     * 
     * @param mySelf  the entity corresponding to this player.
     */
    public Player() {
        model = null;
        self = null;
        stepCount = 0;
    }
    
    /**
     * Returns the first entity that can be seen in the given
     * direction. This will not be <code>null</code> unless the
     * direction is <code>null</code>.
     * 
     * @param direction  the direction in which to look.
     * @return  the first entity found in the query direction,
     *   or <code>null</code> if the query direction is <code>null</code>.
     */
    public final Entity look(Direction direction) {
        ensureModelExists("look");
        return model.look(self, direction);
    }
    
    /**
     * Returns <code>true</code> if this entity can legally make
     * a single step in the current direction. The step will be
     * illegal only if a bush is blocking it. This always returns
     * <code>true</code> if the current direction is <code>null</code>.
     * 
     * @return <code>true</code> if this entity can legally make
     *   a single step in the current direction.
     */
    public final boolean canStep(Direction direction) {
        ensureModelExists("canStep");
        if(direction == null) {
            return true;
        } else {
            int newRow = self.getRow()    + direction.getDeltaY();
            int newCol = self.getColumn() + direction.getDeltaX();
            Entity there = model.get(newRow, newCol);
            return there == null || !there.isBush();
        }
    }
    
    /**
     * Returns the number of steps this animal has made so far.
     * 
     * @return the number of steps takes by this animal.
     */
    public final int getStepCount() {
        return stepCount;
    }

    /**
     * Decides which direction this animal should be facing for its next
     * move. It can choose <code>null</code> to stay stationary.
     * 
     * @return the direction this animal should go for its next move,
     *         or <code>null</code> to remain stationary.
     */
    public abstract Direction decide();
    
    /**
     * Makes one turn. The method will first call <code>decide</code>
     * so that the player can choose the direction of the move. The
     * <code>step</code> method should be called only from within <code>Model</code>.
     * 
     * @param source  the model initiating the request. This is not really used
     *                within the method: It acts basically as a password to
     *                ensure the calling method has the right to alter the model.
     */
    public final void step(Model source) {
        // the parameter here ensures that the rabbit can't cheat
        // by making several steps in the same move.
        if(source != model) {
            throw new IllegalArgumentException("You cannot use the step method.");
        }

        Direction direction = decide();
        if(direction != null && canStep(direction)) {
            self.setLocation(model,
                    self.getRow() + direction.getDeltaY(),
                    self.getColumn() + direction.getDeltaX());
        }
        stepCount++;
    }
    
    /**
     * Configures the model where this player lives. This method
     * should only be called by the <code>Model</code> class.
     * 
     * @param myModel  the model where the entity lives.
     * @param mySelf   the entity that this player controls.
     */
    public final void setModel(Model myModel, Entity mySelf) {
        if(model != null || self != null) {
            throw new IllegalArgumentException("You cannot use the setModel method");
        }
        model = myModel;
        self = mySelf;
    }
    
    /**
     * Ensures that <code>setModel</code> has already been called. This
     * helper method is for other methods that cannot be called within
     * the player's constructor.
     * 
     * @param methodName  the initiating method name, used to construct a
     *                    descriptive error message.
     */
    private void ensureModelExists(String methodName) {
        if(model == null) {
            throw new IllegalArgumentException("You cannot call " + methodName
                    + " within the constructor method.");
        }
    }
}
