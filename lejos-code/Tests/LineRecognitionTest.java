package Tests;

import Config.Ports;
import Controlling.LightFluctuationController;

import javafx.util.Pair;

/**
 * LineRecognitionTest is a test class that extends the Test class.
 * It is designed to test the functionality of the LightFluctuationController
 * in recognizing lines using light sensors.
 * 
 * @author leonweimann
 * @version 1.0
 */
public class LineRecognitionTest extends Test {
    public static void main(String[] args) {
        LineRecognitionTest test = new LineRecognitionTest();
        test.boot();
    }

    LightFluctuationController controller = new LightFluctuationController(Ports.LIGHT_SENSOR_LEFT, Ports.LIGHT_SENSOR_RIGHT);
    
    @Override
    protected void setup() {
        controller.calibrateSensors();
    }

    @Override
    protected boolean executionLoop() {
        Pair<Boolean, Boolean> isBlack = controller.getIsBlack(20);
        System.out.println("Left: " + isBlack.getKey() + ", Right: " + isBlack.getValue());
        return true; // Continue execution, exit only via ESCAPE button
    }
}