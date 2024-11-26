package Coordination;

import lejos.nxt.Button;

public final class UserInputHandler {
    private UserInputHandler() {
        // Prevent instantiation
    }

    public static boolean isButtonPressed(Button btn) {
        return btn.isDown();
    }

    public static void awaitButtonPress(Button btn) {
        System.out.println("Press any" + btn.getId() + " to continue ...");
        while (!isButtonPressed(btn)) {
            // Wait for button press
        }
        System.out.println("Continuing execution ...");
    }
}
