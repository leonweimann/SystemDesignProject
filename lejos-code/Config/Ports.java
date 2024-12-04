package Config;

import lejos.nxt.SensorPort;
import lejos.nxt.MotorPort;

/**
 * The Ports class provides static references to all sensor and motor ports used in the robot.
 * This allows for centralized configuration of all hardware connections, improving maintainability
 * and reducing the likelihood of incorrect port assignments.
 * 
 * The class is designed as a utility class with a private constructor to prevent instantiation.
 * 
 * @author leonweimann
 * @version 1.1
 */
public final class Ports {
    // Private constructor to prevent instantiation
    private Ports() {
    }

    // Sensor Ports

    /**
     * Port for the left light sensor, used for line detection on the left side of the robot.
     */
    public static final SensorPort LIGHT_SENSOR_LEFT = SensorPort.S4;

    /**
     * Port for the right light sensor, used for line detection on the right side of the robot.
     */
    public static final SensorPort LIGHT_SENSOR_RIGHT = SensorPort.S1;

    /**
     * Port for the center light sensor, used for line detection in the center of the robot.
     */
    public static final SensorPort LIGHT_SENSOR_CENTER = SensorPort.S3;

    /**
     * Port for the touch sensor, used to detect physical contact or obstacles.
     */
    public static final SensorPort TOUCH_SENSOR = SensorPort.S2;

    // Motor Ports

    /**
     * Port for the lift motor, used to control the lifting mechanism for handling objects.
     */
    public static final MotorPort LIFT_MOTOR = MotorPort.A;

    /**
     * Port for the left motor, used to control the left wheel of the robot.
     */
    public static final MotorPort MOTOR_LEFT = MotorPort.B;

    /**
     * Port for the right motor, used to control the right wheel of the robot.
     */
    public static final MotorPort MOTOR_RIGHT = MotorPort.C;
}
