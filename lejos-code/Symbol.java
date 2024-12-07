

/**
 * The {@code Symbol} class represents a symbol that can be detected by the
 * light sensors. A symbol consists of three parts: the left side, the right
 * side, and the center. Each part can be either black or white.
 * 
 * 
 * 
 * @author leonweimann
 * @version 2.0
 */
public class Symbol {
    /**
     * Constructs a Symbol with the specified left, right, and center values.
     *
     * @param left   the left value of the Symbol
     * @param right  the right value of the Symbol
     * @param center the center value of the Symbol
     */
    public Symbol(int left, int right, int center) {
        this(left, right, center, false);
    }

    /**
     * Constructs a new Symbol with the given left, right, and center values.
     * 
     * @param left   the left value of the symbol
     * @param right  the right value of the symbol
     * @param center the center value of the symbol
     * @param withTimestamp whether to include a timestamp
     */
    public Symbol(int left, int right, int center, boolean withTimestamp) {
        this.left = left;
        this.right = right;
        this.center = center;
        this.timestamp = withTimestamp ? System.currentTimeMillis() : null;
    }

    public final int left, right, center;
    public final Long timestamp;

    /**
     * Checks if the symbol has a timestamp.
     *
     * @return true if the symbol has a timestamp, false otherwise.
     */
    public boolean isTimestamped() {
        return timestamp != null;
    }

    /**
     * Returns a string representation of this Symbol.
     * 
     * @return a string representation of this Symbol
     */
    public String debugDescription() {
        return "L: " + left + "\nR: " + right + "\nC: " + center;
    }
}