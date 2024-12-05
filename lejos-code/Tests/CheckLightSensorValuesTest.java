package Tests;

import Config.Ports;
import Coordination.LCDHelper;
import Coordination.UserInputHandler;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;

public class CheckLightSensorValuesTest extends Test {
    LightSensor left = new LightSensor(Ports.LIGHT_SENSOR_LEFT);
    LightSensor right = new LightSensor(Ports.LIGHT_SENSOR_RIGHT);
    LightSensor center = new LightSensor(Ports.LIGHT_SENSOR_CENTER);

    NXTRegulatedMotor mLeft = new NXTRegulatedMotor(Ports.MOTOR_LEFT);
    NXTRegulatedMotor mRight = new NXTRegulatedMotor(Ports.MOTOR_RIGHT);

    @Override
    protected void setup() {
        mLeft.setSpeed(200);
        mRight.setSpeed(200);
        
        UserInputHandler.awaitButtonPress(Button.ENTER, "ENTER", "White calibration");

        left.calibrateHigh();
        right.calibrateHigh();
        center.calibrateHigh();

        UserInputHandler.awaitButtonPress(Button.ENTER, "ENTER", "Black calibration");

        left.calibrateLow();
        right.calibrateLow();
        center.calibrateLow();
    }

    @Override
    protected boolean executionLoop() {
        int leftReading = left.getLightValue();
        int rightReading = right.getLightValue();
        int centerReading = center.getLightValue();

        if (leftReading < rightReading && leftReading < centerReading) {
            LCDHelper.appendingToDisplay("Turn Left", false, 1);
            // steeringAngle = Math.min(steeringAngle + STEERING_STEP, 100);
            // steeringAngle = 100;

            mLeft.stop();
            mRight.backward();
        } else if (rightReading < leftReading && rightReading < centerReading) {
            LCDHelper.appendingToDisplay("Turn Right", false, 1);
            // steeringAngle = Math.max(steeringAngle - STEERING_STEP, -100);
            // steeringAngle = -100;

            mLeft.backward();
            mRight.stop();
        } else {
            LCDHelper.appendingToDisplay("Go Straight", false, 1);
            // steeringAngle = 0;
            // steeringDurationFactor = 0;

            mLeft.backward();
            mRight.backward();
        }

        // steeringDurationFactor += 1;

        // if (Math.abs(steeringAngle) == MAX_STEERING_ANGLE && steeringDurationFactor > 10) {
            
        // }

        // runtime.motorController.moveWithAngle(steeringAngle);

        // if (10 < Math.abs(leftReading - rightReading) && 10 < Math.abs(leftReading - centerReading) && 10 < Math.abs(rightReading - centerReading)) {
        //     LCDHelper.appendingToDisplay("Small differences", false, 3);
        // }

        LCDHelper.appendingToDisplay("L: " + leftReading + "\nR: " + rightReading + "\nC: " + centerReading, false, 2);
        return true;
    }
}
