package Controlling;

import Utils.DrivingMotor;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;

/**
 * MotorController class manages the left and right motors of the robot.
 * It provides methods to control motor speeds, enabling movement and steering.
 * The class supports setting base speed, moving with a specified angle, and
 * rotating.
 * It also includes methods for gradual and immediate stops.
 * 
 * @author leonweimann
 * @version 2.1
 */
public class MC {
    private DrivingMotor leftMotor;
    private DrivingMotor rightMotor;
    
    private boolean rotationInProgress = false;

    private static final int DEFAULT_SPEED = 500;

    /**
     * The distance each wheel travels per full rotation in centimeters.
     */
    private static final double DISTANCE_PER_ROTATION = 9.42478;

    /**
     * The distance between the wheels of the robot in centimeters.
     */
    private static final double ROBOT_TRACK_WIDTH = 13.0;

    /**
     * Constructs a MotorController with specified motor ports for left and right
     * motors.
     * 
     * @param leftPort  The motor port for the left motor.
     * @param rightPort The motor port for the right motor.
     */
    public MC(MotorPort leftPort, MotorPort rightPort) {
        this.leftMotor = new DrivingMotor(leftPort);
        this.rightMotor = new DrivingMotor(rightPort);
        resetConfig();
    }

    /**
     * Resets the base speed to the default value and ensures smooth acceleration.
     */
    public void resetConfig() {
        leftMotor.setSpeed(DEFAULT_SPEED);
        leftMotor.setAcceleration(6000);
        rightMotor.setSpeed(DEFAULT_SPEED);
        rightMotor.setAcceleration(6000);
    }

    /**
     * Moves the robot by adjusting motor speeds based on the specified angle.
     * Positive angle turns right, negative angle turns left.
     * 
     * @param angle The angle to steer the robot, from -100 (full left) to 100 (full
     *              right).
     */
    public void moveWithAngle(int angle) {
        if (rotationInProgress) {
            return; // Prevent other motor actions during rotation
        }

        int leftSpeed;
        int rightSpeed;

        // Clamp angle to be between -100 and 100
        angle = Math.max(-45, Math.min(45, angle));

        if (angle < 0) { // Turn left
            leftSpeed = (int) (DEFAULT_SPEED * (1.0 + angle / 45.0));
            rightSpeed = DEFAULT_SPEED;
        } else if (angle > 0) { // Turn right
            leftSpeed = DEFAULT_SPEED;
            rightSpeed = (int) (DEFAULT_SPEED * (1.0 - angle / 45.0));
        } else { // Move straight
            leftSpeed = DEFAULT_SPEED;
            rightSpeed = DEFAULT_SPEED;
        }

        leftMotor.setSpeed(leftSpeed);
        rightMotor.setSpeed(rightSpeed);

        if (!motorsRunning()) {
            leftMotor.backward();
            rightMotor.backward();
        }
    }

    /**
     * Stops both motors immediately, acting as an emergency brake.
     */
    public void stop() {
        leftMotor.stop();
        rightMotor.stop();
        rotationInProgress = false;
    }

    public boolean motorsRunning() {
        return leftMotor.isMoving() || rightMotor.isMoving();
    }

    /**
     * Rotates the robot by a specified angle.
     * Positive angle rotates right, negative angle rotates left.
     * Ensures no other motor actions during rotation.
     * 
     * @param angle The angle to rotate the robot, normalized to -360 to 360
     *              degrees.
     */
    public void rotate(int angle) {
        rotationInProgress = true;

        // Normalize the angle to be within -360 to 360 degrees
        angle = angle % 360;

        // Calculate the distance each wheel needs to travel to achieve the desired
        // rotation
        double turnCircumference = Math.PI * ROBOT_TRACK_WIDTH; // Circumference of the robot's turning circle
        double rotationDistance = (turnCircumference * angle) / 360.0; // Distance each wheel must travel
        int degreesToRotate = (int) ((rotationDistance / DISTANCE_PER_ROTATION) *
                360); // Convert distance to degrees

        // leftMotor.rotate(degreesToRotate);
        // rightMotor.rotate(-degreesToRotate);

        resetConfig();
        leftMotor.forward();
        rightMotor.backward();

        while (motorsRunning() && Button.ESCAPE.isUp()) {
        }

        stop();
        rotationInProgress = false;
    }
}