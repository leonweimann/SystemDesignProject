import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;

public final class Setup {
    public static void setup(LightSensor left, LightSensor right, LightSensor center) {
        long nextDialogTime = System.currentTimeMillis();
        while (true) {
            if (nextDialogTime < System.currentTimeMillis()) {
                nextDialogTime = System.currentTimeMillis() + 200;
                LCDHelper.display("Press\nENTER\nto start or hold\nESCAPE\nfor 3 seconds to calibrate ...", true);
            }

            if (Button.ENTER.isDown()) {
                while (Button.ENTER.isDown()) {
                }
                LCDHelper.display("Continuing ...", true);
                break;
            } else if (checkForCalibrationRequestSimultaneously()) {
                calibrateSensors(left, right, center);
                continue;
            }
        }
    }

    public static boolean checkForCalibrationRequestSimultaneously() {
        if (Button.ESCAPE.isDown()) {
            double lastRemainingTimeRounded = 0.0;
            long startTime = System.currentTimeMillis();
            while (Button.ESCAPE.isDown()) {
                if (System.currentTimeMillis() - startTime >= 3000) {
                    LCD.clear();
                    return true;
                } else {
                    double remainingTime = 3 - (System.currentTimeMillis() - startTime) / 1000.0;
                    double remainingTimeRounded = Math.round(remainingTime * 10) / 10.0;

                    if (remainingTimeRounded != lastRemainingTimeRounded) {
                        LCDHelper.display("Keep holding ESCAPE for\n \n" + remainingTimeRounded + " seconds", true);
                        lastRemainingTimeRounded = remainingTimeRounded;
                    }
                }
            }
            LCD.clear();
        }

        return false;
    }

    public static void calibrateSensors(LightSensor left, LightSensor right, LightSensor center) {
        UserInputHandler.awaitButtonPress(Button.ENTER, "ENTER", "White calibration");
        left.calibrateHigh();
        right.calibrateHigh();
        center.calibrateHigh();

        // Place robot over black line
        UserInputHandler.awaitButtonPress(Button.ENTER, "ENTER", "Black calibration");
        left.calibrateLow();
        right.calibrateLow();
        center.calibrateLow();
    }
}
