import Coordination.RuntimeCoordinator;

/**
 * The Main class is responsible for initializing and running the robot program.
 * It includes the boot process and the main execution loop, which continues
 * until a user presses a button to stop the program.
 * 
 * @author leonweimann
 * @version 1.9
 */
public class Main {
    /**
     * The runtime coordinator responsible for managing the runtime environment.
     */
    private static RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();

    /**
     * The main method initializes the runtime environment and starts the program.
     * It catches any exceptions thrown during execution and prints the error
     * message to the console.
     * 
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        runtime.boot();
        runtime.execute();
    }
}
