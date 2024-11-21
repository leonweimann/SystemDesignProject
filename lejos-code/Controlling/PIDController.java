package Controlling;

/**
 * The PIDController class implements a simple PID controller used to minimize the difference 
 * between a setpoint and an actual value.
 * 
 * The PID controller consists of three components:
 * - Proportional (P): Reacts proportionally to the current error.
 * - Integral (I): Accumulates past errors to correct long-term deviations.
 * - Derivative (D): Considers the rate of change of the error to minimize overshooting.
 * 
 * The PIDController can be used to precisely control robot movements, such as line following or speed control.
 * 
 * @author leonweimann
 * @version 1.0
 */
public class PIDController {

    /** The proportional gain factor. */
    private double kp;

    /** The integral gain factor. */
    private double ki;

    /** The derivative gain factor. */
    private double kd;

    private double integral = 0;
    private double previousError = 0;

    /**
     * Creates a new PID controller with the specified gain factors.
     * 
     * @param kp The proportional gain factor.
     * @param ki The integral gain factor.
     * @param kd The derivative gain factor.
     */
    public PIDController(double kp, double ki, double kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }

    /**
     * Calculates the control value based on the setpoint and the current actual value.
     * 
     * @param setpoint The desired setpoint.
     * @param actualValue The current actual value.
     * @return The calculated control value to minimize the error.
     */
    public double calculate(double setpoint, double actualValue) {
        double error = setpoint - actualValue;
        integral += error;
        double derivative = error - previousError;

        double output = kp * error + ki * integral + kd * derivative;
        previousError = error;

        return output;
    }
}
