import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A component holding the controls for the user to manipulate the
 * hunt's progress.
 * 
 * @author David Matuszek
 * @version October 24, 2001
 * 
 * @author Carl Burch
 * @version 2005-07-22
 */
public class Controller extends JPanel
        implements Runnable, ActionListener, ChangeListener, Observer {
    /** The model we are controlling. */
    private Model model;

    // instance variables -- used for program control
    /** The number of milliseconds to delay with each animation. */
    private int animationDelay;

    /** The number of steps that the user has requested the model
     * to take. This should be -1 for continuous run. */
    private int stepsLeft = 0;
    
    /** Whether the model is currently being stepped. */
    private boolean isStepping = false;

    // instance variables -- display elements
    /** The Step button. */
    private JButton stepButton = new JButton("Step");

    /** The Run button. */
    private JButton runButton = new JButton("Run");

    /** The Stop button. */
    private JButton stopButton = new JButton("Stop");

    /** The Reset button. */
    private JButton resetButton = new JButton("Reset");

    /** The Replace button. */
    private JButton replayButton = new JButton("Replay");

    /** The Speed slider. */
    private JSlider speedBar = new JSlider();

    /**
     * Constructs a Controller that uses the given model.
     */
    Controller(Model model) {
        super(new BorderLayout());
        this.model = model;
        model.addObserver(this);
        
        createGui();
        stateChanged(null); // initialize animation delay
        update(null, null); // initialize buttons' enabled states
    }

    /**
     * Sets up the graphical user interface, including adding listeners to the
     * components.
     */
    private void createGui() {
        // create panel of buttons using flow layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(stepButton);
        buttonPanel.add(runButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(replayButton);

        // add all elements into this panel using border layout.
        add(new JLabel("Speed:"), BorderLayout.WEST);
        add(speedBar, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);

        // configure the speed slider
        speedBar.addChangeListener(this);
        speedBar.setMinimumSize(new Dimension(50, 4));
        speedBar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        speedBar.setValue(50);
        speedBar.setMaximum(90);

        // add listeners to the buttons
        stepButton.addActionListener(this);
        runButton.addActionListener(this);
        stopButton.addActionListener(this);
        resetButton.addActionListener(this);
        replayButton.addActionListener(this);
    }

    /**
     * Controls the animation. All steps are made from this thread.
     */
    public void run() {
        while(true) {
            // wait until we should be taking steps
            synchronized(this) {
                while(stepsLeft == 0) {
                    try { wait(); }
                    catch(InterruptedException ex) { }
                }
            }
            
            // make the step
            long nextStep = System.currentTimeMillis() + animationDelay;
            isStepping = true;
            model.step();
            if(stepsLeft > 0) stepsLeft--;
            synchronized(this) {
                isStepping = false;
                notifyAll();
            }
            
            // wait until we are eligible to take the next step
            int wait = (int) (nextStep - System.currentTimeMillis());
            if(wait > 0) {
                try { Thread.sleep(wait); }
                catch(InterruptedException ex) { }
            }
        }
    }

    /**
     * Responds to the press of a button.
     * 
     * @param event  information about the action.
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if(source == stepButton) {
            // Increment the number of steps to take.
            int newSteps = stepsLeft + 1;
            if(newSteps > 0) setStepsLeft(newSteps);
        } else if(source == runButton) {
            // Make the simulation thread go indefinitely.
            setStepsLeft(-1);
        } else if(source == stopButton) {
            // Stops all simulation.
            setStepsLeft(0);
        } else if(source == resetButton) {
            // Stops the simulation and resets it.
            setStepsLeft(0);
            model.reset();
        } else if(source == replayButton) {
            // Stops the simulation and restarts it.
            setStepsLeft(0);
            model.replay();
        }
    }

    /**
     * Sets the number of steps that the stepping thread should
     * take.
     *  
     * @param value  the number of steps for the stepping thread.
     */
    private void setStepsLeft(int value) {
        synchronized(this) {
            if(stepsLeft == value) return; // nothing to do
            stepsLeft = value;
            model.setLookDelay(stepsLeft > 0 ? animationDelay : 0);
            notifyAll(); // awake stepping thread
            if(value == 0) {
                // if the simulation is being stopped, wait until
                // current thread is done so that repopulation of
                // the grid is effective.
                while(isStepping) {
                    try { wait(); }
                    catch(InterruptedException ex) { }
                }
            }
        }
    }

    /**
     * Computes the animation delay and the delay between "looks" based on the
     * current value of the speedBar.
     * 
     * @param event  information about the change in state (ignored).
     */
    public void stateChanged(ChangeEvent event) {
        int value = speedBar.getValue();
        animationDelay = (int) (2320 - 500 * Math.log(value + 10));
        if(stepsLeft > 0) {
            model.setLookDelay(animationDelay);
        }
    }

    /**
     * Updates the buttons' enabled states to reflect the current
     * model state.
     * 
     * @param source  the model initiating the update (ignored).
     * @param data  information about the update (ignored).
     */
    public void update(Observable source, Object data) {
        boolean isRunning = stepsLeft < 0;
        boolean isOver = model.isGameOver();
        boolean isStart = model.getStepCount() == 0;
        stepButton.setEnabled(!isRunning && !isOver);
        runButton.setEnabled(!isRunning && !isOver);
        stopButton.setEnabled(isRunning);
        resetButton.setEnabled(true);
        replayButton.setEnabled(!isStart);
    }
}
