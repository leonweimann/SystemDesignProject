package Exceptions;

/**
 * The BootingException class represents an exception that occurs during the boot process
 * of the robot. This exception is thrown if any error is encountered during initialization.
 * 
 * @author leonweimann
 * @version 1.0
 */
public class BootingException extends Exception {
    /**
     * Constructs a new BootingException with the specified detail message.
     * 
     * @param message The detail message that describes the cause of the exception.
     */
    public BootingException(String message) {
        super(message);
    }
}
