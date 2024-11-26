package Controlling;

import Coordination.RuntimeCoordinator;

import Utils.DynamicMotor;
import lejos.nxt.MotorPort;

/**
 * MotorController class manages the left and right motors of the robot.
 * It provides methods to control motor speeds, enabling movement and steering.
 * The class supports setting base speed, moving with a specified angle, and rotating.
 * It also includes methods for gradual and immediate stops.
 * 
 * @author leonweimann
 * @version 2.1
 */
public class MotorController {
    private DynamicMotor leftMotor;
    private DynamicMotor rightMotor;
    private TouchController touchController;
    private boolean motorsRunning = false;
    private boolean rotationInProgress = false;
    private int baseSpeed = 200; // Default base speed for the robot

    /**
     * The distance each wheel travels per full rotation in centimeters.
     */
    private static final double DISTANCE_PER_ROTATION = 9.42478;

    /**
     * The distance between the wheels of the robot in centimeters.
     */
    private static final double ROBOT_TRACK_WIDTH = 13.0;

    /**
     * Constructs a MotorController with specified motor ports for left and right motors.
     * 
     * @param leftPort  The motor port for the left motor.
     * @param rightPort The motor port for the right motor.
     */
    public MotorController(MotorPort leftPort, MotorPort rightPort) {
        this.leftMotor = new DynamicMotor(leftPort);
        this.rightMotor = new DynamicMotor(rightPort);
        this.touchController = RuntimeCoordinator.getInstance().touchController;
    }

    /**
     * Sets the base speed for the robot's movement.
     * 
     * @param speed The base speed in degrees per second.
     */
    public void setBaseSpeed(int speed) {
        this.baseSpeed = speed;
    }

    /**
     * Resets the base speed to the default value and ensures smooth acceleration.
     */
    public void resetSpeed() {
        this.baseSpeed = 200;
        // this.leftMotor.resetCurrentSpeed();
        // this.rightMotor.resetCurrentSpeed();
    }

    /**
     * Moves the robot by adjusting motor speeds based on the specified angle.
     * Positive angle turns right, negative angle turns left.
     * 
     * @param angle The angle to steer the robot, from -100 (full left) to 100 (full right).
     */
    public void moveWithAngle(int angle) {
        if (rotationInProgress) {
            return; // Prevent other motor actions during rotation
        }

        int leftSpeed;
        int rightSpeed;

        // Clamp angle to be between -100 and 100
        angle = Math.max(-100, Math.min(100, angle));

        if (angle < 0) { // Turn left
            leftSpeed = (int) (baseSpeed * (1.0 + angle / 100.0));
            rightSpeed = baseSpeed;
        } else if (angle > 0) { // Turn right
            leftSpeed = baseSpeed;
            rightSpeed = (int) (baseSpeed * (1.0 - angle / 100.0));
        } else { // Move straight
            leftSpeed = baseSpeed;
            rightSpeed = baseSpeed;
        }

        leftMotor.adjustSpeedDynamically(leftSpeed);
        rightMotor.adjustSpeedDynamically(rightSpeed);

        if (!motorsRunning) {
            leftMotor.setBackward();
            rightMotor.setBackward();
            motorsRunning = true;
        }
    }

    /**
     * Stops both motors gradually by decelerating dynamically until they stop.
     */
    public void stopGradually() {
        leftMotor.adjustSpeedDynamically(0);
        rightMotor.adjustSpeedDynamically(0);

        if (leftMotor.isStopped() && rightMotor.isStopped()) {
            motorsRunning = false;
            rotationInProgress = false;
        }
    }

    /**
     * Stops both motors immediately, acting as an emergency brake.
     */
    public void hardStop() {
        leftMotor.hardStop();
        rightMotor.hardStop();
        motorsRunning = false;
        rotationInProgress = false;
    }

    /**
     * Rotates the robot by a specified angle.
     * Positive angle rotates right, negative angle rotates left.
     * Ensures no other motor actions during rotation.
     * 
     * @param angle The angle to rotate the robot, normalized to -360 to 360 degrees.
     */
    public void rotate(int angle) {
        rotationInProgress = true;

        // Normalize the angle to be within -360 to 360 degrees
        angle = angle % 360;

        // Calculate the distance each wheel needs to travel to achieve the desired
        // rotation
        double turnCircumference = Math.PI * ROBOT_TRACK_WIDTH; // Circumference of the robot's turning circle
        double rotationDistance = (turnCircumference * Math.abs(angle)) / 360.0; // Distance each wheel must travel
        int degreesToRotate = (int) ((rotationDistance / DISTANCE_PER_ROTATION) * 360); // Convert distance to degrees

        leftMotor.adjustSpeedDynamically(baseSpeed);
        rightMotor.adjustSpeedDynamically(baseSpeed);

        if (angle < 0) { // Rotate left
            leftMotor.rotate(-degreesToRotate);
            rightMotor.rotate(degreesToRotate);
        } else if (angle > 0) { // Rotate right
            leftMotor.rotate(degreesToRotate);
            rightMotor.rotate(-degreesToRotate);
        }

        while (leftMotor.isMoving() || rightMotor.isMoving()) {
            // Check if the touch sensor detects an obstacle
            if (touchController.obstacleFound()) {
                hardStop();
                break; // Stop rotation if an obstacle is detected
            }
        }

        rotationInProgress = false;
    }
}