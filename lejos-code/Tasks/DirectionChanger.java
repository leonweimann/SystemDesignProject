package Tasks;

import Controlling.MotorController;
import Coordination.RuntimeCoordinator;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class DirectionChanger extends Task {
    private MotorController motorController;

    public DirectionChanger() {
        RuntimeCoordinator runtime = RuntimeCoordinator.getInstance();
        motorController = runtime.motorController;
    }

    private int angle = 0;
    
    @Override
    public void run() { // TODO: Handle also positioning etc.
        LCD.clear();
        LCD.drawString("Angle: " + angle, 0, 0);
        
        if (Button.LEFT.isDown()) {
            angle -= 10;
        } else if (Button.RIGHT.isDown()) {
            angle += 10;
        }

        motorController.moveWithAngle(angle);
    }
}
