package Tests;

import Coordination.RuntimeCoordinator;
import Controlling.MotorController;

/**
 * Test class to verify the driving capabilities of the motor controller.
 * 
 * @author leonweimann
 * @version 1.1
 */
public class DrivingCapabilityTest extends Test {
    private MotorController controller = RuntimeCoordinator.getInstance().motorController;
    private byte currentTest = 0;

    @Override
    protected void setup() {
        // Nothing to do here
    }

    @Override
    protected boolean executionLoop() {
        attachMultiTesting();
        switch (currentTest) {
            case 0:
                controller.moveWithAngle(0);
                return true;
            case 1:
                controller.moveWithAngle(50);
                return true;
            case 2:
                controller.moveWithAngle(100);
                return true;
            case 4:
                controller.rotate(360);
            default:
                return false;
        }
    }
}
