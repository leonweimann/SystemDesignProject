package Utils;

import Controlling.PIDController;
import lejos.nxt.LightSensor;
import Config.Ports; // Importiere die Ports-Klasse
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author MarianMeißner
 * @version 2.0
 */
public class LightSensorAdapter {

    // Instance of the physical light sensor
    private LightSensor sensor;

    // Queue to store the last N light values for fluctuation analysis
    private Queue<Integer> lightValues;
    private static final int MAX_VALUES = 10;

    // PID controller for fluctuation analysis
    private PIDController pidController;

    /**
     * Constructor to initialize the light sensor adapter with a specific sensor
     * instance.
     *
     * @param sensor The light sensor to be used by this adapter.
     */
    public LightSensorAdapter(LightSensor sensor) {
        this.sensor = sensor;
        this.lightValues = new LinkedList<>();
        this.pidController = new PIDController(1.0, 0.0, 0.0); // Example PID values, adjust as needed
    }

    /**
     * Reads the current light value from the sensor and stores it for fluctuation
     * analysis.
     *
     * @return The current light intensity value.
     */
    public int getLightValue() {
        int currentValue = sensor.getLightValue();
        if (lightValues.size() >= MAX_VALUES) {
            lightValues.poll(); // Remove the oldest value if the queue is full
        }
        lightValues.add(currentValue); // Add the current value to the queue
        return currentValue;
    }

    /**
     * Calibrates the sensor to adjust for ambient light conditions.
     * This method should be called before starting to use the sensor for
     * measurements.
     */
    public void calibrateSensor() {
        int calibrationValue = sensor.getLightValue();
        System.out.println("Calibration value: " + calibrationValue);
    }

    /**
     * Checks if there is a significant fluctuation in the light value using a PID
     * controller.
     *
     * @param threshold The threshold value to determine significant fluctuation.
     * @return True if there is a significant change, otherwise false.
     */
    public boolean isLightFluctuating(int threshold) {
        if (lightValues.size() < 2) {
            return false; // Not enough data to determine fluctuations
        }

        // Get the last two values from the queue for fluctuation analysis
        int previousValue = lightValues.peek(); // Oldest value in the queue
        int currentValue = sensor.getLightValue(); // Current value from the sensor

        // Calculate the PID output based on the change in light values
        double pidOutput = pidController.calculate(previousValue, currentValue);

        // If the fluctuation exceeds the threshold, return true
        return Math.abs(pidOutput) > threshold;
    }

    /**
     * Resets the sensor to its default state.
     * This can be used to clear calibration settings or reset the state.
     */
    public void resetSensor() {
        lightValues.clear();
        System.out.println("Sensor reset.");
    }

    /**
     * Main method to demonstrate the functionality of the LightSensorAdapter.
     */
    public static void main(String[] args) {
        // Use the Ports class to get the sensor port
        LightSensor leftSensor = new LightSensor(Ports.LIGHT_SENSOR_LEFT); // Using the left sensor port from Ports class
        LightSensor rightSensor = new LightSensor(Ports.LIGHT_SENSOR_RIGHT); // Using the right sensor port from Ports class

        // Create instances of LightSensorAdapter for both sensors
        LightSensorAdapter leftAdapter = new LightSensorAdapter(leftSensor);
        LightSensorAdapter rightAdapter = new LightSensorAdapter(rightSensor);

        // Example usage of the adapter
        System.out.println("Left Sensor Calibration:");
        leftAdapter.calibrateSensor();

        System.out.println("Right Sensor Calibration:");
        rightAdapter.calibrateSensor();

        // Get light values for both sensors and print them
        System.out.println("Left Sensor Current Light Value: " + leftAdapter.getLightValue());
        System.out.println("Right Sensor Current Light Value: " + rightAdapter.getLightValue());

        // Check for light fluctuation (set a threshold value for testing)
        int threshold = 5; // Example threshold for light fluctuation
        if (leftAdapter.isLightFluctuating(threshold)) {
            System.out.println("Left sensor is fluctuating.");
        }
        if (rightAdapter.isLightFluctuating(threshold)) {
            System.out.println("Right sensor is fluctuating.");
        }

        // Reset the sensor after use
        leftAdapter.resetSensor();
        rightAdapter.resetSensor();
    }
}