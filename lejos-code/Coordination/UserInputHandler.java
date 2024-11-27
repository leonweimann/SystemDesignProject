package Coordination;

import lejos.nxt.Button;

/**
 * The UserInputHandler class provides utility methods for handling user input
 * through button presses. This class cannot be instantiated.
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
     * Waits for the specified button to be pressed and then released before continuing execution.
     *
     * @param btn the button to wait for
     */
    public static void awaitButtonPress(Button btn) {
        System.out.println("Press " + btn.getId() + " to continue ...");
        // Wait for button press
        while (!isButtonPressed(btn)) {
            // Do nothing, just wait for the button to be pressed
        }
        // Wait for button release
        while (isButtonPressed(btn)) {
            // Do nothing, just wait for the button to be released
        }
        System.out.println("Continuing execution ...");
    }

    /**
     * Waits for the user to either press any button to continue or hold the ESCAPE
     * button for 3 seconds to exit the program.
     */
    public static void awaitContinueOrExit() {
        long escapePressStartTime = 0;
        while (true) {
            if (Button.ESCAPE.isDown()) {
                if (escapePressStartTime == 0) {
                    escapePressStartTime = System.currentTimeMillis();
                    System.out.println("Hold ESCAPE for 3 seconds to exit...");
                } else if (System.currentTimeMillis() - escapePressStartTime >= 3000) {
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            } else {
                escapePressStartTime = 0;
                if (Button.readButtons() != 0) {
                    System.out.println("Continuing execution...");
                    break;
                }
            }
        }
    }
}
