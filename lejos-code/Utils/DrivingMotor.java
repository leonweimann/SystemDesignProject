package Utils;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;;

/**
 * The DrivingMotor class is a wrapper for the NXTRegulatedMotor class that represents the driving motor of the robot.
 */
public class DrivingMotor extends NXTRegulatedMotor {    
    /**
     * Constructs a new DrivingMotor instance with the specified motor port.
     *
     * @param motorPort the motor port to be used by this motor
     */
    public DrivingMotor(MotorPort motorPort) {
        super(motorPort);
    }
}
