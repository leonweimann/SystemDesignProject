package Controlling;

import Coordination.RuntimeCoordinator;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import javafx.util.Pair;

/**
 * The LightFluctuationController class is responsible for coordinating and
 * managing multiple light sensors.
 * It provides higher-level functionality for analyzing light data from multiple
 * sensors and determining if significant fluctuations are occurring.
 * This class uses instances of LightSensorAdapter to read and analyze data from
 * each sensor, and potentially aggregate data for comparison.
 * 
 * Responsibilities:
 * - Manage multiple LightSensorAdapter instances.
 * - Compare light values between different sensors.
 * - Detect synchronized or significant fluctuations in light data across
 * multiple sensors.
 * - Provide methods to aggregate and analyze light intensity data from multiple
 * sources.
 * 
 * @author leonweimann
 * @version 1.2
 */
public class LightFluctuationController {
    public LightFluctuationController(SensorPort leftPort, SensorPort rightPort) {
        this.leftSensor = new LightSensor(leftPort);
        this.rightSensor = new LightSensor(rightPort);
    }

    private LightSensor leftSensor;
    private LightSensor rightSensor;

    private double leftThreshold = 50; // Initial threshold value
    private double rightThreshold = 50; // Initial threshold value
    private static final double ALPHA = 0.1; // Smoothing factor for EMA
    private static final int DIFFERENCE_THRESHOLD = 10; // Threshold for differential comparison

    /**
     * Determines if the left and right light sensors are currently measuring black
     * color using an optimized method with dynamic thresholding and differential
     * comparison.
     *
     * @param threshold The threshold value to determine significant fluctuation.
     * @return a Pair of two Booleans, where the first Boolean indicates whether the
     *         left light sensor is currently measuring black color, and the second
     *         Boolean indicates whether the right light sensor is currently
     *         measuring black color.
     */
    public Pair<Boolean, Boolean> getIsBlack(int threshold) {
        int leftReading = leftSensor.getLightValue();
        int rightReading = rightSensor.getLightValue();

        // Update thresholds using EMA
        leftThreshold = ALPHA * leftReading + (1 - ALPHA) * leftThreshold;
        rightThreshold = ALPHA * rightReading + (1 - ALPHA) * rightThreshold;

        // Determine black or white
        boolean isLeftBlack = leftReading < leftThreshold;
        boolean isRightBlack = rightReading < rightThreshold;

        // Differential comparison
        int difference = leftReading - rightReading;
        if (Math.abs(difference) > DIFFERENCE_THRESHOLD) {
            isLeftBlack = difference < 0;
            isRightBlack = difference > 0;
        }

        return new Pair<>(isLeftBlack, isRightBlack);
    }

    /**
     * Calibrates the sensors to adjust for ambient light conditions.
     * This method should be called before starting to use the sensors for
     * measurements.
     */
    public void calibrateSensors() {
        RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();
        
        runtime.waitForEnterPress();

        // Assume robot is placed over white surface
        leftSensor.calibrateHigh();
        rightSensor.calibrateHigh();

        int whiteLeft = leftSensor.getLightValue();
        int whiteRight = rightSensor.getLightValue();

        // Place robot over black line
        // ... wait for user input ...
        runtime.waitForEnterPress();

        leftSensor.calibrateLow();
        rightSensor.calibrateLow();

        int blackLeft = leftSensor.getLightValue();
        int blackRight = rightSensor.getLightValue();

        // Initialize thresholds
        leftThreshold = (whiteLeft + blackLeft) / 2;
        rightThreshold = (whiteRight + blackRight) / 2;
    }
}