package Controlling;

import Coordination.UserInputHandler;
import Models.Symbol;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

/**
 * The {@code LightFluctuationController} class is responsible for controlling
 * the light sensors and detecting symbols based on the readings from these
 * sensors. The class provides methods for calibrating the sensors, reading
 * light values, and creating symbols based on these values.
 * 
 * The class also contains a buffer for storing symbols and methods for
 * inserting, removing, and resetting symbols in the buffer. The buffer is
 * implemented as an array of boolean values, with each element representing a
 * symbol. The buffer has a fixed size, and new symbols are inserted at the
 * current index, which is then incremented and wrapped around if it exceeds the
 * buffer size.
 * 
 * @author leonweimann
 * @version 2.0
 */
public class LightFluctuationController {
    private LightSensor leftSensor;
    private LightSensor rightSensor;
    private LightSensor centerSensor;

    private final int FLUCTUATION_BUFFER = 5;

    // private Symbol[] symbolBuffer = new Symbol[SYMBOL_BUFFER_SIZE];
    // private int symbolIndex = 0; // TODO: Currently never resetted, instead
    // worked with modulo. Could lead to
    // // overflow...
    // private static final int SYMBOL_BUFFER_SIZE = 8; // TODO: Instead delete after e.g. 5000 ms -> timestamp

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

        // Place robot over black line
        UserInputHandler.awaitButtonPress(Button.ENTER, "ENTER", "Black calibration");
        leftSensor.calibrateLow();
        rightSensor.calibrateLow();
        centerSensor.calibrateLow();
    }

    /**
     * Reads the light values from the sensors and creates a symbol based on the
     * readings.
     * 
     * @return a symbol representing the current light readings
     */
    public Symbol readSymbol() {
        int leftReading = leftSensor.getLightValue();
        int rightReading = rightSensor.getLightValue();
        int centerReading = centerSensor.getLightValue();
        return new Symbol(leftReading, rightReading, centerReading);
    }

    public boolean shouldTurnLeft(Symbol symbol) {
        return symbol.left < symbol.right && symbol.left < symbol.center;
    }

    public boolean shouldTurnRight(Symbol symbol) {
        return symbol.right < symbol.left && symbol.right < symbol.center;
    }

    /**
     * Checks if the given symbol is most likely black or white based on its center,
     * left, and right values.
     *
     * @param checkingCoded A Boolean indicating which part of the symbol to check:
     *                      - null: check the center value.
     *                      - true: check the right value.
     *                      - false: check the left value.
     * @param symbol        The Symbol object containing the center, left, and right
     *                      values to compare.
     * @return A Boolean indicating the result:
     *         - true: the symbol is most likely black.
     *         - false: the symbol is most likely white.
     *         - null: unable to determine if the symbol is black or white.
     */
    public Boolean checkForBlack(Boolean checkingCoded, Symbol symbol) {
        int checking, compareMoreLeft, compareMoreRight;
        if (checkingCoded == null) {
            checking = symbol.center;
            compareMoreLeft = symbol.left;
            compareMoreRight = symbol.right;
        } else if (checkingCoded) {
            checking = symbol.right;
            compareMoreLeft = symbol.left;
            compareMoreRight = symbol.center;
        } else {
            checking = symbol.left;
            compareMoreLeft = symbol.center;
            compareMoreRight = symbol.right;
        }

        boolean mostLikelyBlack = checking < compareMoreLeft - FLUCTUATION_BUFFER
                && checking < compareMoreRight - FLUCTUATION_BUFFER;
        boolean mostLikelyWhite = checking > compareMoreLeft - FLUCTUATION_BUFFER
                && checking > compareMoreRight - FLUCTUATION_BUFFER;

        return mostLikelyBlack ? true : mostLikelyWhite ? false : null;
    }

    /**
     * Checks if the last symbol in the buffer matches the given color.
     *
     * @param isBlack a boolean indicating the color to check (true for black, false
     *                for white).
     * @return true if the last symbol in the buffer matches the given color, false
     *         otherwise.
     */
    // private boolean isAlreadyLastSymbol(Symbol symbol) {
    // int lastIndex = (symbolIndex - 1 + SYMBOL_BUFFER_SIZE) % SYMBOL_BUFFER_SIZE;
    // return symbolBuffer[lastIndex].equals(symbol);
    // }

    /**
     * Inserts a symbol into the symbol buffer.
     * 
     * @param isBlack A boolean indicating whether the symbol is black.
     *                If true, the symbol is black; otherwise, it is not.
     *                The symbol is stored in the buffer at the current index.
     *                The index is then incremented and wrapped around if it
     *                exceeds the buffer size.
     */
    // private void insertSymbol(Symbol symbol) {
    // symbolBuffer[symbolIndex] = symbol;
    // symbolIndex = (symbolIndex + 1) % SYMBOL_BUFFER_SIZE;
    // }

    /**
     * Removes the first symbol from the symbol buffer by shifting all elements
     * one position to the left. The last position in the buffer is set to a default
     * value (false).
     *
     * This method assumes that the symbol buffer is an array of boolean values
     * and that SYMBOL_BUFFER_SIZE is a constant representing the size of the
     * buffer.
     */
    // public void removeFirstSymbol() {
    // for (int i = 0; i < SYMBOL_BUFFER_SIZE - 1; i++) {
    // symbolBuffer[i] = symbolBuffer[i + 1];
    // }
    // symbolBuffer[SYMBOL_BUFFER_SIZE - 1] = new Symbol(false, false, false); // or
    // any default value
    // }

    /**
     * Resets the symbol buffer by setting all its elements to their default value.
     * This method iterates through the symbol buffer and assigns a default value
     * (in this case, false) to each element.
     */
    // public void resetSymbolBuffer() {
    // for (int i = 0; i < SYMBOL_BUFFER_SIZE; i++) {
    // symbolBuffer[i] = new Symbol(false, false, false); // or any default value
    // }
    // }

    /**
     * Updates the symbol buffer based on the provided boolean value indicating
     * whether the current symbol is black or not. If the symbol is already the
     * last one in the buffer, the method returns without making any changes.
     * Otherwise, it inserts the new symbol into the buffer. If the buffer size
     * exceeds the predefined limit, the first symbol in the buffer is removed.
     *
     * @param isBlack a boolean indicating whether the current symbol is black
     */
    // public void updateSymbolBuffer(Symbol symbol) {
    // if (isAlreadyLastSymbol(symbol))
    // return;

    // insertSymbol(symbol);

    // if (symbolIndex >= SYMBOL_BUFFER_SIZE)
    // removeFirstSymbol();
    // }

    /**
     * Retrieves the current symbol buffer.
     *
     * @return a boolean array representing the current symbol buffer.
     */
    // public Symbol[] currentSymbol() {
    // return symbolBuffer;
    // }

    // public Symbol createSymbol(int leftReading, int rightReading, int
    // centerReading) {
    // // Update thresholds using EMA
    // leftThreshold = ALPHA * leftReading + (1 - ALPHA) * leftThreshold;
    // rightThreshold = ALPHA * rightReading + (1 - ALPHA) * rightThreshold;
    // centerThreshold = ALPHA * centerReading + (1 - ALPHA) * centerThreshold;

    // // Determine black or white
    // boolean isLeftBlack = leftReading < leftThreshold;
    // boolean isRightBlack = rightReading < rightThreshold;
    // boolean isCenterBlack = centerReading < centerThreshold;

    // // Differential comparison
    // // int leftRightDifference = leftReading - rightReading;
    // // int centerLeftDifference = centerReading - leftReading;
    // // int centerRightDifference = centerReading - rightReading;

    // // if (Math.abs(leftRightDifference) > DIFFERENCE_THRESHOLD) {
    // // isLeftBlack = leftRightDifference < 0;
    // // isRightBlack = leftRightDifference > 0;
    // // }

    // // if (Math.abs(centerLeftDifference) > DIFFERENCE_THRESHOLD) {
    // // isCenterBlack = centerLeftDifference < 0;
    // // }

    // // if (Math.abs(centerRightDifference) > DIFFERENCE_THRESHOLD) {
    // // isCenterBlack = centerRightDifference < 0;
    // // }

    // return new Symbol(isLeftBlack, isRightBlack, isCenterBlack);
    // }
}