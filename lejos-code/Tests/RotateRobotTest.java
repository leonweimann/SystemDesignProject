package Tests;

import Controlling.MotorController;
import Coordination.RuntimeCoordinator;

public class RotateRobotTest extends Test {
    private MotorController controller = RuntimeCoordinator.getInstance().motorController;

    @Override
    protected void setup() {
    }

    @Override
    protected boolean executionLoop() {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 5000) {
            controller.rotateMotors(true);
        }

        return false;
    }
}
