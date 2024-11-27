package Tests;

import Coordination.RuntimeCoordinator;
import lejos.util.Delay;
import Controlling.MotorController;

/**
 * Test class to verify the driving capabilities of the motor controller.
 * 
 * @author leonweimann
 * @version 1.2
 */
public class DrivingCapabilityTest extends Test {
    private MotorController controller = RuntimeCoordinator.getInstance().motorController;

    @Override
    protected void setup() {
        // Nothing to do here
    }

    @Override
    protected boolean executionLoop() {
        attachMultiTesting();
        switch (currentTestCount) {
            case 0:
                controller.moveWithAngle(0);
                return true;
            case 1:
                controller.moveWithAngle(50);
                return true;
            case 2:
                controller.moveWithAngle(100);
                return true;
            case 3:
                controller.moveWithAngle(-50);
                return true;
            case 4:
                controller.rotate(360);
                System.out.println("Test completed.");
                Delay.msDelay(1000);
            default:
                return false;
        }
    }
}
