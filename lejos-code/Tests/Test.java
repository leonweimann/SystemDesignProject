package Tests;

import lejos.nxt.Button;

/**
 * The {@code Test} class provides a framework for running tests with a boot
 * sequence, logging, and a loop for execution. It includes methods for waiting
 * for user input to start the test and to either repeat the test or exit.
 * 
 * @author leonweimann
 * @version 1.1
 */
public abstract class Test {
    /**
     * Initializes the system by displaying a greeting and performing setup
     * operations.
     * Continuously executes the main loop until the executionLoop method returns
     * false.
     */
    public void boot() {
        showGreeting();
        setup();
        while (true) {
            if (!executionLoop()) {
                break;
            }
        }
    }

    /**
     * Displays a greeting message indicating the start of the test.
     * It prints the class name and prompts the user to press any button to start.
     */
    private void showGreeting() {
        String className = this.getClass().getSimpleName();
        System.out.println("Starting " + className + "...");
        System.out.println("Press any button to start the test.");
        Button.waitForAnyPress();
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
     * Waits for the user to press any button to continue testing or the ESCAPE
     * button to exit.
     * If the ESCAPE button is pressed, the program will print "Test finished." and
     * terminate.
     */
    protected void waitForNextTestOrExit() {
        System.out.println("Press any button to test again or ESCAPE to exit.");
        int button = Button.waitForAnyPress();
        if (button == Button.ID_ESCAPE) {
            System.out.println("Test finished.");
            System.exit(0);
        }
    }
}