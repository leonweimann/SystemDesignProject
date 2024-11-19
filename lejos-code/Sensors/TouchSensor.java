package Sensors;

import lejos.nxt.Button;

public class TouchSensor {
    public void touch() {
        System.out.println("Touched");
        Button.waitForAnyPress();
    }
}
