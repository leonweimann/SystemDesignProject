import Coordination.RuntimeCoordinator;
import Coordination.TaskCoordinator;

/**
 * The Main class is responsible for initializing and running the robot program.
 * It includes the boot process and the main execution loop, which continues
 * until a user presses a button to stop the program.
 * 
 * @author leonweimann
 * @version 1.5
 */
public class Main {
    private static RuntimeCoordinator runtime = new RuntimeCoordinator();

    public static void main(String[] args) {
        try {
            runtime.boot();

            while (runtime.shouldRun()) {
                TaskCoordinator.execute();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
