package Tests;

import Config.Ports;
import Coordination.LCDHelper;
import Coordination.RuntimeCoordinator;
// import Coordination.UserInputHandler;
import lejos.nxt.LightSensor;
// import lejos.nxt.NXTRegulatedMotor;

public class CheckLightSensorValuesTest extends Test {
    private RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();

    private LightSensor left = new LightSensor(Ports.LIGHT_SENSOR_LEFT);
    private LightSensor right = new LightSensor(Ports.LIGHT_SENSOR_RIGHT);
    private LightSensor center = new LightSensor(Ports.LIGHT_SENSOR_CENTER);

    // private NXTRegulatedMotor mLeft = new NXTRegulatedMotor(Ports.MOTOR_LEFT);
    // private NXTRegulatedMotor mRight = new NXTRegulatedMotor(Ports.MOTOR_RIGHT);

    private int steeringAngle = 0;
    private static final int STEERING_STEP = 20;

    @Override
    protected void setup() {
        // mLeft.setSpeed(200);
        // mRight.setSpeed(200);
        
        // UserInputHandler.awaitButtonPress(Button.ENTER, "ENTER", "White calibration");

        // left.calibrateHigh();
        // right.calibrateHigh();
        // center.calibrateHigh();

        // UserInputHandler.awaitButtonPress(Button.ENTER, "ENTER", "Black calibration");

        // left.calibrateLow();
        // right.calibrateLow();
        // center.calibrateLow();
    }

    @Override
    protected boolean executionLoop() {
        int leftReading = left.getLightValue();
        int rightReading = right.getLightValue();
        int centerReading = center.getLightValue();

        if (leftReading < rightReading && leftReading < centerReading) {
            LCDHelper.appendingToDisplay("Turn Left", false, 1);
            steeringAngle = Math.max(steeringAngle - STEERING_STEP, -100);
            runtime.motorController.setBaseSpeed(100);
        } else if (rightReading < leftReading && rightReading < centerReading) {
            LCDHelper.appendingToDisplay("Turn Right", false, 1);
            steeringAngle = Math.min(steeringAngle + STEERING_STEP, 100);
            runtime.motorController.setBaseSpeed(100);
        } else {
            LCDHelper.appendingToDisplay("Go Straight", false, 1);
            steeringAngle = 0;
            runtime.motorController.setBaseSpeed(500);
        }

        runtime.motorController.moveWithAngle(steeringAngle);

        LCDHelper.appendingToDisplay("L: " + leftReading + "\nR: " + rightReading + "\nC: " + centerReading, false, 2);
        return true;
    }
}
