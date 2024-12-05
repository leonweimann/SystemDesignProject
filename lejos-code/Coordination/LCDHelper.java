package Coordination;

import java.util.StringTokenizer;

import lejos.nxt.LCD;

/**
 * The LCDHelper class provides utility methods for displaying messages on the
 * LCD screen. It supports displaying messages with optional centering and
 * handles word wrapping.
 * 
 * @author leonweimann
 * @version 1.4
 */
public class LCDHelper {
    /**
     * Utility class for LCD-related helper methods.
     * This class is not meant to be instantiated.
     */
    private LCDHelper() {
        // Prevent instantiation
    }

    /**
     * A static variable to hold the current string displayed or processed by the LCDHelper.
     */
    private static String currentString = "";

    /**
     * Displays a message on the LCD screen. Handles overflow by wrapping words to
     * the next line.
     *
     * @param message The message to be displayed. It can contain multiple lines
     *                separated by '\n'.
     */
    public static void display(String message) {
        display(message, false);
    }

    /**
     * Displays a message on the LCD screen. Handles overflow by wrapping words to
     * the next line.
     *
     * @param message The message to be displayed. It can contain multiple lines
     *                separated by '\n'.
     * @param center  If true, each line of the message will be centered on the
     *                screen.
     */
    public static void display(String message, boolean center) {
        int width = LCD.DISPLAY_CHAR_WIDTH;
        int height = LCD.DISPLAY_CHAR_DEPTH;
        int lineCount = 0;

        clear();
        StringTokenizer tokenizer = new StringTokenizer(message, "\n");
        while (tokenizer.hasMoreTokens() && lineCount < height) {
            String msgLine = tokenizer.nextToken();
            while (msgLine.length() > 0 && lineCount < height) {
                int endIndex = Math.min(width, msgLine.length());
                String line = msgLine.substring(0, endIndex);

                // Check if the line ends in the middle of a word
                if (endIndex < msgLine.length() && msgLine.charAt(endIndex) != ' '
                        && msgLine.charAt(endIndex - 1) != ' ') {
                    int lastSpace = line.lastIndexOf(' ');
                    if (lastSpace != -1) {
                        line = line.substring(0, lastSpace);
                        endIndex = lastSpace + 1;
                    }
                }

                // Center the line if isCentered is true
                if (center) {
                    int padding = (width - line.length()) / 2;
                    StringBuilder paddedLine = new StringBuilder();
                    for (int i = 0; i < padding; i++) {
                        paddedLine.append(' ');
                    }
                    paddedLine.append(line);
                    line = paddedLine.toString();
                }

                LCD.drawString(line, 0, lineCount);
                msgLine = msgLine.substring(endIndex).trim();
                lineCount++;
            }
        }
    }

    /**
     * Adds a message to the display.
     *
     * @param message the message to be displayed
     */
    public static void addToDisplay(String message) {
        addToDisplay(message, false);
    }

    /**
     * Adds a message to the current display string and updates the display.
     *
     * @param message the message to be added to the display
     * @param center  if true, the message will be centered on the display
     */
    public static void addToDisplay(String message, boolean center) {
        String newMessage = currentString + "\n" + message;
        display(newMessage, center);
    }

    /**
     * Clears the LCD display and resets the current string.
     * This method calls the LCD.clear() method to clear the display
     * and sets the currentString variable to an empty string.
     */
    public static void clear() {
        LCD.clear();
        currentString = "";
    }
}
