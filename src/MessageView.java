import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

/**
 * Shows messages to the user about the game's progress.
 * 
 * @author Carl Burch
 * @version 2005-07-22
 */
public class MessageView extends JLabel implements Observer {
    /** The model that we are tracking. */
    private Model model;
    
    /** Whether the current game has been completed. */
    private boolean currentComplete = false;
    
    /** How many games have been completed so far. */
    private int gamesComplete = 0;
    
    /** How many games have been won so far. */
    private int gamesWon = 0;
    
    /**
     * Constructs a message view corresponding to a model.
     * 
     * @param myModel  the model that this message represents.
     */
    public MessageView(Model myModel) {
        model = myModel;
        model.addObserver(this);
        setText("Let the hunt begin!");
    }

    /**
     * Updates the message to reflect changes to the model.
     */
    public void update(Observable source, Object data) {
        if(data == Model.STEP_ACTION) {
            int steps = model.getStepCount();
            if(model.isGameOver()) {
                if(!currentComplete) {
                    currentComplete = true;
                    gamesComplete++;
                    if(!model.isRabbitCaught()) gamesWon++;
                }
                
                // create a string representing the overall record. The
                // division here is save because the earlier if statement,
                // plus the fact that currentComplete starts out as false,
                // ensures gamesComplete will be at least 1.
                String record = gamesWon + "/" + gamesComplete + ": "
                    + (int) Math.round(100.0 * gamesWon / gamesComplete) + "%";
                if(model.isRabbitCaught()) {
                    setText("The fox eats the rabbit after "
                            + steps + " turns! " + record);
                } else {
                    setText("The rabbit escapes! " + record);
                }
            } else {
                setText("step " + steps);
            }
        } else if(data == Model.RESET_ACTION) {
            currentComplete = false;
            setText("Let the hunt begin!");
        } else if(data == Model.REPLAY_ACTION) {
            setText("Instant replay...");
        }
    }
}
