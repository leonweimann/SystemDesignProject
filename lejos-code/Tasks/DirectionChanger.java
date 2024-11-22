package Tasks;

import Controlling.MotorController;

public class DirectionChanger implements Task {
    private MotorController motorController;

    public DirectionChanger() {
        motorController = MotorController.getInstance();
    }

    @Override
    public void run() {
        motorController.rotate(360);
    }
}
