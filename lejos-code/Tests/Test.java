package Tests;

import Coordination.RuntimeCoordinator;
import Coordination.UserInputHandler;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.Delay;

/**
 * The {@code Test} class provides a framework for running tests with a boot
 * sequence, logging, and a loop for execution. It includes methods for waiting
 * for user input to start the test and to either repeat the test or exit.
 * 
 * @author leonweimann
 * @version 1.4
 */
public abstract class Test {
    /**
     * The current count of tests that have been executed.
     */
    public int currentTestCount = 0;

    /**
     * Initializes the system by displaying a greeting and performing setup
     * operations.
     * Continuously executes the main loop until the executionLoop method returns
     * false.
     */
    public void boot() {
        showGreeting();
        setup();
        while (executionLoop()) {
            attachMultiTesting();
            if (checkExitCondition()) {
                break;
            }
            RuntimeCoordinator.executionFrequencyDelay();
        }
        System.out.println("Test completed.");
        Delay.msDelay(1000);
    }

    /**
     * Displays a greeting message indicating the start of the test.
     * It prints the class name and prompts the user to press any button to start.
     */
    private void showGreeting() {
        UserInputHandler.awaitContinueOrExit();
        displayCurrentTestCount();
    }

    /**
     * Sets up the necessary configurations or initializations required before the
     * main functionality is executed.
     * This method should be implemented by subclasses to provide specific setup
     * logic.
     */
    protected abstract void setup();

    /**
     * Executes the main loop of the implementation.
     * This method should contain the core logic that needs to be repeatedly
     * executed.
     * 
     * @return true if the loop should continue, false if it should terminate.
     */
    protected abstract boolean executionLoop();

    /**
     * Checks if the exit condition is met and waits for 5 seconds to confirm the
     * exit.
     * 
     * This method prints a message instructing the user to hold the left and right
     * buttons for 5 seconds to exit. It then waits for 5 seconds while continuously
     * checking the exit condition. If the exit condition is still met after 5
     * seconds, it prints an exit message and returns true, indicating that the exit
     * condition has been confirmed.
     * 
     * @return true if the exit condition is confirmed after 5 seconds, false
     *         otherwise.
     */
    private boolean checkExitCondition() {
        if (exitCondition()) {
            long startTime = System.currentTimeMillis();
            while (exitCondition()) {
                if (System.currentTimeMillis() - startTime > 5000) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if both the LEFT and RIGHT buttons are pressed down.
     *
     * @return true if both LEFT and RIGHT buttons are pressed, false otherwise.
     */
    private boolean exitCondition() {
        return Button.LEFT.isDown() && Button.RIGHT.isDown();
    }

    /**
     * Adjusts the current test count based on button presses.
     * If the LEFT button is pressed, the current test count is decremented.
     * If the RIGHT button is pressed, the current test count is incremented.
     * The updated test count is then displayed.
     */
    public void attachMultiTesting() {
        if (Button.LEFT.isDown()) {
            while (Button.LEFT.isDown()) {
                if (Button.RIGHT.isDown()) {
                    return;
                }
            }
            currentTestCount--;
            displayCurrentTestCount();
        } else if (Button.RIGHT.isDown()) {
            while (Button.RIGHT.isDown()) {
                if (Button.LEFT.isDown()) {
                    return;
                }
            }
            currentTestCount++;
            displayCurrentTestCount();
        }
    }

    /**
     * Displays the current test count on the LCD screen.
     * Clears the LCD screen before displaying the test count.
     */
    private void displayCurrentTestCount() {
        LCD.clear();
        LCD.drawString("Test count: " + currentTestCount, 0, 0);
    }
}