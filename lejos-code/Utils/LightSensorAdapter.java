package Utils;

import Controlling.PIDController;

import lejos.nxt.LightSensor;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The LightSensorAdapter class is responsible for providing an interface to
 * interact with a light sensor.
 * It acts as an abstraction layer between the physical sensor and the
 * higher-level logic of your application.
 * This class is designed to facilitate communication with a light sensor,
 * offering methods for retrieving and analyzing light values.
 * 
 * Responsibilities:
 * - Initialize the light sensor.
 * - Read current light intensity values.
 * - Detect significant fluctuations in light intensity.
 * - Provide calibration methods for accurate light detection.
 * 
 * @author leonweimann
 * @version 1.0
 */
public class LightSensorAdapter {

    // Instance of the physical light sensor
    private LightSensor sensor;

    // TODO: USEFULL OR BETTER APPROACH?
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
    public int getLightValue() { // TODO: USEFULL?
        int currentValue = sensor.getLightValue();
        if (lightValues.size() >= MAX_VALUES) {
            lightValues.poll();
        }
        lightValues.add(currentValue);
        return currentValue;
    }

    /**
     * Calibrates the sensor to adjust for ambient light conditions.
     * This method should be called before starting to use the sensor for
     * measurements.
     */
    public void calibrateSensor() {
        // TODO: Implement calibration logic for the sensor.
    }

    /**
     * Checks if there is a significant fluctuation in the light value using a PID
     * controller.
     * 
     * @param threshold The threshold value to determine significant fluctuation.
     * @return True if there is a significant change, otherwise false.
     */
    public boolean isLightFluctuating(int threshold) { // USEFULL OR BETTER APPROACH?
        if (lightValues.size() < 2) {
            return false; // Not enough data to determine fluctuations
        }

        int previousValue = lightValues.peek();
        int currentValue = sensor.getLightValue();
        double pidOutput = pidController.calculate(previousValue, currentValue);

        return Math.abs(pidOutput) > threshold;
    }

    /**
     * Resets the sensor to its default state.
     * This can be used to clear calibration settings or reset the state.
     */
    public void resetSensor() { // USEFULL OR BETTER APPROACH?
        lightValues.clear();
        // TODO: Implement additional logic to reset the sensor if needed.
    }
}