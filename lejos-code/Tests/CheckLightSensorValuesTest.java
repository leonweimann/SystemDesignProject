package Tests;

import Config.Ports;
import Coordination.LCDHelper;
import Coordination.RuntimeCoordinator;

import java.util.LinkedList;
import lejos.nxt.LightSensor;

public class CheckLightSensorValuesTest extends Test {
    private RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();

    private LightSensor left = new LightSensor(Ports.LIGHT_SENSOR_LEFT);
    private LightSensor right = new LightSensor(Ports.LIGHT_SENSOR_RIGHT);
    private LightSensor center = new LightSensor(Ports.LIGHT_SENSOR_CENTER);

    private int steeringAngle = 0;
    private static final int STEERING_STEP = 20;

    private static final int FLUCTUATION_BUFFER = 10;
    private static final int MAX_AGE_MS = 3000;
    private LinkedList<LightSensorValue> centerValues = new LinkedList<>();

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

        LightSensorValue(int value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
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

        updateCenterValues(lightValues.center);

        // // Line following logic
        if (lightValues.left < lightValues.right && lightValues.left < lightValues.center) {
            LCDHelper.appendingToDisplay("Turn Left", false, 1);
            steeringAngle = Math.max(steeringAngle - STEERING_STEP, -100);
            runtime.motorController.setBaseSpeed(100);
        } else if (lightValues.right < lightValues.left && lightValues.right < lightValues.center) {
            LCDHelper.appendingToDisplay("Turn Right", false, 1);
            steeringAngle = Math.min(steeringAngle + STEERING_STEP, 100);
            runtime.motorController.setBaseSpeed(100);
        } else {
            LCDHelper.appendingToDisplay("Go Straight", false, 1);
            steeringAngle = 0;
            runtime.motorController.setBaseSpeed(200);
        }

        runtime.motorController.moveWithAngle(steeringAngle);

        LinePattern linePattern = analyzeCenterValues(lightValues);
        LCDHelper.appendingToDisplay("Line Pattern: " + linePattern, false, 3);

        return true;
    }

    private void updateCenterValues(int value) {
        long currentTime = System.currentTimeMillis();
        centerValues.add(new LightSensorValue(value, currentTime));

        // Remove old values
        for (LightSensorValue sensorValue : centerValues) {
            if (currentTime - sensorValue.timestamp > MAX_AGE_MS) {
                centerValues.remove(sensorValue);
            }
        }
    }

    private LinePattern analyzeCenterValues(LightValues lightValues) {
        int whiteCount = 0;
        int blackCount = 0;
        boolean inWhiteSection = false;
        boolean inBlackSection = false;

        for (LightSensorValue sensorValue : centerValues) {
            if (sensorValue.value > lightValues.left + FLUCTUATION_BUFFER && sensorValue.value > lightValues.right + FLUCTUATION_BUFFER) {
                whiteCount++;
                if (!inWhiteSection) {
                    inWhiteSection = true;
                    inBlackSection = false;
                }
            } else if (sensorValue.value < lightValues.left - FLUCTUATION_BUFFER && sensorValue.value < lightValues.right - FLUCTUATION_BUFFER) {
                blackCount++;
                if (!inBlackSection) {
                    inBlackSection = true;
                    inWhiteSection = false;
                }
            }
        }

        if (whiteCount > 10) {
            return LinePattern.LONG_WHITE_SECTION;
        } else if (blackCount > 2 && whiteCount > 0) {
            return LinePattern.SHORT_BLACK_SECTIONS;
        } else {
            return LinePattern.DEFAULT_LINE;
        }
    }
}
