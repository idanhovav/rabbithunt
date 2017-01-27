import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

/**
 * Displays the current hunt's field.
 * 
 * @author David Matuszek
 * @version October 20, 2001
 * 
 * @author Carl Burch
 * @version 2005-07-22
 */
public class View extends JPanel implements Observer {
    //
    // constants
    //
    /** The color of the grid lines. */
    private static final Color LINE_COLOR = Color.GRAY;

    /** The color of the rabbit. */
    private static final Color RABBIT_COLOR = new Color(0xAA, 0x88, 0);

    /** The color of the fox. */
    private static final Color FOX_COLOR = Color.RED;

    /** The color of a bush. */
    private static final Color BUSH_COLOR = new Color(0, 0xAA, 0);

    /** The color of the marker that a square is seen. */
    private static final Color SEEN_COLOR = new Color(0xDD, 0xDD, 0);

    //
    // instance variables
    //
    /** The model that we are viewing. */
    private Model model;

    /**
     * Constructs a view of a model's field.
     * 
     * @param model  the model that this view represents.
     */
    public View(Model model) {
        this.model = model;
        model.addObserver(this);
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);
    }

    /**
     * Paints the grid onto this component.
     * 
     * @param graphics  the <code>Graphics</code> object to paint with.
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        int width = getWidth();
        int height = getHeight();
        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();

        // draw grid
        graphics.setColor(LINE_COLOR);
        for(int col = 1; col < columnCount; col++) {
            int x = col * width / columnCount;
            graphics.drawLine(x, 0, x, height);
        }
        for(int row = 1; row < rowCount; row++) {
            int y = row * height / rowCount;
            graphics.drawLine(0, y, width, y);
        }

        // loop through the field array and draw the things in it
        int y = 0;
        for(int row = rowCount - 1; row >= 0; row--) {
            int nextY = (rowCount - row) * height / rowCount;
            int cellHeight = nextY - y;
            int x = 0;
            for(int col = 0; col < columnCount; col++) {
                int nextX = (col + 1) * width / columnCount;
                int cellWidth = nextX - x;
                int cellMinDim = Math.min(cellWidth, cellHeight);
                Entity entity = model.get(row, col);
                if(entity == null) {
                    if(model.isSeen(row, col)) { // draw seen marker
                        graphics.setColor(SEEN_COLOR);
                        fillOval(graphics, x, y, cellWidth, cellHeight, cellMinDim / 4);
                    }
                } else {
                    if(entity.isBush()) { // draw bush
                        graphics.setColor(BUSH_COLOR);
                        graphics.fillOval(x + cellWidth / 4, y + 1,
                                cellWidth / 2, cellHeight - 2);
                        graphics.fillOval(x + 1, y + 1 + cellHeight / 4,
                                cellWidth - 2, cellHeight / 2);
                    } else if(entity.isFox()) { // draw fox
                        graphics.setColor(FOX_COLOR);
                        fillOval(graphics, x, y, cellWidth, cellHeight, 3 * cellMinDim / 4);
                    } // save drawing rabbit until later
                }
                x = nextX;
            }
            y = nextY;
        }
        
        // draw the rabbit. We save this until later so that if the fox
        // and rabbit are in the same square, the rabbit is drawn on top
        Entity r = model.getRabbit();
        graphics.setColor(RABBIT_COLOR);
        int rx = r.getColumn() * width / columnCount;
        int ry = (rowCount - 1 - r.getRow()) * height / rowCount;
        int rwid = (r.getColumn() + 1) * width / columnCount - rx;
        int rht  = (rowCount - r.getRow()) * height / rowCount - ry;
        int rmin = Math.min(rwid, rht);
        fillOval(graphics, rx, ry, rwid, rht, rmin / 2);
    }
    
    /**
     * Helper method for centering a circle within a cell.
     * 
     * @param graphics  the <code>Graphics</code> object to draw with.
     * @param x  the x-coordinate of the cell's left edge.
     * @param y  the y-coordinate of the cell's top edge.
     * @param width  the width of the cell.
     * @param height  the height of the cell.
     * @param diam  the diameter of the circle to draw.
     */
    private void fillOval(Graphics graphics, int x, int y,
            int width, int height, int diam) {
        graphics.fillOval(x + (width - diam) / 2, y + (height - diam) / 2,
                diam, diam);
    }

    /**
     * Updates the view to reflect changes to the model.
     */
    public void update(Observable source, Object data) {
        repaint();
    }
}
