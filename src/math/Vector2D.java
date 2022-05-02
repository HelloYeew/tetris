package math;

/**
 * Class that mock 2D vector in some popular game engines.
 */
public class Vector2D {
    /**
     * The X position
     */
    public int x;

    /**
     * The Y position
     */
    public int y;

    /**
     * Constructor to create a new position
     * @param x The X position
     * @param y The Y position
     */
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this(0, 0);
    }

    /**
     * Get the x position
     * @return The x position
     */
    public int getX() {
        return x;
    }

    /**
     * Set the x position
     * @param x Target x position
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the y position
     * @return The y position
     */
    public int getY() {
        return y;
    }

    /**
     * Set the y position
     * @param y Target y position
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Represent the position as a string
     * @return Readable position in string
     */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
