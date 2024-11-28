package Tasks;

import Coordination.RuntimeCoordinator;
import Controlling.LightFluctuationController;
import Controlling.MotorController;
import Controlling.TouchController;
import Models.Symbol;

public class LineFollower extends Task {
    public LineFollower() {
        RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();
        lightFluctuationController = runtime.lightController;
        motorController = runtime.motorController;
        touchController = runtime.touchController;
    }

    private LightFluctuationController lightFluctuationController;
    private MotorController motorController;
    private TouchController touchController;

    private boolean isLeftSensorFocussing = true;

    private int movingAngle = 0;

    @Override
    public void run() {
        if (isFocusedBlack() && isHelperBlack()) {

        } else if (isFocusedBlack()) { // helper white
            motorController.moveWithAngle(movingAngle);
        } else if (isHelperBlack()) { // focused white
            refocus();
        } else { // both white
            // ignore for some time, then react, if no symbol could exist (max 6cm missing ...)
        }
    }
 
    private void switchSensorRole() {
        isLeftSensorFocussing = !isLeftSensorFocussing;
    }

    
    private boolean isFocusedBlack() {
        Symbol isBlack = lightFluctuationController.getIsBlack();
        return isLeftSensorFocussing ? isBlack.isLeftBlack() : isBlack.isRightBlack();
    }

    private boolean isHelperBlack() {
        Symbol isBlack = lightFluctuationController.getIsBlack();
        return isLeftSensorFocussing ? isBlack.isRightBlack() : isBlack.isLeftBlack();
    }

    private void refocus() { // When refocus -> switch sensor role
        motorController.moveWithAngle(isLeftSensorFocussing ? 100 : -100);
    }
}
