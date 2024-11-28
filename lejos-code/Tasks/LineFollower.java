package Tasks;

import Controlling.LightFluctuationController;
import Controlling.MotorController;
import Controlling.TouchController;
import Coordination.RuntimeCoordinator;
import Models.Direction;
import Models.Symbol;
import Tests.Test;

public class LineFollower extends Task {
    public LineFollower() {
        RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();
        lightFluctuationController = runtime.lightController;
        motorController = runtime.motorController;
        touchController = runtime.touchController;
    }

    private final int STEERING_STEP = 10;
    private final int SEARCH_ANGLE = 15;

    private LightFluctuationController lightFluctuationController;
    private MotorController motorController;
    private TouchController touchController;

    private int movingAngle = 0;

    private boolean foundLineIfNecessary = false;

    @Override
    public void run() {
        if (touchController.obstacleFound()) {
            handleObstacleDetedted();
            return;
        }

        Symbol current = currentSymbol();
        correctMovingAngle(current);
        motorController.moveWithAngle(movingAngle); // TODO: Add reversed parameter -> moveWithAngle(angle, reversed);
    }

    private Symbol currentSymbol() {
        return lightFluctuationController.readSymbol();
    }

    private void correctMovingAngle(Symbol current) {
        Direction direction = movingDirection(current);
        movingAngle = newMovingAngle(direction);
    }

    private Direction movingDirection(Symbol current) {
        if (current.isLeftBlack() && current.isRightBlack()) {
            // ?
            return Direction.BACK; // ???? -> eher zurücksetzen und suche?
        } else if (current.isLeftBlack()) { // right white -> move left
            return Direction.LEFT;
        } else if (current.isRightBlack()) { // left white -> move right
            return Direction.RIGHT;
        } else { // both white -> move straight
            return Direction.STRAIGHT;
        }
    }

    private int newMovingAngle(Direction direction) {
        switch (direction) {
            case STRAIGHT:
                return movingAngle / 2;
            case LEFT:
                return Math.max(movingAngle - STEERING_STEP, -100);
            case RIGHT:
                return Math.min(movingAngle + STEERING_STEP, 100);
            default:
                return 0;
        }
    }

    private void handleObstacleDetedted() {
        motorController.stop();
    }

    // TODO: After testing, make this method to a private
    public void searchLine() {
        foundLineIfNecessary = false;

        int iterations = 0;
        boolean searchLeft = false;
        while (!foundLineIfNecessary) {
            iterations++;
            // Ensure correct execution frequency to prevent excessive CPU usage
            RuntimeCoordinator.executionFrequencyDelay();

            if (Test.checkExitCondition()) {
                return;
            }

            if (touchController.obstacleFound()) {
                // TODO: Maybe better reverse last moves if an obstacle is found?
                motorController.backOff();
                iterations--; // Retry iteration cycle
                continue;
            }

            if (iterations > 2 && iterations % 2 == 0) {
                motorController.backOff(); // Move backward periodically to expand search area
            }

            // Dynamically expand search angle
            int angle = (searchLeft ? SEARCH_ANGLE : -SEARCH_ANGLE) * ((iterations % 2) + 1);

            motorController.rotateWith(angle, () -> checkSymbolForLine()); // Rotate and check for line

            // No line found, switch direction
            searchLeft = !searchLeft;

            // Abort search after 10 iterations
            if (iterations > 10) {
                motorController.stop();
                System.exit(0);
            }
        }
    }

    private void checkSymbolForLine() {
        Symbol s = currentSymbol();
        if (s.isLeftBlack() || s.isRightBlack()) {
            motorController.stop();
            foundLineIfNecessary = true; // stop searchLine while
        }
    }
}
