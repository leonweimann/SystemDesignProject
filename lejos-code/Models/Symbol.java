package Models;

/**
 * Represents a symbol with two sides, each of which can be black or not.
 */
public class Symbol {
    /**
     * Constructs a Symbol with specified colors for the left and right sides.
     * 
     * @param isLeftBlack  true if the left side is black, false otherwise
     * @param isRightBlack true if the right side is black, false otherwise
     */
    public Symbol(boolean isLeftBlack, boolean isRightBlack) {
        this.isLeftBlack = isLeftBlack;
        this.isRightBlack = isRightBlack;
    }

    private boolean isLeftBlack;
    private boolean isRightBlack;

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
     * Compares this symbol to another symbol for equality.
     * 
     * @param other the other symbol to compare to
     * @return true if both symbols have the same colors on both sides, false otherwise
     */
    public boolean equals(Symbol other) {
        return this.isLeftBlack == other.isLeftBlack && this.isRightBlack == other.isRightBlack;
    }
}