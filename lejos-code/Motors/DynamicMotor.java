package Motors;

import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;

// motor.getRotationSpeed() ?
// what is Tacho ?

/**
 * The DynamicMotor class represents a motor with dynamic speed adjustment
 * capabilities.
 * It manages acceleration and deceleration to ensure smooth movement.
 */
class DynamicMotor {
    private NXTRegulatedMotor motor;
    private int currentSpeed = 50; // Initial speed for gradual acceleration
    private int accelerationStep = 20; // Step size for acceleration

    /**
     * Constructs a new DynamicMotor instance.
     * 
     * @param port The motor port to which the motor is connected.
     */
    public DynamicMotor(MotorPort port) {
        this.motor = new NXTRegulatedMotor(port);
    }

    /**
     * Adjusts the speed of the motor dynamically to reach the target speed.
     * 
     * @param targetSpeed The target speed to reach (in degrees per second).
     */
    public void adjustSpeedDynamically(int targetSpeed) {
        if (currentSpeed < Math.abs(targetSpeed)) {
            currentSpeed = Math.min(currentSpeed + accelerationStep, Math.abs(targetSpeed));
        } else if (currentSpeed > Math.abs(targetSpeed)) {
            currentSpeed = Math.max(currentSpeed - accelerationStep, Math.abs(targetSpeed));
        }
        motor.setSpeed(currentSpeed);
    }

    /**
     * Immediately stops the motor.
     * Resets the current speed for next use.
     */
    public void hardStop() {
        motor.stop();
        currentSpeed = 0;
    }

    // /**
    // * Resets the current speed to the initial value for smooth acceleration.
    // */
    // public void resetCurrentSpeed() {
    // this.currentSpeed = 50;
    // }

    /**
     * Checks if the motor is stopped.
     * 
     * @return True if the current speed is zero, false otherwise.
     */
    public boolean isStopped() {
        return currentSpeed == 0;
    }

    /**
     * Checks if the motor is currently moving.
     * 
     * @return True if the motor is moving, false otherwise.
     */
    public boolean isMoving() {
        return motor.isMoving();
    }

    /**
     * Sets the motor to move backward.
     */
    public void setBackward() {
        motor.backward();
    }

    /**
     * Rotates the motor by a specified angle.
     * 
     * @param angle The angle by which to rotate the motor.
     */
    public void rotate(int angle) {
        motor.rotate(angle);
    }

    /**
     * Gets the underlying NXTRegulatedMotor instance.
     * 
     * @return The NXTRegulatedMotor instance.
     */
    public NXTRegulatedMotor getMotor() {
        return motor;
    }
}
