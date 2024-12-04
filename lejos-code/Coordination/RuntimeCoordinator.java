package Coordination;

import Config.Ports;
import Controlling.*;
import Coordination.LCDHelper.Alignment;
import lejos.util.Delay;

/**
 * The {@code RuntimeCoordinator} class is responsible for managing the booting
 * and runtime coordination of a robot. It ensures that the robot goes through
 * necessary setup phases before starting and provides mechanisms to handle
 * user cancellation requests.
 * 
 * @author leonweimann
 * @version 2.2
 */
public final class RuntimeCoordinator {
    /**
     * The frequency at which the runtime coordinator executes its tasks.
     * This value is specified in Hertz (Hz), indicating the number of times
     * the tasks are executed per second.
     * 
     * In this case, the tasks are executed {@code 5 times per second}.
     */
    private static final int EXCECUTION_FREQUENCY = 5;

    public TaskCoordinator taskCoordinator;
    public MotorController motorController;
    public TouchController touchController;
    public LightFluctuationController lightController;

    private static RuntimeCoordinator instance;

    /**
     * Returns the singleton instance of the {@code RuntimeCoordinator}.
     * If the instance is {@code null}, it initializes a new one.
     * 
     * @return the singleton instance of {@code RuntimeCoordinator}
     */
    public static RuntimeCoordinator getInstance() {
        if (instance == null) {
            instance = new RuntimeCoordinator();
        }
        return instance;
    }

    /**
     * Private constructor to initialize the controllers.
     */
    private RuntimeCoordinator() {
        taskCoordinator = new TaskCoordinator();
        motorController = new MotorController(Ports.MOTOR_LEFT, Ports.MOTOR_RIGHT);
        touchController = new TouchController(Ports.TOUCH_SENSOR);
        lightController = new LightFluctuationController(Ports.LIGHT_SENSOR_LEFT, Ports.LIGHT_SENSOR_RIGHT,
                Ports.LIGHT_SENSOR_CENTER);
    }

    /**
     * Connects the robot by booting it and starting the execution process.
     */
    public void connect() {
        boot();
        execute();
    }

    /**
     * Boots the robot by executing the necessary setup phases.
     */
    private void boot() {
        System.out.println("Starting robot...");

        // Setup phase 1
        setupPhase1();

        // Setup phase 2
        setupPhase2();

        // Setup phase 3
        setupPhase3();

        System.out.println("Ready for take off");
        UserInputHandler.awaitContinueOrExit();
        System.out.println("> Started");
    }

    /**
     * Executes the first setup phase, including the calibration of light sensors.
     */
    private void setupPhase1() {
        System.out.println("Executing setup phase 1...");
        // Add setup phase 1 logic here

        System.out.println("Calibrating light sensors...");
        lightController.calibrateSensors();
        System.out.println("Light sensors calibrated");
    }

    /**
     * Executes the second setup phase.
     */
    private void setupPhase2() {
        System.out.println("Executing setup phase 2...");
        // Add setup phase 2 logic here
    }

    /**
     * Executes the third setup phase.
     */
    private void setupPhase3() {
        System.out.println("Executing setup phase 3...");
        // Add setup phase 3 logic here
    }

    /**
     * Starts the execution loop, running frequent and crucial tasks.
     */
    private void execute() {
        long nextExecutionTime = 0;
        while (UserInputHandler.checkForExitSimultaneously()) {
            if (nextExecutionTime < System.currentTimeMillis()) {
                nextExecutionTime = (long) (System.currentTimeMillis() + getExecutionFrequencyDelay());
                executeFrequent();
            }

            Delay.msDelay(1000);

            executeCrutial();
        }

        LCDHelper.display("Execution stopped!", Alignment.CENTER);
        Delay.msDelay(1000);
    }

    /**
     * Executes tasks that need to run frequently.
     */
    private void executeFrequent() {
        // taskCoordinator.executeTasks();
        LCDHelper.display("Executing frequent tasks...", Alignment.CENTER);
    }

    /**
     * Executes crucial tasks that need to run continuously.
     */
    private void executeCrutial() {
        // Add crucial execution logic here
        LCDHelper.display("Executing crucial tasks...", Alignment.CENTER);
    }

    /**
     * Returns the delay between executions based on the execution frequency.
     * 
     * @return the delay in milliseconds
     */
    public static double getExecutionFrequencyDelay() {
        return 1000 / EXCECUTION_FREQUENCY;
    }
}
