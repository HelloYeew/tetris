package game.main.tetromino;

import game.main.math.Vector2D;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class representing an I Tetromino.
 */
public class TetrominoI implements Tetromino {
    private Color color = Color.CYAN;
    private Vector2D origin;
    private ArrayList<Vector2D> positions;

    public TetrominoI(Vector2D origin) {
        this.origin = origin;
        generateBlock();
    }

    /**
     * Initialize other block from origin
     */
    @Override
    public void generateBlock() {
        positions = new ArrayList<>();
        positions.add(new Vector2D(origin.x, origin.y));
        positions.add(new Vector2D(origin.x + 1, origin.y));
        positions.add(new Vector2D(origin.x + 2, origin.y));
        positions.add(new Vector2D(origin.x + 3, origin.y));
    }

    /**
     * Rotate the tetromino
     */
    @Override
    public void rotate() {
        // TODO: Implement the state design pattern on this since we are not sure on how many states we will have
    }

    /**
     * Update the position of the tetromino
     */
    @Override
    public void update() {
        for (Vector2D position : positions) {
            position.y += 1;
        }
    }

    /**
     * Get the color of the tetromino
     *
     * @return Color Current color of the block
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * Set the tetromino's color
     *
     * @param color Color of the tetromino
     */
    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Get the origin of the tetromino
     *
     * @return Vector2D Origin of the tetromino
     */
    @Override
    public Vector2D getOrigin() {
        return origin;
    }

    /**
     * Set the origin of the tetromino
     *
     * @param origin Set the origin of the tetromino
     */
    @Override
    public void setOrigin(Vector2D origin) {
        this.origin = origin;
        // after setting the origin, we need to update the position of the block
        generateBlock();
    }

    /**
     * Get the list of all blocks of the tetromino
     *
     * @return ArrayList<Vector2D> List of all blocks of the tetromino
     */
    @Override
    public ArrayList<Vector2D> getPositions() {
        return positions;
    }

    /**
     * Set the position of all block of the tetromino to a new position
     *
     * @param positions A new list of position that will replace the old list
     */
    @Override
    public void setPositions(ArrayList<Vector2D> positions) {
        this.positions = positions;
    }

    /**
     * Get the type of the tetromino
     *
     * @return String Type of the tetromino
     */
    public String toString() {
        return "I";
    }
}
