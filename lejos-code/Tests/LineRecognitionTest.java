package Tests;

import Coordination.RuntimeCoordinator;
import Models.Symbol;
import lejos.nxt.LCD;
import Controlling.LightFluctuationController;

/**
 * LineRecognitionTest is a test class that extends the Test class.
 * It is designed to test the functionality of the LightFluctuationController
 * in recognizing lines using light sensors.
 * 
 * @author leonweimann
 * @version 1.4
 */
public class LineRecognitionTest extends Test {
    private LightFluctuationController controller = RuntimeCoordinator.getInstance().lightController;

    @Override
    protected void setup() {
        controller.calibrateSensors();
    }

    @Override
    protected boolean executionLoop() {
        Symbol symbol = controller.readSymbol();

        LCD.clear();
        LCD.drawString("L: " + symbol.isLeftBlack(), 0, 1);
        LCD.drawString("C: " + symbol.isCenterBlack(), 0, 2);
        LCD.drawString("R: " + symbol.isRightBlack(), 0, 3);

        return true; // Continue execution
    }
}