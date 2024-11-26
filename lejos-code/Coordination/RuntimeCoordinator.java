package Coordination;

import Config.Ports;
import Controlling.*;

import lejos.nxt.Button;
import lejos.nxt.LCD;

/**
 * The {@code RuntimeCoordinator} class is responsible for managing the booting
 * and runtime coordination of a robot. It ensures that the robot goes through
 * necessary setup phases before starting and provides mechanisms to handle
 * user cancellation requests.
 * 
 * @author leonweimann
 * @version 1.8
 */
public class RuntimeCoordinator {
    /**
     * Singleton instance of the RuntimeCoordinator class.
     * This static variable holds the single instance of the RuntimeCoordinator
     * to ensure that only one instance of the class is created and used
     * throughout the application.
     */
    private static RuntimeCoordinator instance;

    /**
     * The TaskCoordinator instance responsible for managing and coordinating tasks.
     */
    private TaskCoordinator taskCoordinator;

    /**
     * The MotorController instance used to control the motors.
     */
    public MotorController motorController;

    /**
     * An instance of TouchController that handles touch sensor interactions.
     */
    public TouchController touchController;

    /**
     * The controller responsible for managing light fluctuations.
     */
    public LightFluctuationController lightController;

    /**
     * Returns the singleton instance of the RuntimeCoordinator.
     * This method is synchronized to ensure thread safety.
     * If the instance is null, it initializes a new RuntimeCoordinator.
     *
     * @return the singleton instance of RuntimeCoordinator
     */
    public static synchronized RuntimeCoordinator getInstance() {
        if (instance == null) {
            instance = new RuntimeCoordinator();
        }
        return instance;
    }

    /**
     * Constructs a new RuntimeCoordinator instance.
     * Initializes the TaskCoordinator, MotorController, TouchController, and
     * LightFluctuationController instances.
     */
    private RuntimeCoordinator() {
        taskCoordinator = new TaskCoordinator();

        motorController = new MotorController(Ports.MOTOR_LEFT, Ports.MOTOR_RIGHT);
        touchController = new TouchController(Ports.TOUCH_SENSOR);
        lightController = new LightFluctuationController(Ports.LIGHT_SENSOR_LEFT, Ports.LIGHT_SENSOR_RIGHT);
    }

    /**
     * Boots the robot by executing the setup phases and starting the runtime
     * coordination process.
     * The robot will wait for the user to press any button before starting.
     * 
     * @throws Exception if an error occurs during the booting process
     */
    public void boot() throws Exception {
        System.out.println("Starting robot...");

        // Setup phase 1
        setupPhase1();

        // Setup phase 2
        setupPhase2();

        // Setup phase 3
        setupPhase3();

        System.out.println("Ready for take off");
        System.out.println("Press any button to start");
        taskCoordinator.finishBooting();
        Button.waitForAnyPress(); // Wait for the user to press any button to start
        System.out.println("> Started");
    }

    private void setupPhase1() {
        System.out.println("Executing setup phase 1...");
        // Add setup phase 1 logic here

        System.out.println("Calibrating light sensors...");
        System.out.println("Place over black line and press Enter, then place over white surface and press Enter");
        lightController.calibrateSensors();
        System.out.println("Light sensors calibrated");
    }

    private void setupPhase2() {
        System.out.println("Executing setup phase 2...");
        // Add setup phase 2 logic here
    }

    private void setupPhase3() {
        System.out.println("Executing setup phase 3...");
        // Add setup phase 3 logic here
    }

    /**
     * Executes the runtime coordination process.
     * Continuously refreshes the task coordinator as long as the condition to run
     * is met.
     */
    public void execute() {
        while (shouldRun()) {
            taskCoordinator.refresh();
        }
    }

    /**
     * Checks if the robot should continue running.
     * The robot should continue running if no cancellation request has been made.
     * A cancellation request is made if any button is pressed or if the robot is
     * not in the booting phase.
     * 
     * @return {@code true} if the robot should continue running;
     *         {@code false} otherwise.
     */
    private boolean shouldRun() {
        displayCancelationMessage();
        return !isCancelationRequested();
    }

    /**
     * Displays a cancellation message on the LCD screen.
     * The message instructs the user to press any button to cancel.
     * Clears the LCD screen before displaying the message.
     */
    private void displayCancelationMessage() {
        LCD.clear();
        LCD.drawString("Press any button to cancel", 0, 0);
    }

    /**
     * Checks if a cancellation request has been made.
     * A cancellation request is made if any button is pressed or if the robot is
     * not in the booting phase.
     * 
     * @return {@code true} if a cancellation request has been made;
     *         {@code false} otherwise.
     */
    private boolean isCancelationRequested() {
        if (Button.readButtons() != 0) {
            System.out.println("Program stopped by user.");
            return true;
        }

        boolean isBooting = taskCoordinator.getPhase() != Phase.BOOTING;
        return !isBooting;
    }

    /**
     * Pauses the execution of the program until the Enter button on the brick is
     * pressed.
     * This can be used, for example, during sensor calibration.
     */
    public void waitForEnterPress() {
        System.out.println("Press Enter to continue...");
        while (Button.ENTER.isUp()) {
            // Wait until the Enter button is pressed
        }
        System.out.println("Continuing execution...");
    }
}
