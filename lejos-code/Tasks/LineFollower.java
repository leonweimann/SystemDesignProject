package Tasks;

import Controlling.MotorController;
import Controlling.PIDController;
import Utils.LightSensorAdapter;
import Config.Ports; // Die Ports-Klasse importieren

/**
 * The LineFollower task handles the process of following a black line using
 * two light sensors and a PID controller to adjust the robot's movements.
 * The robot uses its left and right sensors to detect the line and adjust
 * its motors accordingly to stay on track.
 * @author MarianMeißner
 * @version 1.0
 */
public class LineFollower implements Task {

    private MotorController motorController;
    private LightSensorAdapter leftSensor;
    private LightSensorAdapter rightSensor;
    private PIDController pidController;
    
    private final double setpoint = 0.0;  // Ideal position (center of the line)

    public LineFollower() {
        this.motorController = MotorController.getInstance();
        // Verwenden der Ports-Klasse für die Sensoren
        this.leftSensor = new LightSensorAdapter(new lejos.nxt.LightSensor(Ports.LIGHT_SENSOR_LEFT));
        this.rightSensor = new LightSensorAdapter(new lejos.nxt.LightSensor(Ports.LIGHT_SENSOR_RIGHT));
        this.pidController = new PIDController(0.5, 0.1, 0.05);  // PID constants, can be adjusted
    }

    @Override
    public void run() {
        // Start the line following process
        while (true) {
            followLine();
        }
    }

    /**
     * Follow the line using the left and right light sensors.
     * Adjust the robot's motors using the PID controller to correct its path.
     */
    private void followLine() {
        // Read the light values from both sensors
        int leftLightValue = leftSensor.getLightValue();
        int rightLightValue = rightSensor.getLightValue();

        // Calculate the error (difference between left and right sensors)
        double error = leftLightValue - rightLightValue;

        // Use the PID controller to calculate the correction value
        double correction = pidController.calculate(setpoint, error);

        // Adjust motor speeds based on the correction
        adjustMotorSpeeds(correction);
    }

    /**
     * Adjust the motor speeds based on the PID correction.
     * The correction determines how much to turn the robot.
     * 
     * @param correction The correction value calculated by the PID controller.
     */
    private void adjustMotorSpeeds(double correction) {
        if (correction > 0) {
            // Turn left
            motorController.moveWithAngle(-30);  // Adjust this angle for smoother turns
        } else if (correction < 0) {
            // Turn right
            motorController.moveWithAngle(30);  // Adjust this angle for smoother turns
        } else {
            // Move straight
            motorController.moveWithAngle(0);
        }
    }
}