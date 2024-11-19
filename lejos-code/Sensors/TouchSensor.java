package Sensors;

import lejos.nxt.SensorPort;

/**
 * The TouchSensor class provides a wrapper around the leJOS TouchSensor to simplify usage.
 * It is used to detect whether the robot's touch sensor is pressed.
 * 
 * @author leonweimann
 * @version 1.0
 */
public class TouchSensor {
    private lejos.nxt.TouchSensor sensor;

    /**
     * Constructs a new TouchSensor using the specified sensor port.
     * 
     * @param port The sensor port to which the touch sensor is connected.
     */
    public TouchSensor(SensorPort port) {
        sensor = new lejos.nxt.TouchSensor(port);
    }

    /**
     * Checks if the touch sensor is pressed.
     * 
     * @return true if the sensor is pressed, false otherwise.
     */
    public boolean isPressed() {
        return sensor.isPressed();
    }
}
