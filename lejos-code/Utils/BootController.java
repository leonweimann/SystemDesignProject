package Utils;

import Exceptions.BootingException;
import Utils.BootController;
import lejos.nxt.Button;

public class BootController {
    public boolean hasBooted = false;
    protected BootingException bootingException;

    public BootController() {
    }

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

    private void throwBootException(BootingException e) throws BootingException {
        bootingException = e;
        throw e;
    }
}
