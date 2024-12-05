package Models;

/**
 * The {@code Symbol} class represents a kind of tuple with three boolean
 * values, used for representing light sensor values in the
 * LightFluctuationController class.
 * 
 * @author leonweimann
 * @version 1.2
 */
public class Symbol {
    /**
     * Creates a new Symbol with the specified sides.
     * 
     * @param isLeftBlack  true if the left side is black, false otherwise
     * @param isRightBlack true if the right side is black, false otherwise
     */
    public Symbol(boolean isLeftBlack, boolean isRightBlack, boolean isCenterBlack) {
        this.isLeftBlack = isLeftBlack;
        this.isRightBlack = isRightBlack;
        this.isCenterBlack = isCenterBlack;
    }

    private boolean isLeftBlack;
    private boolean isRightBlack;
    private boolean isCenterBlack;

    /**
     * Returns whether the left side of the symbol is black.
     * 
     * @return true if the left side is black, false otherwise
     */
    public boolean isLeftBlack() {
        return isLeftBlack;
    }

    /**
     * Returns whether the right side of the symbol is black.
     * 
     * @return true if the right side is black, false otherwise
     */
    public boolean isRightBlack() {
        return isRightBlack;
    }

    /**
     * Returns whether the center of the symbol is black.
     * 
     * @return true if the center is black, false otherwise
     */
    public boolean isCenterBlack() {
        return isCenterBlack;
    }

    /**
     * Compares this Symbol to another Symbol.
     * 
     * @param other the Symbol to compare to
     * @return true if the two Symbols are equal, false otherwise
     */
    public boolean equals(Symbol other) {
        return this.isLeftBlack == other.isLeftBlack && this.isRightBlack == other.isRightBlack
                && this.isCenterBlack == other.isCenterBlack;
    }

    /**
     * Returns a string representation of this Symbol.
     * 
     * @return a string representation of this Symbol
     */
    public String debugDescription() {
        return "L: " + isLeftBlack + "\n, R: " + isRightBlack + "\n, C: " + isCenterBlack;
    }
}