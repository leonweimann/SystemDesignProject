package Tests;

import Coordination.RuntimeCoordinator;
import Controlling.LightFluctuationController;
import Models.Symbol;

/**
 * LineRecognitionTest is a test class that extends the Test class.
 * It is designed to test the functionality of the LightFluctuationController
 * in recognizing lines using light sensors.
 * 
 * @author leonweimann
 * @version 1.2
 */
public class LineRecognitionTest extends Test {
    private LightFluctuationController controller = RuntimeCoordinator.getInstance().lightController;

    @Override
    protected void setup() {
        controller.calibrateSensors();
    }

    @Override
    protected boolean executionLoop() {
        Symbol isBlack = controller.getIsBlack();
        System.out.println("L: " + isBlack.isLeftBlack() + ", R: " + isBlack.isRightBlack());
        return true; // Continue execution
    }
}