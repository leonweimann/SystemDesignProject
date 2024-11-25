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
 * <p>
 * Usage example:
 * 
 * <pre>
 * {@code
 * RuntimeCoordinator coordinator = new RuntimeCoordinator();
 * try {
 *     coordinator.boot();
 *     while (coordinator.shouldRun()) {
 *         // Start robot operations
 *     }
 * } catch (Exception e) {
 *     e.printStackTrace();
 * }
 * }
 * </pre>
 * </p>
 * 
 * <p>
 * Methods:
 * <ul>
 * <li>{@link #RuntimeCoordinator()}: Constructs a new
 * {@code RuntimeCoordinator}.</li>
 * <li>{@link #boot()}: Boots the robot by executing setup phases.</li>
 * <li>{@link #shouldRun()}: Checks if the robot should continue running or if a
 * cancellation is requested.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Private Methods:
 * <ul>
 * <li>{@link #setupPhase1()}: Executes the first setup phase.</li>
 * <li>{@link #setupPhase2()}: Executes the second setup phase.</li>
 * <li>{@link #setupPhase3()}: Executes the third setup phase.</li>
 * <li>{@link #displayCancelationMessage()}: Displays a cancellation message to
 * the user.</li>
 * <li>{@link #isCancelationRequested()}: Checks if a cancellation has been
 * requested by the user.</li>
 * </ul>
 * </p>
 * 
 * @see Exception
 * @see Button
 * @see LCD
 * 
 * @author leonweimann
 * @version 1.7
 */
public class RuntimeCoordinator {
    private static RuntimeCoordinator instance;

    private TaskCoordinator taskCoordinator;

    public MotorController motorController;
    public TouchController touchController;
    public LightFluctuationController lightController;

    public static synchronized RuntimeCoordinator getInstance() {
        if (instance == null) {
            instance = new RuntimeCoordinator();
        }
        return instance;
    }

    /** Constructs a new {@code RuntimeCoordinator} */
    private RuntimeCoordinator() {
        taskCoordinator = new TaskCoordinator();

        motorController = new MotorController(Ports.MOTOR_LEFT, Ports.MOTOR_RIGHT);
        touchController = new TouchController(Ports.TOUCH_SENSOR);
        lightController = new LightFluctuationController(Ports.LIGHT_SENSOR_LEFT, Ports.LIGHT_SENSOR_RIGHT);
    }

    /**
     * Boots the robot by executing setup phases. If an issue occurs during the
     * booting, a {@link Exception} is thrown.
     *
     * @throws Exception if there is an issue during the booting process.
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

    private void setupPhase1() throws Exception {
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

    public void execute() {
        while (shouldRun()) {
            taskCoordinator.refresh();
        }
    }

    /**
     * Determines whether the runtime coordinator should continue running.
     * 
     * This method displays a cancellation message and checks if a cancellation
     * request has been made.
     * 
     * @return {@code true} if the coordinator should continue running;
     *         {@code false} if a cancellation request has been made.
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
     * Checks if a cancellation has been requested by the user.
     * 
     * This method reads the button states to determine if any button has been
     * pressed.
     * If a button press is detected, it prints a message indicating that the
     * program
     * has been stopped by the user and returns true. Otherwise, it returns the
     * value
     * of the `hasBooted` variable.
     * 
     * @return true if a cancellation has been requested by the user or if the
     *         system
     *         has booted; false otherwise.
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
