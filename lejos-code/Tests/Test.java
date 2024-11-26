package Tests;

import lejos.nxt.Button;

/**
 * The {@code Test} class provides a framework for running tests with a boot
 * sequence, logging, and a loop for execution. It includes methods for waiting
 * for user input to start the test and to either repeat the test or exit.
 * 
 * @author leonweimann
 * @version 1.0
 */
public abstract class Test {
    /**
     * Boots the system by printing starting logs, waiting for any button press,
     * and then entering the execution loop.
     */
    public void boot() {
        printStartingLogs();
        Button.waitForAnyPress();
        executionLoop();
    }

    /**
     * Prints the starting logs for the test.
     * This includes the name of the class and a prompt to press any button to start the test.
     */
    private void printStartingLogs() {
        String className = this.getClass().getSimpleName();
        System.out.println("Starting " + className + "...");
        System.out.println("Press any button to start the test.");
    }

    /**
     * This method represents the main execution loop for a specific task.
     * Subclasses must provide an implementation for this method to define
     * the behavior of the execution loop.
     */
    protected abstract void executionLoop();

    /**
     * Waits for the user to press any button to continue testing or the ESCAPE button to exit.
     * If the ESCAPE button is pressed, the program will print "Test finished." and terminate.
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