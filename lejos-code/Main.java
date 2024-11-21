import Controlling.*;
import Coordination.TaskCoordinator;
import lejos.nxt.Button;
import lejos.nxt.LCD;

/**
 * The Main class is responsible for initializing and running the robot program.
 * It includes the boot process and the main execution loop, which continues
 * until
 * a user presses a button to stop the program.
 * 
 * @author leonweimann
 * @version 1.4
 */
public class Main {
    private static BootController bootController = new BootController();

    public static void main(String[] args) {
        try {
            bootController.boot();

            // Main execution loop
            while (shouldRun()) {
                // Main robot loop continues as long as run() returns true
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * The main run loop of the robot. This method displays instructions to the LCD
     * and checks if the user has pressed any button to cancel the program.
     * 
     * @return true if the loop should continue, false if the user has pressed a
     *         button.
     */
    private static boolean shouldRun() {
        // Display message on the LCD screen
        LCD.clear();
        LCD.drawString("Press any button to cancel", 0, 0);

        // Check if any button is pressed to stop the code
        if (Button.readButtons() != 0) {
            System.out.println("Program stopped by user.");
            return false; // Stop the loop
        }

        // Simulate robot actions here
        return true; // Continue running if no button is pressed
    }
}
