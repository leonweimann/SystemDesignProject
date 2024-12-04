package Controlling;

import Coordination.LCDHelper;
import Coordination.UserInputHandler;
import Models.Symbol;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;

/**
 * The LightFluctuationController class is responsible for controlling the light
 * sensors and detecting black lines on the ground. The class uses Exponential
 * Moving Average (EMA) to update the thresholds for black detection and performs
 * a differential comparison to enhance accuracy. The class also maintains a
 * symbol buffer to store the last few symbols detected by the sensors.
 * 
 * @author leonweimann
 * @version 1.6
 */
public class LightFluctuationController {
    private LightSensor leftSensor;
    private LightSensor rightSensor;
    private LightSensor centerSensor;

    private double leftThreshold = 50; // Initial threshold value
    private double rightThreshold = 50; // Initial threshold value
    private double centerThreshold = 50; // Initial threshold value
    private static final double ALPHA = 0.1; // Smoothing factor for EMA
    private static final int DIFFERENCE_THRESHOLD = 10; // Threshold for differential comparison

    private Symbol[] symbolBuffer = new Symbol[SYMBOL_BUFFER_SIZE];
    private int symbolIndex = 0; // TODO: Currently never resetted, instead worked with modulo. Could lead to
                                 // overflow...
    private static final int SYMBOL_BUFFER_SIZE = 8;

    public LightFluctuationController(SensorPort leftPort, SensorPort rightPort, SensorPort centerPort) {
        this.leftSensor = new LightSensor(leftPort);
        this.rightSensor = new LightSensor(rightPort);
        this.centerSensor = new LightSensor(centerPort);
    }

    /**
     * Calibrates the sensors to adjust for ambient light conditions.
     * This method should be called before starting to use the sensors for
     * measurements.
     */
    public void calibrateSensors() {
        UserInputHandler.awaitButtonPress(Button.ENTER, "ENTER", "White calibration");

        leftSensor.calibrateHigh();
        rightSensor.calibrateHigh();
        centerSensor.calibrateHigh();
        int whiteLeft = leftSensor.getLightValue();
        int whiteRight = rightSensor.getLightValue();
        int whiteCenter = centerSensor.getLightValue();

        // Place robot over black line
        UserInputHandler.awaitButtonPress(Button.ENTER, "ENTER", "Black calibration");

        leftSensor.calibrateLow();
        rightSensor.calibrateLow();
        centerSensor.calibrateLow();
        int blackLeft = leftSensor.getLightValue();
        int blackRight = rightSensor.getLightValue();
        int blackCenter = centerSensor.getLightValue();

        // Initialize thresholds
        leftThreshold = (whiteLeft + blackLeft) / 2;
        rightThreshold = (whiteRight + blackRight) / 2;
        centerThreshold = (whiteCenter + blackCenter) / 2;
    }

    /**
     * Determines whether the left, right, and center sensors detect a black
     * surface.
     * The method uses Exponential Moving Average (EMA) to update the thresholds
     * for black detection and performs a differential comparison to enhance
     * accuracy.
     *
     * @return A Symbol object indicating if the left, right, and center sensors
     *         detect black.
     */
    public Symbol readSymbol() {
        int leftReading = leftSensor.getLightValue();
        int rightReading = rightSensor.getLightValue();
        int centerReading = centerSensor.getLightValue();

        // Update thresholds using EMA
        leftThreshold = ALPHA * leftReading + (1 - ALPHA) * leftThreshold;
        rightThreshold = ALPHA * rightReading + (1 - ALPHA) * rightThreshold;
        centerThreshold = ALPHA * centerReading + (1 - ALPHA) * centerThreshold;

        // Determine black or white
        boolean isLeftBlack = leftReading < leftThreshold;
        boolean isRightBlack = rightReading < rightThreshold;
        boolean isCenterBlack = centerReading < centerThreshold;

        // Differential comparison
        int leftRightDifference = leftReading - rightReading;
        int centerLeftDifference = centerReading - leftReading;
        int centerRightDifference = centerReading - rightReading;

        if (Math.abs(leftRightDifference) > DIFFERENCE_THRESHOLD) {
            isLeftBlack = leftRightDifference < 0;
            isRightBlack = leftRightDifference > 0;
        }

        if (Math.abs(centerLeftDifference) > DIFFERENCE_THRESHOLD) {
            isCenterBlack = centerLeftDifference < 0;
        }

        if (Math.abs(centerRightDifference) > DIFFERENCE_THRESHOLD) {
            isCenterBlack = centerRightDifference < 0;
        }

        return new Symbol(isLeftBlack, isRightBlack, isCenterBlack);
    }

    /**
     * Checks if the last symbol in the buffer matches the given color.
     *
     * @param isBlack a boolean indicating the color to check (true for black, false
     *                for white).
     * @return true if the last symbol in the buffer matches the given color, false
     *         otherwise.
     */
    private boolean isAlreadyLastSymbol(Symbol symbol) {
        int lastIndex = (symbolIndex - 1 + SYMBOL_BUFFER_SIZE) % SYMBOL_BUFFER_SIZE;
        return symbolBuffer[lastIndex].equals(symbol);
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
    private void insertSymbol(Symbol symbol) {
        symbolBuffer[symbolIndex] = symbol;
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
        symbolBuffer[SYMBOL_BUFFER_SIZE - 1] = new Symbol(false, false, false); // or any default value
    }

    /**
     * Resets the symbol buffer by setting all its elements to their default value.
     * This method iterates through the symbol buffer and assigns a default value
     * (in this case, false) to each element.
     */
    public void resetSymbolBuffer() {
        for (int i = 0; i < SYMBOL_BUFFER_SIZE; i++) {
            symbolBuffer[i] = new Symbol(false, false, false); // or any default value
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
    public void updateSymbolBuffer(Symbol symbol) {
        if (isAlreadyLastSymbol(symbol))
            return;

        insertSymbol(symbol);

        if (symbolIndex >= SYMBOL_BUFFER_SIZE)
            removeFirstSymbol();
    }

    /**
     * Retrieves the current symbol buffer.
     *
     * @return a boolean array representing the current symbol buffer.
     */
    public Symbol[] currentSymbol() {
        return symbolBuffer;
    }
}