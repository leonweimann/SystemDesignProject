package Tests;

import Config.Ports;
import Coordination.LCDHelper;
import Coordination.RuntimeCoordinator;
import Models.Symbol;
import lejos.nxt.LightSensor;

public class CheckLightSensorValuesTest extends Test {
    private RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();

    private LightSensor left = new LightSensor(Ports.LIGHT_SENSOR_LEFT);
    private LightSensor right = new LightSensor(Ports.LIGHT_SENSOR_RIGHT);
    private LightSensor center = new LightSensor(Ports.LIGHT_SENSOR_CENTER);

    private int steeringAngle = 0;
    private static final int STEERING_STEP = 20;

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

        Symbol lightValues = new Symbol(left.getLightValue(), right.getLightValue(), center.getLightValue());
        LCDHelper.appendingToDisplay(
                "L: " + lightValues.left + "\nR: " + lightValues.right + "\nC: " + lightValues.center, false, 2);

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

        runtime.motorController.moveWithAngle(steeringAngle);

        return true;
    }
}
