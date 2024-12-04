package Coordination;

import Config.Ports;
import Controlling.*;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.Delay;

/**
 * The {@code RuntimeCoordinator} class is responsible for managing the booting
 * and runtime coordination of a robot. It ensures that the robot goes through
 * necessary setup phases before starting and provides mechanisms to handle
 * user cancellation requests.
 * 
 * @author leonweimann
 * @version 2.0
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

    public MotorController motorController;
    public TouchController touchController;
    public LightFluctuationController lightController;

    private static RuntimeCoordinator instance;

    public static RuntimeCoordinator getInstance() {
        if (instance == null) {
            instance = new RuntimeCoordinator();
        }
        return instance;
    }

    private RuntimeCoordinator() {
        motorController = new MotorController(Ports.MOTOR_LEFT, Ports.MOTOR_RIGHT);
        touchController = new TouchController(Ports.TOUCH_SENSOR);
        lightController = new LightFluctuationController(Ports.LIGHT_SENSOR_LEFT, Ports.LIGHT_SENSOR_RIGHT,
                Ports.LIGHT_SENSOR_CENTER);
    }

    public void connect() {
        boot();
        execute();
    }

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

    private void setupPhase1() {
        System.out.println("Executing setup phase 1...");
        // Add setup phase 1 logic here

        System.out.println("Calibrating light sensors...");
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

    private void execute() {
        long nextExecutionTime = -1;
        while (shouldRun()) {
            if (nextExecutionTime < System.currentTimeMillis()) {
                nextExecutionTime = (long) (System.currentTimeMillis() + getExecutionFrequencyDelay());
                execute();
            }

            executeCrutial();
        }

        LCD.clear();
        LCD.drawString("Execution stopped!", 0, 0);
        Delay.msDelay(1000);
    }

    private void executeCrutial() {
        // Add crucial execution logic here
    }

    private boolean shouldRun() {
        if (Button.ESCAPE.isDown()) {
            long startTime = System.currentTimeMillis();
            while (Button.ESCAPE.isDown()) {
                if (System.currentTimeMillis() - startTime >= 3000) {
                    return false;
                } else {
                    double remainingTime = 3 - (System.currentTimeMillis() - startTime) / 1000;
                    double remainingTimeRounded = Math.round(remainingTime * 10) / 10.0;
                    LCD.clear();
                    LCD.drawString("Keep holding ESCAPE for " + remainingTimeRounded + " seconds", 0, 0);
                }
            }
        }

        return true;
    }

    public static double getExecutionFrequencyDelay() {
        return 1000 / EXCECUTION_FREQUENCY;
    }
}
