/**
 * Package containing utility classes for robot operations.
 */
package Controlling;

import Controlling.BootController;
import Exceptions.BootingException;
import lejos.nxt.Button;

/**
 * The {@code BootController} class manages the booting process of the robot.
 * It performs the necessary setup phases and waits for user input to start the robot.
 */
public class BootController {
    /**
     * Indicates whether the robot has successfully booted.
     */
    public boolean hasBooted;

    /**
     * Stores any exception that occurs during the booting process.
     */
    public BootingException bootingException;

    /**
     * Constructs a new {@code BootController}.
     */
    public BootController() {
        hasBooted = false;
    }

    /**
     * Boots the robot by executing setup phases. If an issue occurs during the booting,
     * a {@link BootingException} is thrown.
     *
     * @throws BootingException if there is an issue during the booting process.
     */
    public void boot() throws BootingException {
        System.out.println("Start roboting .");
        // Setup phase 1

        // temporarily
        String check = "no";
        if ("Sensors are calibrated" == check) {
            throwBootException(new BootingException("Test"));
        }

        System.out.println("Start roboting ..");
        // Setup phase 2
        System.out.println("Start roboting ...");
        // Setup phase 3
        System.out.println("Ready for take off");
        System.out.println("Press any button to start");
        hasBooted = true;
        Button.waitForAnyPress(); // Wait for the user to press any button to start
        System.out.println("> Started");
    }

    /**
     * Throws a {@link BootingException} and updates the state of the boot controller.
     *
     * @param e the {@code BootingException} to be thrown.
     * @throws BootingException the provided booting exception.
     */
    private void throwBootException(BootingException e) throws BootingException {
        hasBooted = false;
        bootingException = e;
        throw e;
    }
}
