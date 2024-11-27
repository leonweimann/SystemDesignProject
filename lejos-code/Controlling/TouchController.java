package Controlling;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class TouchController {
    private TouchSensor sensor;

    public TouchController(SensorPort port) {
        sensor = new TouchSensor(port);
    }

    public boolean obstacleFound() {
        return sensor.isPressed(); // TODO: Check with status verfolgung. d.h. er weis welche phase wir sind etc.
    }
}
