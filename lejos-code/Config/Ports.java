package Config;

import lejos.nxt.SensorPort;
import lejos.nxt.MotorPort;

public class Ports {
    private Ports() {
    }

    // Sensor Ports
    public static final SensorPort LIGHT_SENSOR_LEFT = SensorPort.S4;
    public static final SensorPort LIGHT_SENSOR_RIGHT = SensorPort.S1;

    public static final SensorPort TOUCH_SENSOR = SensorPort.S2;

    // Motor Ports
    public static final MotorPort LIFT_MOTOR = MotorPort.A;
    
    public static final MotorPort MOTOR_LEFT = MotorPort.B;
    public static final MotorPort MOTOR_RIGHT = MotorPort.C;
}
