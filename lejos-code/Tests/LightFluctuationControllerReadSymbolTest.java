package Tests;

import Controlling.LightFluctuationController;
import Coordination.LCDHelper;
import Coordination.RuntimeCoordinator;
import Coordination.UserInputHandler;
import Models.Symbol;
import lejos.nxt.Button;

public class LightFluctuationControllerReadSymbolTest extends Test {
    private LightFluctuationController controller = RuntimeCoordinator.getInstance().lightController;

    @Override
    protected void setup() {
        controller.calibrateSensors();
    }

    @Override
    protected boolean executionLoop() {
        int[][] testValues = {
                { 30, 40, 50 },
                { 60, 70, 80 },
                { 90, 100, 110 },
                { 120, 130, 140 },
                { 10, 20, 30 },
                { 15, 15, 15 },
                { 50, 50, 50 },
                { 45, 55, 50 }
        };

        boolean[][] expectedResults = {
                { true, false, false },
                { false, true, false },
                { false, false, true },
                { true, true, true },
                { true, false, false },
                { false, false, false },
                { false, false, false },
                { true, false, false }
        };

        for (int i = 0; i < testValues.length; i++) {
            StringBuilder result = new StringBuilder();

            Symbol symbol = controller.createSymbol(testValues[i][0], testValues[i][1], testValues[i][2]);
            boolean pass = symbol.isLeftBlack() == expectedResults[i][0] &&
                    symbol.isCenterBlack() == expectedResults[i][1] &&
                    symbol.isRightBlack() == expectedResults[i][2];

            result.append("Test ").append(i + 1).append(": ")
                    .append(pass ? "Pass" : "Fail")
                    .append(" (L: ").append(symbol.isLeftBlack())
                    .append(", C: ").append(symbol.isCenterBlack())
                    .append(", R: ").append(symbol.isRightBlack())
                    .append(")\n");

            LCDHelper.display(result.toString());
            UserInputHandler.awaitButtonPress(Button.ENTER, "ENTER", "Next test", false);
        }

        UserInputHandler.awaitButtonPress(Button.ESCAPE, "ESCAPE", "Exit test");

        return false;
        
    }
}
