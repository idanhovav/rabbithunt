import java.util.ArrayList;

/**
 * Represents the rabbit player.
 * 
 * @author Idan Hovav
 * @version 2
 */
public class Rabbit extends Player {
  private Game game;
  private Entity self;
  //Includes perimeter bushes
  int rows;
  int columns;
  int turn = 0;
  ArrayList<Integer> timesSpotted = new ArrayList<Integer>();
  int lastDirection;
  static int NUM_OPTIONS = Direction.NUM_DIRECTIONS + 1; // +1 for not moving
  int foxX;
  int foxY;
  int foxDist;
  boolean spotted;
  
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
    rows = game.getRowCount();
    columns = game.getColumnCount();
    foxX = -1;
    foxY = -1;
    foxDist = -1;
    spotted = false;
  }
  
  /**
   * Decides which direction this rabbit should go in its next move.
   * 
   * @return the direction to go, or <code>null</code> to remain stationary.
   */
  public Direction decide() {
    int maxPref = -2;
    int maxDirection = -1;
    spotted = false;
    
    //array that stores values of preferences to each option of movement
    int[] pref = createArrayOfOnes(NUM_OPTIONS);
    for (int i = 0; i < pref.length - 1; i++) {
      updateForBushes(pref, i);
      updateForFox(pref, i);
    }
     //this for loop checks all directions to see if the fox can see them
    if (spotted) {
      for (int i = 0; i < pref.length - 1; i++) {
        Direction direction = Direction.NORTH.rotate(i);
        if (pref[i] != -1) { // doesn't change bushes
          //the location of the adjacent square in that direction.
          int row = self.getRow() + direction.getDeltaY();
          int col = self.getColumn() + direction.getDeltaX();

          //value of 0 denotes that fox can see square, but can still move there 
          if (foxCanSeeSquare(row, col, foxY, foxX)) {
            pref[i] = 0;
          }
        }
      }
    }
    //gotta add a statement to check the present square since fox could see it.
    //can't put in for loop b/c not a direction
    if (spotted) {
      if (foxCanSeeSquare(self.getRow(), self.getColumn(), foxY, foxX)) {
        pref[8] = 0;
      }
    }

    //at this point, there are two routes we need to split into
    //the first is if there is at least 1 viable direction to take
    //the second is if there are 0 viable options to take

    //this for loop changes values of viable squares
    //based on their distance to the center 
    for (int i = 0; i < pref.length - 1; i++) {
      if (pref[i] > 0) {
        Direction direction = Direction.NORTH.rotate(i);
        int row = self.getRow() + direction.getDeltaY();
        int col = self.getColumn() + direction.getDeltaX();
        pref[i] = 25 - Math.max(Math.abs(row - (rows / 2)),
                    Math.abs(col - (columns / 2))
                 );
      }
    }
    //this is for null option of staying in same place
    if (pref[8] > 0) {
      pref[8] = 25 - Math.max(Math.abs(self.getRow() - (rows / 2)),
                  Math.abs(self.getColumn() - (columns / 2)));
    }
    maxPref = getMaxVal(pref);
    maxDirection = getMaxIndex(pref);

    //make options out of directions that have equal values
    ArrayList<Integer> options = chooseOptions(pref, maxPref);

    /*
     * Make algorithm for choosing from options if more than one
     * if fox spotted, choose one that makes farther away from fox
     */

    turn++;
    if (!spotted && maxPref >= 5) {
      return null; // keeps rabbit from moving around in the middle
    }
    if (maxDirection == 9) {
      return null;
    }
    lastDirection = maxDirection;
    return Direction.NORTH.rotate(maxDirection);
  }

  private int[] createArrayOfOnes(int size) {
    int[] arr = new int[size];
    for (int i = 0; i < size; i++) {
      arr[i] = 1;
    }
    return arr;
  }

  private void updateForBushes(int[] prefs, int index) {
    Direction direction = Direction.NORTH.rotate(index);
    Entity entity = look(direction);
    if (self.distanceTo(entity) == 1) {
      if (entity.isBush()) {
        prefs[index] = -1;
      }
    }
  }

  private void updateForFox(int[] prefs, int index) {
    Direction direction = Direction.NORTH.rotate(index);
    Entity entity = look(direction);
    if (entity.isFox() && !spotted) {     
      prefs[index] = -2; // make that direction the worst.

      //Updates fox location
      foxDist = self.distanceTo(entity);
      foxX = self.getColumn() + (foxDist * direction.getDeltaX());
      foxY = self.getRow() + (foxDist * direction.getDeltaY());             
      spotted = true;
      timesSpotted.add(turn);                  
    }
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

  /*
   * Returns ArrayList of indices of equal max values.
   */
  private ArrayList<Integer> chooseOptions(int[] pref, int max) {
    ArrayList<Integer> options = new ArrayList<Integer>();
    for (int index = 0; index < pref.length; index++) {
      if (pref[index] == max) {
        options.add(index);
      }
    }
    return options;
  }
}
