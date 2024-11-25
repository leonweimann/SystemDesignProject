package Tasks;

import Controlling.MotorController;
import Coordination.RuntimeCoordinator;

public class DirectionChanger implements Task {
    private MotorController motorController;

    public DirectionChanger() { // TODO: Here should be also code to place the robot correctly !!!
        motorController = RuntimeCoordinator.getInstance().motorController;
    }

    @Override
    public void run() {
        motorController.rotate(360);
    }
}
