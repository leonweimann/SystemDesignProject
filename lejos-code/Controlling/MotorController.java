package Controlling;

import Utils.DrivingMotor;

import lejos.nxt.MotorPort;
import lejos.util.Delay;

/**
 * The MotorController class manages the left and right motors of the robot.
 * It provides methods to control motor speeds, enabling movement and steering.
 * The class supports setting base speed, moving with a specified angle, and
 * rotating.
 * It also includes methods for gradual and immediate stops.
 * 
 * @author leonweimann
 * @version 2.3
 */
public class MotorController {
    private DrivingMotor leftMotor;
    private DrivingMotor rightMotor;

    /**
     * The default speed for the motor controller.
     * This value is used when no other speed is specified.
     */
    private static final int DEFAULT_SPEED = 500;

    /**
     * The distance each wheel travels per full rotation in centimeters.
     */
    private static final int WHEEL_DIAMETER = 3;

    private static final int ROBOT_WIDHT = 13;

    /**
     * Constructs a MotorController with specified motor ports for left and right
     * motors.
     * 
     * @param leftPort  The motor port for the left motor.
     * @param rightPort The motor port for the right motor.
     */
    public MotorController(MotorPort leftPort, MotorPort rightPort) {
        this.leftMotor = new DrivingMotor(leftPort);
        this.rightMotor = new DrivingMotor(rightPort);
    }

    /**
     * Checks if either of the motors is currently running.
     * 
     * @return true if either motor is running, false otherwise.
     */
    public boolean motorsRunning() {
        return leftMotor.isMoving() || rightMotor.isMoving();
    }

    /**
     * Sets the speed for both motors.
     * 
     * @param speed The speed to set for both motors.
     */
    public void setSpeeds(int speed) {
        leftMotor.setSpeed(speed);
        rightMotor.setSpeed(speed);
    }

    /**
     * Resets the speeds of both motors to the default speed.
     */
    public void resetSpeeds() {
        leftMotor.setSpeed(DEFAULT_SPEED);
        rightMotor.setSpeed(DEFAULT_SPEED);
    }

    /**
     * Moves the robot by adjusting motor speeds based on the specified angle.
     * Positive angle turns right, negative angle turns left.
     * 
     * @param angle The angle to steer the robot, from -100 (full left) to 100 (full
     *              right).
     */
    public void moveWithAngle(int angle) {
        int leftSpeed;
        int rightSpeed;

        // Clamp angle to be between -90 and 90
        angle = Math.max(-90, Math.min(90, angle));

        if (angle < 0) { // Turn left
            leftSpeed = (int) (DEFAULT_SPEED * (1.0 + angle / 100.0));
            rightSpeed = DEFAULT_SPEED;
        } else if (angle > 0) { // Turn right
            leftSpeed = DEFAULT_SPEED;
            rightSpeed = (int) (DEFAULT_SPEED * (1.0 - angle / 100.0));
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
     * Stops both motors immediately and resets their speeds.
     */
    public void stop() {
        leftMotor.stop();
        rightMotor.stop();
        resetSpeeds();
    }

    /**
     * Rotates the robot by a specified angle.
     *
     * @param angle the angle in degrees to rotate the robot. Positive values rotate
     *              clockwise, and negative values rotate counterclockwise. The
     *              angle is normalized to the range [0, 360).
     */
    public void rotate(int angle) {
        final int ROTATION_SPEED = 250; // Custom speed for rotation TODO: Adjust
        setSpeeds(ROTATION_SPEED);
        angle = angle % 360;

        double travelDistance = (Math.abs(angle) / 360.0) * ROBOT_WIDHT;
        double wheelSpeedPerRotation = ROTATION_SPEED / 360;
        long rotationTime = (long) ((travelDistance / WHEEL_DIAMETER) * wheelSpeedPerRotation);

        if (angle < 0) {
            leftMotor.backward();
            rightMotor.forward();
        } else {
            leftMotor.forward();
            rightMotor.backward();
        }

        Delay.msDelay(rotationTime);
        stop();
    }
}