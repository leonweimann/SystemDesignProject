package Coordination;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.Delay;

/**
 * The UserInputHandler class provides utility methods for handling user input
 * through button presses. This class cannot be instantiated.
 * 
 * @author leonweimann
 * @version 1.4
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
     * Waits for the specified button to be pressed.
     *
     * @param btn   the button to wait for
     * @param label the label to display while waiting
     */
    public static void awaitButtonPress(Button btn, String label) {
        awaitButtonPress(btn, label, null);
    }

    /**
     * Waits for a specific button to be pressed and then released.
     * Displays a message on the LCD screen instructing the user to press the button.
     * 
     * @param btn The button to wait for.
     * @param label The label to display on the LCD screen.
     * @param clearLCD If true, clears the LCD screen before displaying the message.
     * @param alignement The alignment of the message on the LCD screen.
     */
    public static void awaitButtonPress(Button btn, String label, String message) {
        LCDHelper.display("Press\n\n" + label + "\n\nto continue ..." + "\n\n" + message);
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
        long nextDialogTime = System.currentTimeMillis();
         while (true) {
            if (nextDialogTime < System.currentTimeMillis()) {
                nextDialogTime = System.currentTimeMillis() + 200;
                LCDHelper.display("Press\nENTER\nto continue or hold\nESCAPE\nfor 3 seconds to exit ...", true);
            }
            
            if (isButtonPressed(Button.ENTER)) {
                LCDHelper.display("Continuing ...", true);
                break;
            } else if (!checkForExitSimultaneously()) {
                LCDHelper.display("Exiting ...", true);
                Delay.msDelay(1000);
                System.exit(0);
            }
        }
    }

    /**
     * Checks if the ESCAPE button is held down for at least 3 seconds.
     * If the ESCAPE button is held down continuously for 3 seconds, the method
     * returns false.
     * Otherwise, it returns true.
     *
     * During the 3-second period, the remaining time is displayed on the LCD
     * screen.
     * The display is updated every 0.1 seconds to show the remaining time.
     *
     * @return false if the ESCAPE button is held down for 3 seconds, true
     *         otherwise.
     */
    public static boolean checkForExitSimultaneously() {
        if (isButtonPressed(Button.ESCAPE)) {
            double lastRemainingTimeRounded = 0.0;
            long startTime = System.currentTimeMillis();
            while (isButtonPressed(Button.ESCAPE)) {
                if (System.currentTimeMillis() - startTime >= 3000) {
                    LCD.clear();
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
