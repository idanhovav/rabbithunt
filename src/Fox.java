/**
 * Represents the fox player.
 * 
 * @author David Matuszek
 * @version October 12, 2001
 * 
 * @author Carl Burch
 * @version 2005-07-22
 */
public class Fox extends Player {
    private Game game;
    private Entity self;
    private Direction facing;
    private int distanceToRabbit;

    /**
     * Constructs the fox player.
     * 
     * @param myGame  the game in which this player plays.
     * @param mySelf  the entity that this player controls.
     */
    public Fox(Game myGame, Entity mySelf) {
        super();
        game = myGame;
        self = mySelf;
        facing = Direction.NORTH;
        distanceToRabbit = -1;
    }

    /**
     * Decides which direction this fox should go in its next
     * move.
     * 
     * @return the direction to go, or <code>null</code> to remain stationary.
     */
    public Direction decide() {
        // look all around for rabbit
        boolean found = false;
        for(int i = 0; !found && i < Direction.NUM_DIRECTIONS; i++) {
            Direction dir = facing.rotate(i);
            Entity there = look(dir);
            if(there.isRabbit()) {
                facing = dir;
                distanceToRabbit = self.distanceTo(there);
                found = true;
            }
        }

        if(distanceToRabbit > 0) {
            // If rabbit has been seen recently (not necessarily this time),
            // move toward its last known position. We are already facing
            // in that direction.
            distanceToRabbit--;
            return facing;
        } else {
            // We can't find the rabbit. Find a direction that we can go,
            // preferably close to our current direction.
            if(canStep(facing)) {
                ; // don't change the direction we face
            } else if(canStep(facing.rotate(1))) {
                facing = facing.rotate(1);
                return facing;
            } else if(canStep(facing.rotate(-1))) {
                facing = facing.rotate(-1);
            } else {
                int steps = 2;
                while(!canStep(facing.rotate(steps)) && steps < Direction.NUM_DIRECTIONS) {
                    steps++;
                }
                facing = facing.rotate(steps);
            }
            return facing;
        }
    }

}
