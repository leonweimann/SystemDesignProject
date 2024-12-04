package Coordination;

import lejos.nxt.Button;
import lejos.nxt.LCD;

/**
 * The UserInputHandler class provides utility methods for handling user input
 * through button presses. This class cannot be instantiated.
 * 
 * @author leonweimann
 * @version 1.2
 */
public final class UserInputHandler {
    /**
     * Prevents instantiation of the UserInputHandler class.
     */
    private UserInputHandler() {
        // Prevent instantiation
    }

    /**
     * Checks if the specified button is currently pressed.
     *
     * @param btn the button to check
     * @return true if the button is pressed, false otherwise
     */
    public static boolean isButtonPressed(Button btn) {
        return btn.isDown();
    }

    /**
     * Waits for the specified button to be pressed and then released before
     * continuing execution.
     *
     * @param btn the button to wait for
     */
    public static void awaitButtonPress(Button btn, String label) {
        LCDHelper.display("Press\n\n" + label + "\n\nto continue ...", true);
        // Wait for button press
        while (!isButtonPressed(btn)) {
            // Do nothing, just wait for the button to be pressed
        }
        // Wait for button release
        while (isButtonPressed(btn)) {
            // Do nothing, just wait for the button to be released
        }
        LCDHelper.display("Continuing ...", true);
    }

    /**
     * Waits for the user to either press any button to continue or hold the ESCAPE
     * button for 3 seconds to exit the program.
     */
    public static void awaitContinueOrExit() {
        do {
            if (isButtonPressed(Button.ESCAPE)) {
                LCDHelper.display("Press any button to continue or hold\nESCAPE\nfor 3 seconds to exit ...", true);
            }
        } while (checkForExitSimultaneously());
        LCD.clear();
    }

    public static boolean checkForExitSimultaneously() {
        if (Button.ESCAPE.isDown()) {
            double lastRemainingTimeRounded = 0.0;
            long startTime = System.currentTimeMillis();
            while (Button.ESCAPE.isDown()) {
                if (System.currentTimeMillis() - startTime >= 3000) {
                    return false;
                } else {
                    double remainingTime = 3 - (System.currentTimeMillis() - startTime) / 1000.0;
                    double remainingTimeRounded = Math.round(remainingTime * 10) / 10.0;

                    if (remainingTimeRounded != lastRemainingTimeRounded) {
                        LCDHelper.display("Keep holding ESCAPE for\n \n" + remainingTimeRounded + " seconds", true);
                        lastRemainingTimeRounded = remainingTimeRounded;
                    }
                }
            }
            LCD.clear();
        }

        return true;
    }
}
