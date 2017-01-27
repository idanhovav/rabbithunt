/**
 * Executes a number of "rabbit hunts", in which a fox moves around a field
 * containing bushes, looking for a rabbit, and prints out the percentage of
 * times that the rabbit escapes.
 * 
 * @author David Matuszek
 * @version October 24, 2001
 * 
 * @author Carl Burch
 * @version 2005-07-22
 */
public class Grader {
    static final int NUMBER_OF_TRIALS = 10000;

    /**
     * Main class for starting rabbit hunts and counting games.
     */
    public static void main(String args[]) {
        countGames();
        checkModel(new Model(15, 13));
        checkModel(new Model(25, 25));
    }

    /**
     * Runs NUMBER_OF_TRIALS rabbit hunts and prints out the results as a
     * percentage of times the rabbit escapes.
     */
    private static void countGames() {
        // compute base score as percent of rabbit escapes
        Model model = new Model(22, 22);
        int numberOfEscapes = 0;
        for(int i = 0; i < NUMBER_OF_TRIALS; i++) {
            model.reset();
            int steps = 0;
            while(!model.isGameOver()) {
                model.step();
            }
            if(!model.isRabbitCaught()) {
                numberOfEscapes++;
            }
        }
        double percent = 100.0 * numberOfEscapes / NUMBER_OF_TRIALS;
        int roundedPercent = (int) Math.round(percent);
        System.out.println("Rabbit escapes: " + numberOfEscapes
                + " times out of " + NUMBER_OF_TRIALS + ", or "
                + roundedPercent + "%");
    }

    /**
     * Verifies that the program works properly on a particular model, in
     * particular looking for assumptions about the model size.
     */
    private static void checkModel(Model model) {
        try {
            for(int i = 0; i < 10; i++) {
                model.reset();
                while(!model.isGameOver())
                    model.step();
            }
        } catch(Throwable e) {
            String size = "[" + model.getRowCount() + "x"
                    + model.getColumnCount() + "]";
            System.out.println("Error detected in use of constants. " + size);
            e.printStackTrace();
        }
    }
}
