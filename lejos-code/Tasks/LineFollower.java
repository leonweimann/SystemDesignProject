package Tasks;

import Coordination.LCDHelper;
import Coordination.RuntimeCoordinator;
import Models.Symbol;

public class LineFollower extends Task {
    public LineFollower(RuntimeCoordinator runtime) {
        super(runtime);
    }

    private static final int SHARP_TURN_THRESHOLD = 50;
    private static final int STEERING_STEP = 40;
    private int steeringAngle = 0;

    @Override
    protected void main() {
        Symbol currentSymbol = currentSymbol();

        final int newAngle = newSteeringAngle(currentSymbol);
        if (Math.abs(newAngle) > 100) {
            runtime.motorController.stop();
            do {
                runtime.motorController.rotate(newAngle > 0 ? 90 : -90);
            } while (runtime.motorController.isRotating());
        } else {
            steeringAngle = newAngle;
            runtime.motorController.moveWithAngle(steeringAngle);
        }

        // LCDHelper.display("Symbol: \n" + currentSymbol.debugDescription());
        LCDHelper.appendingToDisplay("Symbol:\n" + currentSymbol.debugDescription(), false, 42);
    }

    @Override
    public void terminate() {
        super.terminate();
        runtime.motorController.stop();
    }

    private Symbol currentSymbol() {
        return runtime.lightController.readSymbol();
    }

    private int newSteeringAngle(Symbol symbol) {
        final int leftRightDifference = Math.abs(symbol.left - symbol.right);
        if (leftRightDifference > SHARP_TURN_THRESHOLD) {
            if (symbol.left < symbol.right) {
                return -360;
            } else {
                return 360;
            }
        } else if (runtime.lightController.shouldTurnLeft(symbol)) {
            return Math.max(-100, steeringAngle - STEERING_STEP);
        } else if (runtime.lightController.shouldTurnRight(symbol)) {
            return Math.min(100, steeringAngle + STEERING_STEP);
        } else if (runtime.lightController.noHugeDifference(symbol)) {
            return steeringAngle;
        } else {
            return Math.abs(steeringAngle) > 40 ? steeringAngle / 2 : 0;
        }
    }
}