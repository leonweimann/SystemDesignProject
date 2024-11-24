package Controlling;

import Config.Ports;

import Utils.DynamicMotor;
import lejos.nxt.MotorPort;

/**
 * The MotorController class is responsible for controlling the left and right
 * motors of the robot.
 * It provides an interface to adjust motor speeds, enabling the robot to move
 * and steer.
 * The main functionality centers around moving the robot with a specified angle
 * and speed.
 * 
 * @author leonweimann
 * @version 2.0
 */
public class MotorController {
    private static MotorController instance;

    public static MotorController getInstance() {
        if (instance == null) {
            instance = new MotorController(Ports.MOTOR_LEFT, Ports.MOTOR_RIGHT, TouchController.getInstance());
        }
        return instance;
    }

    private DynamicMotor leftMotor;
    private DynamicMotor rightMotor;

    private TouchController touchController;

    private boolean motorsRunning = false;
    private boolean rotationInProgress = false;
    private int baseSpeed = 200; // Default base speed for the robot

    private static final double DISTANCE_PER_ROTATION = 9.42478; // cm per full wheel rotation
    private static final double ROBOT_TRACK_WIDTH = 13.0; // Distance between the wheels in cm

    /**
     * Constructs a new MotorController instance with the specified motor ports for
     * the left and right motors.
     * 
     * @param leftPort  The motor port to which the left motor is connected.
     * @param rightPort The motor port to which the right motor is connected.
     */
    public MotorController(MotorPort leftPort, MotorPort rightPort, TouchController touchController) {
        this.leftMotor = new DynamicMotor(leftPort);
        this.rightMotor = new DynamicMotor(rightPort);
        this.touchController = touchController;
    }

    /**
     * Sets the base speed for the robot's movement.
     * 
     * @param speed The base speed to be used for movement (in degrees per second).
     */
    public void setBaseSpeed(int speed) {
        this.baseSpeed = speed;
    }

    /**
     * Resets the base speed to the default value.
     * Also resets the current speed to ensure smooth acceleration from default.
     */
    public void resetSpeed() {
        this.baseSpeed = 200;
        // this.leftMotor.resetCurrentSpeed();
        // this.rightMotor.resetCurrentSpeed();
    }

    /**
     * Moves the robot by adjusting motor speeds based on the specified angle and
     * base speed.
     * A positive angle means turning right, and a negative angle means turning
     * left.
     * The method adjusts the motor speeds proportionally to make smoother turns.
     * 
     * @param angle The angle to steer the robot, where -100 represents a full left
     *              turn and 100 represents a full right turn.
     *              Values in between adjust the steering proportionally.
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
     * Stops both motors gradually.
     * The motors decelerate dynamically until they stop.
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
     * Stops both motors immediately, like an emergency brake.
     */
    public void hardStop() {
        leftMotor.hardStop();
        rightMotor.hardStop();
        motorsRunning = false;
        rotationInProgress = false;
    }

    /**
     * Rotates the robot by a specified angle.
     * A positive angle rotates to the right, and a negative angle rotates to the
     * left.
     * The method ensures no other motor actions are executed during the rotation.
     * The input angle is normalized to be within -360 to 360 degrees.
     * 
     * @param angle The angle to rotate the robot, which will be normalized to be
     *              within -360 to 360 degrees.
     *              Negative values indicate a left turn, positive values indicate a
     *              right turn.
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