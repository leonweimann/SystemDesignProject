import Sensors.TouchSensor;
import lejos.nxt.Button;

public class Main {
    public static void main(String[] args) {
        System.out.println("ES GEHT!");
        Button.waitForAnyPress();
    
        TouchSensor sensor = new TouchSensor();
        sensor.touch();
    }
}