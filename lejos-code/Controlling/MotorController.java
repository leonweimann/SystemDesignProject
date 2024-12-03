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
     * The distance {@code (cm)} each wheel travels per full rotation in
     * centimeters.
     */
    private static final int WHEEL_DIAMETER = 3; // cm

    /**
     * The width of the robot in centimeters {@code (cm)}.
     * This constant is used to define the distance between the two wheels of the
     * robot.
     */
    private static final int ROBOT_WIDTH = 13; // cm

    private Long rotationEndTime;

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

    public void rotate(int angle) {
        if (rotationEndTime == null) {
            final int ROTATION_SPEED = 250; // [Degrees / Second] // Custom speed for rotation TODO: Adjust
            setSpeeds(ROTATION_SPEED);

            int normalizedAngle = Math.abs(angle) % 360; // Normalize angle to the range [0, 360)
            // Calculate the time needed to rotate the robot by the specified angle
            double travelDistance = (normalizedAngle / 360.0) * ROBOT_WIDTH; // cm
            int rotationTime = calculateRequiredRotationTime(travelDistance, ROTATION_SPEED);

            rotationEndTime = System.currentTimeMillis() + rotationTime;

            leftMotor.stop();
            rightMotor.stop();

            // Determine the direction of rotation based on the sign of the angle
            if (motorsRunning()) { // Don't change motors movement if they are already moving
            } else if (angle < 0) {
                leftMotor.backward();
                rightMotor.forward();
            } else {
                leftMotor.forward();
                rightMotor.backward();
            }
        } else if (rotationEndTime > System.currentTimeMillis()) {
            rotationEndTime = null;
            stop();
        }
    }

    public boolean isRotating() {
        return rotationEndTime != null;
    }

    /**
     * Calculates the required rotation time for the robot to travel a specified
     * distance at a given speed.
     *
     * @param distance The distance the robot needs to travel in
     *                 {@code centimeterss}.
     * @param speed    The speed at which the robot is traveling in
     *                 {@code degrees per second}.
     * @return The time in milliseconds required for the robot to travel the
     *         specified distance.
     */
    private int calculateRequiredRotationTime(double distance, double speed) {
        return (int) ((distance / WHEEL_DIAMETER) * rotationSpeed(speed) * 1000);
    }

    /**
     * Converts the rotational speed of the wheel from {@code degrees per second} to
     * {@code rotations per second}.
     *
     * @param speed The speed of the wheel in degrees per second.
     * @return The speed of the wheel in rotations per second.
     */
    private double rotationSpeed(double speed) {
        return speed / 360;
    }

    /**
     * Moves the robot backward for a predefined distance.
     * The method calculates the required rotation time based on the
     * BACK_OFF_DISTANCE and DEFAULT_SPEED, sets the motor speeds,
     * and then moves both motors forward for the calculated delay.
     * After the delay, the motors are stopped.
     */
    public void backOff() {
        final int BACK_OFF_DISTANCE = 5;
        final int delay = calculateRequiredRotationTime(BACK_OFF_DISTANCE, DEFAULT_SPEED);
        setSpeeds(DEFAULT_SPEED);

        leftMotor.forward();
        rightMotor.forward();

        Delay.msDelay(delay);
        stop();
    }
}
