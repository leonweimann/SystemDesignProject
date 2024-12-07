import lejos.nxt.LightSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.util.Delay;

public class Main {
    private static final int EXECUTION_FREQUENCY = 5;
    private static final int BLACK_THRESHOLD = 30;
    private static final int FLUCTUATION_BUFFER = 5;

    private static final int DEFAULT_SPEED = 200;

    private static LightSensor leftSensor = new LightSensor(Ports.LIGHT_SENSOR_LEFT);
    private static LightSensor rightSensor = new LightSensor(Ports.LIGHT_SENSOR_RIGHT);
    private static LightSensor centerSensor = new LightSensor(Ports.LIGHT_SENSOR_CENTER);

    private static NXTRegulatedMotor leftMotor = new NXTRegulatedMotor(Ports.MOTOR_LEFT);
    private static NXTRegulatedMotor rightMotor = new NXTRegulatedMotor(Ports.MOTOR_RIGHT);

    private static TouchSensor touchSensor = new TouchSensor(Ports.TOUCH_SENSOR);

    private static Boolean lastTurnedLeft;
    private static boolean madeRotatingTask = false;

    public static void main(String[] args) {
        Setup.setup(leftSensor, rightSensor, centerSensor);
        setSpeeds(DEFAULT_SPEED);

        long nextExecution = 0;
        while (true) {
            if (isTouching()) {
                if (madeRotatingTask) {
                    break;
                }

                backoff(1500);
                rotate(true);
                Delay.msDelay(5000);
                stop();
                madeRotatingTask = true;
                
                while (!searchLine()) {
                }
                continue;
            }

            if (System.currentTimeMillis() < nextExecution) {
                continue;
            }
            nextExecution = System.currentTimeMillis() + 1000 / EXECUTION_FREQUENCY;
            LCDHelper.resetAppendedItems();

            Symbol symbol = readSymbol();
            LCDHelper.appendingToDisplay(symbol.debugDescription(), false, 1);

            followLine(symbol);
        }
    }

    private static void move(int amount) {
        int leftSpeed;
        int rightSpeed;

        amount = Math.max(-100, Math.min(100, amount));

        if (amount < 0) { // Turn left
            leftSpeed = (int) (DEFAULT_SPEED * (1.0 + amount / 100.0));
            rightSpeed = DEFAULT_SPEED;
            lastTurnedLeft = true;
        } else if (amount > 0) { // Turn right
            leftSpeed = DEFAULT_SPEED;
            rightSpeed = (int) (DEFAULT_SPEED * (1.0 - amount / 100.0));
            lastTurnedLeft = false;
        } else { // Move straight
            leftSpeed = DEFAULT_SPEED;
            rightSpeed = DEFAULT_SPEED;
            lastTurnedLeft = null;
        }

        leftMotor.setSpeed(leftSpeed);
        rightMotor.setSpeed(rightSpeed);

        moveForward();
    }

    private static void moveForward() {
        leftMotor.backward();
        rightMotor.backward();
    }

    private static void moveBackward() {
        leftMotor.forward();
        rightMotor.forward();
    }

    private static void stop() {
        leftMotor.stop();
        rightMotor.stop();
    }

    private static void setSpeeds(int speed) {
        leftMotor.setSpeed(speed);
        rightMotor.setSpeed(speed);
    }

    private static void rotate(boolean clockwise) {
        stop();
        setSpeeds(150);

        if (clockwise) {
            leftMotor.backward();
            rightMotor.forward();
        } else {
            leftMotor.forward();
            rightMotor.backward();
        }
    }

    private static boolean isTouching() {
        return touchSensor.isPressed();
    }

    private static Symbol readSymbol() {
        int leftReading = leftSensor.getLightValue();
        int rightReading = rightSensor.getLightValue();
        int centerReading = centerSensor.getLightValue();
        return new Symbol(leftReading, rightReading, centerReading);
    }

    private static boolean shouldTurnLeft(Symbol symbol) {
        return symbol.left < symbol.right && symbol.left < symbol.center;
    }

    private static boolean shouldTurnRight(Symbol symbol) {
        return symbol.right < symbol.left && symbol.right < symbol.center;
    }

    private static boolean noHugeDifference(Symbol symbol) {
        final int maxDifference = 15; // TODO: Adjust
        return Math.abs(symbol.left - symbol.right) < maxDifference
                && Math.abs(symbol.left - symbol.center) < maxDifference
                && Math.abs(symbol.right - symbol.center) < maxDifference;
    }

    private static boolean allBlack(Symbol symbol) {
        return symbol.left < BLACK_THRESHOLD && symbol.right < BLACK_THRESHOLD && symbol.center < BLACK_THRESHOLD
                && noHugeDifference(symbol);
    }

    private static void backoff(int duration) {
        stop();
        setSpeeds(150);
        moveBackward();
        Delay.msDelay(duration);
        stop();
    }

    private static void followLine(Symbol symbol) {
        if (!allBlack(symbol) && noHugeDifference(symbol)) {
            LCDHelper.appendingToDisplay("FORWARD LIKE GOD SAYS", true, 2);
            if (lastTurnedLeft != null) {
                move(lastTurnedLeft ? -100 : 100);
            } else {
                move(0);
            }
        } else if (shouldTurnLeft(symbol)) {
            LCDHelper.appendingToDisplay("LEFT", true, 2);
            move(-100);
        } else if (shouldTurnRight(symbol)) {
            LCDHelper.appendingToDisplay("RIGHT", true, 2);
            move(100);
        } else {
            LCDHelper.appendingToDisplay("FORWARD", true, 2);
            move(0);
        }
    }

    private static long rotationSectionStart = 0;
    private static boolean clockwise = true;
    private static boolean needsForward = false;
    private static long forwardStartTime = 0;
    private static long realignStartTime = 0;

    private static boolean searchLine() {
        Symbol symbol = readSymbol();

        boolean atLeastOneBlack = checkForBlack(null, symbol) || checkForBlack(false, symbol) || checkForBlack(true, symbol);

        LCDHelper.appendingToDisplay("ALB: " + atLeastOneBlack, false, 42);

        if (atLeastOneBlack) {
            rotationSectionStart = 0;
            stop();
            return true;
        }

        if (rotationSectionStart == 0) {
            rotationSectionStart = System.currentTimeMillis();
        }

        if (System.currentTimeMillis() - rotationSectionStart > 1000) {
            stop();
            clockwise = !clockwise;
            rotationSectionStart = 0;
        } else {
            rotate(clockwise);
        }

        if (!clockwise) {
            if (realignStartTime == 0) {
                realignStartTime = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - realignStartTime > 1000) {
                stop();
                needsForward = true;
                realignStartTime = 0;
            } else {
                rotate(!clockwise);
            }

            if (needsForward) {
                if (forwardStartTime == 0) {
                    forwardStartTime = System.currentTimeMillis();
                }

                if (System.currentTimeMillis() - forwardStartTime > 1000) {
                    stop();
                    needsForward = false;
                    forwardStartTime = 0;
                } else {
                    moveForward();
                }
            }
        }

        return false;
    }

    private static Boolean checkForBlack(Boolean checkingCoded, Symbol symbol) {
        int checking, compareMoreLeft, compareMoreRight;
        if (checkingCoded == null) {
            checking = symbol.center;
            compareMoreLeft = symbol.left;
            compareMoreRight = symbol.right;
        } else if (checkingCoded) {
            checking = symbol.right;
            compareMoreLeft = symbol.left;
            compareMoreRight = symbol.center;
        } else {
            checking = symbol.left;
            compareMoreLeft = symbol.center;
            compareMoreRight = symbol.right;
        }

        boolean mostLikelyBlack = checking < compareMoreLeft - FLUCTUATION_BUFFER
                && checking < compareMoreRight - FLUCTUATION_BUFFER;
        boolean mostLikelyWhite = checking > compareMoreLeft - FLUCTUATION_BUFFER
                && checking > compareMoreRight - FLUCTUATION_BUFFER;

        return mostLikelyBlack ? true : mostLikelyWhite ? false : null;
    }
}
