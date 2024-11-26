import Coordination.RuntimeCoordinator;

/**
 * The Main class is responsible for initializing and running the robot program.
 * It includes the boot process and the main execution loop, which continues
 * until a user presses a button to stop the program.
 * 
 * @author leonweimann
 * @version 1.7
 */
public class Main {
    /**
     * The runtime coordinator responsible for managing the runtime environment.
     */
    private static RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();

    /**
     * The main entry point for the application.
     * 
     * <p>
     * This method initializes the runtime environment and enters a loop where it
     * continuously executes tasks coordinated by the TaskCoordinator until the
     * runtime
     * signals to stop. If an exception occurs during execution, it is caught and
     * its
     * message is printed to the standard output.
     * </p>
     * 
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        try {
            runtime.boot();
            runtime.execute();
        } catch (Exception e) {
            // TODO: Add handler to RuntimeCoordinator -> Thrown Exceptions from excecution
            // must be handled as well there, so may rething integration of runtime
            // internally.
            System.out.println(e.getMessage());
        }
    }
}
