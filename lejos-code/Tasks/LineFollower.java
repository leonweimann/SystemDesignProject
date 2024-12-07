package Tasks;

import Coordination.LCDHelper;
import Coordination.RuntimeCoordinator;
import Models.Direction;
import Models.Symbol;

public class LineFollower extends Task {
    public LineFollower(RuntimeCoordinator runtime) {
        super(runtime);
    }

    private static final int STEERING_STEP = 10;

    private int steeringAngle = 0;
    private boolean onRoute = false;

    @Override
    public void main() {
        // Symbol currentSymbol = currentSymbol();
        // correctSteeringAngle(currentSymbol);
        // runtime.motorController.moveWithAngle(steeringAngle);
    }

    @Override
    public void terminate() {
        super.terminate();
        // runtime.motorController.stop();
    }

    // private Symbol currentSymbol() {
    //     return runtime.lightController.readSymbol();
    // }

    // private void correctSteeringAngle(Symbol current) {
    //     Direction direction = movingDirection(current);
    //     steeringAngle = newMovingAngle(direction);
        
    //     LCDHelper.appendingToDisplay(direction.toString(), false, 2);
    //     LCDHelper.appendingToDisplay("Steering Angle: " + steeringAngle, false, 3);
    // }

    // private Direction movingDirection(Symbol current) {
    //     if (current.isLeftBlack() && current.isRightBlack()) {
    //         // ?
    //         return Direction.BACK; // ???? -> eher zurücksetzen und suche?
    //     } else if (current.isLeftBlack()) { // right white -> move left
    //         return Direction.LEFT;
    //     } else if (current.isRightBlack()) { // left white -> move right
    //         return Direction.RIGHT;
    //     } else { // both white -> move straight
    //         return Direction.STRAIGHT;
    //     }
    // }

    // private int newMovingAngle(Direction direction) {
    //     switch (direction) {
    //         case STRAIGHT:
    //             return steeringAngle / 2;
    //         case LEFT:
    //             return Math.max(steeringAngle - STEERING_STEP, -100);
    //         case RIGHT:
    //             return Math.min(steeringAngle + STEERING_STEP, 100);
    //         default:
    //             return 0;
    //     }
    // }
}
