package Coordination;

import java.util.StringTokenizer;

import lejos.nxt.LCD;

/**
 * The LCDHelper class provides utility methods for displaying messages on the
 * LCD screen. It supports displaying messages with optional centering and
 * handles word wrapping.
 * 
 * @author leonweimann
 * @version 1.3
 */
public class LCDHelper {
    /**
     * Utility class for LCD-related helper methods.
     * This class is not meant to be instantiated.
     */
    private LCDHelper() {
        // Prevent instantiation
    }

    public enum Alignment {
        TOP, BOTTOM, LEFT, RIGHT, CENTER
    }

    /**
     * Displays a message on the LCD screen. Handles overflow by wrapping words to
     * the next line.
     *
     * @param message The message to be displayed. It can contain multiple lines
     *                separated by '\n'.
     */
    public static void display(String message) {
        display(message, true, Alignment.TOP);
    }

    /**
     * Displays a message on the LCD screen. Handles overflow by wrapping words to
     * the next line.
     *
     * @param message    The message to be displayed. It can contain multiple lines
     *                   separated by '\n'.
     * @param alignment  The alignment of the text on the screen (top, bottom, left, right, center).
     */
    public static void display(String message, Alignment alignment) {
        display(message, true, alignment);
    }

    /**
     * Displays a message on the LCD screen. Handles overflow by wrapping words to
     * the next line.
     *
     * @param message    The message to be displayed. It can contain multiple lines
     *                   separated by '\n'.
     * @param autoClear  If true, the screen will be cleared before displaying the
     *                   message.
     */
    public static void display(String message, boolean autoClear) {
        display(message, autoClear, null);
    }

    /**
     * Displays a message on the LCD screen. Handles overflow by wrapping words to
     * the next line.
     *
     * @param message    The message to be displayed. It can contain multiple lines
     *                   separated by '\n'.
     * @param autoClear  If true, the screen will be cleared before displaying the
     *                   message.
     * @param alignment  The alignment of the text on the screen (top, bottom, left, right, center).
     */
    public static void display(String message, boolean autoClear, Alignment alignment) {
        int width = LCD.DISPLAY_CHAR_WIDTH;
        int height = LCD.DISPLAY_CHAR_DEPTH;
        int lineCount = 0;

        if (autoClear) {
            LCD.clear();
        }

        StringTokenizer tokenizer = new StringTokenizer(message, "\n");
        int totalLines = tokenizer.countTokens();
        int startLine = 0;

        // if (alignment == null) {
        //     alignment = Alignment.LEFT;
        // }

        if (alignment == Alignment.BOTTOM) {
            startLine = Math.max(0, height - totalLines);
        } else if (alignment == Alignment.CENTER) {
            startLine = Math.max(0, (height - totalLines) / 2);
        }

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

                // Align the line based on the alignment parameter
                if (alignment == Alignment.CENTER || alignment == Alignment.TOP) {
                    int padding = (width - line.length()) / 2;
                    StringBuilder paddedLine = new StringBuilder();
                    for (int i = 0; i < padding; i++) {
                        paddedLine.append(' ');
                    }
                    paddedLine.append(line);
                    line = paddedLine.toString();
                } else if (alignment == Alignment.RIGHT) {
                    int padding = width - line.length();
                    StringBuilder paddedLine = new StringBuilder();
                    for (int i = 0; i < padding; i++) {
                        paddedLine.append(' ');
                    }
                    paddedLine.append(line);
                    line = paddedLine.toString();
                }

                LCD.drawString(line, 0, startLine + lineCount);
                msgLine = msgLine.substring(endIndex).trim();
                lineCount++;
            }
        }
    }
}
