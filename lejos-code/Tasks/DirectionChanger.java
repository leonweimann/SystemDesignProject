package Tasks;

import Coordination.RuntimeCoordinator;
import Controlling.MotorController;

public class DirectionChanger implements Task {
    private MotorController motorController;

    public DirectionChanger() { // TODO: Here should be also code to place the robot correctly !!!
        RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();
        motorController = runtime.motorController;
    }

    @Override
    public void run() {
        motorController.rotate(360);
    }
}
