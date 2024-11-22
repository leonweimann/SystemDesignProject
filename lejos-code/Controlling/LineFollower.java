import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;
import lejos.util.Delay;

public class LineFollower {

    privat static LightSensor leftSensor = new LightSensor()import lejos.nxt.Button;
    import lejos.nxt.LCD;
    import lejos.nxt.Motor;
    import lejos.nxt.SensorPort;
    import lejos.nxt.LightSensor;
    import lejos.util.Delay;
    
    public class LineFollower {
    
        private static LightSensor leftSensor = new LightSensor(SensorPort.S4);
        private static LightSensor rightSensor = new LightSensor(SensorPort.S1);
    
        private static final int MOTOR_SPEED = 300;
        private static final int TURN_SPEED = 150;
        private static final int SEARCH_SPEED = 200;
    
        private static int leftBaseValue, rightBaseValue;
    
        public static void main(String[] args) {
    
            leftSensor.setFloodlight(true);
            rightSensor.setFloodlight(true);
    
            LCD.clear();
            LCD.drawString("Press any button", 0, 0);
            Button.waitForAnyPress();
    
            initializeLightValues();
    
            while (!Button.ESCAPE.isPressed()) {
                int leftValue = leftSensor.getLightValue();
                int rightValue = rightSensor.getLightValue();
    
                LCD.clear();
                LCD.drawString("Left: " + leftValue, 0, 0);
                LCD.drawString("Right: " + rightValue, 0, 1);
    
                int leftDiff = Math.abs(leftValue - leftBaseValue);
                int rightDiff = Math.abs(rightValue - rightBaseValue);
    
                if (leftDiff > 15 || rightDiff > 15) {
                    if (leftValue < leftBaseValue && rightValue < rightBaseValue) {
                        Motor.B.setSpeed(MOTOR_SPEED);
                        Motor.C.setSpeed(MOTOR_SPEED);
                        Motor.B.forward();
                        Motor.C.forward();
                    } else if (leftValue < leftBaseValue) {
                        Motor.B.setSpeed(MOTOR_SPEED);
                        Motor.C.setSpeed(TURN_SPEED);
                        Motor.B.forward();
                        Motor.C.forward();
                    } else if (rightValue < rightBaseValue) {
                        Motor.B.setSpeed(TURN_SPEED);
                        Motor.C.setSpeed(MOTOR_SPEED);
                        Motor.B.forward();
                        Motor.C.forward();
                    }
                } else {
                    Motor.B.setSpeed(SEARCH_SPEED);
                    Motor.C.setSpeed(SEARCH_SPEED);
                    Motor.B.forward();
                    Motor.C.backward();
    
                    while (leftValue >= leftBaseValue && rightValue >= rightBaseValue) {
                        leftValue = leftSensor.getLightValue();
                        rightValue = rightSensor.getLightValue();
                        Delay.msDelay(100);
                    }
    
                    Motor.B.setSpeed(MOTOR_SPEED);
                    Motor.C.setSpeed(MOTOR_SPEED);
                    Motor.B.forward();
                    Motor.C.forward();
                }
    
                Delay.msDelay(50);
            }
    
            Motor.B.stop();
            Motor.C.stop();
        }
    
        private static void initializeLightValues() {
            LCD.clear();
            LCD.drawString("Calibrating...", 0, 0);
            Delay.msDelay(2000);
    
            leftBaseValue = leftSensor.getLightValue();
            rightBaseValue = rightSensor.getLightValue();
    
            LCD.clear();
            LCD.drawString("Left Base: " + leftBaseValue, 0, 0);
            LCD.drawString("Right Base: " + rightBaseValue, 0, 1);
            Delay.msDelay(1000);
        }
    }