package Tests;

import Config.Ports;
import Coordination.LCDHelper;
import Coordination.RuntimeCoordinator;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;

public class CheckLightSensorValuesTest extends Test {
    private RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();

    private LightSensor left = new LightSensor(Ports.LIGHT_SENSOR_LEFT);
    private LightSensor right = new LightSensor(Ports.LIGHT_SENSOR_RIGHT);
    private LightSensor center = new LightSensor(Ports.LIGHT_SENSOR_CENTER);

    private int steeringAngle = 0;
    private static final int STEERING_STEP = 20;

    private static final int FLUCTUATION_BUFFER = 5;
    private static final int MAX_AGE_MS = 5000;
    private LightSensorValue[] centerValues = new LightSensorValue[getMaxCenterValueSize()];
    private int centerValuesIndex = 0;

    private class LightValues {
        int left;
        int right;
        int center;

        LightValues(int left, int right, int center) {
            this.left = left;
            this.right = right;
            this.center = center;
        }
    }

    private class LightSensorValue {
        int value;
        long timestamp;
        Boolean isBlack;

        LightSensorValue(int value, long timestamp, Boolean isBlack) {
            this.value = value;
            this.timestamp = timestamp;
            this.isBlack = isBlack;
        }
    }

    private enum LinePattern {
        DEFAULT_LINE,
        LONG_WHITE_SECTION,
        SHORT_BLACK_SECTIONS,
        UNKNOWN
    }

    @Override
    protected void setup() {
        // mLeft.setSpeed(200);
        // mRight.setSpeed(200);

        // UserInputHandler.awaitButtonPress(Button.ENTER, "ENTER", "White
        // calibration");

        // left.calibrateHigh();
        // right.calibrateHigh();
        // center.calibrateHigh();

        // UserInputHandler.awaitButtonPress(Button.ENTER, "ENTER", "Black
        // calibration");

        // left.calibrateLow();
        // right.calibrateLow();
        // center.calibrateLow();
    }

    @Override
    protected boolean executionLoop() {
        if (runtime.touchController.obstacleFound()) {
            LCDHelper.appendingToDisplay("Obstacle detected", false, 1);
            runtime.motorController.stop();
            runtime.motorController.backOff();
            return true;
        }

        LightValues lightValues = new LightValues(left.getLightValue(), right.getLightValue(), center.getLightValue());
        LCDHelper.appendingToDisplay(
                "L: " + lightValues.left + "\nR: " + lightValues.right + "\nC: " + lightValues.center, false, 2);

        updateCenterValues(lightValues);

        // // Line following logic
        if (lightValues.left < lightValues.right && lightValues.left < lightValues.center) {
            LCDHelper.appendingToDisplay("Turn Left", false, 1);
            steeringAngle = Math.max(steeringAngle - STEERING_STEP, -100);
        } else if (lightValues.right < lightValues.left && lightValues.right < lightValues.center) {
            LCDHelper.appendingToDisplay("Turn Right", false, 1);
            steeringAngle = Math.min(steeringAngle + STEERING_STEP, 100);
        } else {
            LCDHelper.appendingToDisplay("Go Straight", false, 1);
            steeringAngle = 0;
        }

        // runtime.motorController.moveWithAngle(steeringAngle);

        LinePattern linePattern = analyzeCenterValues();
        LCDHelper.appendingToDisplay("Line Pattern: " + linePattern, false, 3);

        return true;
    }

    private void updateCenterValues(LightValues values) {
        long currentTime = System.currentTimeMillis();
        centerValues[centerValuesIndex] = new LightSensorValue(values.center, currentTime, couldBeBlack(null, values));
        centerValuesIndex = (centerValuesIndex + 1) % getMaxCenterValueSize();

        // Remove old values
        for (int i = 0; i < getMaxCenterValueSize(); i++) {
            if (centerValues[i] != null && currentTime - centerValues[i].timestamp > MAX_AGE_MS) {
                centerValues[i] = null;
            }
        }
    }

    private static int getMaxCenterValueSize() {
        return MAX_AGE_MS / 1000 * RuntimeCoordinator.EXCECUTION_FREQUENCY;
    }

    private int getCenterValuesSize() {
        int size = 0;
        for (LightSensorValue value : centerValues) {
            if (value != null) {
                size++;
            }
        }
        return size;
    }

    private LinePattern analyzeCenterValues() {
        if (centerValues[getCenterValuesSize()] != null) {
            LCDHelper.appendingToDisplay("Black? " + centerValues[getCenterValuesSize()].isBlack, false,
                    currentTestCount);
        } else {
            LCDHelper.appendingToDisplay("Black? null", false, currentTestCount);
        }

        return LinePattern.DEFAULT_LINE;
    }

    /**
     * 
     * @param checkingCoded null -> center; false -> left; true -> right
     * @param values
     * @return null -> unsure; true -> black; false -> white
     */
    private Boolean couldBeBlack(Boolean checkingCoded, LightValues values) {
        int val, com1, com2;
        if (checkingCoded == null) {
            val = values.center;
            com1 = values.left;
            com2 = values.right;
        } else if (checkingCoded) {
            val = values.right;
            com1 = values.left;
            com2 = values.center;
        } else {
            val = values.left;
            com1 = values.center;
            com2 = values.right;
        }

        if (val < com1 - FLUCTUATION_BUFFER && val < com2 - FLUCTUATION_BUFFER) {
            return true;
        } else if (val > com1 - FLUCTUATION_BUFFER && val > com2 - FLUCTUATION_BUFFER) {
            return false;
        }

        return null;
    }
}
