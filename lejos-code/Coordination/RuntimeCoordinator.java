package Coordination;

import Config.Ports;
import Controlling.*;
import java.util.StringTokenizer;

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
        long nextExecutionTime = 0;
        while (shouldRun()) {
            if (nextExecutionTime < System.currentTimeMillis()) {
                nextExecutionTime = (long) (System.currentTimeMillis() + getExecutionFrequencyDelay());
                executeFrequent();
            }

            executeCrutial();
        }

        displayOnLCD("Execution stopped!", true);
        Delay.msDelay(1000);
    }

    private void executeFrequent() {
        // Add frequent execution logic here
    }

    private void executeCrutial() {
        // Add crucial execution logic here
    }

    private boolean shouldRun() {
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
                        displayOnLCD("Keep holding ESCAPE for \n" + remainingTimeRounded + "\n seconds", true);
                        lastRemainingTimeRounded = remainingTimeRounded;
                    }
                }
            }
            LCD.clear();
        }

        return true;
    }

    public void displayOnLCD(String message) {
        displayOnLCD(message, false);
    }

    public void displayOnLCD(String message, boolean isCentered) {
        int width = LCD.DISPLAY_CHAR_WIDTH;
        int height = LCD.DISPLAY_CHAR_DEPTH;
        int lineCount = 0;

        LCD.clear();
        StringTokenizer tokenizer = new StringTokenizer(message, "\n");
        while (tokenizer.hasMoreTokens() && lineCount < height) {
            String msgLine = tokenizer.nextToken();
            while (msgLine.length() > 0 && lineCount < height) {
                int endIndex = Math.min(width, msgLine.length());
                String line = msgLine.substring(0, endIndex);

                // Check if the line ends in the middle of a word
                if (endIndex < msgLine.length() && msgLine.charAt(endIndex) != ' ' && msgLine.charAt(endIndex - 1) != ' ') {
                    int lastSpace = line.lastIndexOf(' ');
                    if (lastSpace != -1) {
                        line = line.substring(0, lastSpace);
                        endIndex = lastSpace + 1;
                    }
                }

                // Center the line if isCentered is true
                if (isCentered) {
                    int padding = (width - line.length()) / 2;
                    StringBuilder paddedLine = new StringBuilder();
                    for (int i = 0; i < padding; i++) {
                        paddedLine.append(' ');
                    }
                    paddedLine.append(line);
                    line = paddedLine.toString();
                }

                LCD.drawString(line, 0, lineCount);
                msgLine = msgLine.substring(endIndex).trim();
                lineCount++;
            }
        }
    }

    public static double getExecutionFrequencyDelay() {
        return 1000 / EXCECUTION_FREQUENCY;
    }
}
