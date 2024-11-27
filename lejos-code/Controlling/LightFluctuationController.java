package Controlling;

import Coordination.UserInputHandler;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

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
 * @version 1.3
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

    private boolean[] symbolBuffer = new boolean[SYMBOL_BUFFER_SIZE];
    private int symbolIndex = 0; // TODO: Currently never resetted, instead worked with modulo. Could lead to
                                 // overflow...
    private static final int SYMBOL_BUFFER_SIZE = 8;

    /**
     * Calibrates the sensors to adjust for ambient light conditions.
     * This method should be called before starting to use the sensors for
     * measurements.
     */
    public void calibrateSensors() {
        System.out.println("Place both sensors over white surface ...");
        UserInputHandler.awaitButtonPress(Button.ENTER);

        leftSensor.calibrateHigh();
        rightSensor.calibrateHigh();
        int whiteLeft = leftSensor.getLightValue();
        int whiteRight = rightSensor.getLightValue();

        // Place robot over black line
        System.out.println("Place both sensors over black surface ...");
        UserInputHandler.awaitButtonPress(Button.ENTER);

        leftSensor.calibrateLow();
        rightSensor.calibrateLow();
        int blackLeft = leftSensor.getLightValue();
        int blackRight = rightSensor.getLightValue();

        // Initialize thresholds
        leftThreshold = (whiteLeft + blackLeft) / 2;
        rightThreshold = (whiteRight + blackRight) / 2;
    }

    /**
     * Determines whether the left and right sensors detect a black surface.
     * The method uses Exponential Moving Average (EMA) to update the thresholds
     * for black detection and performs a differential comparison to enhance
     * accuracy.
     *
     * @return A boolean array where the first element indicates if the left sensor
     *         detects black and the second element indicates if the right sensor
     *         detects black.
     */
    public boolean[] getIsBlack() {
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

        return new boolean[] { isLeftBlack, isRightBlack };
    }

    /**
     * Checks if the last symbol in the buffer matches the given color.
     *
     * @param isBlack a boolean indicating the color to check (true for black, false
     *                for white).
     * @return true if the last symbol in the buffer matches the given color, false
     *         otherwise.
     */
    private boolean isAlreadyLastSymbol(boolean isBlack) {
        int lastIndex = (symbolIndex - 1 + SYMBOL_BUFFER_SIZE) % SYMBOL_BUFFER_SIZE;
        return symbolBuffer[lastIndex] == isBlack;
    }

    /**
     * Inserts a symbol into the symbol buffer.
     * 
     * @param isBlack A boolean indicating whether the symbol is black.
     *                If true, the symbol is black; otherwise, it is not.
     *                The symbol is stored in the buffer at the current index.
     *                The index is then incremented and wrapped around if it
     *                exceeds the buffer size.
     */
    private void insertSymbol(boolean isBlack) {
        symbolBuffer[symbolIndex] = isBlack;
        symbolIndex = (symbolIndex + 1) % SYMBOL_BUFFER_SIZE;
    }

    /**
     * Removes the first symbol from the symbol buffer by shifting all elements
     * one position to the left. The last position in the buffer is set to a default
     * value (false).
     *
     * This method assumes that the symbol buffer is an array of boolean values
     * and that SYMBOL_BUFFER_SIZE is a constant representing the size of the
     * buffer.
     */
    public void removeFirstSymbol() {
        for (int i = 0; i < SYMBOL_BUFFER_SIZE - 1; i++) {
            symbolBuffer[i] = symbolBuffer[i + 1];
        }
        symbolBuffer[SYMBOL_BUFFER_SIZE - 1] = false; // or any default value
    }

    /**
     * Resets the symbol buffer by setting all its elements to their default value.
     * This method iterates through the symbol buffer and assigns a default value
     * (in this case, false) to each element.
     */
    public void resetSymbolBuffer() {
        for (int i = 0; i < SYMBOL_BUFFER_SIZE; i++) {
            symbolBuffer[i] = false; // or any default value
        }
    }

    /**
     * Updates the symbol buffer based on the provided boolean value indicating
     * whether the current symbol is black or not. If the symbol is already the
     * last one in the buffer, the method returns without making any changes.
     * Otherwise, it inserts the new symbol into the buffer. If the buffer size
     * exceeds the predefined limit, the first symbol in the buffer is removed.
     *
     * @param isBlack a boolean indicating whether the current symbol is black
     */
    public void updateSymbolBuffer(boolean isBlack) {
        if (isAlreadyLastSymbol(isBlack))
            return;

        insertSymbol(isBlack);

        if (symbolIndex >= SYMBOL_BUFFER_SIZE)
            removeFirstSymbol();
    }

    /**
     * Retrieves the current symbol buffer.
     *
     * @return a boolean array representing the current symbol buffer.
     */
    public boolean[] currentSymbol() {
        return symbolBuffer;
    }
}