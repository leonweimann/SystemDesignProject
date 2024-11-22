package Controlling;

import Config.Ports;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class TouchController {
    private static TouchController instance;

    public static TouchController getInstance() {
        if (instance == null) {
            instance = new TouchController(Ports.TOUCH_SENSOR);
        }
        return instance;
    }

    private TouchSensor sensor;

    public TouchController(SensorPort port) {
        sensor = new TouchSensor(port);
    }

    public boolean obstacleFound() {
        return sensor.isPressed(); // Check with status verfolgung. d.h. er weis welche phase wir sind etc.
    }
}
