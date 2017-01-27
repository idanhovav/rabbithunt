import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

/**
 * Describes a "rabbit hunt", in which a fox moves around a field
 * containing bushes, looking for a rabbit. The rabbit, of course,
 * tries not to be caught by the fox.
 * 
 * @author David Matuszek
 * @version October 12, 2001
 * 
 * @author Carl Burch
 * @version 2005-07-22
 */
public class RabbitHunt {
    /** There is no reason to construct <code>RabbitHunt</code> objects. */
    private RabbitHunt() { }

    // class variables
    private static Model model;
    private static MessageView message;
    private static View view;
    private static Controller controller;

    /**
     * Main class for starting a rabbit hunt; no parameters
     * are needed or used.
     */
    public static void main(String args[]) {
        model = new Model(22, 22);
        
        message = new MessageView(model);
        view = new View(model);
        controller = new Controller(model);
        
        JFrame frame = new JFrame("Rabbit Hunt");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contents = frame.getContentPane();
        contents.add(message, BorderLayout.NORTH);
        contents.add(view, BorderLayout.CENTER);
        contents.add(controller, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        
        Thread controllerThread = new Thread(controller);
        controllerThread.start();
    }
}
