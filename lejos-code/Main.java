import Exceptions.BootingException;
import lejos.nxt.Button;
import lejos.nxt.LCD;

/**
 * The Main class is responsible for initializing and running the robot program.
 * It includes the boot process and the main execution loop, which continues until
 * a user presses a button to stop the program.
 * 
 * @author leonweimann
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        try {
            boot();
            
            // Main execution loop
            while (run()) {
                // Main robot loop continues as long as run() returns true
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handles the boot sequence of the robot, displaying messages to indicate progress.
     * 
     * @throws BootingException if the boot process encounters an error.
     */
    private static void boot() throws BootingException {
        System.out.println("Start roboting .");
        // Setup phase 1
        System.out.println("Start roboting ..");
        // Setup phase 2
        System.out.println("Start roboting ...");
        // Setup phase 3
        System.out.println("Ready for take off");
        System.out.println("Press any button to start");
        Button.waitForAnyPress(); // Wait for the user to press any button to start
        System.out.println("> Started");
    }

    /**
     * The main run loop of the robot. This method displays instructions to the LCD
     * and checks if the user has pressed any button to cancel the program.
     * 
     * @return true if the loop should continue, false if the user has pressed a button.
     */
    private static boolean run() {
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
